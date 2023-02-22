package com.bansi.eventreminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bansi.eventreminder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForeGroundDialogActivity extends AppCompatActivity {

    public static ForeGroundDialogActivity instance = null;
    public ForeGroundDialogActivity()
    {
        instance = this;
    }
    public static synchronized ForeGroundDialogActivity getInstance() {
        if (instance == null) {
            instance = new ForeGroundDialogActivity();
        }
        return instance;
    }

    public Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public void showDialog(String des,String name,String time,String date,String day) {

        ForeGroundDialogActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog=new Dialog(ForeGroundDialogActivity.this,R.style.CustomDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window=dialog.getWindow();

                if(window != null){
                    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    window.setDimAmount(0.7f);
                }
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setContentView(R.layout.activity_fore_ground_dialog);

                TextView tvValidation = dialog.findViewById(R.id.tvValidation);
                TextView btnYes = dialog.findViewById(R.id.btnYes);
                TextView btnNo = dialog.findViewById(R.id.btnNo);
                ImageView ivclock=dialog.findViewById(R.id.ivClock);
                TextView mtvEName= dialog.findViewById(R.id.tvEName);
                window.setGravity(Gravity.CENTER);

                tvValidation.setText(des);
                mtvEName.setText(name);

               btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.dismiss();
                    }
               });
            btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i =new Intent(ForeGroundDialogActivity.this,EventDetailActivity.class)
                                .putExtra("name",name)
                                .putExtra("des",des)
                                .putExtra("time",time)
                                .putExtra("date",date)
                                .putExtra("day",day);

                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            }
        });
    }
}