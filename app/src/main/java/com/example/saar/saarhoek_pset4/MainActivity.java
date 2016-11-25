package com.example.saar.saarhoek_pset4;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    private SimpleCursorAdapter adapter;
    private ListView listview;
    private Cursor cursor;

    final String[] from = new String[] {DatabaseHelper.CHECK, DatabaseHelper.TASK};
    final int[] to = new int[] {R.id.checkbox, R.id.task};

    int[] imageIDs = {
            android.R.drawable.checkbox_off_background,
            android.R.drawable.checkbox_on_background,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate database helper
        helper = new DatabaseHelper(this);

        // get tasks in cursor
        cursor = helper.fetch();
        // create list
        maker();
    }

    public void add(View view){
        TextView field = (TextView)findViewById(R.id.field);
        String task = field.getText().toString();

        if(task.isEmpty()){
            Toast.makeText(this, "What do you need to do?", Toast.LENGTH_SHORT).show();
            return;
        }

        else{
            helper.create(imageIDs[0], task);
            field.setText("");
            adapter.notifyDataSetChanged();
        }
    }

    public void maker(){
        listview = (ListView)findViewById(R.id.listview);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);
        cursor = helper.fetch();

        // assign adapter to ListView
        listview.setAdapter(adapter);

        // if a task is held down, remove from list and database
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                // delete selected item and update cursos and listview
                helper.delete(l);
                cursor.requery();
                adapter.notifyDataSetChanged();

                Toast toast = Toast.makeText(MainActivity.this, "Item deleted!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });


        // on click makes the task checked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get the info of the task clicked
                Cursor ptr = helper.location(l);
                if (ptr.moveToFirst()) {

                    // getting the right data
                    int img_id = ptr.getInt(1);
                    String task = ptr.getString(2);

                    // image of the checkbox
                    if (img_id == imageIDs[0]) {
                        img_id = imageIDs[1];
                    }
                    else {
                        img_id = imageIDs[0];
                    }

                    // update
                    helper.update(l, img_id, task);
                }
                ptr.close();

                // update cursor and listview
                cursor.requery();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}
