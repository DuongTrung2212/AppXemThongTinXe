package com.endproject.endproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    private List<Slide> arrSlides;

    public SlideAdapter(Context context, List<Slide> arrSlides) {
        this.context = context;
        this.arrSlides = arrSlides;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide,container,false);
        ImageView img_infoPhoto=view.findViewById(R.id.img_infoPhoto);

        Slide slide=arrSlides.get(position);
        if(slide!=null){
            Glide.with(context).load(slide.getImg()).into(img_infoPhoto);
        }
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(arrSlides!=null){
            return  arrSlides.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
//        super.destroyItem(container, position, object);
    }
}
