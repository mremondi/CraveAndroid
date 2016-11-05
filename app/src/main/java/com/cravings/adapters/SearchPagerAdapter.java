package com.cravings.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cravings.fragments.ItemSearchFragment;
import com.cravings.fragments.RestaurantSearchFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {

    public SearchPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ItemSearchFragment();
            case 1:
                return new RestaurantSearchFragment();
            default:
                return new ItemSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ItemSearchFragment.TITLE;
            case 1:
                return RestaurantSearchFragment.TITLE;
            default:
                return "unknown";
        }
    }
}
