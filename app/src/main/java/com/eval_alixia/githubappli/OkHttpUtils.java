package com.eval_alixia.githubappli;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {
    public static  String sendGetOkHttpRequest(String url) throws Exception {
        Log.w("tag", "url:" + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.code() < 200 || response.code() >= 300) {
            if( response.code() ==404){
                throw new Exception("reponse du serveur incorret : " + response.code() + "\n"
                        + "L'un des champs n'est pas correct, veuillez vérifier votre saisie");
            }else {
                throw new Exception("reponse du serveur incorret : " + response.code());
            }
        } else {
            return response.body().string();
        }
    }
}
