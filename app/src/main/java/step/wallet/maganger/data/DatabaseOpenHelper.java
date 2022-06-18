package step.wallet.maganger.data;

import static step.wallet.maganger.data.DBConstants.DATABASE_CREATE_1;
import static step.wallet.maganger.data.DBConstants.DATABASE_CREATE_2;
import static step.wallet.maganger.data.DBConstants.DATABASE_CREATE_3;
import static step.wallet.maganger.data.DBConstants.DATABASE_CREATE_4;
import static step.wallet.maganger.data.DBConstants.DATABASE_CREATE_5;
import static step.wallet.maganger.data.DBConstants.DB_NAME;
import static step.wallet.maganger.data.DBConstants.DB_VERSION;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import step.wallet.maganger.app.App;

class DatabaseOpenHelper extends SQLiteOpenHelper {

    static SQLiteDatabase getAppDatabase() {
        final DatabaseOpenHelper helper = new DatabaseOpenHelper(App.getInstance());
        return helper.getWritableDatabase();
    }

    private DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_1);
        database.execSQL(DATABASE_CREATE_2);
        database.execSQL(DATABASE_CREATE_3);
        database.execSQL(DATABASE_CREATE_4);
        database.execSQL(DATABASE_CREATE_5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onCreate(database);
    }
}
