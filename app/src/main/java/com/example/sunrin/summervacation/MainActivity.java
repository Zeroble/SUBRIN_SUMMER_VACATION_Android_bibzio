package com.example.sunrin.summervacation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    LinearLayout linearLayout;
    TextView dist;
    int distance = 100;
    GpsInfo jjjj;
    double lat;
    double lon;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationPermissionChecker();
        seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setMax(900);
        linearLayout = (LinearLayout) findViewById(R.id.stroke);
        dist = (TextView) findViewById(R.id.dist);
        jjjj=new GpsInfo(this);
        checkBox1 = (CheckBox) findViewById(R.id.btn1);
        checkBox2 = (CheckBox) findViewById(R.id.btn2);
        checkBox3 = (CheckBox) findViewById(R.id.btn3);
        checkBox4 = (CheckBox) findViewById(R.id.btn4);
        checkBox5 = (CheckBox) findViewById(R.id.btn5);
        checkBox6 = (CheckBox) findViewById(R.id.btn6);
        checkBox7 = (CheckBox) findViewById(R.id.btn7);
        checkBox8 = (CheckBox) findViewById(R.id.btn8);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("TAG", "" + progress);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (150 + (progress * 0.5)), (int) (150 + (progress * 0.5))));
                dist.setText(progress + 100 + "m\n이내 검색");
                distance = 100+progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }
    public void LocationPermissionChecker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)//권한 비허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)//권한 비허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
    }
    public void Search(View v){
        Toast.makeText(getApplicationContext(),"검색 중",Toast.LENGTH_SHORT).show();
        jjjj.getLocation();
        lat = jjjj.lat;
        lon = jjjj.lon;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestrantService service = retrofit.create(RestrantService.class);

        String keyword="";
        String type;


        if(checkBox1.isChecked())
            keyword+="|치킨";
        if(checkBox2.isChecked())
            keyword+="|피자";
        if(checkBox3.isChecked())
            keyword+="|히오스";
        if(checkBox4.isChecked())
            keyword+="|양식";
        if(checkBox5.isChecked())
            keyword+="|한식";
        if(checkBox6.isChecked())
            keyword+="|일식";
        if(checkBox7.isChecked())
            keyword+="|치킨";
        if(checkBox8.isChecked())
            keyword+="|치킨";


        Call<RetroItem> repos = (Call<RetroItem>) service.listRepos(lat+","+lon,keyword,distance,"","AIzaSyAE78Um1ZuE9quVniPZSiLEz0Cw9dOPGuk");
        repos.enqueue(new Callback<RetroItem>() {
            @Override
            public void onResponse(Call<RetroItem> call, Response<RetroItem> response) {
                RetroItem user = response.body();
                int random = (int)(Math.random()*user.getResults().size())+1;
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("lat",user.getResults().get(random).getGeometry().getLocation().getLat());
                intent.putExtra("lon",user.getResults().get(random).getGeometry().getLocation().getLng());
                intent.putExtra("vi",""+user.getResults().get(random).getName());
                intent.putExtra("name",""+user.getResults().get(random).getVicinity());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RetroItem> call, Throwable throwable) {
                Log.i("TAG", "실패함 : "+throwable);
            }
        });
    }
    public interface RestrantService {
        @GET("maps/api/place/nearbysearch/json")
        Call<RetroItem>listRepos(@Query("location") String location, @Query("keyword")String keword, @Query("radius")int radius, @Query("type")String type, @Query("key")String key);

    }
}
