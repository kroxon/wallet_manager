package step.wallet.maganger.classes;

public class CurrencyStrings {
    String a;
    String b;

    public CurrencyStrings(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public String getName() {
        return b;
    }

    public String getSymbol() {
        return a;
    }
}