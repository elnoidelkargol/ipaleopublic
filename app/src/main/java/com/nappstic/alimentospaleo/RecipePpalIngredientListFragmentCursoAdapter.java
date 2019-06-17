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

public class RecipePpalIngredientListFragmentCursoAdapter extends CursorAdapter {
    private Cursor CategoriesCursor;
    private String language;

    public RecipePpalIngredientListFragmentCursoAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.language = Locale.getDefault().toString(); // es_ES
        Log.d("idioma", "bindView: "+ language);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.fragment_ingredient_recipe_list_row,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Typeface typeFaceBarlowBold = Typeface.createFromAsset(context.getAssets(),"fonts/BarlowCondensed-Bold.ttf");
        ImageView catBackground = (ImageView) view.findViewById(R.id.imgViewCatListBackground);
        TextView txtCat = (TextView) view.findViewById(R.id.txtViewCategoryListName);

        String imgBackground = cursor.getString(cursor.getColumnIndexOrThrow("imagen"));
        //String catIconTxt = cursor.getString(cursor.getColumnIndexOrThrow("icon_for_category_list"));
        String txtCategoria = cursor.getString(cursor.getColumnIndexOrThrow("nombre_es"));

        Log.d("CATIMG", "Category Image: " + imgBackground);

        txtCat.setText(txtCategoria);
        txtCat.setTypeface(typeFaceBarlowBold);
        Glide.with(catBackground.getContext())
                .load(imgBackground)
                .into(catBackground);



    }
}
