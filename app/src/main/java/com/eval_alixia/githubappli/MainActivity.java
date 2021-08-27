package com.eval_alixia.githubappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,25,0,"Voir le dernier tag");
        menu.add(0,26,0,"Voir tous les tags");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 25:
                Toast.makeText(this,"c'est parti",Toast.LENGTH_LONG).show();
                Intent lastTag = new Intent(this,TagActivity.class);
                startActivity(lastTag);
                break;
            case 26:
                Intent allTag= new Intent(this,AllTagActivity.class);
                startActivity(allTag);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}