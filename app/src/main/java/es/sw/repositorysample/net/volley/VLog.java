package es.sw.repositorysample.net.volley;

import android.util.Log;

import com.android.volley.VolleyError;

import java.util.Map;

public class VLog extends Object{
	
	private static final String TAG = VLog.class.getSimpleName();
	
	public static void e(String tag, VolleyError volleyError){
		int code = -1;
		String message = null;
		
		if (volleyError.networkResponse != null){
			 code = volleyError.networkResponse.statusCode;
		}
		if (volleyError.getMessage() != null && !volleyError.getMessage().isEmpty()){
			message = volleyError.getMessage();
		}
		if (message == null && volleyError.getLocalizedMessage() != null && !volleyError.getLocalizedMessage().isEmpty()){
			message = volleyError.getLocalizedMessage();
		}
		
		if (message == null && volleyError.networkResponse != null && volleyError.networkResponse.data != null){
			message = new String(volleyError.networkResponse.data);
		}
		
		if (message == null && volleyError.networkResponse != null && volleyError.networkResponse.headers != null){
			Map<String, String>headers = volleyError.networkResponse.headers;
			StringBuilder string = new StringBuilder();
			for(Map.Entry<String, String> entry : headers.entrySet()){
				string.append(entry.getValue());
				string.append("\n");
			}
			message = string.toString();
		}
		if (message == null){
			message = "Empty message";
		}
		Log.e(tag, "statusCode: "+ code + ", error: " + message);
	}
	
	public static String message(VolleyError volleyError){
		String message = null;
		
		if (volleyError.getMessage() != null && !volleyError.getMessage().isEmpty()){
			message = volleyError.getMessage();
		}
		if (message == null && volleyError.getLocalizedMessage() != null && !volleyError.getLocalizedMessage().isEmpty()){
			message = volleyError.getLocalizedMessage();
		}
		if (message == null && volleyError.networkResponse != null && volleyError.networkResponse.data != null){
			message = new String(volleyError.networkResponse.data);
		}
		if (message == null){
			message = "";
		}
		
		return  message;
	}
	
	public static int statusCode(VolleyError volleyError){
		if (volleyError != null && volleyError.networkResponse != null){
			Log.d(TAG, "statusCode: " + volleyError.networkResponse.statusCode);
			return volleyError.networkResponse.statusCode;
		}
		Log.d(TAG, "statusCode: -1");
		return -1; 
	}
}
