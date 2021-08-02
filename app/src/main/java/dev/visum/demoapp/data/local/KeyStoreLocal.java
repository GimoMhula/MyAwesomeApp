package dev.visum.demoapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.model.ClientModel;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.UserAgentResponseModel;
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

    public Object getModel(String key, @Nullable Type type) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(sharedPreferences.getString(key, null) , type == null ? Object.class : type);
        } catch (Exception e) {
            return null;
        }
    }

    private void removeString(String type) {
        sharedPreferences.edit().remove(type).apply();
    }

    private void setString(String type, String value) {
        sharedPreferences.edit().putString(type, value).apply();
    }

    private String getString(String type) {
        return sharedPreferences.getString(type, null);
    }


    // Get user accessToken
    // NOTE: Legacy support until production release
    public void setToken(String value) {
        setString(Constants.getInstance().SP_TOKEN, value);
    }

    public String getToken() {
        return getString(Constants.getInstance().SP_TOKEN);
    }

    // store user id
    // NOTE: Legacy support until production release
    public void setUserId(String value) {
        setString(Constants.getInstance().SP_USER_ID, value);
    }

    public String getUserId() {
        return getString(Constants.getInstance().SP_USER_ID);
    }

    // store user
    public void setUser(UserAgentResponseModel value) {
        setModel(Constants.getInstance().SP_USER_MODEL, value);
    }

    @Nullable
    public UserAgentResponseModel getUser() {
        Type type = new TypeToken<UserAgentResponseModel>() {}.getType();
        return (UserAgentResponseModel) getModel(Constants.getInstance().SP_USER_MODEL, type);
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

    public ArrayList<AddSaleModel> getOfflineSales() {
        Type type = new TypeToken<ArrayList<AddSaleModel>>() {}.getType();

        Object o = getModel(Constants.getInstance().SP_OFFLINE_SALES, type);

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


    public ArrayList<CustomerResponseModel> getOfflineClients() {
        Type type = new TypeToken<ArrayList<CustomerResponseModel>>() {}.getType();

        Object o = getModel(Constants.getInstance().SP_OFFLINE_CLIENTS, type);

        if (o == null || !Tools.isCollection(o)) {
            return new ArrayList<>();
        } else {
            return (ArrayList<CustomerResponseModel>) Tools.convertObjectToList(o);
        }
    }

    public void setOfflineClients(ArrayList<CustomerResponseModel> list) {
        setModel(Constants.getInstance().SP_OFFLINE_CLIENTS, list);
    }

}
