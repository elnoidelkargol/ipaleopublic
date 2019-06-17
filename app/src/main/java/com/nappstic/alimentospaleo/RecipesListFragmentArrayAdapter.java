package com.nappstic.alimentospaleo;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class RecipesListFragmentArrayAdapter extends ArrayAdapter<Recipe> {

    List<Recipe> listRecipes;
    Recipe recipe ;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mRecipeFavouriteDbReference;


    Boolean isFavourite;
    String uid;

    public RecipesListFragmentArrayAdapter(@NonNull Context context, int resource, List<Recipe> recipesList) {
        super(context, resource,recipesList);
        this.listRecipes = recipesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_recipes,parent,false);
        }

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference().child("recipes");
        mFirebaseAuth = FirebaseAuth.getInstance();
        uid = mFirebaseAuth.getUid();
        recipe = listRecipes.get(position);

        TextView txtNumberPeople = (TextView) convertView.findViewById(R.id.txtVIewNumberOfpeople);
        TextView txtRecipeTime = (TextView) convertView.findViewById(R.id.txtViewTimeToCook);
        TextView txtRecipeName = (TextView) convertView.findViewById(R.id.txtViewRecipeName);
        ImageView imgRecipe = (ImageView) convertView.findViewById(R.id.imgViewRecipe);
        //ImageView favourite = (ImageView)convertView.findViewById(R.id.imgViewRecipeFavourite);
        /*if(recipe.getFavourite()){
            favourite.setVisibility(View.VISIBLE);
        }else{
            favourite.setVisibility(View.INVISIBLE);
        }*/
        txtRecipeName.setText(recipe.obtainCorrectName().toString());
        txtNumberPeople.setText(recipe.getPersonas().toString());
        if (recipe.getTiempo() == 0){
            txtRecipeTime.setText("-");
        }else{

            txtRecipeTime.setText(recipe.getTiempo().toString());
        }
        Glide.with(imgRecipe.getContext())
                .load(recipe.getFoto())
                .into(imgRecipe);
        return convertView;
    }
}
