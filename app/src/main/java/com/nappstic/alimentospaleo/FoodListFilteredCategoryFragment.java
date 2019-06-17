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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FoodListFilteredCategoryFragment extends Fragment {

    private MyDBAdapter mDBHelper;
    private ListView lv;
    private ProgressBar mProgressBar;
    private Button buttonClear;

    Fragment fragment = null;


    Cursor myCursor;
    Cursor myShoppingListCursor;

    private FoodListFragmentArrayAdapter mfoodListFragmentArrayAdapter;

    //String[] from = new String[]{MyDBAdapter.KEY_NOMBRE_EN,MyDBAdapter.KEY_CAT,MyDBAdapter.KEY_ID_CAT};
    //int[] to = new int[]{R.id.txtViewFoodNameList,R.id.txtViewFoodCategoryList,R.id.ImgViewIconFoodList};
    String icName;
    View rootView;
    Bundle bundle;
    String categoryId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFoodListDatabaseReference;
    private ChildEventListener mChildEventListener;


    public FoodListFilteredCategoryFragment() {


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_list_filtered_category, container, false);
        lv = (ListView) rootView.findViewById(R.id.lstViewAlimentos);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarFoodList);
        buttonClear = (Button) rootView.findViewById(R.id.buttonClearCategory);


        mDBHelper = new MyDBAdapter(getActivity());
        try {
            mDBHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create DB");
        }
        try {
            mDBHelper.openDataBase();
        } catch(SQLException sqle) {
            throw sqle;
        }

        //mProgressBar.setVisibility(View.VISIBLE);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = new Bundle();
                bundle.putString("id_category","0");
                fragment = new FoodListTabs();
                fragment.setArguments(bundle);
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,"Food List")
                            .addToBackStack("Food List")
                            .commit();

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Food mFood = (Food) parent.getItemAtPosition(position);

                Log.d("ID DEL ITEM", "onItemClick: nombre del item clickado " + mFood.get_id());
                Log.d("ID DEL ITEM", "onItemClick: id del item clickado " + id);


                fragment = new FoodDetailFragment();
                bundle = new Bundle();
                bundle.putSerializable("food",mFood);
                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,"Detail Food")
                            .addToBackStack("Detail Food")
                            .commit();

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });

        fillData();
        return rootView;

    }
    @SuppressWarnings("deprecation")
    private void fillData() {
        String value = "";
        bundle = this.getArguments();
        value = bundle.getString("id_category");
        myCursor = mDBHelper.fetchListFoodByCategory(value);
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
