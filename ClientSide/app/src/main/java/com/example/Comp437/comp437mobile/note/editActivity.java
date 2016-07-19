package com.example.kwoksinman.comp437mobile.note;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kwoksinman.comp437mobile.MainActivity;
import com.example.kwoksinman.comp437mobile.R;
import com.example.kwoksinman.comp437mobile.SystemConstant;
import com.example.kwoksinman.comp437mobile.asyncTask.networkTool;
import com.example.kwoksinman.comp437mobile.login.UserLocalStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by KwokSinMan on 24/3/2016.
 */
public class editActivity extends AppCompatActivity {
    ListView noteListView;
    List<String> myList = new ArrayList<>();
    UserLocalStore userLocalStore;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteListView = (ListView) findViewById(R.id.addNoteListView);
        noteListView.setOnItemClickListener(new editNoteOnItemClickListener(this));
        userLocalStore = new UserLocalStore(this);

        Intent intent = getIntent();
        note = new Note(Long.parseLong(intent.getExtras().getString("id")),intent.getExtras().getString("title"),intent.getExtras().getString("content"),intent.getExtras().getString("userId"),intent.getExtras().getString("isDeleted"),Long.parseLong(intent.getExtras().getString("serverNoteId")),Long.parseLong(intent.getExtras().getString("lastUpdate")));
        //System.out.println("Candy note.toString()=" + note.toString());

        EditText titleEditText = (EditText) findViewById(R.id.title);
        titleEditText.setText(note.getTitle());

        if (!note.getContent().isEmpty()){
            myList = new ArrayList<String>(Arrays.asList(note.getContent().split(",")));
            addItemListAdapter noteAdapter = new addItemListAdapter(this, myList);
            noteListView.setAdapter(noteAdapter);
        }

        TextView sysDate = (TextView)findViewById(R.id.sysDate);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
        sysDate.setText(sdf.format(calendar.getTime()));

        LinearLayout addItemArea = (LinearLayout) findViewById(R.id.addItemArea);
        addItemArea.setBackgroundResource(R.drawable.border_additem_ui);
        addItemArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new addItemDialogFragment();
                newFragment.show(getFragmentManager(), "addItemDialog");
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class editNoteOnItemClickListener implements  ListView.OnItemClickListener {
        Activity activity;
        public editNoteOnItemClickListener(Activity activity){ this.activity=activity; }

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            DialogFragment newFragment = new editItemDialogFragment(myList.indexOf(adapter.getItemAtPosition(position)));
            newFragment.show(getFragmentManager(), "editItemDialog");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  android.R.id.home){
            handleBackAction();
        }
        if (item.getItemId() ==  R.id.note_menuItem_add){
            NoteDAO noteDAO = new NoteDAO(getApplicationContext());

            EditText titleEditText = (EditText) findViewById(R.id.title);
            String title = titleEditText.getText().toString();
            if (title.isEmpty()){
                TextView sysDataTextView = (TextView) findViewById(R.id.sysDate);
                title = sysDataTextView.getText().toString();
            }

            String content = "";
            for (int i=0; i<myList.size(); i++){
                content += myList.get(i).toString() + ",";
            }
            if (!content.isEmpty()){
                content = content.substring(0, content.length() - 1);
            }
            note.setTitle(title);
            note.setContent(content);
            note.setLastUpdate(System.currentTimeMillis());
            noteDAO.update(note);
            //Candy "Has Run"
            noteAsync connect = new noteAsync(this);
            connect.execute();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        handleBackAction();
    }

    private void handleBackAction() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class editItemDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

        EditText input;
        int position;
        public editItemDialogFragment(int position){
            this.position = position;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater factory=LayoutInflater.from(getActivity());
            View v1 = factory.inflate(R.layout.add_dialog, null);
            input = (EditText) v1.findViewById(R.id.itemName);
            input.setText(myList.get(position));
            ImageButton btnSpeak = (ImageButton) v1.findViewById(R.id.speakButton);
            btnSpeak.setOnClickListener(new speakToTextOnClickListener(this.getActivity()));
            btnSpeak.setEnabled(networkTool.isConnected(getActivity()));
            return new android.app.AlertDialog.Builder(getActivity())
                    .setTitle(R.string.addItem)
                    .setView(v1)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String itemName = input.getText().toString();
                                    if (itemName.isEmpty()) {
                                        itemName = "(empty)";
                                    }
                                    myList.set(position,itemName);
                                    addItemListAdapter noteAdapter = new addItemListAdapter(getActivity(), myList);
                                    noteListView.setAdapter(noteAdapter);
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

        private class speakToTextOnClickListener implements View.OnClickListener{
            Activity activity;
            public speakToTextOnClickListener(Activity activity){ this.activity=activity; }
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "zh-TW");
                try {
                    // start 2 way interface ( A pass data to B, B return data to A)
                    startActivityForResult(intent, SystemConstant.GET_SPEECH_RESULT3_CODE);
                } catch (ActivityNotFoundException aException) {
                    Toast t = Toast.makeText(activity,"你的手機不支援語音辦別", Toast.LENGTH_LONG);
                    t.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // get the result from the google speech to text (Call Back)
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case SystemConstant.GET_SPEECH_RESULT3_CODE: {
                    if (resultCode == SystemConstant.GET_SPEECH_RESULT_OK && null != data) {
                        ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        input.setText(text.get(0));
                    }
                    break;
                }

            }
        }
    }


    private class addItemDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        EditText input;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater factory=LayoutInflater.from(getActivity());
            View v1 = factory.inflate(R.layout.add_dialog, null);
            input = (EditText) v1.findViewById(R.id.itemName);
            ImageButton btnSpeak = (ImageButton) v1.findViewById(R.id.speakButton);
            btnSpeak.setOnClickListener(new speakToTextOnClickListener(this.getActivity()));
            btnSpeak.setEnabled(networkTool.isConnected(getActivity()));
            return new android.app.AlertDialog.Builder(getActivity())
                    .setTitle(R.string.addItem)
                    .setView(v1)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String itemName = input.getText().toString();
                                    if(itemName.isEmpty()){
                                        itemName = "(empty)";
                                    }
                                    myList.add(itemName);
                                    addItemListAdapter noteAdapter = new addItemListAdapter(getActivity(), myList);
                                    noteListView.setAdapter(noteAdapter);
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

        private class speakToTextOnClickListener implements View.OnClickListener{
            Activity activity;
            public speakToTextOnClickListener(Activity activity){ this.activity=activity; }
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "zh-TW");
                try {
                    // start 2 way interface ( A pass data to B, B return data to A)
                    startActivityForResult(intent, SystemConstant.GET_SPEECH_RESULT1_CODE);
                } catch (ActivityNotFoundException aException) {
                    Toast t = Toast.makeText(activity,"你的手機不支援語音辦別", Toast.LENGTH_LONG);
                    t.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // get the result from the google speech to text (Call Back)
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case SystemConstant.GET_SPEECH_RESULT1_CODE: {
                    if (resultCode == SystemConstant.GET_SPEECH_RESULT_OK && null != data) {
                        ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        input.setText(text.get(0));
                    }
                    break;
                }

            }
        }
    }

    private class addItemListAdapter extends ArrayAdapter<String> {
        private Activity activity;
        private List<String> item;

        public addItemListAdapter(Activity activity, List<String> item) {
            super(activity, R.layout.add_note_list, item);
            this.activity = activity;
            this.item = item;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.add_note_list, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            txtTitle.setText(item.get(position).toString());

            ImageView imageView = (ImageView) rowView.findViewById(R.id.deleteButton);
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myList.remove((int) view.getTag());
                    addItemListAdapter noteAdapter = new addItemListAdapter(activity, myList);
                    noteListView.setAdapter(noteAdapter);
                }
            });

            return rowView;
        }


    }



}
