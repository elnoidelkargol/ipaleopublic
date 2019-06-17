package com.nappstic.alimentospaleo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class HomeFragment extends Fragment {
    private Fragment fragment = null;
    String fragmentName;
    private AdView mAdView;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-1663798572028841~7156663735");
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Typeface typeFaceBarlowBold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BarlowCondensed-Bold.ttf");
        TextView txtViewPaleoFoodHome =  rootView.findViewById(R.id.textViewPaleoFoodHome);
       // TextView txtViewTabataTimer =  rootView.findViewById(R.id.textViewTabataTimerHome);
        TextView txtViewCartList =  rootView.findViewById(R.id.txtViewCartListHome);
        TextView txtViewRecipes =  rootView.findViewById(R.id.txtViewRecipesTxtHome);

        RelativeLayout relativeLayoutFoodList =  rootView.findViewById(R.id.relativeLytPaleoFoodHome);
        //RelativeLayout relativeLayoutTimer = (RelativeLayout) rootView.findViewById(R.id.relativeLytTimerHome);
        RelativeLayout relativeLayoutRecipes =  rootView.findViewById(R.id.relativeLytRecipesHome);
        RelativeLayout relativeLayoutCart =  rootView.findViewById(R.id.relativeLytCartHome);



        //txtViewTabataTimer.setTypeface(typeFaceBarlowBold);
        txtViewPaleoFoodHome.setTypeface(typeFaceBarlowBold);
        txtViewCartList.setTypeface(typeFaceBarlowBold);
        txtViewRecipes.setTypeface(typeFaceBarlowBold);

        relativeLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ShoppingListFragment();
                //fragment = new ShoppingListFragment2();
                fragmentName = "Cart List";
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,fragmentName)
                            .addToBackStack(fragmentName)
                            .commit();

                } else {
                    Log.e("HOMEFRAGMENT", "Error in creating CartList fragment");
                }
            }
        });

        relativeLayoutFoodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fragment = new FoodListFragment();
                fragment = new FoodListTabs();
                fragmentName = "Food List";
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment,fragmentName)
                            .addToBackStack(fragmentName)
                            .commit();

                } else {
                    Log.e("HOMEFRAGMENT", "Error in creating FoodList fragment");
                }
            }
        });

        relativeLayoutRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new RecipesListTabs();
                fragmentName = "Recipe List";
                if (fragment !=null){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame,fragment,fragmentName)
                            .addToBackStack(fragmentName)
                            .commit();
                }else{
                    Log.e("HOMEFRAGMENT", "Error in creating FoodList fragment");

                }
            }
        });

        return rootView;
    }
}
