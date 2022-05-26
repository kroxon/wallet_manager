package step.wallet.maganger.data;

public class DBConstants {

    public static final String DB_LOCATION = "/data/data/step.wallet.maganger/databases/" + DBConstants.DB_NAME;

    static final String DB_NAME = "WalletManager";
    static final int DB_VERSION = 1;

    static final String TABLE_INFO = "TABLE_INFO";
    static final String TABLE_CATEGORY = "TABLE_CATEGORY";

    static final String INFO_FIELD_TEXT = "INFO_FIELD_TEXT";
    static final String INFO_F = "INFO_FIELD_TEXT";

    static final String DATABASE_CREATE;
    static final String DATABASE_CREATE_2;
    static {
        DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_INFO
                + " (ID INTEGER PRIMARY KEY, " + INFO_FIELD_TEXT + " TEXT);";
    }
    static {
        DATABASE_CREATE_2 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CATEGORY
                + " (ID INTEGER PRIMARY KEY, " + INFO_F + " TEXT);";
    }
}
