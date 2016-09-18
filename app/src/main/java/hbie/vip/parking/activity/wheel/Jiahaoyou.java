package hbie.vip.parking.activity.wheel;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import hbie.vip.parking.R;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class Jiahaoyou extends ListActivity {
    private ListAdapter mAdapter;
    public TextView pbContact;
    public static String PBCONTACT;
    public static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_CREATE=0;
    // 当activity第一次创建完成后调用
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Cursor C = getContentResolver().query(Contacts.People.CONTENT_URI, null, null, null, null);
        startManagingCursor(C);

        String[] columns = new String[] {Contacts.People.NAME};
//        int[] names = new int[] {R.id.row_entry};

//        mAdapter = new SimpleCursorAdapter(this, R.layout.mycontacts, C, columns, names);
//        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor C = (Cursor) mAdapter.getItem(position);
        PBCONTACT = C.getString(C.getColumnIndex(Contacts.People.NAME));

//        Intent i = new Intent(this, NoteEdit.class);
//        startActivityForResult(i, ACTIVITY_CREATE);
    }
}
