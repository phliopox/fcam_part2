package com.ph.fastcam_part2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/*
$kiosk = ($_GET['kiosk']=="")?$_POST['kiosk']:$_GET['kiosk'];
$product = ($_GET['product']=="")?$_POST['product']:$_GET['product'];
$number = ($_GET['no']=="")?$_POST['no']:$_GET['no'];
$singleset = ($_GET['singleset']=="")?$_POST['singleset']:$_GET['singleset'];
$quantity = ($_GET['quantity']=="")?$_POST['quantity']:$_GET['quantity'];
$expiration = ($_GET['expiration']=="")?$_POST['expiration']:$_GET['expiration'];
$message = ($_GET['message']=="")?$_POST['message']:$_GET['message'];
 */
public interface RetrofitInterface {

/*    @FormUrlEncoded
    @POST("/vending/sold.php")
    Call<Note> saveLog(@Field("log") String log);
    */

    @FormUrlEncoded
    @POST("/vending/sold.php")
    Call<Note> testLogInsert(
            @Field("kiosk") String kiosk,
            @Field("product") String product,
            @Field("number") String number,
            @Field("singleset") String singleset,
            @Field("expiration") String quantity
    );
}

