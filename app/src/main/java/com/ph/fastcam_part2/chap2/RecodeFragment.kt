package com.ph.fastcam_part2.chap2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.red
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.databinding.Chap2Binding
import kotlinx.coroutines.NonCancellable.start
import java.io.IOException

class RecodeFragment : Fragment(),OnTimerTickListener {
    private lateinit var binding: Chap2Binding

    //상태
    //릴리즈 -> 녹음중 -> 릴리즈
    //릴리즈 -> 재생 -> 릴리즈
    private enum class State {
        RELEASE, RECODING, PLAYING
    }

    private var state: State = State.RELEASE // 초기값 릴리즈
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var fileName: String = ""
    private lateinit var timer : Timer

    //요청 권한 결과 받았을때
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onRecord(true)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    showPermissionRationalDialog()
                } else {
                    showPermissionSettingDialog()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap2Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileName = "${requireActivity().externalCacheDir?.absolutePath}/audiorecodetext.3gp"
        timer = Timer(this)

        binding.recodeBtn.setOnClickListener {
            when (state) {
                State.RELEASE -> {
                    requestPermission()

                }
                State.RECODING -> {
                    onRecord(false)
                }
                else -> {

                }
            }
        }

        binding.playBtn.setOnClickListener {
            when (state) {
                State.RELEASE -> {
                    onPlay(true)
                }
                else -> {
                    //do nothing
                }
            }
        }
        binding.stopBtn.setOnClickListener {
            when (state) {
                State.PLAYING -> {
                    onPlay(false)
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                //권한 있음 -> 녹음 시작
                onRecord(true)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) -> {
                //안내문구 다이얼로그 노출
                showPermissionRationalDialog()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }

    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("녹음 권한을 허용해야 앱을 정상적으로 사용 가능합니다")
            .setPositiveButton("권한 허용") { _, _ ->
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
            .setNegativeButton("거부", null)
            .show()
    }

    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("녹음 권한을 허용해야 앱을 정상적으로 사용 가능합니다")
            .setPositiveButton("설정으로 이동") { _, _ ->
                navigateToAppSetting()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            //우리 앱의 권한 설정창으로 이동
            data = Uri.fromParts("package", requireActivity().packageName, null)
        }
        startActivity(intent)
    }

    private fun onRecord(start: Boolean) = if (start) startRecording() else stopRecording()
    private fun onPlay(start: Boolean) = if (start) startPlaying() else stopPlaying()

    private fun startRecording() {
        state = State.RECODING

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.d(TAG, "RecodeFragment - onRecord: prepareFailed $e ");
            }

            start()
        }

        timer.start()

        binding.recodeBtn.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_stop_24
                )
            )
            imageTintList = ColorStateList.valueOf(Color.BLACK)
        }
        binding.playBtn.apply {
            isEnabled = false
            alpha = 0.3f
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        timer.stop()

        state = State.RELEASE
        binding.recodeBtn.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_fiber_manual_record_24
                )
            )
            imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
        }
        binding.playBtn.apply {
            isEnabled = true
            alpha = 1.0f
        }
    }

    private fun startPlaying() {
        state = State.PLAYING

        player = MediaPlayer().apply {
            setDataSource(fileName)
            try {
                prepare()
            } catch (e: IOException) {
                Log.d(TAG, "RecodeFragment - startPlaying:play prepare failed $e");
            }
            start()
        }

        //파일 재생이 끝났을 때
        player?.setOnCompletionListener {
            stopPlaying()
        }

        binding.recodeBtn.apply {
            isEnabled = false
            alpha = 0.3f
        }
    }

    private fun stopPlaying() {
        state = State.RELEASE

        player?.release()
        player = null

        binding.recodeBtn.apply {
            isEnabled = true
            alpha = 1.0f
        }
    }

    override fun onTick(duration: Long) {
        binding.waveFormView.addAmplitude(recorder?.maxAmplitude?.toFloat()?:0f)
    }
}