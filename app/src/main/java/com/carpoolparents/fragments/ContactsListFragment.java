package com.carpoolparents.fragments;

/**
 * Created by zoya on 9/28/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.carpoolparents.R;

public class ContactsListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;

    // Defines an array that contains column names to move from the Cursor to the ListView.
    private static final String[] FROM = { Contacts.DISPLAY_NAME_PRIMARY };
    private static final int[] TO = { android.R.id.text1 };
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    // columns requested from the database
    private static final String[] PROJECTION = {
            Contacts._ID, // _ID is always required
            Contacts.DISPLAY_NAME_PRIMARY // that's what we want to display
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createAdapter() {
        Log.d("carpoolparents", "createAdaptor");
        // create adapter once
        if (mAdapter == null){
            int layout = android.R.layout.simple_list_item_1;
            Cursor c = null; // there is no cursor yet
            int flags = 0; // no auto-requery! Loader requeries.
            mAdapter = new SimpleCursorAdapter(getActivity(), layout, c, FROM, TO, flags);
            setListAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("carpoolparents", "onActivityCreated");
        if (checkPermissions()) {
            Log.d("carpoolparents", "has permissions");
            createAdapter();
            // and tell loader manager to start loading
            getLoaderManager().initLoader(0, null, this);
        }
        //TODO: doesn't work - if no permissions keeps showing loading screen
        //setEmptyText("NO PERMISSIONS");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mAdapter != null) {
            // load from the "Contacts table"
            Uri contentUri = Contacts.CONTENT_URI;

            // no sub-selection, no sort order, simply every row
            // projection says we want just the _id and the name column
            return new CursorLoader(getActivity(),
                    contentUri,
                    PROJECTION,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            // Once cursor is loaded, give it to adapter
            mAdapter.swapCursor(data);

            Log.d("carpoolparents", "onLoadFinished contacts count = " + mAdapter.getCount());
            if (mAdapter.getCount() > 0) {
                Log.d("carpoolparents", mAdapter.getItem(0).toString());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            // on reset take any old cursor away
            mAdapter.swapCursor(null);
        }
    }

    // check if have read contacts permissions. If not - ask for permittions
    private boolean checkPermissions() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    createAdapter();
                }
                return;
            }
        }
    }
}