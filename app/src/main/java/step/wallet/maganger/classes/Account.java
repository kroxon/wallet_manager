package step.wallet.maganger.classes;

import android.content.Context;

import java.util.ArrayList;

import step.wallet.maganger.data.CurrencyDatabase;

public class Account {
    private String accountId;
    private String accountName;
    private String accountType;
    private String accountCurrency;
    private String accountDescription;
    private String accountBalance;

    public Account(String accountId, String accountName, String accountCurrency, String accountDescription, String accountBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountType = "normal";
        this.accountCurrency = accountCurrency;
        this.accountDescription = accountDescription;
        this.accountBalance = accountBalance;
    }

    public Account() {
        this.accountId = new String();
        this.accountName = new String();
        this.accountType = new String();
        this.accountCurrency = new String();
        this.accountDescription = new String();
        this.accountBalance = new String();
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccoutType(String accoutType) {
        this.accountType = accoutType;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccoutType() {
        return accountType;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public String getAccountCurrencySymbol(Context context) {
        CurrencyDatabase currencyDatabase = new CurrencyDatabase(context);
        ArrayList<CurrencyStrings> currentList = currencyDatabase.getCurrenciesList();
        String symbol = "";
        for (int j = 0; j < currentList.size(); j++) {
            if (currentList.get(j).getName().equals(getAccountCurrency())) {
                 symbol = currentList.get(j).getSymbol();
            }
        }
        return symbol;
    }
}
