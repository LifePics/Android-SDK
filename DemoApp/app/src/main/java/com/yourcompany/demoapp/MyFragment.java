package com.yourcompany.demoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taylorcorp.lifepics.MainApplication;
import com.taylorcorp.lifepics.listeners.OrderStatusListener;
import com.taylorcorp.lifepics.lp.LifePicsResponse;
import com.taylorcorp.lifepics.model.LifePicsPreferences;
import com.taylorcorp.lifepics.model.purchases.Cart;
import com.taylorcorp.lifepics.model.purchases.Product;
import com.taylorcorp.lifepics.model.sources.DeviceImageSource;
import com.taylorcorp.lifepics.utils.AlertUtils;
import com.taylorcorp.lifepics.utils.ResourceUtils;
import com.taylorcorp.lifepics.webservices.LifePicsWebService;
import com.taylorcorp.lifepics.webservices.LifePicsWebServiceResponse;
import com.taylorcorp.lifepics.webservices.data.AccountInfo;
import com.taylorcorp.lifepics.webservices.data.ProductCategoryInfo;
import com.taylorcorp.lifepics.webservices.data.ProductInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 10/22/2014.
 */
public class MyFragment extends Fragment implements OrderStatusListener {
    private String TAG = "IntroFragment";

    private Button btnTraditionalPrints, btnSquarePrints;
    private TextView mVersionTextView;

    private LifePicsPreferences mPreferences;

    public static MyFragment createFragment() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lp_fragment_intro, null);

        mVersionTextView = (TextView) v.findViewById(R.id.lp_txt_version);
        btnTraditionalPrints = (Button) v.findViewById(R.id.lp_btn_Traditional_Prints);
        btnSquarePrints = (Button) v.findViewById(R.id.lp_btn_Square_Prints);

        mPreferences = new LifePicsPreferences(getActivity());

        configureView();

        return v;
    }

    private String loadProductsFromAssets() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            if (MainApplication.isDebug())
                Log.e("LP", "Load product", e);
        }
        return json;
    }

    private void populateProducts() {
        try {
            String json = loadProductsFromAssets();

            List<ProductInfo> plist = ProductInfo.parseList(json);

            List<Product> products = new ArrayList<>();
            for (ProductInfo p : plist) {
                Product prod = new Product(p);
                products.add(prod);
            }

            MainApplication.setProducts(products);
        } catch (Exception e) {
            if (MainApplication.isDebug())
                Log.e("LP", "Load List", e);
        }
    }

    private void configureView() {
        try {
            String buildVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            int buildVersionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
            String buildTarget = getString(R.string.lp_build_target);
            String version = getString(R.string.lp_intro_version, buildVersion, buildVersionCode, buildTarget);

            mVersionTextView.setText(version);

            populateProducts();
        } catch (PackageManager.NameNotFoundException ex) {
            mVersionTextView.setText(getString(R.string.lp_intro_version_unknown));
        }

        // listen in
        MainApplication.getShoppingCart().setOrderStatusListener(this);
        String userID = MainApplication.getAppPreferences().getUserID();

        if (userID == null || userID.isEmpty()) {

        } else {
            if (MainApplication.getAppPreferences().getMerchantID() != null &&
                    !MainApplication.getAppPreferences().getMerchantID().isEmpty()) {

                MainApplication.loadCart(MainApplication.getAppPreferences().getUserID(),
                        new LifePicsResponse() {
                            @Override
                            public void didComplete(Object response, Exception ex) {

                            }
                        });
            }
        }

        btnTraditionalPrints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = "Traditional Prints";
                Intent i = new Intent(getActivity(), com.taylorcorp.lifepics.products.ProductsActivity.class);
                loadProducts(categoryName,i);
            }
        });

        btnSquarePrints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = "Square Prints";
                Intent i = new Intent(getActivity(), com.taylorcorp.lifepics.products.ProductsActivity.class);
                loadProducts(categoryName,i);
            }
        });
    }

    HashMap<String, ProductCategoryInfo> categoryNames;

    private void loadProducts(final String category, final Intent intent) {

        MainApplication.setMerchantID(getString(ResourceUtils.getString(getActivity(), "lp_merchant_id")));

        final List<Product> finalList = new ArrayList<>();
        MainApplication.loadProducts(MainApplication.getAppPreferences().getMerchantID(), 1, new LifePicsResponse() {
            @Override
            public void didComplete(Object response, Exception ex) {

                try {
                    final List<ProductInfo> fromWeb = (List<ProductInfo>) response;

                    if (fromWeb.size() == 0) {
                        Toast.makeText(getActivity(), "Product load failed, please try again later.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String json;
                    json = loadProductsFromAssets();

                    try {

                        categoryNames = ProductInfo.parseCategoryInfo(json);

                        List<ProductInfo> plist = ProductInfo.parseList(json);
                        for (ProductInfo p : plist) {
                            if (p.getCategoryName().equals(category)) {
                                for (ProductInfo fromWebProduct : fromWeb) {
                                    if (fromWebProduct.getProductID() == p.getProductID()) {
                                        p.setLengthResolution(fromWebProduct.getLengthResolution());
                                        p.setWidthResolution(fromWebProduct.getWidthResolution());
                                        p.setProductID(fromWebProduct.getProductID());

                                        finalList.add(new Product(p));
                                    }
                                }

                            }
                        }

                        MainApplication.setProductCategory(category);
                        MainApplication.setProductCategoryInfo(categoryNames.get(category));
                        MainApplication.setProducts(finalList);
                        startActivity(intent);

                    } catch (Exception e) {
                        if (MainApplication.isDebug()) {
                            Log.e("LP", "Exception", e);
                        }
                    }
                } catch (Exception exx) {
                    if (MainApplication.isDebug()) {
                        Log.e("LP", "Exception", exx);
                    }

                    Toast.makeText(getActivity(), "Product load failed, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

