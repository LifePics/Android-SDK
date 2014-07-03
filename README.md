LifePics Android SDK Version 1.0.0
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


Installation 
------------

These instructions are for setting up the LifePics SDK in an app using Android Studio:

First, copy ksoap2-android-assembly-2.3-jar-with-dependencies into your libs/ folder. It's a required dependency.

You'll also need to make sure to link in Google Play Services for the Maps usage later.

Next, copy the resources from res.zip into your project. (They're all prefixed with lp- to keep them visually differentiated from your own resources.)

You'll need to change at least one value in these resources, specifically the lp_partner_source_id value in lp_settings.xml:

	<!-- lifepics -->
	<string name="lp_partner_source_id">11</string>

Then, allow the following permissions in your AndroidManifest.xml:

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CALL_PHONE"/>
	
In the &lt;application&gt; tag, you'll need to add these &lt;meta-data&gt; tags: ([you'll need a Google Maps v2 key](https://developers.google.com/maps/documentation/android/start#obtain_a_google_maps_api_key))
	
	<meta-data android:name="com.google.android.maps.v2.API_KEY" android:value="YOUR_MAPS_V2_KEY" />
    <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
    	
Also, add the following activities in your AndroidManifest.xml:

	<activity android:name="com.taylorcorp.lifepics.activities.OrderActivity" android:screenOrientation="portrait" />
	<activity android:name="com.taylorcorp.lifepics.activities.CartActivity" android:screenOrientation="portrait" />
	<activity android:name="com.taylorcorp.lifepics.activities.PickupLocationsActivity" android:screenOrientation="portrait" />
	<activity android:name="com.taylorcorp.lifepics.activities.ChangeLocationActivity" android:screenOrientation="portrait" />
	<activity android:name="com.taylorcorp.lifepics.activities.ContactInfoActivity" android:screenOrientation="portrait" />
	<activity android:name="com.taylorcorp.lifepics.activities.OrderCompletedActivity" android:screenOrientation="portrait" />

Then, to support mapping, you need to [provide a key](https://developers.google.com/maps/documentation/android/start#obtain_a_google_maps_api_key) for the V2 Maps:

	<meta-data android:name="com.google.android.maps.v2.API_KEY" android:value="YOUR_KEY_HERE" />
    
Now, in your program logic, you'll want to connect to the LifePics network by providing your Partner ID, Source ID, and password. You can do this in the activity (or fragment) where you plan on presenting the LifePics print selector:

	service.startSession(this, "partnerID", "password", new LifePicsWebServiceResponse() {
		@Override
		public void resultHandler(boolean isSuccess, Object response, ErrorBE error, String message) {
			// respond however you like
		}
	});

Finally, when you want to present the print selector:

	Intent i = new Intent(context, OrderActivity.class);
	startActivity(i);


Customization
-------------

You can customize the colors used by the LifePics Order Activity by tweaking the primary and secondary colors in lp_colors.xml:

	<!-- tints -->
	<color name="lp_primary_tint_color">#3bb021</color>
	<color name="lp_secondary_tint_color">#fcb024</color>

Further interface customizations can be made using the XML files.
