package com.eval_alixia.githubappli;

import com.google.gson.annotations.SerializedName;

public class ObjetGitBean {
    @SerializedName("url")
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
