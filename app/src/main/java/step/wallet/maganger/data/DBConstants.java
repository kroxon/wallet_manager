package step.wallet.maganger.data;

public class DBConstants {

    public static final String DB_LOCATION = "/data/data/step.wallet.maganger/databases/" + DBConstants.DB_NAME;

    static final String DB_NAME = "WalletManager.db";
    static final int DB_VERSION = 1;

    static final String TABLE_CATEGORY = "TABLE_CATEGORY";
    static final String TABLE_SUBCATEGORY = "TABLE_SUBCATEGORY";
    static final String TABLE_ACCOUNT = "TABLE_ACCOUNT";
    static final String TABLE_TRANSACTION = "TABLE_TRANSACTION";
    static final String TABLE_GENERAL = "TABLE_GENERAL";

    static final String COL_CAT_ID = "CATEGORY_ID";
    static final String COL_CAT_NAME = "CATEGORY_NAME";
    static final String COL_CAT_ICON = "CATEGORY_ICON";
    static final String COL_CAT_TYPE = "CATEGORY_TYPE";
    static final String COL_CAT_COLOR = "CATEGORY_COLOR";
    static final String COL_CAT_ARCHIVED = "CATEGORY_ARCHIVED";

    static final String COL_SUBCAT_ID = "SUBCATEGORY_ID";
    static final String COL_SUBCAT_SUPERCAT_ID = "SUPERCATEGORY_ID";
    static final String COL_SUBCAT_NAME = "INFO_FIELD_TEXT";

    static final String COL_ACC_ID = "ACCOUNT_ID";
    static final String COL_ACC_NAME = "ACCOUNT_NAME";
    static final String COL_ACC_TYPE = "ACCOUNT_TYPE";
    static final String COL_ACC_CURRENCY = "ACCOUNT_CURRENCY";
    static final String COL_ACC_DESC = "ACCOUNT_DESCRIPTION";
    static final String COL_ACC_BALANCE = "ACCOUNT_BALANCE";

    static final String COL_TRANSACTION_ID = "TRANSACTION_ID";
    static final String COL_TRANSACTION_VALUE = "TRANSACTION_VALUE";
    static final String COL_TRANSACTION_ID_CAT = "TRANSACTION_ID_CATEGORY";
    static final String COL_TRANSACTION_ID_SUBCAT = "TRANSACTION_ID_SUBCATEGORY";
    static final String COL_TRANSACTION_DATE = "TRANSACTION_ID_DATETIME";
    static final String COL_TRANSACTION_ID_ACC = "TRANSACTION_ID_ACCOUNT";
    static final String COL_TRANSACTION_CURRENCY = "TRANSACTION_CURRENCY";
    static final String COL_TRANSACTION_NOTE_1 = "TRANSACTION_NOTE_1";
    static final String COL_TRANSACTION_NOTE_2 = "TRANSACTION_NOTE_2";
    static final String COL_TRANSACTION_PHOTO = "TRANSACTION_PHOTO";
    static final String COL_TRANSACTION_TYPE = "TRANSACTION_TYPE";

    static final String COL_GENERAL_ID = "GENERAL_ID";
    static final String COL_GENERAL_SYNCHRONISED = "GENERAL_SYNCHRONISED";
    static final String COL_GENERAL_NOTE_1 = "GENERAL_NOTE_1";
    static final String COL_GENERAL_NOTE_2 = "GENERAL_NOTE_2";
    static final String COL_GENERAL_NOTE_3 = "GENERAL_NOTE_3";
    static final String COL_GENERAL_NOTE_4 = "GENERAL_NOTE_4";
    static final String COL_GENERAL_NOTE_5 = "GENERAL_NOTE_5";


    static final String DATABASE_CREATE_1;
    static final String DATABASE_CREATE_2;
    static final String DATABASE_CREATE_3;
    static final String DATABASE_CREATE_4;
    static final String DATABASE_CREATE_5;

    static {
        DATABASE_CREATE_1 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CATEGORY
                + " (" + COL_CAT_ID + " INTEGER PRIMARY KEY, " + COL_CAT_NAME + " TEXT, " + COL_CAT_ICON + " INTEGER, "
                + COL_CAT_TYPE + " TEXT," + COL_CAT_COLOR + " TEXT," + COL_CAT_ARCHIVED + " TEXT);";
    }

    static {
        DATABASE_CREATE_2 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_SUBCATEGORY
                + " (" + COL_SUBCAT_ID + " INTEGER PRIMARY KEY, " + COL_SUBCAT_SUPERCAT_ID + " INTEGER, " + COL_SUBCAT_NAME + " TEXT);";
    }

    static {
        DATABASE_CREATE_3 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_ACCOUNT
                + " (" + COL_ACC_ID + " INTEGER PRIMARY KEY, " + COL_ACC_NAME + " TEXT, " + COL_ACC_TYPE + " TEXT, "
                + COL_ACC_CURRENCY + " TEXT, " + COL_ACC_DESC + " TEXT, " + COL_ACC_BALANCE + " REAL);";
    }

    static {
        DATABASE_CREATE_4 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_TRANSACTION
                + " (" + COL_TRANSACTION_ID + " INTEGER PRIMARY KEY, " + COL_TRANSACTION_VALUE + " INTEGER, "
                + COL_TRANSACTION_ID_CAT + " INTEGER, " + COL_TRANSACTION_ID_SUBCAT + " INTEGER, "
                + COL_TRANSACTION_DATE + " INTEGER, " + COL_TRANSACTION_ID_ACC + " INTEGER, "
                + COL_TRANSACTION_CURRENCY + " TEXT, " + COL_TRANSACTION_NOTE_1 + " TEXT, " + COL_TRANSACTION_NOTE_2
                + " TEXT, " + COL_TRANSACTION_PHOTO + " TEXT, " + COL_TRANSACTION_TYPE + " TEXT);";
    }

    static {
        DATABASE_CREATE_5 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_GENERAL
                + " (" + COL_GENERAL_ID + " INTEGER PRIMARY KEY, " + COL_GENERAL_SYNCHRONISED + " INTEGER, " +
                COL_GENERAL_NOTE_1 + " TEXT, " + COL_GENERAL_NOTE_2 + " TEXT, " +
                COL_GENERAL_NOTE_3 + " TEXT, " + COL_GENERAL_NOTE_4 + " TEXT, " +
                COL_GENERAL_NOTE_5 + " TEXT);";
    }
}
