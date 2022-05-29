package dev.visum.demoapp.utils;

public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private Constants() {
    }

    // REGEX
    public String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public String EMAIL_REGEX = "((8[1-6][0-9]{7})|(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]))|(8[2-7]{1}[0-9]{7})";

    // SharedPreferences
    public String SP_NAME = "MyApp";
    public String SP_TOKEN = "accessToken"; // NOTE: Legacy support until production release
    public String SP_USER_ID = "id"; // NOTE: Legacy support until production release
    public String SP_USER_MODEL = "userModel";
    public String SP_OFFLINE_SALES = "OfflineSales";
    public String SP_OFFLINE_CLIENTS = "OfflineClients";

    // API
  public String API = "https://mozcarbon.herokuapp.com";
//  public String API = "http://3.10.223.89";
//    public String API = "http://34.237.238.49/";
//   public String API = "192.168.27.203:8000";
    public String INVOICE_PATH = "/final_invoice";

    // Query
    public String EMAIL_QUERY = "email";
    public String PASSWORD_QUERY = "password";

    // Services
    public int MYJOBID = 1;
    public String CHANNEL_ID = "moz_carbon_notify_channel_001";
}
