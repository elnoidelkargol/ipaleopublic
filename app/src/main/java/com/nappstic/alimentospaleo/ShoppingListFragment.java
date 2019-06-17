package com.nappstic.alimentospaleo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uri on 21/10/17.
 */

public class ShoppingListFragment extends Fragment {

    private View rootView;

    private ShoppingListFragmentArrayAdapter mshoppingListFragmentArrayAdapter;
    private ProgressBar mProgressBar;
    private CheckBox mcheckBox;
    private Button mBtnClear;

    private ArrayList<String> checkeds;

    private String uid;

    List<itemCart> itemCarts;

    private itemCart mitemCart;

    private ListView mListView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemCartReference;

    public ShoppingListFragment (){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemCartReference = mFirebaseDatabase.getReference().child("carts").child(uid);

        /*
        mItemCartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearItemCartList();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    mitemCart = itemSnapshot.getValue(itemCart.class);
                    updateitemCartList(mitemCart);
                }
                mProgressBar.setVisibility(View.INVISIBLE);


                mshoppingListFragmentArrayAdapter = new ShoppingListFragmentArrayAdapter(getActivity(), R.layout.row_shopping_cart, itemCarts, new ItemClickListener() {
                    @Override
                    public void onItemClickListener(itemCart mitemCart) {
                        Log.d("checkbox", "onItemClickListener: 2" + mitemCart.obtainCorrectLanguageName());
                    }
                });
                mListView.setAdapter(mshoppingListFragmentArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mItemCartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearItemCartList();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    mitemCart = itemSnapshot.getValue(itemCart.class);
                    updateitemCartList(mitemCart);
                }
                mProgressBar.setVisibility(View.INVISIBLE);


                mshoppingListFragmentArrayAdapter = new ShoppingListFragmentArrayAdapter(getActivity(), R.layout.row_shopping_cart, itemCarts, new ItemClickListener() {
                    @Override
                    public void onItemClickListener(itemCart mitemCart) {
                        Log.d("checkbox", "onItemClickListener: 2" + mitemCart.obtainCorrectLanguageName());
                    }
                });
                mListView.setAdapter(mshoppingListFragmentArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_card_list,container,false);

        mListView = (ListView) rootView.findViewById(R.id.lstViewShoppingCart);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarCartFoodList);
        mBtnClear = (Button) rootView.findViewById(R.id.btnClearShoppingList);
        itemCarts = new ArrayList<>();

        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ITEMLIST", "onItemClick: CLICKED");
            }
        });


        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkeds = mshoppingListFragmentArrayAdapter.getCheckedIds();
                Integer cont = 0;
                for (String id:checkeds) {
                    cont ++;
                    Log.d("checkeds", cont + "onClick: "+id);
                    mItemCartReference.child(id).removeValue();


                }
            }
        });


        return rootView;
    }



    private void updateitemCartList(itemCart item ) {
        itemCarts.add(item);
    }
    private void clearItemCartList(){
        itemCarts.clear();
    }

}
