package com.endproject.endproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    List<Store> listStore;
    private static StoreAdapter.ClickListener clickListener;

    public StoreAdapter(List<Store> listStore) {
        this.listStore = listStore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Store itemStore = listStore.get(position);
        Log.d("nameStore", itemStore.getName());
        holder.storeName.setText(itemStore.getName());
        holder.storeAddress.setText(itemStore.getAddress());
    }

    @Override
    public int getItemCount() {
        if(listStore!=null){
            return listStore.size();
        }
        return 0;
    }
    public void setOnItemClickListener(StoreAdapter.ClickListener clickListener) {
        StoreAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storeName, storeAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getBindingAdapterPosition(), view);
        }
    }
}
