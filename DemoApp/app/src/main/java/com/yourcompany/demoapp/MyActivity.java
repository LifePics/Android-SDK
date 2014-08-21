package com.yourcompany.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.taylorcorp.lifepics.listeners.OrderStatusListener;
import com.taylorcorp.lifepics.model.purchases.Cart;
import com.taylorcorp.lifepics.model.purchases.ShoppingCart;
import com.taylorcorp.lifepics.order.OrderActivity;
import com.taylorcorp.lifepics.products.ProductsActivity;
import com.taylorcorp.lifepics.utils.AlertUtils;
import com.taylorcorp.lifepics.webservices.LifePicsWebService;
import com.taylorcorp.lifepics.webservices.LifePicsWebServiceResponse;

public class MyActivity extends ActionBarActivity implements OrderStatusListener {
    private String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final LifePicsWebService service = new LifePicsWebService(this);

        String partnerId = null;
        String password = null;

        if (partnerId == null || password == null) {
            AlertUtils.showSimpleAlert(this, "LifePics Partner ID Required", "To fully explore this demo, you'll first need to get a LifePics Partner ID. Put it in the MyActivity.java file. Details are provided in the ReadMe.\n\nWe'll take you as far as we can without a partner ID, but you'll see errors when trying to find photofinisher locations.");
        }

        // listen in
        ShoppingCart.getInstance().setOrderStatusListener(this);

        service.startSession(partnerId, password, new LifePicsWebServiceResponse() {
            @Override
            public void resultHandler(boolean b, Object o, com.taylorcorp.lifepics.webservices.entities.Error error, String s) {

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
