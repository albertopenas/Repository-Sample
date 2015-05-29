package es.sw.repositorysample.net.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorHelper {

    private static final String TAG = "VolleyErrorHelper";

    public enum VolleyNetworkError{
        Generic,
        TimeOut,
        Network,
        ServerProblem,
        Unauthorized
    }

     /**
     * Returns appropriate message which is to be displayed to the user 
     * against the specified error object.
     * 
     * @param error
     * @return
     */
  public static VolleyNetworkError getVolleyError(Object error) {
      if (error == null){
          return VolleyNetworkError.Generic;
      }else if (error instanceof TimeoutError) {
          return VolleyNetworkError.TimeOut;
      }else if (isServerProblem(error)) {
          return handleServerError(error);
      }else if (isNetworkProblem(error)) {
          return VolleyNetworkError.Network;
      }
      return VolleyNetworkError.Generic;
  }
  
  /**
  * Determines whether the error is related to network
  * @param error
  * @return
  */
  private static boolean isNetworkProblem(Object error) {
      Log.e(TAG, "isNetworkProblem");
      return (error instanceof NetworkError) || (error instanceof NoConnectionError);
  }
  /**
  * Determines whether the error is related to server
  * @param error
  * @return
  */
  private static boolean isServerProblem(Object error) {
      Log.e(TAG, "isServerProblem");
      return (error instanceof ServerError) || (error instanceof AuthFailureError);
  }
  /**
  * Handles the server error, tries to determine whether to show a stock message or to 
  * show a message retrieved from the server.
  * 
  * @param err
  * @return
  */
  private static VolleyNetworkError handleServerError(Object err) {
      Log.e(TAG, "handleServerError");
      VolleyError error = (VolleyError) err;
  
      NetworkResponse response = error.networkResponse;
  
      if (response != null) {
          switch (response.statusCode) {
            case 401:
                return VolleyNetworkError.Unauthorized;
         }
      }
      return VolleyNetworkError.ServerProblem;
  }
}