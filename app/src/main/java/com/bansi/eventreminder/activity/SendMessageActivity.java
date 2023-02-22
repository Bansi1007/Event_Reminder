package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bansi.eventreminder.R;

public class SendMessageActivity extends AppCompatActivity {
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

         message=getIntent().getStringExtra("msg");
        share();
       /* String digits = "\\d+";
        String mob_num = "987654321";
        if (mob_num.matches(digits))
        {
            try {
                //linking for whatsapp
                Uri uri = Uri.parse("whatsapp://send?phone=+91" + mob_num);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
            catch (ActivityNotFoundException e){
                e.printStackTrace();
                //if you're in anonymous class pass context like "YourActivity.this"
                Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    public void share(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"bansivaghasiya10799@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "hey ");
        i.putExtra(Intent.EXTRA_TEXT, message);
        i.setType("*/*");
        startActivity(Intent.createChooser(i,""));
    }
}