package com.example.mycontactapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName;
    EditText editBirthday;
    EditText editFavColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editBirthday = findViewById(R.id.editText_birthday);
        editFavColor = findViewById(R.id.editText_FavColor);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");
        ((Button)findViewById(R.id.seeTable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewData(v);
            }
        });
    }

    public void addData (View view) {
        boolean isInserted = myDb.insertData(editName.getText().toString(),editBirthday.getText().toString(),
                editFavColor.getText().toString());

        if(isInserted == true) {
            Toast.makeText(MainActivity.this,"Success - contact inserted",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted or already exists", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view) {
        Cursor res = myDb.getAllData();

        if(res.getCount() == 0) {
            showMessage("Error", "No contact data found in the db");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            //append res column 0,.... to the buffer - see StringBuffer and Cursor api's for reference
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Birthday: " + res.getString(2) + "\n");
            buffer.append("Fav Color: " + res.getString(3) + "\n");
        }

        showMessage("Data", buffer.toString());
    }

    public void findData (View view) {
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            showMessage("Error", "No contact data found in the db");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()) {
            if(editName.getText().toString().equals(res.getString(1)) ||
            editBirthday.getText().toString().equals(res.getString(2)) ||
            editFavColor.getText().toString().equals(res.getString(3)))
            {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Name: " + res.getString(1) + "\n");
                buffer.append("Birthday: " + res.getString(2) + "\n");
                buffer.append("Fav Color: " + res.getString(3) + "\n");
            }
        }
        showMessage("Matching Data",buffer.toString());
    }

    public void clearData(View view){
        myDb.clearData();
    }

    public void showMessage (String title , String message) {
        //put Log.d's in here
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
