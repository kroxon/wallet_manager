package step.wallet.maganger.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Account;
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
                                 String note_1, String note_2, String photo, String type) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_ID_CAT, id_category);
        values.put(DBConstants.COL_TRANSACTION_ID_SUBCAT, id_subcategory);
        values.put(DBConstants.COL_TRANSACTION_DATE, date);
        values.put(DBConstants.COL_TRANSACTION_VALUE, amount);
        values.put(DBConstants.COL_TRANSACTION_ID_ACC, id_account);
        values.put(DBConstants.COL_TRANSACTION_CURRENCY, "PLN");
        values.put(DBConstants.COL_TRANSACTION_NOTE_1, note_1);
        values.put(DBConstants.COL_TRANSACTION_NOTE_2, note_2);
        values.put(DBConstants.COL_TRANSACTION_PHOTO, photo);
        values.put(DBConstants.COL_TRANSACTION_TYPE, type);
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

    public void updateTransaction(String idTransaction, String id_category, String id_subcategory, String date, String amount, String id_account,
                                  String note_1, String note_2, String photo, String type) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_TRANSACTION_ID_CAT, id_category);
        values.put(DBConstants.COL_TRANSACTION_ID_SUBCAT, id_subcategory);
        values.put(DBConstants.COL_TRANSACTION_DATE, date);
        values.put(DBConstants.COL_TRANSACTION_VALUE, amount);
        values.put(DBConstants.COL_TRANSACTION_ID_ACC, id_account);
        Account account = getAccount(id_account);
        values.put(DBConstants.COL_TRANSACTION_CURRENCY, account.getAccountCurrency());
        values.put(DBConstants.COL_TRANSACTION_NOTE_1, note_1);
        values.put(DBConstants.COL_TRANSACTION_NOTE_2, note_2);
        values.put(DBConstants.COL_TRANSACTION_PHOTO, photo);
        values.put(DBConstants.COL_TRANSACTION_TYPE, type);

        String[] whereArgs = new String[]{String.valueOf(idTransaction)};
        db.update(DBConstants.TABLE_TRANSACTION, values, DBConstants.COL_TRANSACTION_ID + "=?", whereArgs);
    }

    public void updateCurrencyTransactions(String id_account) {
        ContentValues values = new ContentValues();
        Account account = getAccount(id_account);
        values.put(DBConstants.COL_TRANSACTION_CURRENCY, account.getAccountCurrency());

        String[] whereArgs = new String[]{String.valueOf(id_account)};
        db.update(DBConstants.TABLE_TRANSACTION, values, DBConstants.COL_TRANSACTION_ID_ACC + "=?", whereArgs);
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

    public void updateCategoryColor(String categoryColor, String NameCategory) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_COLOR, categoryColor);
        String[] whereArgs = new String[]{String.valueOf(NameCategory)};
        db.update(DBConstants.TABLE_CATEGORY, values, DBConstants.COL_CAT_NAME + "=?", whereArgs);
    }

    // update Account
    public void updateAccout(@NonNull String nameAccount, String currencyAccount, String descAccount, String balanceAccount, String oldName) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ACC_NAME, nameAccount);
        values.put(DBConstants.COL_ACC_DESC, descAccount);
        values.put(DBConstants.COL_ACC_CURRENCY, currencyAccount);
        values.put(DBConstants.COL_ACC_BALANCE, balanceAccount);
        String[] whereArgs = new String[]{String.valueOf(getIdAccount(oldName))};
        db.update(DBConstants.TABLE_ACCOUNT, values, DBConstants.COL_ACC_ID + "=?", whereArgs);

        updateCurrencyTransactions(getIdAccount(nameAccount));

    }

    public void addCategory(@NonNull String nameCategory, String type) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_NAME, nameCategory);
        Random ran = new Random();
        int x = ran.nextInt(125) + 1;
        values.put(DBConstants.COL_CAT_ICON, "x" + String.valueOf(x));
        values.put(DBConstants.COL_CAT_TYPE, type);
        String[] colors = new String[]{"#ef9a9a", "#f44336", "#f48fb1", "#ad1457", "#1976d2", "#0097a7", "#43a047", "#ffea00", "#ff6f00", "#3e2723"};
        x = ran.nextInt(colors.length - 1);
        values.put(DBConstants.COL_CAT_COLOR, colors[x]);
        values.put(DBConstants.COL_CAT_ARCHIVED, "active");
        db.insert(DBConstants.TABLE_CATEGORY, null, values);
//        db.close();
    }


    public void addDefaultDatabase(String[] categories, List<String[]> subcategories, String[] icons, String[] colors, String[] accountNames) {
        if (getAllCategories().size() == 0) {
            for (int i = 0; i < categories.length; i++) {
                ContentValues valuesCat = new ContentValues();
                valuesCat.put(DBConstants.COL_CAT_NAME, categories[i]);
                if (i < 10)
                    valuesCat.put(DBConstants.COL_CAT_TYPE, "expense");
                else
                    valuesCat.put(DBConstants.COL_CAT_TYPE, "income");
                valuesCat.put(DBConstants.COL_CAT_ICON, icons[i]);
                valuesCat.put(DBConstants.COL_CAT_COLOR, colors[i]);
                valuesCat.put(DBConstants.COL_CAT_ARCHIVED, "active");
                db.insert(DBConstants.TABLE_CATEGORY, null, valuesCat);
            }
            for (int k = 0; k < subcategories.size(); k++) {
                String[] subcats = subcategories.get(k);
                for (int j = 0; j < subcats.length; j++) {
                    ContentValues valuesSubcat = new ContentValues();
                    valuesSubcat.put(DBConstants.COL_SUBCAT_NAME, subcats[j]);
                    valuesSubcat.put(DBConstants.COL_SUBCAT_SUPERCAT_ID, getIdCategory(categories[k]));
                    db.insert(DBConstants.TABLE_SUBCATEGORY, null, valuesSubcat);
                }
            }
        }
        if (getAllAccountsNames().size() == 0) {
            for (int i = 0; i < accountNames.length; i++) {
                ContentValues valuesAcc = new ContentValues();
                valuesAcc.put(DBConstants.COL_ACC_NAME, accountNames[i]);
                valuesAcc.put(DBConstants.COL_ACC_BALANCE, 0);
                Currency currency = Currency.getInstance(Locale.getDefault());
                valuesAcc.put(DBConstants.COL_ACC_CURRENCY, currency.getCurrencyCode());
                valuesAcc.put(DBConstants.COL_ACC_TYPE, currency.getCurrencyCode());
                db.insert(DBConstants.TABLE_ACCOUNT, null, valuesAcc);
            }
        }
    }

    // add Account
    public void addAccount(@NonNull String nameAccount, String currencyAccount, String descAccount, String balanceAccount) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_ACC_NAME, nameAccount);
        values.put(DBConstants.COL_ACC_DESC, descAccount);
        values.put(DBConstants.COL_ACC_CURRENCY, currencyAccount);
        values.put(DBConstants.COL_ACC_BALANCE, balanceAccount);
        db.insert(DBConstants.TABLE_ACCOUNT, null, values);
        db.close();
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

    public void setCategoryArchived(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_ARCHIVED, "archive");
        String[] whereArgs = new String[]{String.valueOf(categoryName)};
        db.update(DBConstants.TABLE_CATEGORY, values, DBConstants.COL_CAT_NAME + "=?", whereArgs);
    }

    public void setCategoryActive(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_CAT_ARCHIVED, "active");
        String[] whereArgs = new String[]{String.valueOf(categoryName)};
        db.update(DBConstants.TABLE_CATEGORY, values, DBConstants.COL_CAT_NAME + "=?", whereArgs);
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

    @Nullable
    public List<String> getAllExpenseCategories() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_TYPE + " = 'expense' AND " + DBConstants.COL_CAT_ARCHIVED + " = 'active'";
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

    @Nullable
    public List<String> getAllIncomeCategories() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_TYPE + " = 'income'";
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


    // get all Acconts
    @Nullable
    public List<String> getAllAccountsNames() {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_ACCOUNT;
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        return list;
    }

    // getting currency
    @Nullable
    public Account getAccount(String accountId) {
        Account account = new Account();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_ACCOUNT + " WHERE " + DBConstants.COL_ACC_ID + " = '" + accountId + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst()) {
                    account.setAccountId(cursor.getString(0));
                    account.setAccountName(cursor.getString(1));
                    account.setAccoutType(cursor.getString(2));
                    account.setAccountCurrency(cursor.getString(3));
                    account.setAccountDescription(cursor.getString(4));
                    account.setAccountBalance(cursor.getString(5));
        }
//        db.close();
        return account;
    }


    //  returning ID category

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

    // get ID Account
    @Nullable
    public String getIdAccount(String accountName) {
        String idCategory = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_ACCOUNT + " WHERE " + DBConstants.COL_ACC_NAME + " = '" + accountName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Account name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(accountName))
                    idCategory = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return idCategory;
    }

    @Nullable
    public String getCategoryName(String idCategory) {
        String categoryName = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_ID + " = '" + idCategory + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(idCategory))
                    categoryName = cursor.getString(1);
            } while (cursor.moveToNext());
        }
//        db.close();
        return categoryName;
    }

    @Nullable
    public String getSubcategoryName(String idSUbcategory) {
        String subcategoryName = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_SUBCATEGORY + " WHERE " + DBConstants.COL_SUBCAT_ID + " = '" + idSUbcategory + "'";
        // on below line we are creating a new array list.
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(idSUbcategory))
                    subcategoryName = cursor.getString(2);
            } while (cursor.moveToNext());
        }
//        db.close();
        return subcategoryName;
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
                if (cursor.getString(1).equals(cateoryName)) {
                    idCategoryIcon = getIdDrawable(cursor.getString(2));
                }
            } while (cursor.moveToNext());
        }
//        db.close();
        return idCategoryIcon;
    }

    @Nullable
    public String getCategoryColor(String cateoryName) {
        String categoryColor = "";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_NAME + " = '" + cateoryName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(cateoryName)) {
                    categoryColor = cursor.getString(4);
                }
            } while (cursor.moveToNext());
        }
//        db.close();
        return categoryColor;
    }


    @Nullable
    public String getIdCategoryIconById(String categoryId) {
        String idCategoryIcon = "0";
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_CATEGORY + " WHERE " + DBConstants.COL_CAT_ID + " = '" + categoryId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Category name
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(categoryId)) {
                    idCategoryIcon = cursor.getString(2);
                    idCategoryIcon = String.valueOf(getIdDrawable(idCategoryIcon));
                }
            } while (cursor.moveToNext());
        }
//        db.close();
        return idCategoryIcon;
    }

    public int getIdDrawable(String resourceName) {
        try {
            Field idField = R.drawable.class.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + R.drawable.class, e);
        }
    }


//    public Transactions

    public ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Accout
        if (cursor.moveToFirst()) {
            do {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(cursor.getString(0));
                    transaction.setTransactionValue(cursor.getString(1));
                    transaction.setTransactionIdCategory(cursor.getString(2));
                    transaction.setTransactionIdSubcategory(cursor.getString(3));
                    transaction.setTransactionDate(cursor.getString(4));
                    transaction.setIdAccount(cursor.getString(5));
//                    transaction.setTransactionCurency(cursor.getString(6));
                    transaction.setTransactionNote1(cursor.getString(7));
                    transaction.setTransactionNote2(cursor.getString(8));
                    transaction.setTransactionPhoto(cursor.getString(9));
                    transaction.setTransactionType(cursor.getString(10));
                    Account account = getAccount(transaction.getIdAccount());
                    transaction.setTransactionCurency(account.getAccountCurrency());
                    transactionsList.add(transaction);
            } while (cursor.moveToNext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            transactionsList.sort((e1, e2) -> new Long(e1.getTransactionDateFormat()).compareTo(new Long(e2.getTransactionDateFormat())));
        }
        Collections.reverse(transactionsList);
        return transactionsList;
    }

    //    read Accounts

    public ArrayList<Account> readAccounts() {
        ArrayList<Account> accountsList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and search for ID Accout
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setAccountId(cursor.getString(0));
                account.setAccountName(cursor.getString(1));
                account.setAccountCurrency(cursor.getString(3));
                account.setAccountDescription(cursor.getString(4));
                account.setAccountBalance(cursor.getString(5));
                accountsList.add(account);

            } while (cursor.moveToNext());
        }
        return accountsList;
    }

    // getting specific transactions
    public ArrayList<Transaction> getSpecificTransactions(String idAccount, double amountFrom, double amountTo, String currency, long startDate, long endDate, String typeOperation) {
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_TRANSACTION + " WHERE " + DBConstants.COL_TRANSACTION_VALUE + " > '" + amountFrom + "' AND "
                + DBConstants.COL_TRANSACTION_VALUE + " < '" + amountTo + "' AND " + DBConstants.COL_TRANSACTION_DATE + " > '" + startDate
                + "' AND " + DBConstants.COL_TRANSACTION_DATE + " < '" + endDate + "'";

        if (!idAccount.equals("0")) {
            selectQuery += " AND " + DBConstants.COL_TRANSACTION_ID_ACC + " = '" + idAccount + "'";
        }
        if (!currency.equals("All")) {
            selectQuery += " AND " + DBConstants.COL_TRANSACTION_CURRENCY + " = '" + currency + "'";
        }
        if (!typeOperation.equals("All")) {
            selectQuery += " AND " + DBConstants.COL_TRANSACTION_TYPE + " = '" + typeOperation + "'";
        }


            Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and search for ID Accout
        if (cursor.moveToFirst()) {
            do {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(cursor.getString(0));
                    transaction.setTransactionValue(cursor.getString(1));
                    transaction.setTransactionIdCategory(cursor.getString(2));
                    transaction.setTransactionIdSubcategory(cursor.getString(3));
                    transaction.setTransactionDate(cursor.getString(4));
                    transaction.setIdAccount(cursor.getString(5));
//                    transaction.setTransactionCurency(cursor.getString(6));
                    transaction.setTransactionNote1(cursor.getString(7));
                    transaction.setTransactionNote2(cursor.getString(8));
                    transaction.setTransactionPhoto(cursor.getString(9));
                    transaction.setTransactionType(cursor.getString(10));
                    Account account = getAccount(transaction.getIdAccount());
                    transaction.setTransactionCurency(account.getAccountCurrency());
                    transactionsList.add(transaction);
            } while (cursor.moveToNext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            transactionsList.sort((e1, e2) -> new Long(e1.getTransactionDateFormat()).compareTo(new Long(e2.getTransactionDateFormat())));
        }
        Collections.reverse(transactionsList);
        return transactionsList;
    }
}
