package com.example.enelson.inventoryapptwo;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.enelson.inventoryapptwo.data.InventoryContract.InventoryEntry;

import org.w3c.dom.Text;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri mCurrentInventoryUri;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mSupplierPhoneEditText;

    private EditText mQuantityEditText;

    private Spinner mSupplierSpinner;

    private int mSupplier = InventoryEntry.SUPPLIER_UNKNOWN;


    private boolean mInventoryChanged = false;



    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent){
            mInventoryChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        if (mCurrentInventoryUri == null){

            setTitle(getString(R.string.editor_activity_title_new_product));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_inventory));

            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_inventory_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_inventory_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mSupplierSpinner = (Spinner) findViewById(R.id.spinner_supplier);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_editor);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String phone = mSupplierPhoneEditText.getText().toString().trim();

                if (phone.equals("") || phone.length() != 10){
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        });

        Button increaseQuantity = (Button) findViewById(R.id.button_quantity_increase);
        Button decreaseQuantity = (Button) findViewById(R.id.button_quantity_decrease);

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = mQuantityEditText.getText().toString().trim();

                if (amountString.equals("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_add_to_none), Toast.LENGTH_SHORT).show();
                    return;
                }
                int amount = Integer.parseInt(amountString);

                if (amount == 9999) {
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_go_above_editor), Toast.LENGTH_SHORT).show();
                }
                else {
                    amount = amount + 1;
                    mQuantityEditText.setText(Integer.toString(amount));
                }
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = mQuantityEditText.getText().toString().trim();

                if (amountString.equals("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_add_to_none), Toast.LENGTH_SHORT).show();
                    return;
                }

                int amount = Integer.parseInt(amountString);

                if (amount == 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_go_below_one_editor), Toast.LENGTH_SHORT).show();
                }

                else {
                    amount = amount - 1;
                    mQuantityEditText.setText(Integer.toString(amount));
                }
            }
        });

    }

    private void setupSpinner(){
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options,android.R.layout.simple_spinner_item);

        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);

        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals(getString(R.string.supplier1))){
                        mSupplier = InventoryEntry.SUPPLIER_1;
                    } else if (selection.equals(getString(R.string.supplier2))){
                        mSupplier = InventoryEntry.SUPPLIER_2;
                    } else if (selection.equals(getString(R.string.supplier3))){
                        mSupplier = InventoryEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = InventoryEntry.SUPPLIER_UNKNOWN;
            }
        });
    }

    private boolean saveInventory() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();
        String supplierName = mSupplierSpinner.getSelectedItem().toString();

        if (nameString == null || nameString.equals("")){
            Toast.makeText(this, getString(R.string.must_have_name), Toast.LENGTH_SHORT).show();
            return false;

        }

        else if (priceString == null || priceString.equals("") || Integer.parseInt(priceString) <= 0){
            Toast.makeText(this, getString(R.string.must_have_price), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (quantityString == null || quantityString.equals("") || Integer.parseInt(quantityString) <= 0){
            Toast.makeText(this, getString(R.string.must_have_quantity), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (supplierPhoneString == null || supplierPhoneString.equals("") || Integer.parseInt(quantityString) != 10){
            Toast.makeText(this, getString(R.string.must_have_quantity), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (supplierPhoneString.length() != 10){
            Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        else {

            if (mCurrentInventoryUri == null && TextUtils.isEmpty(nameString)
                    && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString)
                    && TextUtils.isEmpty(supplierPhoneString)){
                Toast.makeText(this, getString(R.string.blank_inventory_fail), Toast.LENGTH_SHORT).show();
                return false;
            }

            int priceInt = Integer.parseInt(priceString);
            int quantityInt = Integer.parseInt(quantityString);

            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhoneString);
            values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY,quantityInt);
            values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, priceInt);

            if (supplierName == getString(R.string.supplier1)){
                mSupplier = InventoryEntry.SUPPLIER_1;
            } else if (supplierName == getString(R.string.supplier2)){
                mSupplier = InventoryEntry.SUPPLIER_2;
            } else if (supplierName == getString(R.string.supplier3)){
                mSupplier = InventoryEntry.SUPPLIER_UNKNOWN;
            }


            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, mSupplier);


            if (mCurrentInventoryUri == null) {
                Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.insert_supply_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.insert_supply_successful), Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.update_finished), Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_save:

                if (saveInventory()){
                    finish();
                };
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!mInventoryChanged){
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (!mInventoryChanged){
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
        };
        return new CursorLoader(this, mCurrentInventoryUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);


            String name = cursor.getString(nameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int supplier = cursor.getInt(supplierColumnIndex);

            mNameEditText.setText(name);
            mSupplierPhoneEditText.setText(supplierPhone);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));

            switch (supplier){
                case InventoryEntry.SUPPLIER_1:
                    mSupplierSpinner.setSelection(0);
                    break;
                case InventoryEntry.SUPPLIER_2:
                    mSupplierSpinner.setSelection(1);
                    break;
                case InventoryEntry.SUPPLIER_UNKNOWN:
                    mSupplierSpinner.setSelection(2);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

        mNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierSpinner.setSelection(0);

    }


    public void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirm);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel_question, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (mCurrentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0){
                Toast.makeText(this,getString(R.string.delete_inventory), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,getString(R.string.delete_inventory), Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}

