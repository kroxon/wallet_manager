package step.wallet.maganger.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import step.wallet.maganger.data.InfoRepository;

public class Transaction {
    private String transactionId;
    private String transactionValue;
    private String transactionIdCategory;
    private String transactionIdSubcategory;
    private String transactionDate;
    private String idAccount;
    private String transactionNote1;
    private String transactionNote2;
    private String transactionPhoto;

    public Transaction() {
        this.idAccount = new String();
        this.transactionId = new String();
        this.transactionValue = new String();
        this.transactionIdCategory = new String();
        this.transactionIdSubcategory = new String();
        this.transactionDate = new String();
        this.transactionNote1 = new String();
        this.transactionNote2 = new String();
        this.transactionPhoto = new String();
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionValue(String transactionValue) {
        this.transactionValue = transactionValue;
    }

    public void setTransactionIdCategory(String transactionIdCategory) {
        this.transactionIdCategory = transactionIdCategory;
    }

    public void setTransactionIdSubcategory(String transactionIdSubcategory) {
        this.transactionIdSubcategory = transactionIdSubcategory;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public void setTransactionNote1(String transactionNote1) {
        this.transactionNote1 = transactionNote1;
    }

    public void setTransactionNote2(String transactionNote2) {
        this.transactionNote2 = transactionNote2;
    }

    public void setTransactionPhoto(String transactionPhoto) {
        this.transactionPhoto = transactionPhoto;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionValue() {
        return transactionValue;
    }

    public String getTransactionIdCategory() {
        return transactionIdCategory;
    }

    public String getTransactionIdSubcategory() {
        return transactionIdSubcategory;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public String getTransactionNote1() {
        return transactionNote1;
    }

    public String getTransactionNote2() {
        return transactionNote2;
    }

    public String getTransactionPhoto() {
        return transactionPhoto;
    }

    public Date getDateFormat() throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat("dd.MM.yyyy");
        Date d1 = sdformat.parse(transactionDate);
        return d1;
    }
}
