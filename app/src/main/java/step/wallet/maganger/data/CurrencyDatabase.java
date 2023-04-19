package step.wallet.maganger.data;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import step.wallet.maganger.classes.CurrencyStrings;

public class CurrencyDatabase {
    private Context context;
    ArrayList<CurrencyStrings> currenciesList = new ArrayList<>();
    ArrayList<String> currenciesNameList = new ArrayList<>();
    ArrayList<String> currenciesSymbolList = new ArrayList<>();


    public CurrencyDatabase(Context context) {
        this.context = context;
    }

    public ArrayList<CurrencyStrings> getCurrenciesList() {
        currenciesList.clear();
        loadJSON();
        return currenciesList;
    }

    public ArrayList<String> getCurrenciesNameList() {
        currenciesList.clear();
        loadJSON();
        for (int i = 0; i < currenciesList.size(); i++){
            currenciesNameList.add(currenciesList.get(i).getName());
        }
        return currenciesNameList;
    }

    public ArrayList<String> getCurrenciesSymbolList() {
        currenciesList.clear();
        loadJSON();
        for (int i = 0; i < currenciesList.size(); i++){
            currenciesSymbolList.add(currenciesList.get(i).getSymbol());
        }
        return currenciesSymbolList;
    }

    private void loadJSON() {
        try {
            JSONObject jsonObject = new JSONObject(JsonDataFromAsset());
            JSONArray currenciesListNames = jsonObject.names();
            for (int i = 0; i < jsonObject.names().length(); i++) {
                String symbol = jsonObject.getJSONObject(currenciesListNames.get(i).toString()).getString("symbol");
                String name = jsonObject.getJSONObject(currenciesListNames.get(i).toString()).getString("code");
                CurrencyStrings customStrings = new CurrencyStrings(symbol, name);
                currenciesList.add(customStrings);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String JsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("currencies.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }



}
