package es.sw.repositorysample.ui.activity;

import android.app.ProgressDialog;
import android.widget.Toast;

import es.sw.repositorysample.R;

/**
 * Created by albertopenasamor on 28/5/15.
 */
public abstract class UIActivity extends BaseActivity {

    private ProgressDialog progressDialog;

    protected void uiShowProgress(){
        uiShowProgress(getResources().getString(R.string.loading));
    }

    protected void uiShowProgress(String message){
        if (message == null){
            message = "";
        }
        progressDialog = ProgressDialog.show(this, "", message, true);
        progressDialog.setCancelable(true);
    }

    protected void uiHideProgress(){
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void uiNullifyProgress(){
        progressDialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHideProgress();
        uiNullifyProgress();
    }

    protected void toast(String message){
        if (message == null){
            message = "";
        }
        try {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
