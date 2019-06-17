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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FoodListFragment_bk extends Fragment {

    private MyDBAdapter mDBHelper;
    private ListView lv;
    private ProgressBar mProgressBar;

    Cursor myCursor;
    Cursor myShoppingListCursor;

    private FoodListFragmentArrayAdapter mfoodListFragmentArrayAdapter;

    String[] from = new String[]{MyDBAdapter.KEY_NOMBRE_EN,MyDBAdapter.KEY_CAT_EN};
//    int[] to = new int[]{R.id.txtViewFoodNameList,R.id.txtViewFoodCategoryList,R.id.ImgViewIconFoodList};
    int[] to = new int[]{R.id.txtViewFoodNameList,R.id.txtViewFoodCategoryList,R.id.foodListImg};
    String icName;
    View rootView;
    Bundle bundle;
    Bundle bundleCategory;
    Query QueryByCategory;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFoodListDatabaseReference;
    private ChildEventListener mChildEventListener;


    public FoodListFragment_bk() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        String myValue = "0";
        bundleCategory = this.getArguments();
        if (bundleCategory != null){
            myValue = this.getArguments().getString("id_category");
            if (myValue != "0"){
                Log.d("MYVALUE", "onCreate: "+myValue);
                QueryByCategory = mFirebaseDatabase.getReference().child("alimentos").orderByChild("categoria_id").equalTo(myValue);
            }
        }else {

            mFoodListDatabaseReference = mFirebaseDatabase.getReference().child("alimentos");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
        lv = (ListView) rootView.findViewById(R.id.lstViewAlimentos);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarFoodList);


        List<Food> foodList = new ArrayList<>();
        mfoodListFragmentArrayAdapter = new FoodListFragmentArrayAdapter(getActivity(),R.layout.row,foodList);
        lv.setAdapter(mfoodListFragmentArrayAdapter);

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

        mProgressBar.setVisibility(View.VISIBLE);
/*
        mFoodListDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearFoodList();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()){
                    Food food = foodSnapshot.getValue(Food.class);
                    updateFoodList(food);
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });*/
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
        }
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
        /*mChildEventListener  = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Food food = dataSnapshot.getValue(Food.class);
                mfoodListFragmentArrayAdapter.add(food);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mFoodListDatabaseReference.addChildEventListener(mChildEventListener);
*/
        //fillData();
        return rootView;

    }
    @SuppressWarnings("deprecation")
    private void fillData() {

        myCursor = mDBHelper.fetchAllCodes();
        myShoppingListCursor = mDBHelper.fetchShoppingList();

        //Cursor de los alimentos que tenemos en la lista de la compra
        getActivity().startManagingCursor(myShoppingListCursor);
        // miramos si hay alimentos en la lista de la compra
        //final int shoppingListCount = myShoppingListCursor.getCount();
        //Log.v("Cursor ShoppingList", DatabaseUtils.dumpCursorToString(myShoppingListCursor));

        getActivity().startManagingCursor(myCursor);
        FoodListFragmentCursorAdapter foodlistCursorAdapter = new FoodListFragmentCursorAdapter(rootView.getContext(),myCursor,myShoppingListCursor);
        lv.setAdapter(foodlistCursorAdapter);
    }
    private void updateFoodList( Food food ) {
        mfoodListFragmentArrayAdapter.add(food);
    }
    private void clearFoodList(){
        mfoodListFragmentArrayAdapter.clear();
    }
}
