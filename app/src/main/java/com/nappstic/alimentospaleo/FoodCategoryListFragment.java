package com.nappstic.alimentospaleo;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

/**
 * Created by uri on 19/2/18.
 */

public class FoodCategoryListFragment extends Fragment {

    private MyDBAdapter mDBHelper;
    private ListView lv;
    private View rootView;
    private Bundle bundle;

    private String fragmentName = "";


    public FoodCategoryListFragment () {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseAuth mFirebaseAuth;
        FirebaseUser user;

        String TAG = "fOODCATEGORYLIST";

        super.onCreate(savedInstanceState);
        try {
            FirebaseApp.initializeApp(getActivity());
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();

        if (user == null) {
            //fragment = new FoodListFragment();
            Intent i = new Intent(getActivity(),ActivityLogin.class);
            startActivity(i);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_list, container, false);

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


        lv = rootView.findViewById(R.id.lstViewCategoryList);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ID DEL ITEM", "onItemClick: id del item clickado " + id + " en la posicion: " + position);
                Fragment fragment = new FoodListTabs();
                fragmentName = "Food List";
                bundle = new Bundle();
                switch (position){
                    case 0:
                        Log.d("CATEGORIA", "onItemClick: ACEITE");
                        bundle.putString("id_category", "1" );
                        break;
                    case 1:
                        Log.d("CATEGORIA", "onItemClick: CARNE");
                        bundle.putString("id_category", "2" );
                        break;
                    case 2:
                        Log.d("CATEGORIA", "onItemClick: FRUTA");
                        bundle.putString("id_category", "3" );
                        break;
                    case 3:
                        Log.d("CATEGORIA", "onItemClick: F SECOS");
                        bundle.putString("id_category", "4" );
                        break;
                    case 4:
                        Log.d("CATEGORIA", "onItemClick: ESPECIES");
                        bundle.putString("id_category", "5" );
                        break;
                    case 5:
                        Log.d("CATEGORIA", "onItemClick: HUEVOS");
                        bundle.putString("id_category", "6" );
                        break;
                    case 6:
                        Log.d("CATEGORIA", "onItemClick: PESCADO");
                        bundle.putString("id_category", "7" );
                        break;
                    case 7:
                        Log.d("CATEGORIA", "onItemClick: SEMILLAS");
                        bundle.putString("id_category", "8" );
                        break;
                    case 8:
                        Log.d("CATEGORIA", "onItemClick: VEGETALES");
                        bundle.putString("id_category", "9" );
                        break;
                }

                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,fragmentName)
                            .addToBackStack("List Food")
                            .commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });

            getCategories();

        return rootView;
    }

    private void getCategories() {
        Cursor myCursor;

        myCursor = mDBHelper.getCategories();


        //Cursor de los alimentos que tenemos en la lista de la compra
        getActivity().startManagingCursor(myCursor);
        // miramos si hay alimentos en la lista de la compra
        //final int shoppingListCount = myCursor.getCount();

        getActivity().startManagingCursor(myCursor);
        FoodCategoryListFragmentCursoAdapter foodCategoryListFragmentCursoAdapter = new FoodCategoryListFragmentCursoAdapter(rootView.getContext(),myCursor);

        lv.setAdapter(foodCategoryListFragmentCursoAdapter);
    }
}
