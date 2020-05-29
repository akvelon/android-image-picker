
# ImagePicker 

A simple Instagram like library to select images from the gallery and camera.

# Screenshot

<details>
	<summary>Click to see how image picker looks…</summary>
<img
src="https://raw.githubusercontent.com/akvelon/android-image-picker/master/art/example.gif" height="522" width="263"/>
</details>


# Usage

For full example, please refer to the `sample` app.

Add this to your project's `build.gradle`

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
And add this to your module's `build.gradle` 
```groovy
dependencies {
    implementation 'com.github.akvelon.android-image-picker:imagepicker:x.y.z'
    // If you have a problem with Glide, please use the same Glide version or simply open an issue
    implementation 'com.github.bumptech.glide:glide:4.11.0’
}
```
change `x.y.z` to version in the [release page](https://github.com/akvelon/android-image-picker/releases)

## Start image picker activity

The simplest way to start 

```java
ImagePicker.launch(this); // Activity or Fragment
``` 
If you already have selected images

```java
ImagePicker.launchWithPreselectedImages(
this, // Activity or Fragment
 alreadySelectedImages // list of already selected files
);
``` 

## Receive result

```java
  @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldResolve(requestCode, resultCode)) {
            // Get a list of picked images
            List<File> images = ImagePicker.getImages(data);
            // or get a single image only
            File image = ImagePicker.getSingleImageOrNull(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```

# AndroidX

As version 1.0.0 above, we already use AndroidX artifact in our library. 
If you have any trouble adding this version to your current project, please add this to your `gradle.properties` :

```
android.useAndroidX=true
android.enableJetifier=true
```
Or simply create an [issue](https://github.com/akvelon/android-image-picker/issues)
