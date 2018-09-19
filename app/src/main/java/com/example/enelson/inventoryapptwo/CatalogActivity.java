package com.example.enelson.inventoryapptwo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;

import com.example.enelson.inventoryapptwo.data.InventoryContract;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int INVENTORY_LOADER = 0;

    InventoryCursorAdapter mCursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdaptor = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdaptor);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                intent.setData(currentInventoryUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI, projection,
            null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursorAdaptor.swapCursor(null);
    }
}
