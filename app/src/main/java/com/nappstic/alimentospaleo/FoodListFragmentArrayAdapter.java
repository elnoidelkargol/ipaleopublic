package com.nappstic.alimentospaleo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by uri on 4/4/18.
 */

public class FoodListFragmentArrayAdapter extends ArrayAdapter<Food> {

    FoodListFragmentArrayAdapter(Context context, int resource, List<Food> objects) {
        super(context,resource,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row,parent,false);
        }

        TextView txtFoodName =  convertView.findViewById(R.id.txtViewFoodNameList);
        TextView txtFoodCategory =  convertView.findViewById(R.id.txtViewFoodCategoryList);
        //ImageView imgFood = (ImageView) convertView.findViewById(R.id.ImgViewIconFoodList);
        CircleImageView imgFood =  convertView.findViewById(R.id.foodListImg);


        Food food = getItem(position);

        Glide.with(imgFood.getContext())
                .load(food.getImg_firebase())
                .into(imgFood);
        String lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                txtFoodName.setText(food.getNombre_es());
                txtFoodCategory.setText(food.getCategoria_es());
                break;
            case "ca":
                txtFoodName.setText(food.getNombre_ca());
                txtFoodCategory.setText(food.getCategoria_ca());
                break;
            case "en":
                txtFoodName.setText(food.getNombre_en());
                txtFoodCategory.setText(food.getCategoria_en());
                break;
            default:
                txtFoodName.setText(food.getNombre_es());
                txtFoodCategory.setText(food.getCategoria_es());
        }

        //txtFoodName.setText(food.get);


        return convertView;
    }
}
