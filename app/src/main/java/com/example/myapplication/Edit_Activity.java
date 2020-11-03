package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Edit_Activity extends AppCompatActivity {

    DBservice myDb;
    private Button btnCancel;
    private Button btnSave;
    private EditText titleEditText;
    private EditText contentEditText;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        init();
        if(timeTextView.getText().length()==0)
            timeTextView.setText(getTime());
    }

    private void init() {

        myDb = new DBservice(this);
        SQLiteDatabase db = myDb.getReadableDatabase();
        titleEditText = findViewById(R.id.et_title);
        contentEditText = findViewById(R.id.et_content);
        timeTextView = findViewById(R.id.edit_time);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        //按钮点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Edit_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = myDb.getWritableDatabase();
                ContentValues values = new ContentValues();

                String title= titleEditText.getText().toString();
                String content=contentEditText.getText().toString();
                String time= timeTextView.getText().toString();

                if("".equals(titleEditText.getText().toString())){
                    Toast.makeText(Edit_Activity.this,"标题不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(contentEditText.getText().toString())) {
                    Toast.makeText(Edit_Activity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                values.put(DBservice.TITLE,title);
                values.put(DBservice.CONTENT,content);
                values.put(DBservice.TIME,time);
                db.insert(DBservice.TABLE,null,values);
                Toast.makeText(Edit_Activity.this,"保存成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Edit_Activity.this,MainActivity.class);
                startActivity(intent);
                db.close();
            }
        });
    }

    //获取当前时间
    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = sdf.format(date);
        return str;
    }

}