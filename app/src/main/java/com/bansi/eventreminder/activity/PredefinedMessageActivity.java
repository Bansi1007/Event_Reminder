package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bansi.eventreminder.R;
import com.bansi.eventreminder.adapter.ListMsgAdapter;
import com.bansi.eventreminder.helper.Constant;
import com.bansi.eventreminder.helper.Prefmanager;
import com.bansi.eventreminder.helper.Prefs;
import com.bansi.eventreminder.helper.id_interface;
import com.bansi.eventreminder.model.CategoryModel;
import com.bansi.eventreminder.model.MessageModel;
import com.bansi.eventreminder.sqlite.DbHandler;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PredefinedMessageActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBAck;

    @BindView(R.id.tvHeader)
    TextView mtvHeader;
    @BindView(R.id.tvcategory)
    TextView mtvcategory;

    @BindView(R.id.rvMsgs)
    ListView mrvMsgs;

 /*   @BindView(R.id.spCategory)
    Spinner mspCategory;*/

    DbHandler handler;
    String label,category;
    String selectedFromList;
    Prefmanager prefmanager;
    ArrayList<CategoryModel> list = new ArrayList<>();
    ArrayList<MessageModel> msg = new ArrayList<>();
   // ArrayList<ListMsgs>listMsgs=new ArrayList<>();
    ListMsgAdapter listMsgAdapter;
    int id=0;

    int selectedItemPosition;
   // id_interface casAuthentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_message);

        ButterKnife.bind(this);



        category=getIntent().getStringExtra("category");
       mtvcategory.setText(category);
        /*   prefmanager = new Prefmanager(this);
        if (!prefmanager.isFirstTimeLaunch()) {

            handler.insertMsg("",0);

            finish();
        }*/
        mrvMsgs.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                String selectedFromList =(String) (mrvMsgs.getItemAtPosition(position).toString());
                Log.d("Selected: ",  selectedFromList );


                 selectedFromList =(String) (mrvMsgs.getItemAtPosition(position).toString());

                Log.d("Selected item position :- ",selectedFromList);

                Intent i=new Intent(PredefinedMessageActivity.this,SendMessageActivity.class);
                i.putExtra("msg", selectedFromList);
                startActivity(i);
            }

        });
        id= Integer.parseInt(getIntent().getStringExtra("id"));
        //msg.clear();
        handler=new DbHandler(getApplicationContext());
        handler.insertMsg(label,id);
        mivBAck.setVisibility(View.VISIBLE);
        mtvHeader.setVisibility(View.VISIBLE);
        mtvHeader.setText("Predefined Messages");
        //SpinnerData();
       // String msg_id= Prefs.getString(Constant.ID,"");
       // id= handler.Categoryid(msg_id);

        SpinnerDatamsg();
       // msg.addAll(handler.preDefinedMsgList(id));

      /*  LinearLayoutManager ln=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        mrvMsgs.setLayoutManager(ln);
        listMsgAdapter=new ListMsgAdapter(msg,this);
        mrvMsgs.setAdapter(listMsgAdapter);*/
        Log.w("MsgList",":"+msg.size());

    }
  /*  @OnClick(R.id.btnAddEvent)
    public void share(){

    }*/
    @OnClick(R.id.ivBack)
    public void onclick(){
        onBackPressed();
        msg.clear();
    }
  /*  @OnItemSelected(R.id.spCategory)
    public void onIclickSpinner(){
       // list.clear();
        msg.clear();
        String msg_id=  mspCategory.getSelectedItem().toString();

        SpinnerDatamsg();
    }*/

  /*  @OnItemSelected(R.id.spMessage)
    public void sp(){
        category=  mspMessage.getSelectedItem().toString();

    }*/
   /* private void SpinnerData() {
        list.clear();
        list.addAll(handler.getAllLabels());
        Log.w("SpinnerData",":"+list.size());
        ArrayAdapter<CategoryModel> dataAdapter = new ArrayAdapter<CategoryModel>(this,
                android.R.layout.simple_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

      //  mspCategory.setAdapter(dataAdapter);
    }*/


    private void SpinnerDatamsg() {
        msg.clear();
        handler=new DbHandler(getApplicationContext());
        msg.addAll(handler.preDefinedMsgList(Integer.parseInt(Prefs.getPreference(this, Constant.ID))));

        ArrayAdapter<MessageModel> dataAdapter2 = new ArrayAdapter<MessageModel>(this,
                android.R.layout.simple_spinner_dropdown_item, msg);
        dataAdapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        mrvMsgs.setAdapter(dataAdapter2);

        //  msg.setAdapter(dataAdapter2);
    }

}