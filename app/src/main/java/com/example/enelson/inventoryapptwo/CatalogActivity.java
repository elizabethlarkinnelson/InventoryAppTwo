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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.example.enelson.inventoryapptwo.data.InventoryContract;
import com.example.enelson.inventoryapptwo.data.InventoryContract.InventoryEntry;

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

                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                intent.setData(currentInventoryUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }


    private void insertInventory() {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Paintbrush");
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, 5);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, InventoryEntry.SUPPLIER_1);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "888-888-8888");

        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        Log.v("Catalog Activity", "Data inserted into database" );
    }

    private void deleteAllInventory() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("Catalog Acitvity", "Data deleted from database" + rowsDeleted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_insert_dummy_data:
                insertInventory();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this, InventoryEntry.CONTENT_URI, projection,
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

    public void decreaseCount(){
        Toast.makeText(this, "I got here!", Toast.LENGTH_SHORT).show();
    }
}
