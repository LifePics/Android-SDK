package com.yourcompany.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.taylorcorp.lifepics.MainApplication;
import com.taylorcorp.lifepics.listeners.OrderStatusListener;
import com.taylorcorp.lifepics.model.purchases.Cart;
import com.taylorcorp.lifepics.model.purchases.ShoppingCart;
import com.taylorcorp.lifepics.products.ProductsActivity;
import com.taylorcorp.lifepics.utils.AlertUtils;
import com.taylorcorp.lifepics.webservices.LifePicsWebServiceResponse;
import com.taylorcorp.lifepics.webservices.data.AccountInfo;

public class MyActivity extends ActionBarActivity implements OrderStatusListener {
    private String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        String partnerId = null;
        String password = null;
        String developerId = null;

        if (partnerId == null || password == null) {
            AlertUtils.showSimpleAlert(this, "LifePics Partner ID Required", "To fully explore this demo, you'll first need to get a LifePics Partner ID. Put it in the MyActivity.java file. Details are provided in the ReadMe.\n\nWe'll take you as far as we can without a partner ID, but you'll see errors when trying to find photofinisher locations.");
        }

        // listen in
        ShoppingCart.getInstance().setOrderStatusListener(this);

        String developerKey = getString(R.string.lp_developer_key);
        if (developerKey != null) {
            MainApplication.getAppPreferences().setDeveloperKey(developerKey);
        }

        MainApplication.getLifePicsWebService().createTemporaryUser(
                MainApplication.getAppPreferences().getDeveloperKey(),
                MainApplication.getAppPreferences().getMerchantID(), new LifePicsWebServiceResponse() {
            @Override
            public void resultHandler(boolean isSuccess, Object response, com.taylorcorp.lifepics.webservices.entities.Error error, String message) {
                if (isSuccess) {
                    AccountInfo info = (AccountInfo) response;
                    MainApplication.setAccountInfo(info);
                    MainApplication.getAppPreferences().setUserID(info.getUserId());

                    Log.d("LP", "User ID set to " + MainApplication.getAppPreferences().getUserID());

                } else {
                    if (MainApplication.isDebug()) {
                        //Log.e("LP", error.mMessage);
                    }
                }
            }
        });
    }

    public void didClick(View v) {
        ShoppingCart.getInstance().clear();

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
