package com.nappstic.alimentospaleo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uri on 19/2/18.
 */

public class FoodListTabs extends Fragment {

    private Integer viewPagePos = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabs_food_list,container,false);
        ViewPager viewPager =  view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs =  view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager (ViewPager viewPager){

        Adapter adapter = new Adapter(getChildFragmentManager());
        Bundle bundleCategory;
        Fragment foodCategoryFragment = null;

        bundleCategory = this.getArguments();
        if (bundleCategory != null){
            String value = bundleCategory.getString("id_category");
            bundleCategory.putString("id_category",value);
            viewPagePos = 1;
            if (value != "0"){
                foodCategoryFragment = new FoodListFilteredCategoryFragment();
                foodCategoryFragment.setArguments(bundleCategory);
            }else{
                foodCategoryFragment = new FoodCategoryListFragment();
            }
        }else {
            foodCategoryFragment = new FoodCategoryListFragment();
        }
        adapter.addFragment(new FoodListFragment(),getResources().getString(R.string.foodListTabAll));
        //adapter.addFragment(new ShoppingListFragment(),getResources().getString(R.string.foodListTabNutrients));
        adapter.addFragment(foodCategoryFragment,getResources().getString(R.string.foodListTabGrupos));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagePos);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager){
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

        void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}
