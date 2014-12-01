package com.yourcompany.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.taylorcorp.lifepics.MainApplication;
import com.taylorcorp.lifepics.listeners.OrderStatusListener;
import com.taylorcorp.lifepics.lp.LifePicsResponse;
import com.taylorcorp.lifepics.model.purchases.Cart;
import com.taylorcorp.lifepics.products.ProductsActivity;
import com.taylorcorp.lifepics.webservices.LifePicsWebServiceResponse;
import com.taylorcorp.lifepics.webservices.data.AccountInfo;

public class MyActivity extends ActionBarActivity implements OrderStatusListener {
    private String TAG = "MyActivity";


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // listen in
        MainApplication.getShoppingCart().setOrderStatusListener(this);
        String userID = MainApplication.getAppPreferences().getUserID();

        if (userID == null || userID.isEmpty()) {
            MainApplication.getLifePicsWebService().createTemporaryUser(
                    MainApplication.getAppPreferences().getDeveloperKey(),
                    MainApplication.getAppPreferences().getMerchantID(), new LifePicsWebServiceResponse() {
                        @Override
                        public void resultHandler(boolean isSuccess, Object response, com.taylorcorp.lifepics.webservices.entities.Error error, String message) {
                            if (isSuccess) {
                                AccountInfo info = (AccountInfo) response;
                                MainApplication.setAccountInfo(info);
                                MainApplication.getAppPreferences().setUserID(info.getUserId());
                            } else {
                                if (MainApplication.isDebug()) {
                                    //Log.e("LP", error.mMessage);
                                }
                            }

                            if (MainApplication.getAppPreferences().getMerchantID() != null &&
                                    !MainApplication.getAppPreferences().getMerchantID().isEmpty())

                                populateProducts();

                            MainApplication.loadCart(MainApplication.getAppPreferences().getUserID(),
                                    new LifePicsResponse() {
                                        @Override
                                        public void didComplete(Object response, Exception ex) {

                                        }
                                    });
                        }

                    });
        } else {
            if (MainApplication.getAppPreferences().getMerchantID() != null &&
                    !MainApplication.getAppPreferences().getMerchantID().isEmpty()) {
                populateProducts();

                MainApplication.loadCart(MainApplication.getAppPreferences().getUserID(),
                        new LifePicsResponse() {
                            @Override
                            public void didComplete(Object response, Exception ex) {

                            }
                        });
            }
        }


        if (MainApplication.isDebug())
            Log.d("LP", "User id = " + MainApplication.getAppPreferences().getUserID());
    }

    private void populateProducts() {
        MainApplication.loadProducts(MainApplication.getMerchantID(), 1, new LifePicsResponse() {
            @Override
            public void didComplete(Object response, Exception ex) {

            }
        });
    }

    public void didClick(View v) {
        MainApplication.getShoppingCart().clear();

        Intent i = new Intent(this, ProductsActivity.class);
        startActivity(i);
    }

    @Override
    public void didSubmitOrder(Cart cart) {
        Log.d(TAG, "Order was submitted with cart item count: " + cart.getCartItems().size());
    }

    @Override
    public void didCancelOrder() {
        Log.d(TAG, "Order was cancelled!");
    }

}
