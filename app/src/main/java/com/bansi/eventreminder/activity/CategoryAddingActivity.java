package com.bansi.eventreminder.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bansi.eventreminder.sqlite.DbHandler;
import com.bansi.eventreminder.R;
import com.bansi.eventreminder.model.CategoryModel;
import com.bansi.eventreminder.model.MessageModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CategoryAddingActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBack;

    @BindView(R.id.tvHeader)
    TextView mtvHeader;

    @BindView(R.id.etCategory)
    EditText metCategory;

/*    @BindView(R.id.spCategory)
    Spinner mspCategory;*/

    @BindView(R.id.btnAddEvent)
    TextView mbtnAddEvent;

    DbHandler handler;
    boolean is_match=false;
    ArrayList<MessageModel>messageModels=new ArrayList<>();
    ArrayList<CategoryModel>categoryModels=new ArrayList<>();
    int id;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_adding);
        ButterKnife.bind(this);
        mivBack.setVisibility(View.VISIBLE);
        mtvHeader.setVisibility(View.VISIBLE);
        mtvHeader.setText("Add Event Category");

        handler = new DbHandler(getApplicationContext());
        categoryModels.clear();
        handler.insertLabel(label);
      // handler.insertLabel("");
       // SpinnerData();

        mbtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                category();


               // msg();

            }
        });
    }
  /*  @OnItemSelected(R.id.spCategory)
    public void onIclickSpinner(){
        // msg.clear();
        // String msg_id=  mspCategory.getSelectedItem().toString();
        //id= handler.Categoryid(msg_id);
        // Prefs.putString(Constant.ID, String.valueOf(id));
        // SpinnerDatamsg();
        mspCategory.getSelectedItem().toString();
    }*/
   /* private void SpinnerData() {
        categoryModels.clear();
        handler= new DbHandler(getApplicationContext());
         categoryModels.addAll(handler.getAllLabels());
        ArrayAdapter<CategoryModel> dataAdapter = new ArrayAdapter<CategoryModel>(this,
                android.R.layout.simple_spinner_item, categoryModels);

        mspCategory.setAdapter(dataAdapter);

        Log.w("Data",":"+categoryModels);

    }*/
    public void category(){
        label = metCategory.getText().toString();

        if (label.trim().length() > 0) {

            categoryModels.clear();
           // categoryModels.addAll(handler.categoryName());
            for (int i =0;i<categoryModels.size();i++)
            {
                if ((label.equalsIgnoreCase(categoryModels.get(i).getName())))
                {
                    is_match=true;
                }
            }
            if(!is_match)
            {
                categoryModels.clear();
                handler.insertLabel(label);
            } else {
                Toast.makeText(this, "Category already exist ", Toast.LENGTH_SHORT).show();
            }
            Log.w("IDdddddddddddd",":"+id);
           // SpinnerData();
            hideKeyboard();


        } else {
            Toast.makeText(getApplicationContext(), "Please enter Text", Toast.LENGTH_SHORT).show();
        }
    }
    /*public void msg(){
        handler = new DbHandler(getApplicationContext());

        if (label.trim().length() > 0) {

                id= handler.Categoryid(metCategory.getText().toString());
                    handler.insertMsg(id);
                   // SpinnerDatamsg();
                    hideKeyboard();

            }else {
                Toast.makeText(getApplicationContext(), "Please enter Text", Toast.LENGTH_SHORT).show();
        }
    }*/
   /* private void SpinnerDatamsg() {
        handler= new DbHandler(getApplicationContext());
        messageModels.addAll(handler.preDefinedMsgList(id));
        Log.w("SpinnerDatamsg",":"+messageModels.size());
        ArrayAdapter<MessageModel> dataAdapter2 = new ArrayAdapter<MessageModel>(this,
                android.R.layout.simple_spinner_item, messageModels);
    }*/
    public void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
@OnClick(R.id.ivBack)
    public void onback(){
        onBackPressed();
}

}