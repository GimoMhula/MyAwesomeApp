package dev.visum.demoapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;

public class KeyStoreLocal {
    private static final KeyStoreLocal ourInstance = new KeyStoreLocal();
    private static SharedPreferences sharedPreferences;

    public static KeyStoreLocal getInstance(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.getInstance().SP_NAME, Context.MODE_PRIVATE);
        return ourInstance;
    }

    private KeyStoreLocal() {
    }

    public void setModel(String type, Object value) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(value);
        setString(type, jsonString);
    }

    public Object getModel(String type) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(sharedPreferences.getString(type, null) , Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void removeString(String type) {
        sharedPreferences.edit().remove(type);
    }

    private void setString(String type, String value) {
        sharedPreferences.edit().putString(type, value).apply();
    }

    private String getString(String type) {
        return sharedPreferences.getString(type, null);
    }


    // Get user accessToken
    public void setToken(String value) {
        setString(Constants.getInstance().SP_TOKEN, value);
    }

    public String getToken() {
        return getString(Constants.getInstance().SP_TOKEN);
    }

    // store user id
    public void setUserId(String value) {
        setString(Constants.getInstance().SP_USER_ID, value);
    }

    public String getUserId() {
        return getString(Constants.getInstance().SP_USER_ID);
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

    public ArrayList<AddSaleModel> getOfflineSales() {
        Object o = getModel(Constants.getInstance().SP_OFFLINE_SALES);

        if (o == null || !Tools.isCollection(o)) {
            return new ArrayList<>();
        } else {
            return (ArrayList<AddSaleModel>) Tools.convertObjectToList(o);
        }
    }

    public void setOfflineSales(AddSaleModel addSaleModel) {
        ArrayList<AddSaleModel> addSaleModelArrayList = getOfflineSales();

        addSaleModelArrayList.add(addSaleModel);

        setModel(Constants.getInstance().SP_OFFLINE_SALES, addSaleModelArrayList);
    }

    public void clearOfflineSales() {
        removeString(Constants.getInstance().SP_OFFLINE_SALES);
    }
}
