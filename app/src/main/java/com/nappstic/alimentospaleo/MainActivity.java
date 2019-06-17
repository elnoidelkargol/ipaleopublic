package com.nappstic.alimentospaleo;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //vars for navigation drawer
    private DrawerLayout mDrawer;
    Toolbar toolbar;
    NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    public static String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();

        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(MainActivity.this,ActivityLogin.class);
                    startActivity(intent);
                    Log.d(TAG, "2. Current User" + user.getEmail());
                }
            }
        };
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (user == null) {
            //fragment = new FoodListFragment();
            Intent i = new Intent(this,ActivityLogin.class);
            startActivity(i);
        }



        mFirebaseAuth = FirebaseAuth.getInstance();

        // Set a Toolbar to replace the ActionBar.
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer =  findViewById(R.id.drawer_layout);


        nvDrawer =  findViewById(R.id.nView);
        // Set a Toolbar to replace the ActionBar.
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer =  findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        setupDrawerContent(nvDrawer);
        Fragment fragment1 = null;
       Class fragmentClass1 = HomeFragment.class;
        try {
            fragment1 = (Fragment) fragmentClass1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.content_frame, fragment1,"HomeFragment")
                .commit();


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        String fragmentName ="";
        Class fragmentClass = null;
        //Intent intent;

        switch(menuItem.getItemId()) {
            case R.id.foodListFragment:
                fragmentClass = FoodListTabs.class;
                //fragment = new FoodListFragment();
                fragmentName = "Food List";
                break;
            case R.id.recipesListFragment:
                fragmentClass = RecipesListTabs.class;
                fragmentName = "Recipe List";
                break;
            case R.id.chartListFragment:
                fragmentClass = ShoppingListFragment.class;
                fragmentName = "Chart List";
                break;

            case R.id.nav_signOut:
                signOut();
                Intent intent = new Intent(this,ActivityLogin.class);
                startActivity(intent);
                break;
            default:
                //intent = new Intent(getApplicationContext(),ActivityLogin.class);
                //startActivity(intent);
        }
        if (fragmentClass != null){

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment,fragmentName)
                    .addToBackStack(fragmentName)
                    .commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();

        if (user == null) {
            //fragment = new FoodListFragment();
            Intent i = new Intent(this,ActivityLogin.class);
            startActivity(i);
        }else {
            FragmentManager fragments = getSupportFragmentManager();
            Log.d(TAG, "onBackPressed: " + fragments.getBackStackEntryCount());

            if (fragments.getBackStackEntryCount() >= 1) {
                // We have fragments on the backstack that are poppable
                fragments.popBackStackImmediate();
            } else {
                // We are already showing the home screen, so the next stop is out of the app.
                supportFinishAfterTransition();
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void signOut() {
        if (mFirebaseAuth != null) {
            mFirebaseAuth.signOut();
        }
    }
    
}
