# Configuring Image Sources

The LifePics framework presents a set of image sources that can retrieve users' favorite images to select for printing. However, they'll only appear when they're properly configured. The official list of supported image sources are:

* Local Device Images (always presented)
* Instagram
* Facebook
* Flickr
* Google
* SmugMug
* LifePics (always presented)
* Seeded Images (developer-defined)

To properly configure an image source, you generally need to provide a Public Key or options that are going to be unique to each one. Configuration for each source is explained below.

It's advised that you place all your string resources for these image sources in a separate XML file such as keys.xml.  This will keep them separate from future LifePics SDK updates.


### Instagram

1. Create a new client on [Instagram](http://instagram.com/developer/clients/manage/).
2. Set the redirect URL to **ig[Your Instagram Client ID]://authorize**.
2. Add a string resource for the key *lp_instagram_key* and set it to your provided *Instagram Client ID*.


### Facebook

The Facebook SDK must be integrated into your project.  If it's not already included, follow the following steps:

1. [Download](https://developers.facebook.com/docs/android) the Facebook SDK.
2. Unzip the SDK to a permanent place in your project's parent folder or a shared team will all be able to access.
3. Import the module with File->Import Module and choose the Facebook folder. (e.g. facebook-android-sdk-3.16/)  
4. Deselect everything except the :facebook module. (You don't need the samples in the project.)
5. Open up module settings and add a Module dependency to your app.  Choose :facebook.
6. If you see compilation errors regarding SDK constants, open up the build.gradle file in the facebook module and replace:

        compileSdkVersion Integer.parseInt(project.ANDROID_BUILD_SDK_VERSION)
	    buildToolsVersion project.ANDROID_BUILD_TOOLS_VERSION
        
	    defaultConfig {
	        minSdkVersion Integer.parseInt(project.ANDROID_BUILD_MIN_SDK_VERSION)
	        targetSdkVersion Integer.parseInt(project.ANDROID_BUILD_TARGET_SDK_VERSION)
	    }
	    
	with the values you'd like to use (you can copy these from your app's build.gradle):
	
        compileSdkVersion 19
        buildToolsVersion '19.1'
        
        defaultConfig {
            minSdkVersion 10
            targetSdkVersion 19
        }

7. Run "Sync Project with Gradle Files" from the toolbar.

Facebook should now be installed into your project.  Note that if you run into issues involving duplicate DEX files, it's because Facebook uses the V4 support library and LifePics uses the V7 library.  Removing the V4 jar from Facebook's libs/ folder and making it a module dependency instead should work.

Now, you'll need to configure it as an image source:

1. Create a new app from your Facebook account.
2. Follow the first three steps for [configuring the app for iOS](https://developers.facebook.com/docs/ios/getting-started/).
3. Add a string resource for the key *lp_facebook_app_id* and set it to your provided *Facebook App ID*.
4. Add the following meta data to your Android Manifest inside your <application> tag:
	
        <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/lp_facebook_app_id"/>

5. Add the following activity entry to your Android Manifest as well:

        <activity android:name="com.facebook.LoginActivity"/>


### Flickr

1. Create a new project on [Flickr](https://www.flickr.com/services/apps/create/).
2. Add a string resource for the key *lp_flickr_key* and set it to your provided *Flickr Key*.
3. Add a string resource for the key *lp_flickr_secret* and set it to your provided *Flickr Secret*.
4. Add the following activity entry to your Android Manifest as well. Be sure to replace [*Flickr Key*] with your Flickr Key:

        <activity android:name="com.taylorcorp.lifepics.activities.FlickrViewerActivity"
            android:screenOrientation="sensorPortrait">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="flickr[*Flickr Key*]" />
            </intent-filter>
        </activity>

### Google

1. Create a new project on your [Google Dashboard](https://console.developers.google.com/project).
2. In the *Apis and auth* section, add your app's information and package name.
3. Add a string resource for the key *lp_google_key* and set it to your provided *OAuth Client ID (in APIs & auth section)*.

### SmugMug

1. Create a new app key on [SmugMug's Website](https://secure.smugmug.com/settings/?nick=raptureinvenice#section=api-keys).
2. Add a string resource for the key *lp_smugmug_key* and set it to your provided *Key*.

### Seeded Images

Seeded images are a special kind of image source that you define on your own. You might use this image source to provide a set of images bundled in your own app, found on the web somewhere, or perhaps coming from your own in-app photo selection.

To configure this image source, you'll only use code. What you'll need to do is create a class that implements the **ImageDataSource** and **Serializable** protocols. Then, prior to beginning the print order flow, provide a seeded data source to the Image Source Factory:

    ImageSourceFactory.getInstance().setSeededImageDataSource(new SimpleImageDataSource());

An example is provided by the *SimpleImageDataSource* class. You'll need to implement the following methods:

#### - String getName(Context context)

This should return the string you want displayed on the source selection screen. It should represent where the images are coming from or your service name.

#### - Drawable getIcon(Context context)

This should return the image you want displayed on the source selection screen.

#### - Rect getFullImageSizeForImageRepresentation(Context context, ImageRepresentation imageRepresentation)

Return the size (width and height) in pixels of the full-sized image.

#### - void getImageRepresentations(Context context, ImageRepresentationListener listener)

This is an asynchronous method that should call the completion block with an array of *ImageRepresentation* objects that represent each of your images.

You must provide string representations of the thumbnail and full images. In the sample implementation, they are simply resource id's.

#### - void fetchThumbnailForImageRepresentation(Context context, ImageRepresentation imageRepresentation, ImageRepresentationListener listener)

This is an asynchronous method that passes one image representation to you (from the set you provided previously) and asks that you provide a thumbnail image for it. If your image representation *is* a Drawable, you might pass it right back. In the sample implementation, a Drawable is created from the resource bundle file it references. You might also load an image from the web.

#### - void fetchFullSizeImageRepresentation(Context context, ImageRepresentation imageRepresentation, ImageRepresentationListener listener)

This is also an asynchronous method that is identical to the above except it expects a full-size image appropriate for printing. Note that the thumbnail can be this same full-size image, but you shouldn't use a small thumbnail as the full-size image. Providing a low resolution image for printing will result in fuzzy printouts.
