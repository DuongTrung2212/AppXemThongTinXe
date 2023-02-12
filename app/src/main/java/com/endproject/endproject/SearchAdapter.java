package com.endproject.endproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Search> arrSearchs;
    private static SearchAdapter.ClickListener clickListener;
    MainActivity mainActivity;

//    private static CarAdapter.ClickListener clickListener;
    public SearchAdapter(List<Search> arrSearchs) {
        this.arrSearchs = arrSearchs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        return new ViewHolder(view);
    }
    public void updateData(List<Search> viewModels) {
        arrSearchs.clear();
        arrSearchs.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Search itemSearch=arrSearchs.get(position);
        if(itemSearch==null){
            return;
        }
        Glide.with(mainActivity.context).load(itemSearch.getImgCar()).into(holder.img_searchCar);
        Glide.with(mainActivity.context).load(itemSearch.getImgCar()).into(holder.img_searchBgCar);
        holder.tv_searchCarName.setText(itemSearch.getNameCar());
        holder.tv_searchCarCompany.setText(itemSearch.getCompanyCar());



    }

    @Override
    public int getItemCount() {
        if(arrSearchs!=null){
            return arrSearchs.size();
        }
        return 0;
    }
    public void setOnItemClickListener(SearchAdapter.ClickListener clickListener) {
        SearchAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ImageView img_searchCar;
        ImageView img_searchBgCar;
        TextView tv_searchCarName,tv_searchCarCompany;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_searchCar=itemView.findViewById(R.id.img_searchCar);
            img_searchBgCar=itemView.findViewById(R.id.img_searchBgCar);
            tv_searchCarName=itemView.findViewById(R.id.tv_searchCarName);
            tv_searchCarCompany=itemView.findViewById(R.id.tv_searchCarCompany);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getBindingAdapterPosition(), view);
        }
    }
}
