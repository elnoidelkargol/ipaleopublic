package com.nappstic.alimentospaleo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class RecipesListFragment extends Fragment {

    private View rootview;
    private ListView mlistView;
    private ProgressBar mProgressBar;

    private RecipesListFragmentArrayAdapter mrecipesListFragmentArrayAdapter;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mRecipedatabaseReference;

    private FirebaseAuth mFirebaseAuth;

    private String uid;

    public RecipesListFragment (){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRecipedatabaseReference = mfirebaseDatabase.getReference().child("recipes");
        mFirebaseAuth = FirebaseAuth.getInstance();
        uid = mFirebaseAuth.getUid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_recipes_list,container,false);

        mlistView = (ListView) rootview.findViewById(R.id.lstViewRecipes);
        mProgressBar = (ProgressBar)rootview.findViewById(R.id.progressBarRecipes);

        final List<Recipe> recipesList = new ArrayList<>();
        mrecipesListFragmentArrayAdapter = new RecipesListFragmentArrayAdapter(getActivity(),R.layout.row_recipes,recipesList);
        mlistView.setAdapter(mrecipesListFragmentArrayAdapter);

        mProgressBar.setVisibility(View.VISIBLE);

        mRecipedatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearFoodList();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    if (dataSnapshot.child(recipe.getId().toString()).child("favourites").child(uid).exists()){
                        recipe.setFavourite(true);
                    }else{
                        recipe.setFavourite(false);
                    }
                    updateFoodList(recipe);
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());

            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = (Recipe)adapterView.getItemAtPosition(i);
                Fragment fragment = new RecipeDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe",recipe);
                fragment.setArguments(bundle);
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,"Detail Recipe")
                            .addToBackStack("Detail Recipe")
                            .commit();

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        return rootview;
    }

    private void updateFoodList( Recipe recipe ) {
        mrecipesListFragmentArrayAdapter.add(recipe);
    }
    private void clearFoodList(){
        mrecipesListFragmentArrayAdapter.clear();
    }
}
