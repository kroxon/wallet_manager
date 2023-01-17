package step.wallet.maganger.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.classes.Transaction;

public class InfoRepository {

    private final SQLiteDatabase db = DatabaseOpenHelper.getAppDatabase();

    public void writeInfo(@NonNull String info) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_GENERAL_NOTE_1, info);
        db.insert(DBConstants.TABLE_GENERAL, null, values);
        db.close();
    }

    public void writeTransaction(@NonNull String id_category, String id_subcategory, String date, String amount, String id_account,
                                 String note_1, String note_2, String photo) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_ID_CAT, id_category);
        values.put(DBConstants.COL_TRANSACTION_ID_SUBCAT, id_subcategory);
        values.put(DBConstants.COL_TRANSACTION_DATE, date);
        values.put(DBConstants.COL_TRANSACTION_VALUE, amount);
        values.put(DBConstants.COL_TRANSACTION_ID_ACC, id_account);
        values.put(DBConstants.COL_TRANSACTION_NOTE_1, note_1);
        values.put(DBConstants.COL_TRANSACTION_NOTE_2, note_2);
        values.put(DBConstants.COL_TRANSACTION_PHOTO, photo);
        db.insert(DBConstants.TABLE_TRANSACTION, null, values);
//        db.close();

    }

    public void removeTransaction(String idTransaction) {
        String[] whereArgs = new String[]{String.valueOf(idTransaction)};
        db.delete(DBConstants.TABLE_TRANSACTION, DBConstants.COL_TRANSACTION_ID + "=?", whereArgs);
    }

    // remove selected category and move subcategories to right place
    public void removeCategory(String nameCategory) {
        String idCategory = getIdCategory(nameCategory);
        int idCat = Integer.parseInt(idCategory);
        String[] whereArgs = new String[]{String.valueOf(nameCategory)};
        removeSubategory(idCategory);
        db.delete(DBConstants.TABLE_CATEGORY, DBConstants.COL_CAT_NAME + "=?", whereArgs);

//        // reducing the number "id category" to match the correct one
//
//        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_SUBCATEGORY  + " WHERE " + DBConstants.COL_SUBCAT_SUPERCAT_ID + " > '" + idCat + "'" ;;
//        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
//        // looping through all rows and decreasing number
//        if (cursor.moveToFirst()) {
//            do {
//                int idSuperCat = Integer.parseInt(cursor.getString(1));
//                String idSubcat = cursor.getString(0);
//                ContentValues values = new ContentValues();
//                values.put(DBConstants.COL_SUBCAT_SUPERCAT_ID, String.valueOf(idSuperCat - 1));
//                String[] whereArgsUpdate = new String[] { idSubcat };
//                db.update(DBConstants.TABLE_SUBCATEGORY, values, DBConstants.COL_SUBCAT_SUPERCAT_ID + "=?", whereArgsUpdate);
//            } while (cursor.moveToNext());
//        }

    }

    public void updateTransaction(String idTransaction, String date) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_VALUE, date);
        String[] whereArgs = new String[]{String.valueOf(idTransaction)};
        db.update(DBConstants.TABLE_TRANSACTION, values, DBConstants.COL_TRANSACTION_ID + "=?", whereArgs);
    }

    public void updateCategoryName(String newNameCategory, String oldNameCategory) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_NAME, newNameCategory);
        String[] whereArgs = new String[]{String.valueOf(oldNameCategory)};
        db.update(DBConstants.TABLE_CATEGORY, values, DBConstants.COL_CAT_NAME + "=?", whereArgs);
    }

    public void updateCategoryIcon(String iconIdRepository, String NameCategory) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_ICON, iconIdRepository);
        String[] whereArgs = new String[]{String.valueOf(NameCategory)};
        db.update(DBConstants.TABLE_CATEGORY, values, DBConstants.COL_CAT_NAME + "=?", whereArgs);
    }

    public void addCategory(@NonNull String nameCategory) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_NAME, nameCategory);
        values.put(DBConstants.COL_CAT_ICON, "2131165331");
        db.insert(DBConstants.TABLE_CATEGORY, null, values);
//        db.close();
    }

    public void removeSubategoryByName(String nameSubcategory, String idCategory) {
        String[] whereArgs = new String[]{String.valueOf(nameSubcategory), String.valueOf(idCategory)};
        db.delete(DBConstants.TABLE_SUBCATEGORY, DBConstants.COL_SUBCAT_NAME + "=? AND " + DBConstants.COL_SUBCAT_SUPERCAT_ID + "=?", whereArgs);
    }

    public void removeSubategory(String idCategory) {
        String[] whereArgs = new String[]{idCategory};
        db.delete(DBConstants.TABLE_SUBCATEGORY, DBConstants.COL_SUBCAT_SUPERCAT_ID + "=?", whereArgs);
    }

    public void addSubcategory(@NonNull String nameSubcategory, String idSUperCat) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_SUBCAT_NAME, nameSubcategory);
        values.put(DBConstants.COL_SUBCAT_SUPERCAT_ID, idSUperCat);
        db.insert(DBConstants.TABLE_SUBCATEGORY, null, values);
//        db.close();
    }

    public void updateSubcategoryName(String newNameSubcategory, String oldNameSubcategory, String idCategory) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_SUBCAT_NAME, newNameSubcategory);
        String[] whereArgs = new String[]{String.valueOf(oldNameSubcategory), String.valueOf(idCategory)};
        db.update(DBConstants.TABLE_SUBCATEGORY, values, DBConstants.COL_SUBCAT_NAME + "=? AND " + DBConstants.COL_SUBCAT_SUPERCAT_ID + "=?", whereArgs);
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
//        db.close();
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
//        db.close();
        return list;

    }

    // to do - returning ID category

    @Nullable
    public String getIdCategory(String cateoryName) {
        String idCategory = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_NAME + " = '" + cateoryName + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(cateoryName))
                    idCategory = cursor.getString(0);
            } while (cursor.moveToNext());
        }
//        db.close();
        return idCategory;
    }

    @Nullable
    public String getIdSubcategory(String subcateoryName, String categoryId) {
        String idSubcategory = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_SUBCATEGORY + " WHERE " + DBConstants.COL_SUBCAT_NAME + " = '" + subcateoryName + "' AND " + DBConstants.COL_SUBCAT_SUPERCAT_ID + " = '" + categoryId + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(2).equals(subcateoryName))
                    idSubcategory = cursor.getString(0);
            } while (cursor.moveToNext());
        }
//        db.close();
        return idSubcategory;
    }


    // to do - returning subcategories by ID category

    @Nullable
    public List<String> getSubcategories(String cateoryId) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_SUBCATEGORY + " WHERE " + DBConstants.COL_SUBCAT_SUPERCAT_ID + " = '" + cateoryId + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));//adding 2nd column data
            } while (cursor.moveToNext());
        }
//        // closing connection
//        cursor.close();
//        db.close();
        // returning lables
        return list;

    }

    @Nullable
    public int getIdCategoryIcon(String cateoryName) {
        int idCategoryIcon = 0;
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_NAME + " = '" + cateoryName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(cateoryName))
                    idCategoryIcon = Integer.parseInt(cursor.getString(2));
            } while (cursor.moveToNext());
        }
//        db.close();
        return idCategoryIcon;
    }

//    public Transactions

    public ArrayList<Transaction> readTransactions(String idAccount) {
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_TRANSACTION + " WHERE " + DBConstants.COL_TRANSACTION_ID_ACC + " = '" + idAccount + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Accout
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(5).equals(idAccount)) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(cursor.getString(0));
                    transaction.setTransactionValue(cursor.getString(1));
                    transaction.setTransactionIdCategory(cursor.getString(2));
                    transaction.setTransactionIdSubcategory(cursor.getString(3));
                    transaction.setTransactionDate(cursor.getString(4));
                    transaction.setTransactionNote1(cursor.getString(6));
                    transaction.setTransactionNote2(cursor.getString(7));
                    transaction.setTransactionPhoto(cursor.getString(8));
                    transactionsList.add(transaction);
                }
            } while (cursor.moveToNext());
        }
        return transactionsList;
    }
}
