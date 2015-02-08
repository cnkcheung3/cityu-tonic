package com.cityu.tonic.upload;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;

public class AudioFileUploadHandler {
	private Activity mActivity;
	public AudioFileUploadHandler(Activity activity){
		this.mActivity = activity;
	};
	public void uploadAudioFile(Uri uri){
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
	
}
