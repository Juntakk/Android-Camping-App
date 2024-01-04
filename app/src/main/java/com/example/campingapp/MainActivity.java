package com.example.campingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity {

    public Button btnRegister;
    public EditText editTxtName, editTxtEmail;
    public String name, email;
    public DGW gw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        editTxtName = findViewById(R.id.editTxtName);
        editTxtEmail = findViewById(R.id.editTxtEmail);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTxtName.getText().toString();
                email = editTxtEmail.getText().toString();

                if(name.equals("") || email.equals("")){
                    Toast.makeText(MainActivity.this, "Missing information",Toast.LENGTH_SHORT).show();
                }
                else{
                    gw = new DGW(MainActivity.this, "Reservations",null,1);
                    gw.openDB();
                    gw.addCustomer(name, email);
                    gw.closeDB();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Welcome " + name + " !");
                    builder.setMessage("To make a reservation for one of our activities, feel free to use the menu at the top of the page");

                    builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    editTxtName.setText("");
                    editTxtEmail.setText("");
                    builder.create();
                    builder.show();
                }
            }
        });
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem m1 = menu.add(Menu.NONE, Menu.FIRST,0,"Camping");
        MenuItem m2 = menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Riding");
        MenuItem m3 = menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Kayaking");
        MenuItem m4 = menu.add(Menu.NONE, Menu.FIRST + 3, 3, "Climbing");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i;
        switch (item.getItemId()) {
            case Menu.FIRST:
                i = new Intent(this, Camping.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 1:
                i = new Intent(this, Riding.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 2:
                i = new Intent(this, Kayaking.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 3:
                i = new Intent(this, Climbing.class);
                startActivity(i);
                return true;
        }
        return true;
    }
}