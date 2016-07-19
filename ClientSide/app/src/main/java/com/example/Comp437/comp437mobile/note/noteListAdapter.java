package com.example.kwoksinman.comp437mobile.note;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kwoksinman.comp437mobile.R;

import java.util.List;

/**
 * Created by KwokSinMan on 24/3/2016.
 */
public class noteListAdapter extends ArrayAdapter<Note> {
    private Activity activity;
    private List<Note> item;

    public noteListAdapter(Activity activity, List<Note> item) {
        super(activity, R.layout.note_list, item);
        this.activity = activity;
        this.item = item;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.note_list, null, true);

        TextView noteTitle = (TextView) rowView.findViewById(R.id.noteTitle);
        noteTitle.setText(item.get(position).getTitle());

        return rowView;
    }
}
