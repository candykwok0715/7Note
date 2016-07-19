package com.example.kwoksinman.comp437mobile.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.kwoksinman.comp437mobile.MainActivity;
import com.example.kwoksinman.comp437mobile.SystemConstant;
import com.example.kwoksinman.comp437mobile.asyncConstant;
import com.example.kwoksinman.comp437mobile.asyncTask.networkTool;
import com.example.kwoksinman.comp437mobile.note.Note;
import com.example.kwoksinman.comp437mobile.note.NoteDAO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

/**
 * Created by KwokSinMan on 25/3/2016.
 */
public class loginSync extends AsyncTask<Void,Void,String> {
    Activity activity;
    private ProgressDialog progressBar;

    public loginSync(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressDialog(activity);
        progressBar.setCancelable(false);
        progressBar.setTitle("Checking");
        progressBar.setMessage("Please Wait⋯⋯");
        progressBar.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        String result = "";
        if (networkTool.isConnected(activity)) {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpResponse response;
            UserLocalStore userLocalStore = new UserLocalStore(activity);
            String userId = userLocalStore.getLoggedInUser().getUserId();
            try {
                HttpGet get = new HttpGet(SystemConstant.SERVER_ADDRESS + asyncConstant.SYNC_NOTE + "?userId=" + userId);
                response = client.execute(get);
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == asyncConstant.HTTP_STATUS_CODE_OK) {
                        result = EntityUtils.toString(response.getEntity());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            result = result = SystemConstant.NO_INTERNET_CONNECTION;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null && !result.equals(SystemConstant.NO_INTERNET_CONNECTION)){
            try {
                JSONArray jsonResult = new JSONArray(result);
                NoteDAO noteDAO = new NoteDAO(activity.getApplicationContext());
                for (int i = 0; i < jsonResult.length(); i++) {
                    Note note = new Note(jsonResult.getJSONObject(i).getString("title"), jsonResult.getJSONObject(i).getString("content"), jsonResult.getJSONObject(i).getString("userid"), jsonResult.getJSONObject(i).getString("isdeleted"),jsonResult.getJSONObject(i).getLong("noteId"),jsonResult.getJSONObject(i).getLong("lastupdate"));
                    noteDAO.insert(note);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        progressBar.dismiss();
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

}
