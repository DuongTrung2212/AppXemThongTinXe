package com.endproject.endproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import api.APIServices;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CarInfoActivity extends AppCompatActivity {
    TextView tv_nameInfoCar,tv_releaseDateCarInfo,tv_nameCarInfo,tv_companyCarInfo,tv_infomationCarInfo,tv_priceCarInfo, numLike;
    ViewPager viewPager_infoCar;
    CircleIndicator circleIndicator_infoCar;
    SlideAdapter slideAdapter;
    ImageView img_star;
    RecyclerView recyclerViewListStore;
    String id="";
    String idUser="";
    List<Slide> arrSlide=new ArrayList<>();
    List<Store> listStore = new ArrayList<>();
    StoreAdapter storeAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        idUser=intent.getStringExtra("idUser");
        String name = intent.getStringExtra("name");
        initUI();

        callApi();
        context=getApplicationContext();
        tv_nameInfoCar.setText(name);
//        listStore.add(new Store("storeName1", "storeAddress1"));
//        listStore.add(new Store("storeName2", "storeAddress2"));
        slideAdapter=new SlideAdapter(this,arrSlide);
        viewPager_infoCar.setAdapter(slideAdapter);
        circleIndicator_infoCar.setViewPager(viewPager_infoCar);
        slideAdapter.registerDataSetObserver(circleIndicator_infoCar.getDataSetObserver());

        storeAdapter = new StoreAdapter(listStore);
        recyclerViewListStore.setAdapter(storeAdapter);
        recyclerViewListStore.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        storeAdapter.setOnItemClickListener(new StoreAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
//                Toast.makeText(CarInfoActivity.this, "hihi", Toast.LENGTH_SHORT).show();
                Store store = listStore.get(position);
                Uri uri = Uri.parse("google.navigation:q="+store.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(uri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
//                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(mapIntent);
//                } else {
//                    Toast.makeText(context, "nonono", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }



    public void callApi(){
        Log.d("trung",idUser);
        String url= APIServices.baseLink+"car/detail/"+id+"/"+idUser;

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name=response.getString("name");

                    String release=response.getString("release_date");
                    String price=response.getString("price");
                    String company=response.getString("company_name");
                    String liked=response.getString("likedByUser");
                    String infomation="";
                    String numLikeStr = response.getString("num_like");
                    JSONObject objectInfo=response.getJSONObject("infomation");
                    JSONArray stores = response.getJSONArray("store");

                    for (int i = 0; i < stores.length(); i++) {
                        JSONObject store = stores.getJSONObject(i);
                        String storeName = store.getString("name");
                        String storeAddress = store.getString("address");
                        listStore.add(new Store(storeName, storeAddress));

                    }
                    storeAdapter.notifyDataSetChanged();
                    Iterator<String> keys = objectInfo.keys();
                    numLike.setText(numLikeStr);
                    for (int i = 0; i < objectInfo.length(); i++) {
                        String key=keys.next();
                        infomation+=""+key+" : "+objectInfo.getString(key)+"\n";
//                        Log.d("trung",infomation+"");
                    }
                    JSONArray arr=response.getJSONArray("images");
                    for (int i = 0; i < arr.length(); i++) {
                        arrSlide.add(new Slide("http://"+arr.get(i).toString()));
                    }
                    Log.d("trung",response.toString());
                    if (liked.equals("yes")) {
                        img_star.setImageResource(R.drawable.star);
                    }else{
                        img_star.setImageResource(R.drawable.star_line);
                    }
                    img_star.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toggleLike();
                        }
                    });
                    slideAdapter.notifyDataSetChanged();
                    tv_releaseDateCarInfo.setText("Ngày ra mắt : "+release);
                    tv_companyCarInfo.setText("Hãng : "+company);
                    tv_nameCarInfo.setText("Tên xe : "+name);
                    tv_priceCarInfo.setText("Giá thị trường : "+price+"$");
                    tv_infomationCarInfo.setText(infomation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void toggleLike(){
        APIServices.apiServices.toggleLike(id,idUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    switch (jsonObject.getInt("code")){
                        case 201:
                            img_star.setImageResource(R.drawable.star);
                            numLike.setText(jsonObject.getString("numLike"));
                            break;
                        case 202:
                            img_star.setImageResource(R.drawable.star_line);
                            numLike.setText(jsonObject.getString("numLike"));
                            break;
                        case 300:
                            Toast.makeText(CarInfoActivity.this, "Error1", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CarInfoActivity.this, "Internal error!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void initUI(){
        img_star=findViewById(R.id.img_star);
        tv_releaseDateCarInfo=findViewById(R.id.tv_releaseDateCarInfo);
        tv_priceCarInfo=findViewById(R.id.tv_priceCarInfo);
        tv_nameInfoCar=findViewById(R.id.tv_nameInfoCar);
        viewPager_infoCar=findViewById(R.id.viewPager_infoCar);
        circleIndicator_infoCar=findViewById(R.id.circle_indicator_infoCar);
        tv_nameCarInfo=findViewById(R.id.tv_nameCarInfo);
        tv_companyCarInfo=findViewById(R.id.tv_companyCarInfo);
        tv_infomationCarInfo=findViewById(R.id.tv_infomationCarInfo);
        numLike = findViewById(R.id.numLike);
        recyclerViewListStore = findViewById(R.id.recyclerViewListStore);
    }
}