package com.cravings.adapters;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.cravings.MainActivity;
import com.cravings.R;
import com.cravings.fragments.FavoritesFragment;
import com.cravings.fragments.FeaturedFragment;
import com.cravings.fragments.NearMeFragment;
import com.cravings.fragments.ProfileFragment;
import com.cravings.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

/**
 * Created by mremondi on 9/19/16.
 */
public class BottomBarAdapter {

    private BottomBar mBottomBar;
    private AppCompatActivity activity;

    public BottomBarAdapter(BottomBar bottomBar, AppCompatActivity activity){
        this.mBottomBar = bottomBar;
        this.activity = activity;
    }

    public void showFragment(String tag) {
        Fragment fragment = this.activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(SearchFragment.TAG)) {
                fragment = new SearchFragment();
            }
            else if (tag.equals(FeaturedFragment.TAG)) {
                fragment = new FeaturedFragment();
            }
            else if (tag.equals(FavoritesFragment.TAG)) {
                fragment = new FavoritesFragment();
            }
            else if (tag.equals(NearMeFragment.TAG)) {
                fragment = new NearMeFragment();
            }
            else if (tag.equals(ProfileFragment.TAG)) {
                fragment = new ProfileFragment();
            }
        }

        FragmentTransaction trans = this.activity.getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        trans.replace(R.id.main_frame_content, fragment, tag);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void setUpBottomBarMain(){
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(2);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    // go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    showFragment(NearMeFragment.TAG);
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    showFragment(SearchFragment.TAG);
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    showFragment(FavoritesFragment.TAG);
                }
                else if (menuItemId == R.id.nav_bar_profile) {
                    // go to profile fragment
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    //go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    // go to search fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    // go to near me fragment
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    // go to favorites fragments
                }
                else if (menuItemId == R.id.nav_bar_profile) {
                    // go to profile fragment
                }
            }
        });
    }

    public void setUpBottomBar(){
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    // go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                    i.putExtra(MainActivity.FRAGMENT_TAG, NearMeFragment.TAG);
                    activity.startActivity(i);
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                    i.putExtra(MainActivity.FRAGMENT_TAG, SearchFragment.TAG);
                    activity.startActivity(i);
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                    i.putExtra(MainActivity.FRAGMENT_TAG, FavoritesFragment.TAG);
                    activity.startActivity(i);
                }
                else if (menuItemId == R.id.nav_bar_profile) {

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    //go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    // go to search fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    // go to near me fragment
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    // go to favorites fragments
                }
                else if (menuItemId == R.id.nav_bar_profile) {
                    // go to profile fragment
                }
            }
        });

    }
}
