package com.example.kwoksinman.comp437mobile.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.example.kwoksinman.comp437mobile.SystemConstant;
import com.example.kwoksinman.comp437mobile.asyncConstant;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * Created by KwokSinMan on 23/3/2016.
 */
public class loginRegRequest {
    ProgressDialog progressDialog;

    public loginRegRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Checking");
        progressDialog.setMessage("Please Wait⋯⋯");

    }


    public void registerDataInBackground(Activity activity,User user, GetUserCallback userCallback){
        progressDialog.show();
       new registerAsyncTask(activity,user, userCallback).execute();
    }

    public void loginDataInBackground(Activity activity,User user, GetUserCallback callback){
        progressDialog.show();
        new loginAsyncTask(activity,user, callback).execute();
    }

    public class loginAsyncTask extends AsyncTask<Void, Void, User> {
        Activity activity;
        User user;
        GetUserCallback userCallback;

        public loginAsyncTask(Activity activity,User user, GetUserCallback userCallback){
            this.activity = activity;
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {

            User returnUser = null;
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpResponse response;
            JSONObject json = new JSONObject();

            try {
                HttpPost post = new HttpPost(SystemConstant.SERVER_ADDRESS + asyncConstant.GET_LOGIN);
                json.put("name", user.getUserName());
                json.put("pw", user.getPassword());
                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response  = client.execute(post);
                if(response!=null){
                    if (response.getStatusLine().getStatusCode() == asyncConstant.HTTP_STATUS_CODE_OK){
                        String result = EntityUtils.toString(response.getEntity());
                        JSONObject jsonResult = new JSONObject(result);
                        returnUser = new User(jsonResult.getString("id"),jsonResult.getString("name"),jsonResult.getString("pw"));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return returnUser;
        }

        @Override
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }


    //method for login process, connect to server to get back the user data
    public class registerAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        Activity activity;
        GetUserCallback userCallback;

        public registerAsyncTask (Activity activity, User user, GetUserCallback userCallback) {
            this.activity = activity;
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            User returnUser = null;

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, SystemConstant.CONNECTION_TIMEOUT);
            HttpResponse response;
            JSONObject json = new JSONObject();

            try{
                HttpPost post = new HttpPost(SystemConstant.SERVER_ADDRESS + asyncConstant.GET_REGISTER);
                json.put("name", user.getUserName());
                json.put("pw",user.getPassword());
                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response  = client.execute(post);
                if(response!=null){
                    if (response.getStatusLine().getStatusCode() == asyncConstant.HTTP_STATUS_CODE_OK){
                        String result = EntityUtils.toString(response.getEntity());
                        JSONObject jsonResult = new JSONObject(result);
                        returnUser = new User(jsonResult.getString("id"),jsonResult.getString("name"),jsonResult.getString("pw"));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }





}
