package com.cityu.tonic.upload;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/*
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;*/

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class AudioFileUploadHandler {
	private Activity mActivity;
	public AudioFileUploadHandler(Activity activity){
		this.mActivity = activity;
	};
	public void uploadAudioFile(Uri uri){
		
		testStub();
//		HttpURLConnection conn = null;
//	    DataOutputStream dos = null;
//	    DataInputStream inStream = null;
//	    String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mypic.png";
//	    String lineEnd = "\r\n";
//	    String twoHyphens = "--";
//	    String boundary = "*****";
//	    int bytesRead, bytesAvailable, bufferSize;
//	    byte[] buffer;
//	    int maxBufferSize = 1 * 1024 * 1024;
//	    String responseFromServer = "";
//	    String urlString = "http://mywebsite.com/directory/upload.php";
//
//	    try {
//
//	        //------------------ CLIENT REQUEST
//	        FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
//	        // open a URL connection to the Servlet
//	        URL url = new URL(urlString);
//	        // Open a HTTP connection to the URL
//	        conn = (HttpURLConnection) url.openConnection();
//	        // Allow Inputs
//	        conn.setDoInput(true);
//	        // Allow Outputs
//	        conn.setDoOutput(true);
//	        // Don't use a cached copy.
//	        conn.setUseCaches(false);
//	        // Use a post method.
//	        conn.setRequestMethod("POST");
//	        conn.setRequestProperty("Connection", "Keep-Alive");
//	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//	        dos = new DataOutputStream(conn.getOutputStream());
//	        dos.writeBytes(twoHyphens + boundary + lineEnd);
//	        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
//	        dos.writeBytes(lineEnd);
//	        // create a buffer of maximum size
//	        bytesAvailable = fileInputStream.available();
//	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
//	        buffer = new byte[bufferSize];
//	        // read file and write it into form...
//	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//	        while (bytesRead > 0) {
//
//	            dos.write(buffer, 0, bufferSize);
//	            bytesAvailable = fileInputStream.available();
//	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//	        }
//
//	        // send multipart form data necesssary after file data...
//	        dos.writeBytes(lineEnd);
//	        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//	        // close streams
//	        Log.e("Debug", "File is written");
//	        fileInputStream.close();
//	        dos.flush();
//	        dos.close();
//
//	    } catch (MalformedURLException ex) {
//	        Log.e("Debug", "error: " + ex.getMessage(), ex);
//	    } catch (IOException ioe) {
//	        Log.e("Debug", "error: " + ioe.getMessage(), ioe);
//	    }
//
//	    //------------------ read the SERVER RESPONSE
//	    try {
//
//	        inStream = new DataInputStream(conn.getInputStream());
//	        String str;
//
//	        while ((str = inStream.readLine()) != null) {
//
//	            Log.e("Debug", "Server Response " + str);
//
//	        }
//
//	        inStream.close();
//
//	    } catch (IOException ioex) {
//	        Log.e("Debug", "error: " + ioex.getMessage(), ioex);
//	    }
	}
	
	public void testStub(){
		/*
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				
				 AWSCredentials credentials = null;
			     try {
			         credentials = new ProfileCredentialsProvider().getCredentials();
			     } catch (Exception e) {
			         throw new AmazonClientException(
			                    "Cannot load the credentials from the credential profiles file. " +
			                    "Please make sure that your credentials file is at the correct " +
			                    "location (~/.aws/credentials), and is in valid format.",
			                    e);
			    }
			     AmazonS3 s3 = new AmazonS3Client(credentials);
			     String bucketName = "tonicaudio";
			     String key = "myObjectKey";
			     try {
			    	 PutObjectResult result = s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
			    	 result.getContentMd5();
			     } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
		}.execute();
		*/
	     
	}
	
	
		
	 private static File createSampleFile() throws IOException {
	        File file = File.createTempFile("aws-java-sdk-", ".txt");
	        file.deleteOnExit();

	        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
	        writer.write("abcdefghijklmnopqrstuvwxyz\n");
	        writer.write("01234567890112345678901234\n");
	        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
	        writer.write("01234567890112345678901234\n");
	        writer.write("abcdefghijklmnopqrstuvwxyz\n");
	        writer.close();

	        return file;
	    }
	    
	
}
