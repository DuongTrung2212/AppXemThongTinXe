package com.endproject.endproject;

import static api.APIServices.gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.endproject.endproject.placeholder.TabAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.APIServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout_setting,layout_top;
    ImageView image_setting,icon_filter,loadMore;
    SearchView editSearch;
    ViewPager2 view_pagerMain;
    BottomNavigationView bottomNavigationView;
    TabAdapter tabAdapter;
    TextView tv_title;
    HomeFragment homeFragment;
    CarAdapter carAdapter;
    public  static String idUser="";
    FilterAdapter filterAdapter;
    SearchAdapter searchAdapter;
    RecyclerView home_recyclerView,recyclerViewSearch,recyclerViewFilter;
    List<Filter> arrFilters;
    List<Search> arrSearchs;
    List<Cars> arrCars;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Animation animRotate= AnimationUtils.loadAnimation(this,R.anim.anim_rotate);
        initUI();
        bottom();

        Intent intent = getIntent();
        idUser=intent.getStringExtra("idUser");

        recyclerViewSearch=findViewById(R.id.recyclerViewSearch);
        recyclerViewFilter=findViewById(R.id.recyclerViewFilter);
        homeFragment=new HomeFragment();
        arrSearchs=new ArrayList<>();
        arrFilters=new ArrayList<>();
        dataCompany();
//        arrFilters.add(new Filter(R.drawable.anime_bg3,"Honda"));
//        arrFilters.add(new Filter(R.drawable.anime_bg3,"Honda"));

        searchAdapter=new SearchAdapter(arrSearchs);
        filterAdapter=new FilterAdapter(arrFilters);
        recyclerViewFilter.setAdapter(filterAdapter);
        recyclerViewSearch.setAdapter(searchAdapter);
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewSearch.addItemDecoration(itemDecoration);
        editSearch.clearFocus();
        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Cars> arrNewSearchs=new ArrayList<>();
                APIServices.apiServices.searchValues(query).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                        ArrayList arr= response.body();
                        for (int i = 0; i < arr.size(); i++) {
                            JsonObject jsonObject = gson.toJsonTree(arr.get(i)).getAsJsonObject();
//                            try {
                            JsonElement jsonID=jsonObject.get("id");
                            int id=(int) Float.parseFloat(jsonID.toString()) ;
                            JsonElement name = jsonObject.get("name");
                            JsonElement img=jsonObject.get("image");
                            arrNewSearchs.add(new Cars(id, "http://"+img.getAsString(),name.getAsString(),"AAA"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                        }
                        homeFragment.update(arrNewSearchs);
                    }

                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {
                    }
                });
//                Toast.makeText(MainActivity.this, "AAA"+query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Search> arrNewSearchs=new ArrayList<>();
                recyclerViewSearch.setVisibility(View.VISIBLE);
                recyclerViewFilter.setVisibility(View.GONE);
                if(newText.trim().equals("")){
                    arrNewSearchs.clear();
                    searchAdapter.updateData(arrNewSearchs);
                    return false;
                }
                APIServices.apiServices.searchValues(newText).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                        ArrayList arr= response.body();
                        for (int i = 0; i < arr.size(); i++) {
                            JsonObject jsonObject = gson.toJsonTree(arr.get(i)).getAsJsonObject();
//                            try {
                                JsonElement jsonID=jsonObject.get("id");
                                int id=(int) Float.parseFloat(jsonID.toString()) ;
                                JsonElement name = jsonObject.get("name");
                                JsonElement img=jsonObject.get("image");
                                JsonElement company=jsonObject.get("company");
                                arrNewSearchs.add(new Search(id, "http://" +img.getAsString(),"http://"+img.getAsString(),name.getAsString(),company.getAsString()));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                        }
                        searchAdapter.updateData(arrNewSearchs);
                    }

                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {
                    }
                });
                return true;
            }
        });
        icon_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewSearch.setVisibility(View.GONE);
                if(recyclerViewFilter.getVisibility()==View.VISIBLE){
                    recyclerViewFilter.setVisibility(View.GONE);
                }else{
                    recyclerViewFilter.setVisibility(View.VISIBLE);
                }
            }
        });

            ///////
        searchAdapter.setOnItemClickListener(new SearchAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Search item= arrSearchs.get(position);
                Intent intent=new Intent(MainActivity.this,CarInfoActivity.class);
                intent.putExtra("id",item.getIdCar()+"");
                intent.putExtra("idUser",idUser);
                intent.putExtra("name",item.getNameCar()+"");
                startActivity(intent);
            }
        });
        filterAdapter.setOnItemClickListener(new FilterAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("trung",arrFilters.get(position).getId()+"");
                updateDataHome(arrFilters.get(position).getId()+"");
            }
        });
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });

    }

    public void dataSearch(){

    }
    public void dataCompany(){
        APIServices.apiServices.companyValues().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name =jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        String img = jsonObject.getString("logo");
                        arrFilters.add(new Filter("http://" + img,name, id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                String name = jsonObject.get("name");
//                JsonElement id = jsonObject.get("id");
//                Log.d("company", name.toString());
//
//                JsonElement img=jsonObject.get("logo");
//
//                String image="";
//                if(jsonObject.get("logo")!=null){
//                    image="http://"+img.getAsString();
//                }
//                arrFilters.add(new Filter(image,name.getAsString()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void  updateDataHome(String company){
        homeFragment.hideLoadMore();
        ArrayList<Cars> arrNewCar=new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        String url=APIServices.baseLink+"car/cars-of-company/"+company;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray array=null;
                JSONObject company=null;
                try {
                    company=response.getJSONObject("company");
                    array=response.getJSONArray("cars");
                    String companyName=company.getString("name");;
                    for (int i = 0;i<array.length();i++){
                        JSONObject object= array.getJSONObject(i);
                        int id=object.getInt("id");
                        String name = object.getString("name");
                        String img=object.getString("image");
                        arrNewCar.add(new Cars(id,"http://"+img,name,companyName));
                    }

                    TextView tv_title=findViewById(R.id.tv_title);
                    tv_title.setText(companyName);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                homeFragment.update(arrNewCar);
//                carAdapter.updateData(arrNewCar);

                Log.d("trung",array.length()+"");

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("trung","Loi");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View view = getCurrentFocus();
//            if (view != null && view instanceof EditText) {
//                Rect r = new Rect();
//                view.getGlobalVisibleRect(r);
//                int rawX = (int)ev.getRawX();
//                int rawY = (int)ev.getRawY();
//                if (!r.contains(rawX, rawY)) {
//                    view.clearFocus();
////
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void bottom(){
        tabAdapter=new TabAdapter(this);
        view_pagerMain.setAdapter(tabAdapter);

        view_pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        layout_top.setVisibility(View.VISIBLE);
                        bottomNavigationView.getMenu().findItem(R.id.tabHome).setChecked(true);
                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.tabRank).setChecked(true);
//                        break;
                    case 1:
                        layout_top.setVisibility(View.GONE);
                        recyclerViewFilter.setVisibility(View.GONE);
                        recyclerViewSearch.setVisibility(View.GONE);
                        bottomNavigationView.getMenu().findItem(R.id.tabSetting).setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id){
                    case R.id.tabHome:
//                        if (homeFragment.getActivity()!=null){
//                            carAdapter = new CarAdapter(arrCars);
//                            home_recyclerView.setAdapter(carAdapter);
//                            TextView textView=findViewById(R.id.tv_title);
//                            textView.setText("AAAA");
//                        }
                        recyclerViewFilter.setVisibility(View.GONE);
                        recyclerViewSearch.setVisibility(View.GONE);
                       homeFragment.changeTitle();
                        homeFragment.resetData();
                        view_pagerMain.setCurrentItem(0);

                        bottomNavigationView.getMenu().findItem(R.id.tabSetting);

                        break;
//                    case R.id.tabRank:
//                        view_pagerMain.setCurrentItem(1);
//                        break;
                    case R.id.tabSetting:
                        view_pagerMain.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });
    }

    private void initUI(){
//        layout_setting=findViewById(R.id.layout_setting);
//        image_setting=findViewById(R.id.img_setting);

        tv_title=findViewById(R.id.tv_title);
        layout_top=findViewById(R.id.layout_top);
        icon_filter=findViewById(R.id.icon_filter);
            view_pagerMain=findViewById(R.id.view_pagerMain);
            editSearch=findViewById(R.id.editSearch);
            bottomNavigationView=findViewById(R.id.bottomNavigationView);
    }
}