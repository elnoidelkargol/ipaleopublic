package com.nappstic.alimentospaleo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.Locale;

/**
 * Created by uri on 19/2/18.
 */

public class FoodCategoryListFragmentCursoAdapter extends CursorAdapter {

    FoodCategoryListFragmentCursoAdapter(Context context, Cursor c) {
        super(context, c, 0);
        String language = Locale.getDefault().toString();
        Log.d("idioma", "bindView: "+ language);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.fragment_category_list_row,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Typeface typeFaceBarlowBold;
        ImageView catBackground;
        ImageView catIcon;
        TextView txtCat;
        String imgBackground;
        String catIconTxt;
        String txtCategoria;

        typeFaceBarlowBold = Typeface.createFromAsset(context.getAssets(),"fonts/BarlowCondensed-Bold.ttf");
        catBackground = view.findViewById(R.id.imgViewCatListBackground);
        catIcon = view.findViewById(R.id.imgViewCategoryListImage);
        txtCat = view.findViewById(R.id.txtViewCategoryListName);

        imgBackground = cursor.getString(cursor.getColumnIndexOrThrow("background_cat_list"));
        catIconTxt = cursor.getString(cursor.getColumnIndexOrThrow("icon_for_category_list"));
        txtCategoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria_nombre_es"));

        Log.d("CATIMG", "Category Image: " + imgBackground);

        //imgBackground = "ic_fish";
        txtCat.setText(txtCategoria);
        int id = context.getResources().getIdentifier("drawable/"+catIconTxt, null, context.getPackageName());
        catIcon.setImageResource(id);
        int id2 = context.getResources().getIdentifier("drawable/"+imgBackground,null,context.getPackageName());
        catBackground.setImageResource(id2);

        txtCat.setTypeface(typeFaceBarlowBold);

        Glide.with(catBackground.getContext())
                .load(imgBackground)
                .into(catBackground);



    }
}
