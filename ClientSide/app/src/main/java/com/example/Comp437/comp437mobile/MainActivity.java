package com.example.kwoksinman.comp437mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kwoksinman.comp437mobile.login.LoginActivity;
import com.example.kwoksinman.comp437mobile.login.UserLocalStore;
import com.example.kwoksinman.comp437mobile.note.Note;
import com.example.kwoksinman.comp437mobile.note.NoteDAO;
import com.example.kwoksinman.comp437mobile.note.addActivity;
import com.example.kwoksinman.comp437mobile.note.editActivity;
import com.example.kwoksinman.comp437mobile.note.noteAsync;
import com.example.kwoksinman.comp437mobile.note.noteListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass((Activity) view.getContext(), addActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView noRecord = (TextView) findViewById(R.id.noRecord);
        noRecord.setVisibility(View.GONE);

        // note list
        loadNoteList();
        ListView noteListView = (ListView) findViewById(R.id.noteListView);
        ColorDrawable devidrColor = new ColorDrawable(this.getResources().getColor(android.R.color.transparent));
        noteListView.setDivider(devidrColor);
        noteListView.setDividerHeight(5);

        noteListView.setOnItemClickListener(new noteListOnItemClickListener(this));

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                ListView noteListView = (ListView) ((Activity) view.getContext()).findViewById(R.id.noteListView);
                DialogFragment newFragment = new noteDeleteDialogFragment((Note) noteListView.getItemAtPosition(index));
                newFragment.show(getFragmentManager(), "DeleteDialog");
                return true;
            }
        });

        userLocalStore = new UserLocalStore(this);
        //String userId = userLocalStore.getLoggedInUser().getUserId();
        //System.out.println("Candy getUserId="+userId);
        //System.out.println("Candy getUserName="+userLocalStore.getLoggedInUser().getUserName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            noteAsync connect = new noteAsync(this);
            connect.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authentication() == true){

        }else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

    }


    private boolean authentication(){

        return userLocalStore.getUserLoggedIn();
    }

    private class noteListOnItemClickListener implements  ListView.OnItemClickListener {
        Activity activity;
        public noteListOnItemClickListener(Activity activity){ this.activity=activity; }

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            ListView noteList = (ListView) (activity.findViewById(R.id.noteListView));
            Note note = (Note)noteList.getItemAtPosition(position);

            Intent intent = new Intent();
            intent.setClass(activity, editActivity.class);
            intent.putExtra("id", Long.toString(note.getNoteId()));
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("userId", note.getUserId());
            intent.putExtra("isDeleted", note.getIsDeleted());
            intent.putExtra("serverNoteId", Long.toString(note.getServerNoteId()));
            intent.putExtra("lastUpdate",  Long.toString(note.getLastUpdate()));
            activity.startActivity(intent);
            activity.finish();
        }
    }


    private class noteDeleteDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        private Note note;
        public noteDeleteDialogFragment (Note note){
            this.note = note;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            TextView messageTextView =  new TextView(getActivity());
            messageTextView.setText("Are you sure you want to send the note to the trash can？");
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            messageTextView.setPadding(0,30,0,30);

            return new AlertDialog.Builder(getActivity())
                    .setTitle(note.getTitle())
                    .setView(messageTextView)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    NoteDAO noteDAO = new NoteDAO(getApplicationContext());
                                    note.setIsDeleted("true");
                                    note.setLastUpdate(System.currentTimeMillis());
                                    noteDAO.update(note);
                                    noteAsync connect = new noteAsync(getActivity());
                                    connect.execute();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                    .create();
        }
        @Override
        public void onClick(DialogInterface dialog, int whichButton){}
    }


    private void loadNoteList(){
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        List<Note> noteList = noteDAO.getAll();
        List<Note> mylist = new ArrayList<>();
        boolean noAnyRecord= true;
        for(int i=0; i<noteList.size(); i++){
            //System.out.println("Candy noteList="+noteList.get(i).toString());
            if (noteList.get(i).getIsDeleted().equals("false")) {
                mylist.add(noteList.get(i));
                noAnyRecord = false;
            }
        }
        TextView noRecord = (TextView) findViewById(R.id.noRecord);
        noRecord.setVisibility(View.GONE);
        if(noAnyRecord){
            noRecord.setVisibility(View.VISIBLE);
        }
        ListView noteListView = (ListView) findViewById(R.id.noteListView);
        noteListAdapter noteAdapter = new noteListAdapter(this,mylist);
        noteListView.setAdapter(noteAdapter);
    }
}
