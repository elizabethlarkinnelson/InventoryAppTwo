package com.example.enelson.inventoryapptwo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    private InventoryContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.enelson.inventoryapptwo";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY = "inventory";


    public static final class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**Name of database table for inventory*/
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID for inventory
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;


        /**
         *Product Name
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "name";

        /**
         * Price
         *
         * Type: INTEGER
         */

        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity
         *
         * Type: INTEGER
         */

        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Supplier Name
         *
         * Type: INTEGER
         */

        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";

        /**
         * Supplier Phone
         *
         * Type: TEXT
         */

        public final static String COLUMN_PRODUCT_SUPPLIER_PHONE = "supplier_phone";

        /**
         * Accepted supplier identifiers
         */

        public static final int SUPPLIER_UNKNOWN = 0;
        public static final int SUPPLIER_1 = 1;
        public static final int SUPPLIER_2 = 2;

        /**
         * @param supplier
         */

        public static boolean isValidSupplier(int supplier) {
            if (supplier == SUPPLIER_UNKNOWN || supplier == SUPPLIER_1 || supplier == SUPPLIER_2){
                return true;
            }
            return false;
        }
    }
}
