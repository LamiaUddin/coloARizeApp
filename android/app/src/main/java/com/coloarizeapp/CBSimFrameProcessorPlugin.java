package com.coloarizeapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class CBSimFrameProcessorPlugin extends FrameProcessorPlugin {
    CBSimFrameProcessorPlugin() {
        super("simColourBlind");
    }

    @Override
    public Object callback(ImageProxy image, Object[] params) {
        Log.d("FrameProcessorPlugin", image.getWidth() + " x " + image.getHeight() + " Image with format #" + image.getFormat() + ". Logging " + params.length + " parameters:");

        for (Object param : params) {
            Log.d("FrameProcessorPlugin", "  -> " + (param == null ? "(null)" : param.toString() + " (" + param.getClass().getName() + ")"));
        }

        if (image.getFormat() != ImageFormat.YUV_420_888) {
            Log.e("FrameProcessorPlugin", "UNRECOGNIZED FORMAT");
            return image;
        }

        return image;
//        for (int x = 0; x < modifiedImage.getWidth(); x++) {
//            for (int y = 0; y < modifiedImage.getHeight(); y++) {
//                int pixel = modifiedImage.getPixel(x, y);
//                int[] argb = {0, 0, 0, 0};
//
//                for (int i = 3; i >=0; i--) {
//                    argb[i] = pixel & 0xFF;
//                    pixel >>= 8;
//                }
//
//                for (int i = 1; i < 4; i++) {
//                    argb[i] = 255 - argb[i];
//                }
//
//                for (int i = 0; i < 4; i++) {
//                    pixel <<= 8;
//                    pixel |= argb[i];
//                }
//
//                modifiedImage.setPixel(x, y, pixel);
//            }
//        }
//
//        Log.d("FrameProcessorPlugin", "test2");
//        return modifiedImage;
    }

    private Bitmap toBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

//    private ByteBuffer[] toBuffer(Bitmap bitmap) {
//
//    }
}
