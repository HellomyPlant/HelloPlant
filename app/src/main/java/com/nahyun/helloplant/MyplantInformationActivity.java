package com.nahyun.helloplant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyplantInformationActivity extends AppCompatActivity {

    private ArrayList<MyplantInformationData> mpi_arrayList;
    private MyplantInformationAdapter myplantInformationAdapter;
    private RecyclerView myplant_information_RecyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplant_information);

        Intent intent_comefrom_view_myplant_page = getIntent();

        byte[] byteArray_imageBitmap = getIntent().getByteArrayExtra("image_bitmap");
        Bitmap get_image;
        get_image = BitmapFactory.decodeByteArray(byteArray_imageBitmap, 0, byteArray_imageBitmap.length);
        ImageView plant_ImageView = (ImageView)findViewById(R.id.myplant_information_ImageView);
        plant_ImageView.setImageBitmap(get_image);

        JSONObject plantDetailData = new JSONObject();
        String jsonString =
                intent_comefrom_view_myplant_page.getExtras().getString("plantDetailData");
        if (jsonString != null) {
            try {
                plantDetailData = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        myplant_information_RecyclerView = (RecyclerView)findViewById(R.id.myplant_information_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        myplant_information_RecyclerView.setLayoutManager(linearLayoutManager);

        mpi_arrayList = new ArrayList<>();

        myplantInformationAdapter = new MyplantInformationAdapter(mpi_arrayList);
        myplant_information_RecyclerView.setAdapter(myplantInformationAdapter);

        MyplantInformationData familyname = null;
        MyplantInformationData height = null;
        MyplantInformationData place = null;
        MyplantInformationData smell = null;
        MyplantInformationData speed = null;
        MyplantInformationData temperature = null;
        MyplantInformationData pest = null;
        MyplantInformationData waterCycle = null;
        MyplantInformationData waterdrop = null;
        MyplantInformationData light = null;

        String wateringInfomation = "";
        try {
            wateringInfomation = (String)plantDetailData.get("watercycleWinter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int wateringCycle = 0;
        String wateringdrop = "";

        try {
            if (wateringInfomation.charAt(0) == '???') {
                wateringCycle = 1;
                wateringdrop = "?????? ?????? ?????????";
            } else if (wateringInfomation.charAt(0) == '???') {
                wateringCycle = 7;
                wateringdrop = "?????? ????????????";
            } else if (wateringInfomation.charAt(0) == '???') {
                wateringCycle = 14;
                wateringdrop = "?????? ????????? ?????????";
            } else {
                wateringCycle = 30;
                wateringdrop = "?????? ????????? ?????????";
            }
        }
        catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
            wateringdrop = "????????? ????????????.";
            System.out.println("There is no watering information for this plant.");
        }

        String water = Integer.toString(wateringCycle) + " ???";
        String KoreanName = "";
        String ManageLevel = "";
        try {
            familyname = new MyplantInformationData("?????? ??????", (String)plantDetailData.get("familyName"));
            waterCycle = new MyplantInformationData(" ??? ?????? ", water);
            waterdrop = new MyplantInformationData(" ?????? ??? ", wateringdrop);
            light = new MyplantInformationData("????????? ???", (String)plantDetailData.get("light"));
            height = new MyplantInformationData("?????? ??????", (String)plantDetailData.get("height"));
            place = new MyplantInformationData("?????? ??????", ((String)plantDetailData.get("place")).replace(",","\n"));
            smell = new MyplantInformationData("?????? ??????", (String)plantDetailData.get("smell"));
            speed = new MyplantInformationData("?????? ??????", (String)plantDetailData.get("growthSpeed"));
            temperature = new MyplantInformationData("?????? ??????", (String)plantDetailData.get("properTemperature"));
            pest = new MyplantInformationData(" ????????? ", (String)plantDetailData.get("pest"));
            KoreanName = (String)plantDetailData.get("koreanName");
            ManageLevel = (String)plantDetailData.get("manageLevel");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mpi_arrayList.add(familyname);
        mpi_arrayList.add(waterCycle);
        mpi_arrayList.add(waterdrop);
        mpi_arrayList.add(light);
        mpi_arrayList.add(height);
        mpi_arrayList.add(place);
        mpi_arrayList.add(smell);
        mpi_arrayList.add(speed);
        mpi_arrayList.add(temperature);
        mpi_arrayList.add(pest);

        TextView myplant_information_name_TextView = (TextView)findViewById(R.id.myplant_information_name_TextView);
        myplant_information_name_TextView.setText(KoreanName);

        ImageView star_ImageView = (ImageView)findViewById(R.id.difficulty_star_ImageView);
        if(ManageLevel.equals("?????????")){
            // ??? 3
            star_ImageView.setImageResource(R.drawable.star_three);
        }
        else if(ManageLevel.equals("?????????")){
            // ??? 1
            star_ImageView.setImageResource(R.drawable.star_one);
        }
        else if(ManageLevel.equals("?????????")){
            // ??? 5
            star_ImageView.setImageResource(R.drawable.star_five);
        }
        else{
            // ??? 2
            star_ImageView.setImageResource(R.drawable.star_two);
        }

    }
}