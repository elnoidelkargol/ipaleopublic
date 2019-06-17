package com.nappstic.alimentospaleo;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FoodListFragment extends Fragment {

    private MyDBAdapter mDBHelper;
    private ListView lv;

    Cursor myCursor;

    private FoodListFragmentArrayAdapter mfoodListFragmentArrayAdapter;

    View rootView;
    Bundle bundle;

    public FoodListFragment() {


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
        lv =  rootView.findViewById(R.id.lstViewAlimentos);
        //ProgressBar mProgressBar =  rootView.findViewById(R.id.progressBarFoodList);

        mDBHelper = new MyDBAdapter(getActivity());
        try {
            mDBHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create DB");
        }
        mDBHelper.openDataBase();

        //mProgressBar.setVisibility(View.VISIBLE);
/*
        if (bundleCategory != null){
            QueryByCategory.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    clearFoodList();
                    for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        Food food = foodSnapshot.getValue(Food.class);
                        updateFoodList(food);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());

                }
            });
        }else {


            mFoodListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    clearFoodList();
                    for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        Food food = foodSnapshot.getValue(Food.class);
                        updateFoodList(food);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());

                }
            });
        }*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Food mFood = (Food) parent.getItemAtPosition(position);
                Fragment fragment = null;

                Log.d("ID DEL ITEM", "onItemClick: nombre del item clickado " + mFood.get_id());
                Log.d("ID DEL ITEM", "onItemClick: id del item clickado " + id);


                fragment = new FoodDetailFragment();
                bundle = new Bundle();
                bundle.putSerializable("food",mFood);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment,"Detail Food")
                        .addToBackStack("Detail Food")
                        .commit();

            }
        });

        fillData();
        return rootView;

    }
    @SuppressWarnings("deprecation")
    private void fillData() {
        myCursor = mDBHelper.fetchAllCodes();
        getActivity().startManagingCursor(myCursor);
        List<Food> foodList = new ArrayList<>();
        mfoodListFragmentArrayAdapter = new FoodListFragmentArrayAdapter(getActivity(),R.layout.row,foodList);
        lv.setAdapter(mfoodListFragmentArrayAdapter);

        for (myCursor.moveToFirst();!myCursor.isAfterLast();myCursor.moveToNext()){
            Food mfood = new Food();
            mfood.set_id(myCursor.getString(myCursor.getColumnIndexOrThrow("_id")));
            mfood.setCategoria_ca(myCursor.getString(myCursor.getColumnIndexOrThrow("categoria_ca")));
            mfood.setCategoria_en(myCursor.getString(myCursor.getColumnIndexOrThrow("categoria_en")));
            mfood.setCategoria_es(myCursor.getString(myCursor.getColumnIndexOrThrow("categoria_es")));
            mfood.setDescripcio_ca(myCursor.getString(myCursor.getColumnIndexOrThrow("descripcio_ca")));
            mfood.setDescripcio_es(myCursor.getString(myCursor.getColumnIndexOrThrow("descripcio_es")));
            mfood.setDescripcio_en(myCursor.getString(myCursor.getColumnIndexOrThrow("descripcio_en")));
            mfood.setImg_firebase(myCursor.getString(myCursor.getColumnIndexOrThrow("img_firebase")));
            mfood.setNombre_ca(myCursor.getString(myCursor.getColumnIndexOrThrow("nombre_ca")));
            mfood.setNombre_es(myCursor.getString(myCursor.getColumnIndexOrThrow("nombre_es")));
            mfood.setNombre_en(myCursor.getString(myCursor.getColumnIndexOrThrow("nombre_en")));

            foodList.add(mfood);

        }

    }

}
