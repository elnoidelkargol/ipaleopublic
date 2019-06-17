package com.nappstic.alimentospaleo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uri on 19/2/18.
 */

public class RecipesListTabs extends Fragment {
    Bundle bundleFilterIngredient;
    Integer viewPagePos = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabs_recipes_list,container,false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpagerRecipes);
        setupViewPager(viewPager);



        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabLayoutRecipes);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager (ViewPager viewPager){
        String val;
        Adapter adapter = new Adapter(getChildFragmentManager());
        Fragment fragmentFilter = null;
        FirebaseDatabase mFirebaseDatabase = null;

        bundleFilterIngredient = this.getArguments();

        if (bundleFilterIngredient != null){
            viewPagePos = 1;
            val = bundleFilterIngredient.getString("id_category");
            if (val != "0"){

                fragmentFilter = new RecipesFilteredListFragment();
                fragmentFilter.setArguments(bundleFilterIngredient);
            }else{

                fragmentFilter = new RecipePpalIngredientListFragment();
            }
        }else{
            fragmentFilter = new RecipePpalIngredientListFragment();
        }




        adapter.addFragment(new RecipesListFragment(), getResources().getString(R.string.foodListTabAll));
        //adapter.addFragment(new FoodCategoryListFragment(),getResources().getString(R.string.RecipesListTabDish));
        adapter.addFragment(fragmentFilter,getResources().getString(R.string.RecipesListTabIngredient));
        adapter.addFragment(new RecipesFavouritesListFragment(),getResources().getString(R.string.Favourites));


        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagePos);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter (FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment (Fragment fragment,String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}
