package com.mdgd.commons.capture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.mdgd.commons.resources.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Dan
 * on 21/02/2018.
 */

public class TakePictureDelegate {

    private final Activity CONTEXT;
    private final ImageFormats IMAGE_FORMAT;

    private final ISavePictureDirectory FUN_IMAGE_DIR;
    private final ISavePicturePath FUN_IMAGE_PATH ;
    private final String IMAGE_PATH;

    private final int PERMISSIONS_REQUEST_SELECT;
    private final int REQUEST_IMAGE_CAPTURE;
    private final int PERMISSIONS_REQUEST_CAPTURE;
    private final int REQUEST_SELECT_PICTURE;
    private final IOnImageObtained ON_IMAGE_OBTAINED;

    public TakePictureDelegate(Activity activity, ImageFormats format, String savePicDirPath,
                               ISavePicturePath savePicDirPathFun, ISavePictureDirectory savePicDir,
                               int rcPermissionCamera, int rcPermissionSelect, int requestCodeCapture,
                               int requestCodeSelect, IOnImageObtained onImageObtained) {
        this.CONTEXT = activity;
        this.IMAGE_FORMAT = format;
        this.IMAGE_PATH = savePicDirPath;
        this.FUN_IMAGE_PATH = savePicDirPathFun;
        this.FUN_IMAGE_DIR = savePicDir;
        PERMISSIONS_REQUEST_CAPTURE = rcPermissionCamera;
        REQUEST_IMAGE_CAPTURE = rcPermissionSelect;

        PERMISSIONS_REQUEST_SELECT = requestCodeSelect;
        REQUEST_SELECT_PICTURE = requestCodeCapture;

        ON_IMAGE_OBTAINED = onImageObtained;
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
                if(ON_IMAGE_OBTAINED != null) {
                    ON_IMAGE_OBTAINED.onImageObtained(data.getData());
                }
            }
        }
    }

    protected void fetchNewImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = (Bitmap) extras.get("data");
        try {
            File dir = getSavePictureFile();
            boolean b = dir.exists() || dir.mkdirs();

            File file = new File(dir,System.currentTimeMillis() + IMAGE_FORMAT.suffix);
            b = file.exists() || file.createNewFile();

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(IMAGE_FORMAT.format, 100, bos);
            bos.flush();
            bos.close();

            if(ON_IMAGE_OBTAINED != null) {
                ON_IMAGE_OBTAINED.onImageObtained(Uri.fromFile(file));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getSavePictureFile() {
        if(FUN_IMAGE_DIR != null){
            return FUN_IMAGE_DIR.getSavePictureDirectory();
        }
        if(FUN_IMAGE_PATH != null){
            return new File(FUN_IMAGE_PATH.getSavePictureDirectoryPath());
        }
        if(!TextUtils.isEmpty(IMAGE_PATH)){
            return new File(IMAGE_PATH);
        }
        return null;
    }









    public static class Builder {

        private final Activity activity;
        private ImageFormats format;
        private String savePicDirPath;
        private ISavePicturePath savePicDirPathFun;
        private ISavePictureDirectory savePicDir;
        private int rcPermissionSelect;
        private int rcPermissionCamera;
        private int requestCodeSelect;
        private int requestCodeCapture;
        private IOnImageObtained onImageObtained;

        public Builder(Activity activity){
            this.activity = activity;
        }

        public Builder setPictureFormat(ImageFormats format){
            this.format = format;
            return this;
        }

        public Builder setSavePictureDirectory(String path){
            this.savePicDirPath = path;
            return this;
        }

        public Builder setSavePictureDirectory(ISavePicturePath path){
            this.savePicDirPathFun = path;
            return this;
        }

        public Builder setSavePictureDirectory(ISavePictureDirectory directory){
            this.savePicDir = directory;
            return this;
        }

        public Builder setRequestCodePermissionSelectFromGallery(int requestCodePermissionSelectFromGallery){
            this.rcPermissionSelect = requestCodePermissionSelectFromGallery;
            return this;
        }

        public Builder setRequestCodePermissionCamera(int requestCodePermissionCamera){
            this.rcPermissionCamera = requestCodePermissionCamera;
            return this;
        }

        public Builder setRequestCodeSelect(int requestCodeSelect){
            this.requestCodeSelect = requestCodeSelect;
            return this;
        }

        public Builder setRequestCodeCapture(int requestCodeCapture){
            this.requestCodeCapture = requestCodeCapture;
            return this;
        }

        public Builder setOnImageObtained(IOnImageObtained listener){
            this.onImageObtained = listener;
            return this;
        }

        public TakePictureDelegate build(){
            return new TakePictureDelegate(activity, format, savePicDirPath, savePicDirPathFun,
                    savePicDir, rcPermissionCamera, rcPermissionSelect, requestCodeCapture, requestCodeSelect, onImageObtained);
        }
    }
}
