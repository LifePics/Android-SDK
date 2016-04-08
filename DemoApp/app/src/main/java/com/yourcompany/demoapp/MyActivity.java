package com.yourcompany.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.taylorcorp.lifepics.MainApplication;
import com.taylorcorp.lifepics.activities.BaseLeftMenuActivity;

public class MyActivity extends BaseLeftMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawer.setContentView(R.layout.lp_activity_base_single_fragment);

        MainApplication.loadBannedStores(this);
        configureView();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void configureView() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentById(R.id.lp_layout_fragment) == null) {
            Fragment f = MyFragment.createFragment();

            if (f != null) {
                fm.beginTransaction().add(R.id.lp_layout_fragment, f).commit();
            }
        }
    }

    @Override
    protected void homeClick(Activity currentActivity)
    {
        if((currentActivity instanceof MyActivity) && mDrawer.isMenuVisible())
        {
            mDrawer.closeMenu();
        }
        else
        {
            super.homeClick(currentActivity);
        }
    }

    @Override
    protected void signOutClick(Activity currentActivity) {
        if ((currentActivity instanceof MyActivity) && mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        }
        super.signOutClick(currentActivity);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        invalidateOptionsMenu();
    }
}

