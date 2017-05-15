LifePics Android SDK Version 2.0.1
==================================


Overview
--------

LifePics is the market leader in providing retail photofinishers with the latest online imaging services and technologies to conduct business on the Web.

The LifePics OPEN (Open Photo Ecommerce Network) program allows app developers to drive consumer photo orders through the LifePics Network. We will allow you to link your app into our network, and we will pay you a revenue share percentage for every order your app generates.

To learn more about developer revenue share please read the enclosed [LifePics Developer Agreement](https://github.com/LifePics/iOS-SDK/raw/master/LifePics%20Developer%20Agreement.pdf)
 and review Schedule 1.

To request developer keys from LifePics please email a complete signed copy of the LifePics Developer Agreement to [busdev@lifepics.com](mailto:busdev@lifepics.com). LifePics will generate a set of developer keys and email them back to you. Please see Schedule 1 in the [LifePics Developer Agreement](https://github.com/LifePics/iOS-SDK/blob/master/LifePics%20Developer%20Agreement.pdf) on documents to send to LifePics to be eligible for developer revenue share payments.

Please email any questions about the OPEN program to [busdev@lifepics.com](mailto:busdev@lifepics.com).

The LifePics Android SDK
------------------------

The LifePics SDK allows your Android users to select images and have them printed locally at nearby photofinishers.

Users can select images from their photo library, images provided by your application, or, optionally, images from their Facebook, Instagram, Google, or Flickr accounts. (NOTE: the Android SDK doesn't currently support anything besides the device's photo library.)

Prerequisites
------------

You will need a free LifePics developer key. Details are in the Overview section above.

The SDK supports Android 5.0 and later.

These instructions are for setting up the LifePics SDK in an app using Android Studio.  If you're still using the older ADT environment, 
please adjust accordingly.


Installation 
------------

1. Copy the following from Android Files/ into your libs/ folder:

        LifePicsSDK.aar

2. Create a settings.xml file within the values folder.  Inside of this file define the following items with appropriate values:

    	<string name="lp_partner_source_id">ID</string>

3. Create a keys.xml file within the values folder.  Inside of this file define the LifePics developer key (provided by LifePics):
		
		<string name="lp_developer_key">KEY</string>

4. Turn on the Ship To Home option or the Pickup Locations option by adding the following items to the settings.xml file:

		<bool name="lp_find_stores">true</bool>
    	<bool name="lp_ship_to_home">false</bool>
   If you turn on Ship To Home you must also specify a merchant ID within the settings.xml file:

		<string name="lp_merchant_id">ID</string>
   If you turn on Pickup Locations you should specify the store search radius within the settings.xml file:

		<string name="lp_store_search_radius">MILES</string>

	If you want the user to be able to specify discount codes or preferred customer numbers you should declare it by adding the following line to settings.xml

		<string name="lp_show_discount">true</string>


5.  If you turn on Ship To Home you must obtain a Stripe Key in order to receive payments.  Define the Stripe key in the keys.xml file.

		<string name="lp_stripe_publishable_key">key</string>

  	Information about Stripe accounts can be found on the  [Stripe Website](http://www.stripe.com).

6.  If you have defined a merchant ID but want all stores returned in the store list, add the following:

		<bool name="lp_use_merchant_id_for_stores">true</bool>

7. Allow the following permissions in your AndroidManifest.xml:

    	<uses-permission android:name="android.permission.INTERNET"/>
    	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    	<uses-permission android:name="android.permission.GET_ACCOUNTS" />
    	<uses-permission android:name="android.permission.USE_CREDENTIALS"/>
    	<uses-permission android:name="android.permission.MANAGE_ACCOUNTS"/>
    	<uses-permission android:name="android.permission.READ_CONTACTS" />

8. Add Google Play Services and the V7 Support Library as a dependencies.  You can do this in Module Settings.  Select your app, then the "Dependencies" tab, then add these as library dependencies.

9. In the &lt;application&gt; section, you'll need to add these &lt;meta-data&gt; tags: ([supply your own Google Maps v2 key](https://developers.google.com/maps/documentation/android/start#obtain_a_google_maps_api_key))
	
        <meta-data android:name="com.google.android.maps.v2.API_KEY" android:value="YOUR_MAPS_V2_KEY" />
        <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />  
  If you do not turn on Pickup Locations then the Google Maps Key is not required.  

10. Within the &lt;application&gt; tag, add the following value:

		<application
			...
			android:largeHeap="true">

11. Define your application class to be the class com.taylorcorp.lifepics.MainApplication or define your own application class that extends the LifePics class MainApplication.

		<application
			android:name="com.taylorcorp.lifepics.MainApplication"
			...

12. Add the following activities in your AndroidManifest.xml:

        <activity
            android:name="com.taylorcorp.lifepics.products.ProductsActivity"
            android:screenOrientation="sensorPortrait" />

        <activity android:name="com.taylorcorp.lifepics.order.OrderActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop">
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.taylorcorp.lifepics.app.activities.IntroActivity" />
        </activity>

        <activity android:name="com.taylorcorp.lifepics.cart.CartActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop">
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.taylorcorp.lifepics.order.OrderActivity" />
        </activity>

        <activity
            android:name="com.taylorcorp.lifepics.contactinfo.ContactInfoActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

        <activity
            android:name="com.taylorcorp.lifepics.order.OrderCompletedActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

        <activity android:name="com.taylorcorp.lifepics.activities.FeedbackAndSupportActivity"
            android:screenOrientation="sensorPortrait" />

		 <activity android:name="com.taylorcorp.lifepics.activities.OrderHistoryActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop">
        </activity>

        <receiver android:name="com.taylorcorp.lifepics.utils.NetWorkStatusReceiver">
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
            </intent-filter>
        </receiver>

        <activity android:name="com.facebook.FacebookActivity"
            android:configChanges=
                "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

13. If you turn on Pickup Locations then you have to add the following activity to your AndroidManifest.xml file:

		<activity
            android:name="com.taylorcorp.lifepics.locations.PickupLocationsActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

14.  If you turn on Ship To Home then you have to add the following activites to you AndroidManifest.xml file:

		<activity
            android:name="com.taylorcorp.lifepics.shipping.ShippingAndPaymentActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" />
        <activity
            android:name="com.taylorcorp.lifepics.shipping.AddShippingInfoActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" >
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.taylorcorp.lifepics.shipping.ShippingAndPaymentActivity" />
        </activity>
        <activity
            android:name="com.taylorcorp.lifepics.shipping.EditShippingInfoActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" />

        <activity
            android:name="com.taylorcorp.lifepics.shipping.ShippingAddressesActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" />

        <activity
            android:name="com.taylorcorp.lifepics.shipping.AddPaymentActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" >

            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.taylorcorp.lifepics.shipping.ShippingAndPaymentActivity" />
        </activity>

        <activity
            android:name="com.taylorcorp.lifepics.order.ShippingOrderCompletedActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop" />

15. The server endpoints are defined in the lp_settings.xml file.  They point to the staging (test) server.  When you are ready to deploy your app you must change these parameters to point to a live server.  These values can be provided by LifePics.

		<string name="lp_endpoint">http://staging.api.lifepics.com/V4.3/api/</string>
    	<string name="lp_endpoint_secure">https://staging.api.lifepics.com/V4.3/api/</string>

16. Now, in your program logic, you'll need to set your developer key. You can do this in the activity (or fragment) where you plan on presenting the LifePics print selector:

        String developerKey = getString(R.string.lp_developer_key);
        if (developerKey != null) {
            MainApplication.getAppPreferences().setDeveloperKey(developerKey);
        }
        
17. You can log your user into the LifePics server using the following code snippet:
		
		MainApplication.getLifePicsWebService().getUserID(MainApplication.getAppPreferences().getDeveloperKey(),
                emailAddress, password, new LifePicsWebServiceResponse() {
                    @Override
                    public void resultHandler(boolean isSuccess, Object response, com.taylorcorp.lifepics.webservices.entities.Error error, String message) {
                        if (isSuccess) {
                            AccountInfo info = (AccountInfo) response;
                    		MainApplication.setAccountInfo(info);
                    		MainApplication.getAppPreferences().setUserID(info.getUserId());
                        } else {
                            if (MainApplication.isDebug()) {
                                //log error
                            }
                        }
                    }
                });

18. If your user does not log into the LifePics server you need to get a User ID by creating a temporary user.

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
            }
        });

19. When you want to present the LifePics photo selector:

        Intent i = new Intent(getActivity(), ProductsActivity.class);
        startActivity(i);

20. Finally, Copy Json file to ASSETS folder:
		
		There are two main parts you have to set in the .json file.

		* Category attributes
			- CategoryName
			- IsCategory (have to be ture)
			- FinishType 
				- Value 
				- AttributeId
				- IsDefault (have to be ture if you want to set status to selected in the App)

		* Product attributes
			- CategoryName (should have same value as Category attribute defined)
			- ProductID
			- Description
			- SKU
			- Width
			- Lenght

Customization
-------------

You can customize the colors used by the LifePics Order Activity by tweaking the primary and secondary colors in lp\_colors.xml:

	<!-- tints -->
	<color name="lp_primary_tint_color">#3bb021</color>
	<color name="lp_secondary_tint_color">#fcb024</color>

Further interface customizations can be made using the XML files.

New in Version 2.0.1
--------------------

* Add order history.
* Add feedback and support.
* Add network status checking.
* Add product category in json file.
* Add new image editor.

New in Version 1.0.5
--------------------

* Add Navigation Panel.
* Fix some issues when place orders.
* Add auto crop image function.

New in Version 1.0.4
--------------------

* Fixed demo samples loading.
* Cleaned up LifePics and SmugMug authentication.
* Pass back ContactInfo to the Order listener.
* Limited V4.1 API support.

New in Version 1.0.3
--------------------

* Added ability to log out of image sources
* Fixed Facebook permissions

New in Version 1.0.2
--------------------

* Added LifePics Image Support
* Added Google Image Support
* Added SmugMug Image Support

New in Version 1.0.1
--------------------

* Added Ship To Home

