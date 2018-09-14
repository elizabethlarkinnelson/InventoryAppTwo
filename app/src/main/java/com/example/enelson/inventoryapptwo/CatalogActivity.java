package com.example.enelson.inventoryapptwo;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int INVENTORY_LOADER = 0;

    InventoryCursorAdapter mCursorAdaptor;
//    NEED TO IMPLEMENT ADAPTERVIEW!!!!!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
//        NEED TO CREATIVE THIS LAYOUT!!!!!!

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }
}
