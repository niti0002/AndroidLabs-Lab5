package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.androidlabs.MyDatabaseOpenHelper.VERSION_NUM;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> objects = new ArrayList<>();

    BaseAdapter myAdapter;

    EditText chat;

    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chat = findViewById(R.id.chatText);


        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_TEXT, MyDatabaseOpenHelper.COL_SENT, MyDatabaseOpenHelper.COL_RECEIVED};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);





        //find the column indices:
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int textColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_TEXT);
        int isSentColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_SENT);
        int isReceivedColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_RECEIVED);


        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {


            long id = results.getLong(idColIndex);
            String text = results.getString(textColIndex);
            String sent = results.getString(isSentColIndex);
            String received = results.getString(isReceivedColIndex);

            if (sent.equals("1")) {
                objects.add(new Message(id, text, true, false));
            } else if (received.equals("1")) {
                objects.add(new Message(id, text, false, true));
            } else {

            }


        }

        //You only need 2 lines in onCreate to actually display data:
        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(myAdapter = new MyListAdapter());

        theList.setOnItemClickListener((lv, vw, pos, id) -> {

            Toast.makeText(ChatRoomActivity.this,
                    "You clicked on:" + pos, Toast.LENGTH_SHORT).show();

        });

        Button addButton = findViewById(R.id.sendButton);
        addButton.setOnClickListener(clik ->
        {

            String text = chat.getText().toString();
            String isSent = "1";
            String isReceived = "0";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_TEXT, text);
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, isSent);
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, isReceived);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);


            objects.add(new Message(newId, text, true, false));
//
//            message.setId(newId);
//            message.setText(chat.getText().toString());
//            message.setSent(true);
//            message.setReceived(false);
//            objects.add(message);


            myAdapter.notifyDataSetChanged(); //update yourself
            chat.getText().clear();
        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(clik ->
        {
            String text = chat.getText().toString();
            String isSent = "0";
            String isReceived = "1";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_TEXT, text);
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, isSent);
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, isReceived);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);


            objects.add(new Message(newId, text, false, true));

//            message.setId(newId);
//            message.setText(chat.getText().toString());
//            message.setReceived(true);
//            message.setSent(false);
//            objects.add(message);
            myAdapter.notifyDataSetChanged(); //update yourself
            chat.getText().clear();
        });

        printCursor(results);
    }



    public void printCursor(Cursor c) {
        //database version number
        int v = MyDatabaseOpenHelper.VERSION_NUM;
        Log.i("Database version number", String.valueOf(v));
        Log.i("Num col in cursor", String.valueOf(c.getColumnCount()));
        Log.i("Columns names in cursor", Arrays.toString(c.getColumnNames()));
        Log.i("Num results in cursor", String.valueOf(c.getCount()));


        if(c.moveToFirst()){
            do{
                String data = c.getString(c.getColumnIndex("TEXT"));
                Log.i("Row results", data);
            }while (c.moveToNext());
        }

    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return objects.size();
        } //This function tells how many objects to show

        public Message getItem(int position) {
            return objects.get(position);
        }  //This returns the string at position p

        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p

        public View getView(int p, View thisRow, ViewGroup parent) {
            //View thisRow = recycled;

            Message msg = getItem(p);


            if (msg.getSent()) {
                thisRow = getLayoutInflater().inflate(R.layout.table_row_send_layout, null);

                TextView itemText = thisRow.findViewById(R.id.sendTextField);
                itemText.setText(msg.getText());
            } else if (msg.getReceived()) {
                thisRow = getLayoutInflater().inflate(R.layout.table_row_recieve_layout, null);

                TextView itemText = thisRow.findViewById(R.id.receiveTextField);
                itemText.setText(msg.getText());

            } else {

            }


            return thisRow;

        }


    }
}
