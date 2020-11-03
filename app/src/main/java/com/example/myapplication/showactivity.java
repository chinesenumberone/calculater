package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Date;

public class showactivity extends AppCompatActivity {

    private Button btnSave;
    private Button btnCancel;
    private TextView showTime;
    private EditText showContent;
    private EditText showTitle;

    private Values value;
    DBservice myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);

        init();
    }

    public void init() {
        myDb = new DBservice(this);
        btnCancel = findViewById(R.id.show_cancel);
        btnSave = findViewById(R.id.show_save);
        showTime = findViewById(R.id.show_time);
        showTitle = findViewById(R.id.show_title);
        showContent = findViewById(R.id.show_content);

        Intent intent = this.getIntent();
        if (intent != null) {
            value = new Values();

            value.setTime(intent.getStringExtra(DBservice.TIME));
            value.setTitle(intent.getStringExtra(DBservice.TITLE));
            value.setContent(intent.getStringExtra(DBservice.CONTENT));
            value.setId(Integer.valueOf(intent.getStringExtra(DBservice.ID)));

            showTime.setText(value.getTime());
            showTitle.setText(value.getTitle());
            showContent.setText(value.getContent());
        }

        //按钮点击事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();
                String content = showContent.getText().toString();
                String title = showTitle.getText().toString();

                values.put(DBservice.TIME, getTime());
                values.put(DBservice.TITLE,title);
                values.put(DBservice.CONTENT,content);

                db.update(DBservice.TABLE,values,DBservice.ID+"=?",new String[]{value.getId().toString()});
                Toast.makeText(showactivity.this,"修改成功",Toast.LENGTH_LONG).show();
                db.close();
                Intent intent = new Intent(showactivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = showContent.getText().toString();
                final String title = showTitle.getText().toString();
                new AlertDialog.Builder(showactivity.this)
                        .setTitle("提示框")
                        .setMessage("是否保存当前内容?")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLiteDatabase db = myDb.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(DBservice.TIME, getTime());
                                        values.put(DBservice.TITLE,title);
                                        values.put(DBservice.CONTENT,content);
                                        db.update(DBservice.TABLE,values,DBservice.ID+"=?",new String[]{value.getId().toString()});
                                        Toast.makeText(showactivity.this,"修改成功",Toast.LENGTH_LONG).show();
                                        db.close();
                                        Intent intent = new Intent(showactivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(showactivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
            }
        });
    }

    String getTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}