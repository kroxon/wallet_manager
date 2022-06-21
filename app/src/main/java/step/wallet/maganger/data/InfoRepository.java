package step.wallet.maganger.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import step.wallet.maganger.ui.FirstFragment;
import step.wallet.maganger.ui.MainActivity;

public class InfoRepository {

    private final SQLiteDatabase db = DatabaseOpenHelper.getAppDatabase();

    public void writeInfo(@NonNull String info) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_GENERAL_NOTE_1, info);
        db.insert(DBConstants.TABLE_GENERAL, null, values);
        db.close();
    }

    public void writeTransaction(@NonNull String id_category, String id_subcategory, String date, String id_account,
                                 String note_1, String note_2, String photo) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_ID_CAT, id_category);
        values.put(DBConstants.COL_TRANSACTION_ID_SUBCAT, id_subcategory);
        values.put(DBConstants.COL_TRANSACTION_DATE, date);
        values.put(DBConstants.COL_TRANSACTION_ID_ACC, id_account);
        values.put(DBConstants.COL_TRANSACTION_NOTE_1, note_1);
        values.put(DBConstants.COL_TRANSACTION_NOTE_2, note_2);
        values.put(DBConstants.COL_TRANSACTION_PHOTO, photo);
        db.insert(DBConstants.TABLE_TRANSACTION, null, values);
        db.close();

    }

    @Nullable
    public String getInfo() {
        String info;
        final String[] cols = new String[]{DBConstants.COL_GENERAL_NOTE_1};
        try (Cursor cursor = db.query(
                true,
                DBConstants.TABLE_GENERAL,
                cols,
                null,
                null,
                null,
                null,
                null,
                null)) {
            cursor.moveToLast();
            info = cursor.getString(0);
        }
        return info;
    }
}
