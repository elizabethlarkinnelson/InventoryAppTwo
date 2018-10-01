package com.example.enelson.inventoryapptwo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.enelson.inventoryapptwo.data.InventoryContract.InventoryEntry;

import com.example.enelson.inventoryapptwo.data.InventoryContract;
import com.example.enelson.inventoryapptwo.data.InventoryDbHelper;

public class InventoryCursorAdapter extends CursorAdapter{

    public InventoryCursorAdapter(Context context, Cursor c){

        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor){
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        final Button button = (Button) view.findViewById(R.id.button_sale);


        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        final int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);


        final String productID = cursor.getString(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity catalogActivity = (CatalogActivity) context;
                catalogActivity.decreaseCount(productID, Integer.parseInt(productQuantity));
            }
        });
    }
}
