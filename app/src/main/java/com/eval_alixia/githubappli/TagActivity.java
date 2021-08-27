package com.eval_alixia.githubappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;

public class TagActivity extends AppCompatActivity {
    private EditText etRechercheOwner;
    private EditText etRechercheRepo;
    private TextView tvError;
    private TextView tvRef;
    private TextView tvUrl;
    private ProgressBar progressBar;
    private TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tvError = findViewById(R.id.tvError);
        etRechercheOwner = findViewById(R.id.etRechercheOwner);
        etRechercheRepo = findViewById(R.id.etRechercheRepo);
        tvRef = findViewById(R.id.tvRef);
        tvUrl  = findViewById(R.id.tvUrl);
        progressBar = findViewById(R.id.progressBar);
        tvMessage = findViewById(R.id.tvMessage);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,25,0,"Voir tous les tags");
        menu.add(0,26,0,"Accueil");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 25:
                Intent AllTag = new Intent(this,AllTagActivity.class);
                startActivity(AllTag);
                break;
            case 26:
                Intent mainTag= new Intent(this,MainActivity.class);
                startActivity(mainTag);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBTRechercheClick(View view) {
        setError("");
        tvRef.setText("");
        tvMessage.setText("");
        tvUrl.setText("");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            afficherTag();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==
        PackageManager.PERMISSION_GRANTED){
            afficherTag();
        }else{
            setError("Il faut la permission");
        }
    }
    String owner;
    String repo;
    private void afficherMessage(String owner,String repo,String tag) throws Exception {
        ReleaseBean releaseBean  =WSUtils.loadRelease(owner,repo,tag);
        refreshRelease(releaseBean);
        showProgressBar(false);
    }
    private void afficherTag() {
        final String[] texte = new String[1];
        showProgressBar(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    texte[0] = "URL valide";
                    Looper.prepare();
                    // on recup la donnée et on la nettoi
                    owner = etRechercheOwner.getText().toString();
                    repo = etRechercheRepo.getText().toString();
                    repo = TagActivity.this.cleanChain(repo);
                    etRechercheRepo.setText(repo);
                    // cherche la donnée
                    ArrayList<GitBean> gitBeanList = WSUtils.loadWeather(etRechercheOwner.getText().toString(), etRechercheRepo.getText().toString());
                    TagActivity.this.refreshGitBean(gitBeanList);
                    SystemClock.sleep(1000);
                    String tag = tvRef.getText().toString();
                    TagActivity.this.afficherMessage(owner, repo, tag);
                    Toast.makeText(TagActivity.this, texte[0], Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                    TagActivity.this.setError("Erreur : " + e.getMessage());
                }
                TagActivity.this.showProgressBar(false);
            }
        }).start();
    }

    private void showProgressBar(boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void refreshRelease(ReleaseBean releaseBean){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (releaseBean.getBodyMessage() != null && !releaseBean.getBodyMessage().isEmpty()) {
                    String message = releaseBean.getBodyMessage();
                    System.out.println(message);
                    tvMessage.setText(message);
                } else {
                    String error = "\"Objet vide release\"";
                    setError(error);
                }
            }
            });
        showProgressBar(false);
    }
    private void refreshGitBean(ArrayList<GitBean> gitBeanList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gitBeanList.get(0) != null && !gitBeanList.isEmpty()) {
                    int i = gitBeanList.toArray().length;
                    i--;
                    GitBean gitbean = gitBeanList.get(i);
                    String tag = gitbean.getRef();
                    tag = tag.replace("refs/tags/", "");
                    tvRef.setText(tag);
                    ObjetGitBean objgitbean = gitbean.getObjetGit();
                    String url = objgitbean.getUrl();
                    tvUrl.setText(url);
                } else {
                    String error = "\"Objet vide\"";
                    TagActivity.this.setError(error);
                }
            }
        });
    }

    private String cleanChain(String chain){
        chain = chain.toLowerCase();
        chain = chain.replace("/","");
        chain = chain.replace(" ","-");
        chain = Normalizer.normalize(chain, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return chain;
    }
    private void setError(String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMessage.isEmpty()) {
                    tvError.setVisibility(View.GONE);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(errorMessage);
                }
            }
        });
    }

}