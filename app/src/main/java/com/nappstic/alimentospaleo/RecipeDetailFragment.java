package com.nappstic.alimentospaleo;

import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class RecipeDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener{

    private Toolbar toolbarHeader;
    private CardView CVDetailRecipe;
    private CollapsingToolbarLayout collToolBar;

    private MyDBAdapter mDBHelper;
    private Recipe mRecipe;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getUid();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        TextView numberPeople = rootview.findViewById(R.id.txtVIewDetailNumberOfpeople);
        TextView time = rootview.findViewById(R.id.txtViewDetailTimeToCook);
        TextView descriptionRecipe = rootview.findViewById(R.id.TxtViewDetailDescripcionRecipe);
        TextView nameRecipe = rootview.findViewById(R.id.txtViewDetailRecipeName);
        TextView fontRecipe = rootview.findViewById(R.id.txtViewFont);
        AppBarLayout mAppbarLayout = rootview.findViewById(R.id.app_bar_layout_Recipe);
        FloatingActionButton addFavourite = rootview.findViewById(R.id.addFabButtonFloating);
        toolbarHeader = rootview.findViewById(R.id.title_toolbar_detail_recipe);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarHeader);
        CVDetailRecipe = rootview.findViewById(R.id.CVDetailRecipe);
        TextView recipeIngredients = rootview.findViewById(R.id.txtViewRecipeDetailIngredients);
        collToolBar = rootview.findViewById(R.id.recipeDetailCollapsingToolbar);
        ImageView imgViewRecipe = rootview.findViewById(R.id.imgViewDetailRecipe);

        mAppbarLayout.addOnOffsetChangedListener(this);

        mRecipe = (Recipe) this.getArguments().getSerializable("recipe");


        recipeIngredients.setText(Html.fromHtml(mRecipe.obtainCorrectIngredients()));
        time.setText(mRecipe.getTiempo().toString());
        numberPeople.setText(mRecipe.getPersonas().toString());
        nameRecipe.setText(mRecipe.obtainCorrectName());
        descriptionRecipe.setText(Html.fromHtml(mRecipe.obtainCorrectPreparation()));
        fontRecipe.setText(getString(R.string.font) + ": " + mRecipe.getFuente());
        if (mRecipe.getFavourite() != null) {
            if (mRecipe.getFavourite() == true){
                addFavourite.setImageResource(R.mipmap.delete_favourite);
            } else {
                addFavourite.setImageResource(R.mipmap.add_favourite);
            }
        }
        Glide.with(imgViewRecipe.getContext())
                .load(mRecipe.getFoto())
                .into(imgViewRecipe);


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

        addFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(!mRecipe.getFavourite()){

                        mDBHelper.insertToFavouriteRecipe(mRecipe);
                        Toast.makeText(getActivity().getApplicationContext(),R.string.FavouriteRecipeAdded,Toast.LENGTH_LONG).show();
                        //final FavouriteRecipe mFavRecipe= new FavouriteRecipe(mRecipe.getId().toString(),mRecipe.getNombre_ca(),mRecipe.getNombre_es(),mRecipe.getNombre_en(),mRecipe.getPersonas().toString(),mRecipe.getTiempo().toString(),mRecipe.getFoto());
                        //mDatabaseReferenceRecipes = mFirebaseDatabase.getReference().child("recipes").child(mRecipe.getId().toString());
                        //mDatabaseReferenceFavouriteRecipes = mFirebaseDatabase.getReference().child("favouriteRecipes");
                    }else{
                        mDBHelper.deleteFavouriteRecipe(mRecipe.getId());
                        Toast.makeText(getActivity().getApplicationContext(),R.string.FavouriteRecipeDeleted,Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e){
                Log.e("INSERTFAVRECIPE", "ERROR ON INSERT FAVOURITE RECIPE ", e);
                }
            }
        });



        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collapsingtoolbar_menu_recipe_detail,menu);
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            //plegado
            collToolBar.setTitle(mRecipe.obtainCorrectName());
            CVDetailRecipe.setVisibility(View.INVISIBLE);
            //imgAddCartBar.setVisibility(View.VISIBLE);
            toolbarHeader.setVisibility(View.VISIBLE);



        } else {
            collToolBar.setTitle("");
            CVDetailRecipe.setVisibility(View.VISIBLE);
            //imgAddCartBar.setVisibility(View.INVISIBLE);
            toolbarHeader.setVisibility(View.INVISIBLE);
            //no plegado
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {



        } else {


        }
    }

    public void addToFavourite (Recipe recipe){

    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
