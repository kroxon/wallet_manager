package step.wallet.maganger.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public void removeTransaction (String idTransaction){
        String[] whereArgs = new String[] { String.valueOf(idTransaction) };
        db.delete(DBConstants.TABLE_TRANSACTION, DBConstants.COL_TRANSACTION_ID+"=?",whereArgs);
    }

    public void updateTransaction(String idTransaction, String date){
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_VALUE, date);
        String[] whereArgs = new String[] { String.valueOf(idTransaction) };
        db.update(DBConstants.TABLE_TRANSACTION, values, DBConstants.COL_TRANSACTION_ID+"=?", whereArgs);
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

    @Nullable
    public List<String> getAllCategories() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY;
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
//        // closing connection
//        cursor.close();
//        db.close();
        // returning lables
        return list;

    }
}
