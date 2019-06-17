package com.nappstic.alimentospaleo;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FoodListFragmentCursorAdapter extends CursorAdapter {
    private Cursor ShoopingListCursor;


    public FoodListFragmentCursorAdapter(Context context, Cursor c, Cursor shoppingList) {
        super(context, c, 0);
        this.ShoopingListCursor = shoppingList;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtViewFoodName = (TextView) view.findViewById(R.id.txtViewFoodNameList);
        TextView txtViewFoodCategory = (TextView) view.findViewById(R.id.txtViewFoodCategoryList);
        //ImageView imgViewIconCategory = (ImageView) view.findViewById(R.id.ImgViewIconFoodList);
        ImageView imgViewIsInCart = (ImageView) view.findViewById(R.id.imgAddedCart);
        imgViewIsInCart.setVisibility(View.INVISIBLE);
        Typeface typeFaceBarlowRegular = Typeface.createFromAsset(context.getAssets(),"fonts/BarlowCondensed-Regular.ttf");


        String foodName = cursor.getString(cursor.getColumnIndexOrThrow("nombre_en"));
        String foodCategory = cursor.getString(cursor.getColumnIndexOrThrow("categoria_en"));
        String icName = "";
        String idFood = cursor.getString(cursor.getColumnIndexOrThrow("_id"));


        txtViewFoodCategory.setTypeface(typeFaceBarlowRegular);
        txtViewFoodName.setTypeface(typeFaceBarlowRegular);

        txtViewFoodName.setText(foodName);
        txtViewFoodCategory.setText(foodCategory);



        /*if(ShoopingListCursor.moveToFirst()){
            do {

                String id_alimento = ShoopingListCursor.getString(ShoopingListCursor.getColumnIndexOrThrow("id_alimento"));
                if (id_alimento.equals(idFood)){
                    Log.d("COMPARE_ID", foodName+ "id_alimento: " +id_alimento+ "id_food: "+idFood);
                    imgViewIsInCart.setVisibility(View.VISIBLE);
                }
            }while (ShoopingListCursor.moveToNext());
        }*/



        switch (cursor.getString(cursor.getColumnIndexOrThrow("categoria_id"))){
            case "1":
                icName = "ic_oil";
                break;
            case "2":
                icName = "ic_meat";
                break;
            case "3":
                icName = "ic_fruit";
                break;
            case "4":
                icName = "ic_nuts";
                break;
            case "5":
                icName = "ic_spice";
                break;
            case "6":
                icName = "ic_eggs";
                break;
            case "7":
                icName = "ic_fish";
                break;
            case "8":
                icName = "ic_seeds";
                break;
            case "9":
                icName = "ic_vegetables";
                break;
        }


        int id = context.getResources().getIdentifier("drawable/"+icName, null, context.getPackageName());
        //imgViewIconCategory.setImageResource(id);




    }
}
