package com.eval_alixia.githubappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;

public class AllTagActivity extends AppCompatActivity {
    //Données
    private final ArrayList<GitBean> data = new ArrayList<>();
    private  final  ArrayList<ReleaseBean> dataR = new ArrayList<>();
    private ArrayList<GitBean> gitBeanList;
    String tag;
    String owner;
    String repo;

    // composant graphique
    private EditText etRechercheOwnerAll;
    private EditText etRechercheRepoAll;
    private TextView tvErrorAll;
    private RecyclerView rvAll;
    private ProgressBar progressBarAll;
    private GitAdapter gitAdapter = new GitAdapter(data);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tag);
        tvErrorAll = findViewById(R.id.tvErrorAll);
        etRechercheOwnerAll = findViewById(R.id.etRechercheOwnerAll);
        etRechercheRepoAll = findViewById(R.id.etRechercheRepoAll);
        progressBarAll = findViewById(R.id.progressBarAll);
        // recycler view
        rvAll = findViewById(R.id.rvAll);
        rvAll.setLayoutManager(new GridLayoutManager(this, 1));
        rvAll.setAdapter(gitAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,25,0,"Voir le dernier tag");
        menu.add(0,26,0,"Acceuil");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 25:
                Intent lastTag = new Intent(this,TagActivity.class);
                startActivity(lastTag);
                break;
            case 26:
                Intent mainTag= new Intent(this,MainActivity.class);
                startActivity(mainTag);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBTRechercheClickAll(View view) {
        setError("");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            afficherTag();
            showProgressBar(false);
            SystemClock.sleep(1500);
            int i = gitBeanList.toArray().length;
            System.out.println(tag);
            GitBean gitBeandata= new GitBean("Tag : "+ data.size(),data.size());
            gitBeandata.setRef(tag);
            data.add(gitBeandata);
            gitAdapter.notifyDataSetChanged();
            i--;
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
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
                    owner = etRechercheOwnerAll.getText().toString();
                    repo = etRechercheRepoAll.getText().toString();
                    repo = AllTagActivity.this.cleanChain(repo);
                    etRechercheRepoAll.setText(repo);
                    // cherche la donnée
                    gitBeanList = WSUtils.loadWeather(owner, repo);
                    AllTagActivity.this.refreshGitBean(gitBeanList);
                    Toast.makeText(AllTagActivity.this, texte[0], Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                    AllTagActivity.this.setError("Erreur : " + e.getMessage());
                }
                AllTagActivity.this.showProgressBar(false);
            }
        }).start();
    }

    private void showProgressBar(boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    progressBarAll.setVisibility(View.VISIBLE);
                } else {
                    progressBarAll.setVisibility(View.GONE);
                }
            }
        });
    }

    private ArrayList<GitBean> refreshGitBean(ArrayList<GitBean> gitBeanList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gitBeanList.get(0) != null && !gitBeanList.isEmpty()) {
                    int i = gitBeanList.toArray().length;
                    i--;
                    GitBean gitbean = gitBeanList.get(i);
                    tag = gitbean.getRef();
                    ObjetGitBean objgitbean = gitbean.getObjetGit();
                    String url = objgitbean.getUrl();
                } else {
                    String error = "\"Objet vide\"";
                    AllTagActivity.this.setError(error);
                }
            }
        });
        return gitBeanList;
    }

    private void setError(String errorMessage) {
        runOnUiThread(()->{
            if(errorMessage.isEmpty()){
                tvErrorAll.setVisibility(View.GONE);
            }else{
                tvErrorAll.setVisibility(View.VISIBLE);
                tvErrorAll.setText(errorMessage);
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
}