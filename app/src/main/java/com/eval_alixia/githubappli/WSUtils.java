package com.eval_alixia.githubappli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class WSUtils {
    private final static String URL = "https://api.github.com/repos/OWNER/REPO/git/refs/tags";
    private final static String URLRelease = "https://api.github.com/repos/OWNER/REPO/releases/tags/TAG";
    private static Gson gson = new Gson();
    public static ArrayList<GitBean> loadWeather(String owner, String repo) throws Exception{
        // faire la requete
        String reponseJson = OkHttpUtils.sendGetOkHttpRequest(URL.replace("OWNER",owner).replace("REPO",repo));
        System.out.println(reponseJson);
        // convertir l'objet
        ArrayList<GitBean> list = gson.fromJson(reponseJson,new TypeToken<ArrayList<GitBean>>(){}.getType());

        return list;
    }
    public static ReleaseBean loadRelease(String owner,String repo,String tag) throws  Exception{
        String reponseJsonRelease = OkHttpUtils.sendGetOkHttpRequest(URLRelease.replace("OWNER",owner).replace("REPO",repo).replace("TAG",tag));
        return  new Gson().fromJson(reponseJsonRelease,ReleaseBean.class);
    }
}
