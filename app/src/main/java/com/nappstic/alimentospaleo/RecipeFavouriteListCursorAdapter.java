package com.nappstic.alimentospaleo;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RecipeFavouriteListCursorAdapter extends CursorAdapter{
    //private Cursor favouriteRecipeList;


    public RecipeFavouriteListCursorAdapter(Context context, Cursor cursor) {
        super(context,cursor,0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.row_recipes,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNumberPeople = (TextView) view.findViewById(R.id.txtVIewNumberOfpeople);
        TextView txtRecipeTime = (TextView) view.findViewById(R.id.txtViewTimeToCook);
        TextView txtRecipeName = (TextView) view.findViewById(R.id.txtViewRecipeName);
        ImageView imgRecipe = (ImageView) view.findViewById(R.id.imgViewRecipe);
        //ImageView favourite = (ImageView)view.findViewById(R.id.imgViewRecipeFavourite);

        txtNumberPeople.setText(cursor.getString(cursor.getColumnIndexOrThrow("personas")));
        txtRecipeTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("tiempo")));
        txtRecipeName.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre_en")));
        Glide.with(imgRecipe.getContext()).load(cursor.getString(cursor.getColumnIndexOrThrow("foto"))).into(imgRecipe);

    }

}
