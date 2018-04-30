package com.mdgd.commons.capture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.mdgd.commons.resources.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Dan
 * on 21/02/2018.
 */

public abstract class TakePictureDelegate {

    private final int PERMISSIONS_REQUEST_SELECT;
    private final int REQUEST_IMAGE_CAPTURE;
    private final int PERMISSIONS_REQUEST_CAPTURE;
    private final int REQUEST_SELECT_PICTURE;
    private final Activity CONTEXT;
    private final String IMAGE_PATH;
    private final ImageFormats IMAGE_FORMAT;

    protected TakePictureDelegate(Activity context, String imagePath, ImageFormats imageFormat, int permissionRequestCodeCapture,
                                  int imageCaptureRequestCode, int permissionRequestCodeSelect, int imageSelectRequestCode) {
        this.CONTEXT = context;
        this.IMAGE_PATH = imagePath;
        this.IMAGE_FORMAT = imageFormat;
        PERMISSIONS_REQUEST_CAPTURE = permissionRequestCodeSelect;
        REQUEST_IMAGE_CAPTURE = imageCaptureRequestCode;

        PERMISSIONS_REQUEST_SELECT = permissionRequestCodeCapture;
        REQUEST_SELECT_PICTURE = imageSelectRequestCode;
    }

    public void makePicture() {
        if (CONTEXT.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED ||
                CONTEXT.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED ||
                CONTEXT.checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CONTEXT,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAPTURE);
            return;
        }
        PackageManager pManager = CONTEXT.getPackageManager();
        boolean hasCamera = pManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasCamera) {
            Toast.makeText(CONTEXT, R.string.no_camera_on_device, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(pManager) != null) {
            CONTEXT.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void selectFromGallery() {
        if (CONTEXT.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CONTEXT, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_SELECT);
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        CONTEXT.startActivityForResult(Intent.createChooser(intent, getChooserTitle()), REQUEST_SELECT_PICTURE);
    }

    protected String getChooserTitle(){
        return CONTEXT.getString(R.string.select_image);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAPTURE) {
            boolean permissionsGranted = areAllPermissionsGranted(grantResults);
            if (permissionsGranted) {
                makePicture();
            }
        }
        else if (requestCode == PERMISSIONS_REQUEST_SELECT) {
            boolean permissionsGranted = areAllPermissionsGranted(grantResults);
            if (permissionsGranted) {
                selectFromGallery();
            }
        }
    }

    private boolean areAllPermissionsGranted(int[] grantResults) {
        boolean result = false;
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            fetchNewImage(data);
        }
        else if (requestCode == REQUEST_SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                updateImage(data.getData());
            }
        }
    }

    protected void fetchNewImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = (Bitmap) extras.get("data");
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + IMAGE_PATH);
            boolean b = dir.exists() || dir.mkdirs();

            File file = new File(dir,System.currentTimeMillis() + IMAGE_FORMAT.suffix);
            b = file.exists() || file.createNewFile();

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(IMAGE_FORMAT.format, 100, bos);
            bos.flush();
            bos.close();

            updateImage(Uri.fromFile(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void updateImage(Uri imageUri);
}
