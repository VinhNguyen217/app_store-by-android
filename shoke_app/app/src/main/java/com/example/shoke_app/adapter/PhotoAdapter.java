package com.example.shoke_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.shoke_app.R;
import com.example.shoke_app.model.Photo;

import java.util.ArrayList;

//import androidx.viewpager.widget.PagerAdapter;

public class PhotoAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Photo> photos;

    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView img_photo = view.findViewById(R.id.img_photo);

        Photo photo = photos.get(position);
        if(photo != null){
            Glide.with(context).load(photo.getResourceId()).into(img_photo);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(photos!=null)
            return photos.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
