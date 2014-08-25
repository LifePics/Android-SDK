LifePics Android SDK Version 1.0.4
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

The SDK supports Android 2.3.3 and later.

These instructions are for setting up the LifePics SDK in an app using Android Studio.  If you're still using the older ADT environment, 
please adjust accordingly.


Installation 
------------

1. Copy the following from Android Files/ into your libs/ folder:

        ksoap2-android-assembly-2.3-jar-with-dependencies.jar
        LifePicsSDK.jar

2. Unzip and copy the resources from res.zip into your project. You're free to change any of the values in these with discretion, but you should stay more focused on lp\_user\_settings.xml. You'll *need* to change at least one value in these resources, specifically the _lp\_partner\_source\_id_ value in lp\_customer\_settings.xml:

        <!-- lifepics -->
        <string name="lp_partner_source_id">11</string>

3. Allow the following permissions in your AndroidManifest.xml:

        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
	    <uses-permission android:name="android.permission.USE_CREDENTIALS" />

4. Add Google Play Services and the V7 Support Library as a dependencies.  You can do this in Module Settings.  Select your app, then the "Dependencies" tab, then add these as library dependencies.

5. In the &lt;application&gt; tag, you'll need to add these &lt;meta-data&gt;: ([supply your own Google Maps v2 key](https://developers.google.com/maps/documentation/android/start#obtain_a_google_maps_api_key))
	
        <meta-data android:name="com.google.android.maps.v2.API_KEY" android:value="YOUR_MAPS_V2_KEY" />
        <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
    	
6. Add the following activities in your AndroidManifest.xml:

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
            android:name="com.taylorcorp.lifepics.locations.PickupLocationsActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

        <activity
            android:name="com.taylorcorp.lifepics.contactinfo.ContactInfoActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

        <activity
            android:name="com.taylorcorp.lifepics.order.OrderCompletedActivity"
            android:screenOrientation="sensorPortrait"
            android:launchMode="singleTop"/>

7. Now, in your program logic, you'll want to connect to the LifePics network by providing your Partner ID, Source ID, and password. You can do this in the activity (or fragment) where you plan on presenting the LifePics print selector:

        service.startSession("<YOUR PARTNER ID>", "<YOUR PASSWORD>", new LifePicsWebServiceResponse() {
            @Override
		    public void resultHandler(boolean isSuccess, Object response, ErrorBE error, String message) {
			    // if there's an error, you won't be able to connect
            }
        });

8. Finally, when you want to present the LifePics photo selector:

        Intent i = new Intent(getActivity(), ProductsActivity.class);
        startActivity(i);


Customization
-------------

You can customize the colors used by the LifePics Order Activity by tweaking the primary and secondary colors in lp\_colors.xml:

	<!-- tints -->
	<color name="lp_primary_tint_color">#3bb021</color>
	<color name="lp_secondary_tint_color">#fcb024</color>

Further interface customizations can be made using the XML files.

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

