package com.endproject.endproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>{
    private List<Cars> arrCars;
    private static ClickListener clickListener;
    MainActivity mainActivity;
    public CarAdapter(List<Cars> arrCars) {
        this.arrCars = arrCars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item,parent,false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        Cars car=arrCars.get(position);
        if(car==null){
            return;
        }
        Glide.with(mainActivity.context).load(car.getImg()).into(holder.img_car);
        holder.tv_nameCar.setText(car.getName());
        holder.tv_priceCar.setText(car.getPrice());
    }

    @Override
    public int getItemCount() {
        if(arrCars!=null){
            return arrCars.size();
        }
        return 0;
    }
    public void updateData(List<Cars> viewModels) {
        arrCars.clear();
        arrCars.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CarAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_car;
        TextView tv_nameCar;
        TextView tv_priceCar;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_car=itemView.findViewById(R.id.img_car);
            tv_nameCar=itemView.findViewById(R.id.tv_nameCar);
            tv_priceCar=itemView.findViewById(R.id.tv_priceCar);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getBindingAdapterPosition(), v);
        }
    }
}
