package com.example.enelson.inventoryapptwo.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    private InventoryContract(){}

    public static final class InventoryEntry implements BaseColumns {

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
    }
}
