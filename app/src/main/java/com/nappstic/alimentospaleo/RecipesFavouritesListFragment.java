package com.nappstic.alimentospaleo;

import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;

public class RecipesFavouritesListFragment extends Fragment {

    private View rootview;
    private ListView mlistView;
    private ProgressBar mProgressBar;

    private MyDBAdapter mDBAdapter;

    private Cursor mCursor ;

    private RecipeFavouriteListCursorAdapter mRecipeFavouriteCursorAdapter;

    public RecipesFavouritesListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_recipes_list,container,false);

        mlistView = (ListView) rootview.findViewById(R.id.lstViewRecipes);
        mProgressBar = (ProgressBar)rootview.findViewById(R.id.progressBarRecipes);

        //mProgressBar.setVisibility(View.VISIBLE);

        mDBAdapter = new MyDBAdapter(getActivity());

        try {
            mDBAdapter.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create DB");
        }
        try {
            mDBAdapter.openDataBase();
        } catch(SQLException sqle) {
            throw sqle;
        }


        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Recipe recipe = new Recipe();

                recipe.setIngredientes_txt_ca(mCursor.getString(mCursor.getColumnIndexOrThrow("ingredientes_txt_ca")));
                recipe.setIngredientes_txt_es(mCursor.getString(mCursor.getColumnIndexOrThrow("ingredientes_txt_es")));
                recipe.setIngredientes_txt_en(mCursor.getString(mCursor.getColumnIndexOrThrow("ingredientes_txt_en")));
                recipe.setCategoria_ppal(mCursor.getInt(mCursor.getColumnIndexOrThrow("categoria_ppal")));
                recipe.setCategoria_secun(mCursor.getInt(mCursor.getColumnIndexOrThrow("categoria_secun")));
                recipe.setIngredientes_obj(mCursor.getString(mCursor.getColumnIndexOrThrow("ingredientes_obj")));
                recipe.setNombre_ca(mCursor.getString(mCursor.getColumnIndexOrThrow("nombre_ca")));
                recipe.setNombre_es(mCursor.getString(mCursor.getColumnIndexOrThrow("nombre_es")));
                recipe.setNombre_ca(mCursor.getString(mCursor.getColumnIndexOrThrow("nombre_en")));
                recipe.setPreparacion_ca(mCursor.getString(mCursor.getColumnIndexOrThrow("preparacion_ca")));
                recipe.setPreparacion_es(mCursor.getString(mCursor.getColumnIndexOrThrow("preparacion_es")));
                recipe.setPreparacion_en(mCursor.getString(mCursor.getColumnIndexOrThrow("preparacion_en")));
                recipe.setPersonas(mCursor.getInt(mCursor.getColumnIndexOrThrow("personas")));
                recipe.setDificultat(mCursor.getInt(mCursor.getColumnIndexOrThrow("dificultat")));
                recipe.setTiempo(mCursor.getInt(mCursor.getColumnIndexOrThrow("tiempo")));
                recipe.setFoto(mCursor.getString(mCursor.getColumnIndexOrThrow("foto")));
                recipe.setFuente(mCursor.getString(mCursor.getColumnIndexOrThrow("fuente")));
                recipe.setId(mCursor.getInt(mCursor.getColumnIndexOrThrow("id")));
                recipe.setFavourite(true);

                //Recipe recipe = (Recipe)adapterView.getItemAtPosition(i);
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
        fetchFavourites();
        return rootview;
    }

    public Cursor fetchFavourites (){



        mCursor = mDBAdapter.fetchFavouriteRecipeList();
         mRecipeFavouriteCursorAdapter= new RecipeFavouriteListCursorAdapter(getActivity(),mCursor);

        mlistView.setAdapter(mRecipeFavouriteCursorAdapter);
        return mCursor;
    }

}
