package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bansi.eventreminder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationView extends AppCompatActivity {
    @BindView(R.id.textView2)
    TextView mtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        ButterKnife.bind(this);
    }
}