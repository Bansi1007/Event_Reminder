package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bansi.eventreminder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ValidationDialog extends AppCompatActivity {

    public static ValidationDialog instance = null;
    public ValidationDialog()
    {
        instance = this;
    }
    public static synchronized ValidationDialog getInstance() {
        if (instance == null) {
            instance = new ValidationDialog();
        }
        return instance;
    }
    @BindView(R.id.tvValidation)
    TextView mtvValid;

    @BindView(R.id.tvValidation1)
    TextView mtvValidation1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_dialog);
        ButterKnife.bind(this);
    }
}