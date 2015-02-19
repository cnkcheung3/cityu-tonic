package com.cityu.tonic.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageChooseHandler {
	
	private final static int REQUEST_CODE_GALLERY = 435;
	private final static int REQUEST_CODE_CROP_IMAGE = 111;
	private Fragment mFragment;
	private String imgPath, fileName;
    private Bitmap bitmap;
    
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private File mFileTemp;
	
	public ImageChooseHandler(Fragment fragment){
		this.mFragment = fragment;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
    	}
    	else {
    		mFileTemp = new File(mFragment.getActivity().getFilesDir(), TEMP_PHOTO_FILE_NAME);
    	}
		
		
	}
	
	public void loadImagefromGallery() {
     
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        mFragment.startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        
    }
	
	/** When Image is selected from Gallery
	 * should be called in activty onActivityResult()
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
        	
        	if(resultCode !=  Activity.RESULT_OK)
        		return;
        	
        	switch(requestCode){
        	// When an Image is picked
        	case REQUEST_CODE_GALLERY:
        		if (data == null) 
        			return;
                // Get the Image from data
        		try {
                    InputStream inputStream = mFragment.getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    
                    // start Crop Image
                    Intent intent = new Intent(mFragment.getActivity(), CropImage.class);
                    intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
                    intent.putExtra(CropImage.SCALE, true);

                    intent.putExtra(CropImage.ASPECT_X, 1);
                    intent.putExtra(CropImage.ASPECT_Y, 1);

                    mFragment.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

                } catch (Exception e) {

                    Log.v("ken", "Error while creating temp file", e);
                }

              
                break;
        	case REQUEST_CODE_CROP_IMAGE:
        		
        		imgPath = data.getStringExtra(CropImage.IMAGE_PATH);
        		onGetImagePath(mFragment.getActivity(), imgPath);
        		break;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
    
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    
    protected void onGetImagePath(Context context, String imgPath){};
}
