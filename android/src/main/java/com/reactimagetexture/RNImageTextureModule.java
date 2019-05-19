
package com.reactimagetexture;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.reactimagetexture.ResponseHelper;
import com.facebook.react.bridge.ReadableMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.util.Log;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.ColorMatrixColorFilter;
import android.content.res.Resources;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;

import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class RNImageTextureModule extends ReactContextBaseJavaModule {

  public static final int INDEX_FRAME = 1;
  public static final int INDEX_LIGHT = 2;
  public static final int MODE_SCREEN = 3;
  public static final int MODE_MULTIPLY = 1;
  public static final int MODE_OVERLAY = 2;
  public static final int MODE_NONE = -1;
  

  private final ReactApplicationContext reactContext;

  Bitmap textureBitmap = null;
  Bitmap sourceBitmap;
  int bitmapWidth;
  int bitmapHeight;
  int index;

  ResponseHelper responseHelper;
  ReadableMap options;
  protected Callback callback;

  String IMAGE_SOURCE1 = "imageSource1", IMAGE_SOURCE2 = "imageSource2", DATA_TYPE1 = "dataType1",
      DATA_TYPE2 = "dataType2", TEXTURE_TYPE = "textureType", IS_ACCSETS = "isAccsets", MODE = "mode";

  public RNImageTextureModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNImageTexture";
  }

  @ReactMethod
  public void getResourcesImageTexture(final ReadableMap options, final Callback callback) {
    try {
      responseHelper = new ResponseHelper();
      this.callback = callback;
      this.options = options;

      Bitmap bitmap1 = null;
      Bitmap bitmap2 = null;

      //
      if (options.getBoolean(IS_ACCSETS)) {

        if (options.getString(IMAGE_SOURCE1) == null) {
          responseHelper.invokeError(callback, "Source1 Null");
          return;
        } else {
          if (options.getString(DATA_TYPE1).equals("Path")) {
            bitmap1 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE1));
          } else {
            bitmap1 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE1));
          }
        }

        if (bitmap1 == null) {
          responseHelper.invokeError(callback, "Failed to decode. Path is incorrect or image is corrupted");
          return;
        } else {
          if (options.getInt(TEXTURE_TYPE) == MODE_NONE) {
            responseHelper.putString("base64", decodeBipmapToBase64(bitmap1));
            responseHelper.putInt("width", bitmap1.getWidth());
            responseHelper.putInt("height", bitmap1.getHeight());
            responseHelper.invokeResponse(callback);
            return;
          } else {
            Bitmap[] btmArr = { bitmap1, null };
            // ----- Image Processing
            setBitmap(bitmap1);
            MyAsynctackTexture myAsynTaskTexture = new MyAsynctackTexture();
            myAsynTaskTexture.execute(btmArr);

            // ----- Image Processing
          }

        }
      } else {

        index = options.getInt(MODE);
        if (options.getString(IMAGE_SOURCE1) == null || options.getString(IMAGE_SOURCE2) == null) {
          responseHelper.invokeError(callback, "Source Null");
          return;
        } else {

          if (options.getString(DATA_TYPE1).equals("Path")) {
            bitmap1 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE1));
          } else {
            bitmap1 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE1));
          }

          if (options.getString(DATA_TYPE2).equals("Path")) {
            bitmap2 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE2));
          } else {
            bitmap2 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE2));
          }

        }

        if (bitmap1 == null || bitmap2 == null) {
          responseHelper.invokeError(callback, "Failed to decode. Path is incorrect or image is corrupted");
          return;
        } else {

          // ----- Image Processing
          Bitmap[] btmArr = { bitmap1, bitmap2 };
          setBitmap(bitmap1);
          MyAsynctackTexture myAsynTaskTexture = new MyAsynctackTexture();
          myAsynTaskTexture.execute(btmArr);
          // ----- Image Processing

        }

      }
    } catch (Exception ex) {
      Log.e("ERR", ex.getMessage());
    }

  }

  public void setBitmap(Bitmap btm) {
    sourceBitmap = btm;
    bitmapWidth = sourceBitmap.getWidth();
    bitmapHeight = sourceBitmap.getHeight();
    textureBitmap = null;
  }

  // ----------------------------------------------------------------------------------------------

  // AsyncTask Texture
  public class MyAsynctackTexture extends AsyncTask<Bitmap, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {

      Bitmap src1 = bitmaps[0];
      Bitmap src2 = bitmaps[1];

      textureBitmap = src1.copy(Bitmap.Config.ARGB_8888, true);

      Canvas cvs = new Canvas(textureBitmap);
      cvs.drawBitmap(src1, 0.0f, 0.0f, new Paint());
      if (cvs != null) {
        if (!options.getBoolean(IS_ACCSETS)) {
          pipelineTexture(textureBitmap, src2);
        } else {
          pipelineTexture(textureBitmap, getBitmapType());
        }

      } else {
        cancel(true);

      }

      return textureBitmap;

    }

    void pipelineTexture(Bitmap btmPipe, Bitmap btmTexture) {

      filterMultiply(btmPipe, btmTexture);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);

      String encodeIamge = "";
      if (bitmap != null) {
        encodeIamge = decodeBipmapToBase64(bitmap);
        responseHelper.putString("base64", encodeIamge);
        responseHelper.putInt("width", bitmap.getWidth());
        responseHelper.putInt("height", bitmap.getHeight());

        responseHelper.invokeResponse(callback);
      } else {
        responseHelper.invokeError(callback, "Failed! Bitmap Processed Null");
      }

      callback = null;
      options = null;

    }
  }

  public void filterMultiply(Bitmap btm1, Bitmap btmTexture) {
    if (index != 0) {
      Bitmap textureBitmap;
      Paint paint = new Paint(INDEX_FRAME);
      PorterDuff.Mode mode = PorterDuff.Mode.SCREEN;
      if (index == INDEX_FRAME) {
        mode = PorterDuff.Mode.MULTIPLY;
      } else if (index == INDEX_LIGHT&& Build.VERSION.SDK_INT > 10) {
        mode = PorterDuff.Mode.OVERLAY;
      } else if (index == INDEX_LIGHT && Build.VERSION.SDK_INT <= 10) {
        mode = PorterDuff.Mode.MULTIPLY;
      }
      paint.setXfermode(new PorterDuffXfermode(mode));
      Matrix borderMatrix = new Matrix();

      textureBitmap = btmTexture;

      float wScale = ((float) btm1.getWidth()) / ((float) textureBitmap.getWidth());
      float hScale = ((float) btm1.getHeight()) / ((float) textureBitmap.getHeight());
      borderMatrix.reset();
      Canvas cvs = new Canvas(btm1);
      borderMatrix.postScale(wScale, hScale);
      cvs.drawBitmap(textureBitmap, borderMatrix, paint);
      if (textureBitmap != null && btm1 != textureBitmap) {
        textureBitmap.recycle();
      }
    }
  }

  // method Image processing

  Bitmap getBitmapType() {
    Resources res = reactContext.getResources();

    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

    opts.inSampleSize = INDEX_FRAME;

    switch (options.getInt(TEXTURE_TYPE)) {
    case 0:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_01, opts);
    case 1:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_03, opts);
    case 2:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_04, opts);
    case 3:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_22, opts);
    case 4:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_05, opts);
    case 5:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_06, opts);
    case 6:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_07, opts);
    case 7:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_08, opts);
    case 8:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_09, opts);
    case 9:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_10, opts);
    case 10:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_26, opts);
    case 11:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_11, opts);
    case 12:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_12, opts);
    case 13:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_13, opts);
    case 14:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_23, opts);
    case 15:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_14, opts);
    case 16:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_15, opts);
    case 17:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_16, opts);
    case 18:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_18, opts);
    case 19:
      index = MODE_MULTIPLY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_19, opts);
    case 20:
      index = MODE_MULTIPLY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_21, opts);
    case 21:
      index = MODE_SCREEN;
      return BitmapFactory.decodeResource(res, R.drawable.texture_02, opts);
    case 22:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_24, opts);
    default:
      index = MODE_OVERLAY;
      return BitmapFactory.decodeResource(res, R.drawable.texture_01, opts);
    }
  }

  // ----------------------------------------------------------------------------------------------

  //
  public Bitmap encodeBitmapFromBase64(String base64) {
    Bitmap btm = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] imageBytes = baos.toByteArray();
      imageBytes = Base64.decode(base64, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    } catch (Exception ex) {
      Log.e("ERR1", ex.getMessage());
    }
    return btm;
  }

  public String decodeBipmapToBase64(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();

    return Base64.encodeToString(b, Base64.DEFAULT);
  }

}