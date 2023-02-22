package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bansi.eventreminder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView mtvName;

    @BindView(R.id.tvHeader)
    TextView mtvHeader;

    @BindView(R.id.ivBack)
    ImageView mivBAck;

    @BindView(R.id.tvDate)
    TextView mtvDate;

    @BindView(R.id.tvDescription)
    TextView mtvDes;

    @BindView(R.id.tvTime)
    TextView mtvTime;


    @BindView(R.id.tvDay)
    TextView mtvDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        mivBAck.setVisibility(View.VISIBLE);
        mtvHeader.setVisibility(View.VISIBLE);
        mtvHeader.setText("Event Detail");
        String event_name=getIntent().getStringExtra("name");
        String e_des=getIntent().getStringExtra("des");
        String e_time=getIntent().getStringExtra("time");
        String e_date=getIntent().getStringExtra("date");
        String e_day=getIntent().getStringExtra("day");

        mtvName.setText(event_name);
        mtvDes.setText(e_des);
        mtvDate.setText(e_date);
        mtvTime.setText(e_time);
        mtvDay.setText(e_day);
    }

    @OnClick(R.id.ivBack)
    public void onclickBack(){
        onBackPressed();
    }
}