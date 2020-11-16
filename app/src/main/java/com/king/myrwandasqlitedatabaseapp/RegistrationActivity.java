package com.king.myrwandasqlitedatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {
    Button mBtnSave,mBtnView,mBtnDelete;
    EditText mEdtName, mEdtMail, mEdtPhone;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mBtnSave = findViewById(R.id.btn_save);
        mBtnView = findViewById(R.id.btn_view);
        mBtnDelete = findViewById(R.id.btn_delete);
        mEdtName = findViewById(R.id.edt_name);
        mEdtMail = findViewById(R.id.edt_mail);
        mEdtPhone = findViewById(R.id.edt_phone);

        //Create the database
        db = openOrCreateDatabase("rwandan_class",MODE_PRIVATE,null);

        //Create a table
        db.execSQL("CREATE TABLE IF NOT EXISTS students(jina VARCHAR, arafa VARCHAR, simu VARCHAR)");

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start receiving data from the user
                String name, email,phone;
                name = mEdtName.getText().toString().trim();
                email = mEdtMail.getText().toString().trim();
                phone = mEdtPhone.getText().toString().trim();

                if (name.isEmpty()){
                    mEdtName.setError("Please enter name");
                }else if (email.isEmpty()){
                    mEdtMail.setError("Please enter eamil");
                }else if (phone.isEmpty()){
                    mEdtPhone.setError("Please enter phone");
                }else if (phone.length()<10){
                    mEdtPhone.setError("Please enter a number with 10 or more characters");
                }else {
                    //Proceed to save the data
                    db.execSQL("INSERT INTO students VALUES('"+name+"','"+email+"','"+phone+"')");
                    message("SUCCESS!!!","Record saved successfully");

                }
            }
        });

        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start fetching data from the db using Cursor
                Cursor cursor = db.rawQuery("SELECT * FROM students",null);
                if (cursor.getCount()==0){
                    message("EMPTY DATABASE","Sorry, we found no records in the db");
                }else {
                    //Write a loop to display the records one by one
                    //Use StringBuffer to append the records
                    StringBuffer buffer = new StringBuffer();
                    while (cursor.moveToNext()){
                        buffer.append(cursor.getString(0));
                        buffer.append("\n");
                        buffer.append(cursor.getString(1));
                        buffer.append("\n");
                        buffer.append(cursor.getString(2));
                        buffer.append("\n");
                        buffer.append("\n");
                    }
                    message("CURRENT RECORDS",buffer.toString());
                }
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start by receiving a phone from the user
                String phone;
                phone = mEdtPhone.getText().toString().trim();
                if (phone.isEmpty()){
                    mEdtPhone.setError("Please enter phone number");
                }else {
                    Cursor cursor = db.rawQuery("SELECT * FROM students WHERE simu='"+phone+"'",null);
                    if (cursor.getCount()==0){
                        message("EMPTY DATABASE!!!","Sorry, we did't find any student with that number.");
                    }else {
                        db.execSQL("DELETE FROM students WHERE simu='"+phone+"'");
                        message("SUCCESS!!!","Record deleted successfully.");
                    }
                }
            }
        });
    }

    public  void  message(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEdtName.setText("");
                mEdtMail.setText("");
                mEdtPhone.setText("");
            }
        });
        builder.create().show();
    }
}
