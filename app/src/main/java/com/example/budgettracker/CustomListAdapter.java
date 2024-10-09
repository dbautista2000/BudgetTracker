package com.example.budgettracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> titles;
    private final ArrayList<String> amounts;
    //private final String[] name;
    //private final String[] phone;
    public CustomListAdapter(@NonNull Activity context, ArrayList<String> titles, ArrayList<String> amounts) {
        super(context, R.layout.mylist,titles);
        this.context=context;
        this.titles=titles;
        this.amounts=amounts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView text_title = rowView.findViewById(R.id.title_item);
        TextView text_amount = rowView.findViewById(R.id.amount_item);

        text_title.setText(titles.get(position));
        text_amount.setText(amounts.get(position));

        return rowView;
    }

}
