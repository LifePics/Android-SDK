
# Configuring Image Sources

Photo sources are not yet supported by the Android version of the LifePics SDK.

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
