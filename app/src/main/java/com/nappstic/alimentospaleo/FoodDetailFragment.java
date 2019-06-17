package com.nappstic.alimentospaleo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FoodDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private Cursor myCursor = null;
    private String quantity;
    private String titulo;
    private CardView cardViewTitle;
    private Toolbar toolbarDetailHeader;

    private String uid;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFoodCartDatabaseReference;
    private Food mFood;
    private CollapsingToolbarLayout collToolBar;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;

    public FoodDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        uid = mFirebaseAuth.getUid();
        setHasOptionsMenu(true);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;
        final TextView txtTitulo;
        final TextView txtDesc;
        final ImageView imgFoto;
        FloatingActionButton imgAddCartBar;

        rootView = inflater.inflate(R.layout.fragment_foot_detail_collapse, container, false);
        toolbarDetailHeader = rootView.findViewById(R.id.title_toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarDetailHeader);

        mFood = (Food)this.getArguments().getSerializable("food");

        txtTitulo = rootView.findViewById(R.id.txtViewFoodName);
        txtDesc = rootView.findViewById(R.id.TxtViewDescripcion);
        imgFoto = rootView.findViewById(R.id.imgView);
        collToolBar = rootView.findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = rootView.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);
        cardViewTitle = rootView.findViewById(R.id.cardViewTitle);
        imgAddCartBar = rootView.findViewById(R.id.fab);
        Log.d("ID ALIMENTOSDETAIL", "onCreate: Id : " + mFood.getNombre_es());

        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(myCursor));

        collToolBar.setTitle(mFood.obtainCorrectLanguageName());
        txtTitulo.setText(mFood.obtainCorrectLanguageName());
        txtDesc.setText(mFood.obtainCorrectLanguageDescription());
        Glide.with(imgFoto.getContext())
                .load(mFood.getImg_firebase())
                .into(imgFoto);
        imgAddCartBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showChangeLangDialog();
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collapsingtoolbar_menu,menu);


    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.quantity_food_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.quantityFoodCartList);
        final Spinner spnr = dialogView.findViewById(R.id.spinner);
        dialogBuilder.setTitle(String.format(getResources().getString(R.string.dialogAddQuantity),titulo));
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                quantity = edt.getText().toString();
                //mDBHelper.insertIntoShoppingList(id_alimento,titulo,quantity,spnr.getSelectedItemPosition());
                if(quantity.equals("")){

                }else{

                    mFoodCartDatabaseReference = mFirebaseDatabase.getReference().child("carts");
                    final itemCart mFoodCart = new itemCart(mFood,quantity,spnr.getSelectedItem().toString(),false);

                    mFoodCartDatabaseReference.child(uid).child(mFoodCart.get_id()).setValue(mFoodCart);

                    mFoodCartDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.child(uid).child(mFoodCart.get_id()).exists()){
                                mFoodCartDatabaseReference.child(uid).child(mFoodCart.get_id()).push().setValue(mFoodCart);
                            }else{

                                mFoodCartDatabaseReference.child(uid).child(mFoodCart.get_id()).setValue(mFoodCart);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleToolbarTitleVisibility(percentage);
    }
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            //plegado
            collToolBar.setTitle(mFood.obtainCorrectLanguageName());
            cardViewTitle.setVisibility(View.INVISIBLE);
            //imgAddCartBar.setVisibility(View.VISIBLE);
            toolbarDetailHeader.setVisibility(View.VISIBLE);



        } else {
            collToolBar.setTitle("");
            cardViewTitle.setVisibility(View.VISIBLE);
            //imgAddCartBar.setVisibility(View.INVISIBLE);
            toolbarDetailHeader.setVisibility(View.INVISIBLE);
            //no plegado
        }
    }
}
