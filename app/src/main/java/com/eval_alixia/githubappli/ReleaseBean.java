package com.eval_alixia.githubappli;

import com.google.gson.annotations.SerializedName;

public class ReleaseBean {
    private String created_at;
    @SerializedName("body")
    private String bodyMessage;
    @SerializedName("author")
    private AuthorBean authorIcon;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    public AuthorBean getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(AuthorBean authorIcon) {
        this.authorIcon = authorIcon;
    }
}
