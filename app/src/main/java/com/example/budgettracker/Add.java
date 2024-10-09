package com.example.budgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class Add extends AppCompatActivity {

    RadioGroup group_rg;
    EditText title_e;
    EditText amount_e;
    Spinner category_sp;

    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    Button back_b;
    Button add_b;
    SQLiteDatabase db;

    String[] category_s1 = {"luxury","food","house","clothes","auto","other"}; // array of categories for transactions
    String[] category_s2 = {"job","gift","refund","stocks","business","other"}; // array of categories for income

    String checked;
    String selected;
    String title;
    String username;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        back_b = findViewById(R.id.back_2);
        add_b = findViewById(R.id.add_button_alt);
        group_rg = findViewById(R.id.radio_group);
        category_sp = findViewById(R.id.spinner);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,category_s1);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,category_s2);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        db = openOrCreateDatabase("finalProject",MODE_PRIVATE,null);

        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                Intent i = new Intent(v.getContext(),Summary.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    title_e = findViewById(R.id.title);
                    amount_e = findViewById(R.id.amount);
                    title = title_e.getText().toString();
                    amount = Double.parseDouble(amount_e.getText().toString());
                    if (valid(title,amount,selected)) {
                        if (checked.equals("Transaction")) {
                            db.execSQL("insert into transactions(title,amount,category,username) values(" + "'" + title + "'" + "," + "'" + amount + "'" + "," + "'" + selected + "'" + "," + "'" + username + "'" +");");
                            //ShowMessage("PASSED!",username+" "+checked+" "+title+" "+amount+" "+selected);
                        }
                        else if (checked.equals("Income")) {
                            db.execSQL("insert into income(title,amount,category,username) values(" + "'" + title + "'" + "," + "'" + amount + "'" + "," + "'" + selected + "'" + "," + "'" + username + "'" +");");
                            //ShowMessage("PASSED!",username+" "+checked+" "+title+" "+amount+" "+selected);
                        }
                    }
                    else {
                        ShowMessage("ERROR:","Make sure you enter a title, an amount greater than 0, and select a category!");
                    }
                }
                catch (Exception e) {
                    ShowMessage("ERROR:","Enter a valid number!");
                    amount_e.setText("");
                }
            }
        });

        Button init_rb = findViewById(group_rg.getCheckedRadioButtonId());
        if (group_rg.getCheckedRadioButtonId() == R.id.transaction_radio) {
            category_sp.setAdapter(adapter1);
            checked = init_rb.getText().toString();
        }
        else if (group_rg.getCheckedRadioButtonId() == R.id.income_radio) {
            category_sp.setAdapter(adapter2);
            checked = init_rb.getText().toString();
        }

        group_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_rb = findViewById(checkedId);
                if (checkedId == R.id.transaction_radio) {
                    category_sp.setAdapter(adapter1);
                    checked = checked_rb.getText().toString();
                }
                else if (checkedId == R.id.income_radio) {
                    category_sp.setAdapter(adapter2);
                    checked = checked_rb.getText().toString();
                }
            }
        });


        category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RadioGroup group_rg = findViewById(R.id.radio_group);
                int rb_id = group_rg.getCheckedRadioButtonId();
                if (rb_id==R.id.transaction_radio) {
                    selected = category_s1[position];
                }
                else if (rb_id==R.id.income_radio) {
                    selected = category_s2[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void ShowMessage(String title, String message) {
        AlertDialog.Builder messBr = new AlertDialog.Builder(this);
        messBr.setCancelable(true);
        messBr.setTitle(title);
        messBr.setMessage(message);
        messBr.show();
    }

    public boolean valid(String ttle,double amnt,String sltd) {
        return (ttle.length() != 0) && (amnt > 0.0) && (sltd != null);
    }

    public void clear() {
        title_e = findViewById(R.id.title);
        amount_e = findViewById(R.id.amount);
        title_e.setText("");
        amount_e.setText("");
        checked = null;
        selected = null;
    }
}