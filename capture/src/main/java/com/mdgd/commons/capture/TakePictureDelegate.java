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
    private final Activity context;

    protected TakePictureDelegate(Activity context, int permissionRequestCodeCapture, int imageCaptureRequestCode,
                                  int permissionRequestCodeSelect, int imageSelectRequestCode) {
        this.context = context;

        PERMISSIONS_REQUEST_CAPTURE = permissionRequestCodeSelect;
        REQUEST_IMAGE_CAPTURE = imageCaptureRequestCode;

        PERMISSIONS_REQUEST_SELECT = permissionRequestCodeCapture;
        REQUEST_SELECT_PICTURE = imageSelectRequestCode;
    }

    public void makePicture() {
        if (context.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED ||
                context.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED ||
                context.checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAPTURE);
            return;
        }
        PackageManager pManager = context.getPackageManager();
        boolean hasCamera = pManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasCamera) {
            Toast.makeText(context, R.string.no_camera_on_device, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(pManager) != null) {
            context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void selectFromGallery() {
        if (context.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, android.os.Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_SELECT);
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(Intent.createChooser(intent, getChooserTitle()), REQUEST_SELECT_PICTURE);
    }

    protected String getChooserTitle(){
        return context.getString(R.string.select_image);
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
            File dir = new File(Environment.getExternalStorageDirectory() + "/CService/Photo");
            boolean b = dir.exists() || dir.mkdirs();

            File file = new File(dir,System.currentTimeMillis() + ".jpeg");
            b = file.exists() || file.createNewFile();

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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
