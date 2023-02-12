package com.endproject.endproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<Filter> arrFilters;
    private static FilterAdapter.ClickListener clickListener;
    MainActivity mainActivity;
    public FilterAdapter(List<Filter> arrFilters) {
        this.arrFilters = arrFilters;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter,parent,false);
        return new ViewHolder(view);
    }
    public void updateData(List<Filter> viewModels) {
        arrFilters.clear();
        arrFilters.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Filter itemFilter=arrFilters.get(position);
        String url="https://img.icons8.com/color/96/toyota.png";
        if(itemFilter==null){
            return;
        }

        Glide.with(mainActivity.context).load(itemFilter.getImg_Company()).error(R.drawable.anime_bg1).into(holder.img_filter);
//        holder.img_filter.setImageResource(itemFilter.getImg_Company());
        holder.tv_nameFilter.setText(itemFilter.getCompanyName());

    }

    @Override
    public int getItemCount() {
        if(arrFilters!=null){
            return arrFilters.size();
        }
        return 0;
    }
    public void setOnItemClickListener(FilterAdapter.ClickListener clickListener) {
        FilterAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        CircleImageView img_filter;
        TextView tv_nameFilter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_filter=itemView.findViewById(R.id.img_filter);
            tv_nameFilter=itemView.findViewById(R.id.tv_nameFilter);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getBindingAdapterPosition(), view);
        }
    }
}
