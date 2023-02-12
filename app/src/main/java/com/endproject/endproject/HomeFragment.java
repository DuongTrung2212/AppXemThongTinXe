package com.endproject.endproject;

import static api.APIServices.gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import api.APIServices;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView home_recyclerView;
    TextView loadMore;
    public static CarAdapter carAdapter;
    public static Context context;
    public static String trung;
    TextView tv_title;
    public static List<Cars> arrCars;
    List<Cars> originArrCars=new ArrayList<>();
    public static View homeView;
    ImageSlider imageSlider_home;
    private static   MainActivity mainActivity;
    public static int page=1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {

        // Required empty public constructor
    }
    public void update(List<Cars> arr){
        carAdapter.updateData(arr);
    }
    public static void resetData(){
        dataDefault();
        carAdapter.updateData(arrCars);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeView=inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        arrCars=new ArrayList<>();

        context=getContext();
        mainActivity= (MainActivity) getActivity();
        tv_title=homeView.findViewById(R.id.tv_title);
        loadMore=homeView.findViewById(R.id.loadMore);
        home_recyclerView=homeView.findViewById(R.id.home_recyclerView);
        imageSlider_home=homeView.findViewById(R.id.imageSlider_home);

        home_recyclerView.setNestedScrollingEnabled(false);
        dataDefault();
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://xhs.vn/upload/images/quang-cao-o-to-1.png", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://invietlong.com/data/images/mau-to-roi-quang-cao-oto-4.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("http://chothuexe.net.vn/assets/media/emb-s-class-banner-1000x370.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://img3.thuthuatphanmem.vn/uploads/2019/10/08/banner-quang-cao-khuyen-mai-sang-trong_103212930.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://i.ytimg.com/vi/XJy9ieFWgjo/maxresdefault.jpg", ScaleTypes.FIT));
        imageSlider_home.setImageList(slideModels,ScaleTypes.FIT);

//RecyclerView
        carAdapter=new CarAdapter(arrCars);
        carAdapter.setOnItemClickListener(new CarAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Cars car=arrCars.get(position);
//                Toast.makeText(mainActivity, ""+car.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainActivity,CarInfoActivity.class);
                intent.putExtra("id", car.getId()+"");
                intent.putExtra("idUser",mainActivity.idUser);
                intent.putExtra("name", car.getName()+"");
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayout=new GridLayoutManager(mainActivity,2);
        home_recyclerView.setLayoutManager(gridLayout);
        home_recyclerView.setAdapter(carAdapter);
//        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(mainActivity,DividerItemDecoration.VERTICAL);
//        home_recyclerView.addItemDecoration(itemDecoration);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                APIServices.apiServices.listValues(page).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, retrofit2.Response<ArrayList> response) {
                        ArrayList arr= response.body();
                        if(arr.size()<4){
                            loadMore.setVisibility(View.GONE);
                        }
                        else {
                            loadMore.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < arr.size(); i++) {
                            JsonObject jsonObject = gson.toJsonTree(arr.get(i)).getAsJsonObject();
                            JsonElement jsonID=jsonObject.get("id");
                            int id=(int) Float.parseFloat(jsonID.toString()) ;
                            JsonElement name = jsonObject.get("name");

                            JsonElement img=jsonObject.get("image");
                            JsonElement company=jsonObject.get("company_name");
                            arrCars.add(new Cars(id,"http://"+img.getAsString(),name.getAsString(),company.getAsString()));

                        }

                        carAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {

                    }
                });
            }
        });



        return homeView;

    }

    public void changeTitle(){
//        View vie=homeView;
        TextView tv=homeView.findViewById(R.id.tv_title);
        tv.setText("Home");
    }
    public void hideLoadMore(){
//        View vie=homeView;
        TextView load=homeView.findViewById(R.id.loadMore);
        load.setVisibility(View.GONE);
    }

    public static void dataDefault(){
        List<Cars> arrNewCars=new ArrayList<>();
//        recyclerViewSearch.setVisibility(View.VISIBLE);
        page=1;
        TextView imageView= homeView.findViewById(R.id.loadMore);

        APIServices.apiServices.homeValues().enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(Call<ArrayList> call, retrofit2.Response<ArrayList> response) {
                ArrayList arr= response.body();
                if(arr.size()<4){
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject jsonObject = gson.toJsonTree(arr.get(i)).getAsJsonObject();
                    JsonElement jsonID=jsonObject.get("id");
                    int id=(int) Float.parseFloat(jsonID.toString()) ;
                    JsonElement name = jsonObject.get("name");

                    JsonElement img=jsonObject.get("image");
                    JsonElement company=jsonObject.get("company_name");
                    arrCars.add(new Cars(id,"http://"+img.getAsString(),name.getAsString(),company.getAsString()));

                }

                carAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ArrayList> call, Throwable t) {

            }
        });

    }
    public void reloadData(){

    }

}