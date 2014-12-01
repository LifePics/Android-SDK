package com.yourcompany.demoapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.taylorcorp.lifepics.webservices.LifePicsWebServiceResponse;
import com.taylorcorp.lifepics.webservices.data.AccountInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/22/2014.
 */
public class MyFragment extends Fragment implements OrderStatusListener {

    private String TAG = "MyFragment";

    private LifePicsPreferences mPreferences;

    public static MyFragment createFragment() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_fragment, null);


        mPreferences = new LifePicsPreferences(getActivity());

        configureView();

        return v;
    }

    private void configureView() {
        // listen in
        MainApplication.getShoppingCart().setOrderStatusListener(this);
        MainApplication.getShoppingCart().setOrderStatusListener(this);

        checkIfWeNeedSomeLocalImages();

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

                MainApplication.loadCart(MainApplication.getAppPreferences().getUserID(),
                        new LifePicsResponse() {
                            @Override
                            public void didComplete(Object response, Exception ex) {

                            }
                        });
            }
        }

        if (MainApplication.getAppPreferences().getMerchantID() != null && !MainApplication.getAppPreferences().getMerchantID().isEmpty()) {
            MainApplication.loadProducts(MainApplication.getAppPreferences().getMerchantID(), 1, new LifePicsResponse() {

                @Override
                public void didComplete(Object response, Exception ex) {
                    if (MainApplication.isDebug()) {
                        Log.d("LP", "Products Loaded");
                    }
                }
            });
        } else {
            List<Product> products = new ArrayList<Product>();

            products.add(Product.getGenericProduct(4.0, 6.0));
            products.add(Product.getGenericProduct(4.0, 8.0));
            products.add(Product.getGenericProduct(5.0, 7.0));
            products.add(Product.getGenericProduct(8.0, 10.0));
            products.add(Product.getGenericProduct(8.0, 12.0));
            products.add(Product.getGenericProduct(11.0, 14.0));
            products.add(Product.getGenericProduct(20.0, 16.0));
            products.add(Product.getGenericProduct(30.0, 20.0));
            products.add(Product.getGenericProduct(36.0, 24.0));
            products.add(Product.getGenericProduct(4.0, 4.0));
            products.add(Product.getGenericProduct(5.0, 5.0));
            products.add(Product.getGenericProduct(8.0, 8.0));
            products.add(Product.getGenericProduct(10.0, 10.0));


            MainApplication.setProducts(products);

        }

        if (MainApplication.isDebug())
            Log.d("LP", "User id = " + MainApplication.getAppPreferences().getUserID());
    }

    private void checkIfWeNeedSomeLocalImages() {
        boolean isAnyDevicePhotos = DeviceImageSource.isAnyDevicePhotos(getActivity());
        boolean hasAskedForLoremPixelPreviously = mPreferences.getAskForLoremPixelOnStartup();

        if (isAnyDevicePhotos || !hasAskedForLoremPixelPreviously) {
            return;
        }

        mPreferences.setAskForLoremPixelOnStartup(false);

        AlertUtils.showOKCancelAlert(getActivity(), R.string.lp_intro_add_images_title, R.string.lp_intro_add_images_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] resolutions = new String[]{
                        "2448/3264", "3264/2448", "640/640", "1280/960", "960/1280", "300/300",
                        "1280/720", "720/1280", "960/960", "640/960", "960/640", "100/100",
                        "640/480", "480/640", "480/480", "1280/720", "720/1280", "640/640",
                        "640/960", "960/640", "640/640", "640/960", "960/640", "640/640",
                        "640/960", "960/640", "640/640", "640/960", "960/640", "640/640"
                };

                for (String iResolution : resolutions) {
                    String url = String.format("http://lorempixel.com/%s", iResolution);

                    ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            try {
                                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), loadedImage, "Lorem Pixel", "Added by Lorem Pixel");
                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
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
