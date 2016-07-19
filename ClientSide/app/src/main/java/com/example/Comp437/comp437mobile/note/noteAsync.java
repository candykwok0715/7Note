package com.example.kwoksinman.comp437mobile.note;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.kwoksinman.comp437mobile.MainActivity;
import com.example.kwoksinman.comp437mobile.SystemConstant;
import com.example.kwoksinman.comp437mobile.asyncConstant;
import com.example.kwoksinman.comp437mobile.asyncTask.networkTool;
import com.example.kwoksinman.comp437mobile.login.User;
import com.example.kwoksinman.comp437mobile.login.UserLocalStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by KwokSinMan on 24/3/2016.
 */
public class noteAsync extends AsyncTask<Void,Void,String> {
    Activity activity;
    private ProgressDialog progressBar;

    public noteAsync(Activity activity){
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
    protected String doInBackground(Void... params){
        String result="";
        if (networkTool.isConnected(activity)) {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);

            HttpResponse response;
            try {
                HttpPost post = new HttpPost(SystemConstant.SERVER_ADDRESS + asyncConstant.SYNC_NOTE);
                UserLocalStore userLocalStore = new UserLocalStore(activity);
                String userId = userLocalStore.getLoggedInUser().getUserId();
                NoteDAO noteDAO = new NoteDAO(activity.getApplicationContext());
                List<Note> noteList = noteDAO.getAll();

                JSONObject json = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < noteList.size(); i++) {
                    jsonArray.put(noteList.get(i).getNoteJson());
                    //System.out.println("Candy noteList=" + noteList.get(i).getNoteJson().toString());
                }
                //System.out.println("Candy jsonArray.toString()=" + jsonArray.toString());
                json.put("userId", userId);
                json.put("notes", jsonArray);
                //System.out.println("Candy json.toString()=" + json.toString());

                StringEntity se = new StringEntity(json.toString(), "UTF-8" );
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                post.setEntity(se);
                response = client.execute(post);
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == asyncConstant.HTTP_STATUS_CODE_OK) {
                        result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            result = SystemConstant.NO_INTERNET_CONNECTION;
        }
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        if (result!=null && !result.equals(SystemConstant.NO_INTERNET_CONNECTION)){
            //System.out.println("Candy result=" + result);
            try {
                JSONArray jsonResult = new JSONArray(result);
                NoteDAO noteDAO = new NoteDAO(activity.getApplicationContext());
                //System.out.println("Candy =" + noteDAO.deleteAll());
                noteDAO.deleteAll();
                //System.out.println("Candy jsonResult.toString()="+jsonResult.toString());
                for (int i = 0; i < jsonResult.length(); i++) {
                    long serverNoteID = jsonResult.getJSONObject(i).getLong("noteId");
                    long lastupdate = jsonResult.getJSONObject(i).getLong("lastupdate");

                    Note note = new Note(jsonResult.getJSONObject(i).getString("title"),jsonResult.getJSONObject(i).getString("content"),jsonResult.getJSONObject(i).getString("userid"),jsonResult.getJSONObject(i).getString("isdeleted"),serverNoteID,lastupdate);

                    //System.out.println("Candy serverNoteID=" + serverNoteID);
                    //System.out.println("Candy lastupdate=" + lastupdate);
                    //System.out.println("Candy note=" + note.toString());
                    noteDAO.insert(note);
                }
            }catch (Exception e){
                e.printStackTrace();;
            }
        }
        progressBar.dismiss();
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

}
