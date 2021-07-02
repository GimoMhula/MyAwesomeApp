package dev.visum.demoapp.services.offlineSales;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.MyCallbackInterface;
import dev.visum.demoapp.model.NotificationType;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineSalesTasks {
    private static final OfflineSalesTasks ourInstance = new OfflineSalesTasks();
    private GetDataService service;
    private Context context;

    public static OfflineSalesTasks getInstance() {
        return ourInstance;
    }

    public void sendNewSales(Context context) {
        this.context = context;
        service = MozCarbonAPI.getRetrofit(context).create(GetDataService.class);

        if (Tools.isConnected(context)) {
            if (!KeyStoreLocal.getInstance(context).getOfflineSales().isEmpty()) {

                String PATH = Environment.getExternalStorageDirectory().toString()
                        + "/recibos-das-vendas";
                File file = new File(PATH);
                boolean check = file.mkdirs();

                ArrayList<AddSaleModel> addSaleModelsList = KeyStoreLocal.getInstance(context).getOfflineSales();

                for (AddSaleModel saleModel : addSaleModelsList) {
                    try {
                        final String clientName = saleModel.getCustomer_id().replace(" ", "").toLowerCase();

                        getClientId(saleModel.getCustomer_id(), clientId -> {
                            if (clientId != null && !Tools.isStringNil(clientId.toString())) {
                                getProductId(saleModel.getProduct_id(), productId -> {
                                    if (productId != null && !Tools.isStringNil(productId.toString())) {
                                        saleModel.setCustomer_id(clientId.toString());
                                        saleModel.setProduct_id(productId.toString());
                                        if (Tools.getLatLng(context) != null) {
                                            saleModel.setLat(Tools.getLatLng(context).getLatitude());
                                            saleModel.setLng(Tools.getLatLng(context).getLongitude());
                                        }

                                        processSale(saleModel, urlPath -> {
                                            if (urlPath != null && !Tools.isStringNil(urlPath.toString())) {
                                                if (file.isDirectory()) {
                                                    String documentName = context.getString(R.string.app_name) + "-recibo-" + clientName + "-" + System.currentTimeMillis();
                                                    File invoiceFil = new File(PATH + "/" + documentName + ".pdf");
                                                    Uri path = Uri.fromFile(file);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    Intent chooser = Intent.createChooser(intent, context.getResources().getString(R.string.open_invoice_file_with));
                                                    intent.setDataAndType(path, "application/pdf");

                                                    Tools.showNotification(context, NotificationType.ADD, context.getString(R.string.add_sale_ok), intent);

                                                    String[] data = {
                                                            urlPath.toString(),
                                                            PATH + "/",
                                                            clientName,
                                                            documentName
                                                    };

                                                    Tools.createPdf(context, data[0], data[1], data[2], data[3]);
                                                }
                                            } else {
                                                Tools.showNotification(context, NotificationType.ERROR, context.getString(R.string.failed_to_add_sale_processing));
                                            }
                                        });
                                    } else {
                                        Tools.showNotification(context, NotificationType.ERROR, context.getString(R.string.failed_to_add_sale_product_id));
                                    }
                                });
                            } else {
                                Tools.showNotification(context, NotificationType.ERROR, context.getString(R.string.failed_to_add_sale_client_id));
                            }
                        });
                    } catch (Exception e) {
                        Tools.showNotification(context, NotificationType.ERROR, context.getString(R.string.failed_to_add_sale));
                        e.printStackTrace();
                    }
                }

                KeyStoreLocal.getInstance(context).clearOfflineSales();
            }
        }
    }

    private void getClientId(String name, MyCallbackInterface myInterface) {
        Call<ResponseModel<List<CustomerResponseModel>>> call = service.getClientFilteredList(name);

        call.enqueue(new Callback<ResponseModel<List<CustomerResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CustomerResponseModel>>> call, Response<ResponseModel<List<CustomerResponseModel>>> response) {
                boolean foundId = false;
                if (response.isSuccessful()) {
                    if (!response.body().getResponse().isEmpty() && response.body().getResponse() instanceof ArrayList) {
                        for (CustomerResponseModel customerResponseModel:
                                response.body().getResponse()) {
                            if (customerResponseModel.getContact().equalsIgnoreCase(name) || customerResponseModel.getName().equalsIgnoreCase(name)) {
                                foundId = true;
                                myInterface.callback(customerResponseModel.getId());
                                break;
                            }
                        }
                    }
                }

                if (!foundId) {
                    myInterface.callback(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                t.printStackTrace();
                myInterface.callback(null);
            }
        });
    }

    private void getProductId(String name, MyCallbackInterface myInterface) {
        Call<ResponseModel<List<ProductResponseModel>>> call = service.getProductFilteredList(name);

        call.enqueue(new Callback<ResponseModel<List<ProductResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ProductResponseModel>>> call, Response<ResponseModel<List<ProductResponseModel>>> response) {
                boolean foundId = false;

                if (response.isSuccessful()) {
                    if (!response.body().getResponse().isEmpty() && response.body().getResponse() instanceof ArrayList) {
                        for (ProductResponseModel productResponseModel:
                                response.body().getResponse()) {
                            if (name.equalsIgnoreCase(productResponseModel.getName())) {
                                myInterface.callback(Integer.toString(productResponseModel.getId()));
                                foundId = true;
                                break;
                            }
                        }
                    }
                }

                if (!foundId) {
                    myInterface.callback(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ProductResponseModel>>> call, Throwable t) {
                myInterface.callback(null);
            }
        });
    }

    private void processSale(AddSaleModel addSaleModel, MyCallbackInterface myInterface) {
        Call<SaleAddedResponseModel> call  = service.postSale(Tools.convertObjToMap(addSaleModel));

        call.enqueue(new Callback<SaleAddedResponseModel>() {
            @Override
            public void onResponse(Call<SaleAddedResponseModel> call, Response<SaleAddedResponseModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    myInterface.callback(response.body().getUrl());
                } else {
                    myInterface.callback(null);
                }
            }

            @Override
            public void onFailure(Call<SaleAddedResponseModel> call, Throwable t) {
                t.printStackTrace();
                myInterface.callback(null);
            }
        });
    }

}
