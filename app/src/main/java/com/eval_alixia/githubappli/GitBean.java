package com.eval_alixia.githubappli;

public class GitBean {
    private String ref;
    private int visibility;

    private ObjetGitBean object;
    public String getRef() {
        return ref;
    }

    public GitBean(String ref,int visibility) {
        this.ref = ref;
        this.visibility = visibility;
    }

    public GitBean() {
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ObjetGitBean getObjetGit() {
        return object;
    }

    public void setObjetGit(ObjetGitBean objet) {
        this.object = objet;
    }
}
