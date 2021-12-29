package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterSaleCustomerFiltered;
import dev.visum.demoapp.adapter.AdapterSaleProductFiltered;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.AddClientModel;
import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.model.AddSalePrestModel;
import dev.visum.demoapp.model.AddSalePrestResponseModel;
import dev.visum.demoapp.model.AddSaleResponseModel;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.MyCallbackInterface;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseAddClientModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.model.SaleCreatedModel;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.model.SoldItem;
import dev.visum.demoapp.utils.Tools;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSaleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private ProgressBar pb_loading;
    private View parent_view;
    private ImageView iv_add_client;
    private ProgressDialog progressDialog;
    private TextInputLayout text_input_username, text_input_total;
    private AutoCompleteTextView act_product, act_client, act_pay_type, act_total, act_installments_nr, act_installments, act_region, act_area, act_quart, act_nr_house, act_ref_street;
    private Button bt_submit;
    private CompositeDisposable disposable = new CompositeDisposable();
    private LinearLayout ll_sale_prest, ll_total, ll_product_client, ll_region_date;
    private AdapterSaleCustomerFiltered customerFilteredAdapter;
    private ArrayList<CustomerResponseModel> customersRespList = new ArrayList<>();
    private String customerId = "";

    private AdapterSaleProductFiltered productFilteredAdapter;
    private ArrayList<ProductResponseModel> productsRespList = new ArrayList<>();
    private String productId = "";
    private IntentIntegrator qrScan;

    private ImageView iv_qrcode;

    Map<Integer,String> payTypeMap = new HashMap<>();
    private double productPriceToVerify;
    private LinearLayout act_pay_mpesa_code_lyt;
    private AutoCompleteTextView act_pay_mpesa_code;
    private Spinner spinner;
    private String paymentSelected;
    private RadioButton act_gender_selected;
    private boolean auxVerify=true;

    {
        payTypeMap.put(1, "A mão");
        payTypeMap.put(2, "M-Pesa");
        payTypeMap.put(3, "Conta Móvel");
        payTypeMap.put(4, "Transferência");
    }

    MaterialCheckBox checkbox_sign;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    OnAddSaleSelectedListener callback;
    private SoldItem soldItem;
    private SaleType saleType;

    public void setCallback(OnAddSaleSelectedListener callback) {
        this.callback = callback;
    }

    public AddSaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param saleType Parameter sale type
     * @param soldItem
     * @return A new instance of fragment AddSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSaleFragment newInstance(SaleType saleType, SoldItem soldItem) {
        AddSaleFragment fragment = new AddSaleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, saleType);
        args.putSerializable(ARG_PARAM2, soldItem);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnAddSaleSelectedListener {
        public void navigateToCustomerSignSale(Map<String, String> addSaleMap, SaleType saleType);
        public void renderWebView(String saleId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            saleType = (SaleType) getArguments().getSerializable(ARG_PARAM1);
            soldItem = (SoldItem) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent_view = inflater.inflate(R.layout.fragment_add_sale, container, false);

        initToolbar();
        initComponent();

        return parent_view;
    }

    private void initToolbar() {
        String title = getString(R.string.add_sale_title);
        if (saleType == SaleType.NEXT_PREST) {
            title = getString(R.string.add_sale_prest_title);
        } else if (saleType == SaleType.FIRST_PAY) {
            title = getString(R.string.add_sale_title);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        qrScan = new IntentIntegrator(getActivity());
        iv_qrcode = parent_view.findViewById(R.id.iv_qrcode);
        ll_sale_prest = parent_view.findViewById(R.id.ll_sale_prest);
        ll_total = parent_view.findViewById(R.id.ll_total);
        ll_product_client = parent_view.findViewById(R.id.ll_product_client);
        ll_region_date = parent_view.findViewById(R.id.ll_region_date);
        act_region = parent_view.findViewById(R.id.act_region);
        act_area = parent_view.findViewById(R.id.act_area);
        act_quart = parent_view.findViewById(R.id.act_quart);
        act_nr_house = parent_view.findViewById(R.id.act_nr_house);
        act_ref_street = parent_view.findViewById(R.id.act_ref_street);
        act_total = parent_view.findViewById(R.id.act_total);
        act_installments = parent_view.findViewById(R.id.act_installments);
        act_installments_nr = parent_view.findViewById(R.id.act_installments_nr);
        pb_loading = parent_view.findViewById(R.id.pb_loading);
        iv_add_client = parent_view.findViewById(R.id.iv_add_client);
        progressDialog = new ProgressDialog(getActivity());
        text_input_username= parent_view.findViewById(R.id.text_input_username);
        text_input_total= parent_view.findViewById(R.id.text_input_total);
        act_client = parent_view.findViewById(R.id.act_client);
        act_pay_type = parent_view.findViewById(R.id.act_pay_type);
        act_product = parent_view.findViewById(R.id.act_product);
        act_pay_mpesa_code_lyt = parent_view.findViewById(R.id.act_pay_mpesa_code_lyt);
        act_pay_mpesa_code = parent_view.findViewById(R.id.act_pay_mpesa_code);
        spinner = parent_view.findViewById(R.id.act_pay_type_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                        paymentSelected=item.toString();
                    if(item.equals("M-Pesa")){
                        act_pay_mpesa_code.setHint(getResources().getString(R.string.act_pay_mpesa));
                    }
                    if(item.equals("Conta Móvel")){
                        act_pay_mpesa_code.setHint(getResources().getString(R.string.act_pay_contaMovel));
                    }
                    if(item.equals("Transferência")){
                        act_pay_mpesa_code.setHint(getResources().getString(R.string.act_pay_transferencia));
                    }

                    if(!item.equals("A mão")){
                        act_pay_mpesa_code_lyt.setVisibility(View.VISIBLE);
                    }else {
                        act_pay_mpesa_code_lyt.setVisibility(View.GONE);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        bt_submit = parent_view.findViewById(R.id.bt_submit);
        // bt_sale_date = parent_view.findViewById(R.id.bt_sale_date);
        checkbox_sign = parent_view.findViewById(R.id.checkbox_sign);

        if (saleType == SaleType.FIRST_PAY) {
            ll_sale_prest.setVisibility(View.VISIBLE);
        } else if (saleType == SaleType.NEXT_PREST) {
            ll_sale_prest.setVisibility(View.VISIBLE);
            text_input_total.setHint(getString(R.string.remain));
            act_total.setText(soldItem.getRemain()+"");
            ll_product_client.setVisibility(View.GONE);
            ll_region_date.setVisibility(View.GONE);
        }

        iv_qrcode.setColorFilter(getContext().getResources().getColor(R.color.mdtp_transparent_black));
        iv_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

        iv_add_client.setColorFilter(getContext().getResources().getColor(R.color.mdtp_transparent_black));
        iv_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act_client.setText("");
                // final Dialog dialog = new Dialog(getActivity());

                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle(R.string.dialog_title_add_client);
                final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_client_alert_dialog, null);
                dialog.setView(dialogView);

                AutoCompleteTextView act_name = dialogView.findViewById(R.id.act_name);
                AutoCompleteTextView act_address = dialogView.findViewById(R.id.act_address);
                AutoCompleteTextView act_email = dialogView.findViewById(R.id.act_email);
                AutoCompleteTextView act_contact = dialogView.findViewById(R.id.act_contact);
                RadioGroup act_gender = dialogView.findViewById(R.id.rg_gender);
                //RadioButton act_gender_selected = dialogView.findViewById(R.id.rd_male);
                AutoCompleteTextView act_age_day = dialogView.findViewById(R.id.act_age_day);
                AutoCompleteTextView act_age_moth = dialogView.findViewById(R.id.act_age_motn);
                AutoCompleteTextView act_age_year = dialogView.findViewById(R.id.act_age_year);


                Button bt_cancel = dialogView.findViewById(R.id.bt_cancel);
                Button bt_create_client = dialogView.findViewById(R.id.bt_create_client);

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                bt_create_client.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bt_create_client.setEnabled(false);
                        pb_loading.setVisibility(View.VISIBLE);

                        try {
                            String name = act_name.getText().toString();
                            String address = act_address.getText().toString();
                            //String email = act_email.getText().toString();
                            String email = "nomail@mail.com";
                            String contact = act_contact.getText().toString();
                            String age=null;
                            if(!act_age_day.getText().toString().isEmpty()&&!act_age_moth.getText().toString().isEmpty()&&!act_age_year.getText().toString().isEmpty()){
                                if(Integer.parseInt(act_age_day.getText().toString())>32 || Integer.parseInt(act_age_day.getText().toString())<1){
                                    act_age_day.setError("Preencha a data de nascimento correctamente!");
                                    auxVerify=false;
                                }else {
                                    auxVerify=true;
                                }
                                if(Integer.parseInt(act_age_moth.getText().toString())>13 || Integer.parseInt(act_age_moth.getText().toString())<1){
                                    act_age_moth.setError("Preencha a mes de nascimento correctamente!");
                                    auxVerify=false;
                                } else {
                                    auxVerify=true;
                                }

                                    if(Integer.parseInt(act_age_year.getText().toString())<1880 || Integer.parseInt(act_age_year.getText().toString())>2050){
                                        act_age_year.setError("Preencha o ano de nascimento correctamente!");
                                        auxVerify=false;
                                     }else {
                                        auxVerify=true;
                                    }
                                age =act_age_year.getText().toString()+"-"+act_age_day.getText().toString()+"-"+act_age_moth.getText().toString();
                            }else {
                                act_age_day.setError("Preencha a data de nascimento!");
                                age="1997-01-01";
                                auxVerify=true;
                            }

                            int selectedId = act_gender.getCheckedRadioButtonId();
                            act_gender_selected = (RadioButton) dialogView.findViewById(selectedId);

                            String gender=act_gender_selected.getText().toString();

                            Toast.makeText(getContext(), age+" "+act_gender_selected.getText(), Toast.LENGTH_LONG).show();

                            if (!Tools.isStringNil(name)
                                    && !Tools.isStringNil(address)
                                       //TODO do not verify email
                                    && !Tools.isStringNil(contact) && auxVerify) {



                                GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
                                Call<ResponseAddClientModel> call = service.postAddClient(Tools.convertObjToMap(new AddClientModel(name, address, contact,gender,age)));

                                call.enqueue(new Callback<ResponseAddClientModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseAddClientModel> call, Response<ResponseAddClientModel> response) {
                                        if (response.isSuccessful()) {
                                            Snackbar.make(getView(), getString(R.string.create_client_ok), Snackbar.LENGTH_LONG).show();
                                            customerFilteredAdapter.clear();
                                            customerFilteredAdapter.add(response.body().getData());
                                            customerFilteredAdapter.notifyDataSetChanged();
                                            Log.d("TAG", "onResponse Successful: "+response.message());
                                            act_client.showDropDown();
                                            dialog.dismiss();
                                        } else {
                                            bt_create_client.setEnabled(true);
                                            Log.d("TAG", "onResponse Fail: "+response.message());
                                            System.out.println(response.message());
                                            Snackbar.make(getView(), getString(R.string.failed_client), Snackbar.LENGTH_LONG).show();
                                        }

                                        pb_loading.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseAddClientModel> call, Throwable t) {
                                        System.out.println(t.getMessage());
                                        bt_create_client.setEnabled(true);
                                        pb_loading.setVisibility(View.GONE);
                                        Snackbar.make(getView(), getString(R.string.error_client)+" "+t.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                bt_create_client.setEnabled(true);
                                pb_loading.setVisibility(View.GONE);
                                Snackbar.make(getView(), getString(R.string.fields_invalid_client), Snackbar.LENGTH_LONG).show();
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            bt_create_client.setEnabled(true);
                            pb_loading.setVisibility(View.GONE);
                            Snackbar.make(getView(), getString(R.string.error_client)+" "+e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        List<String> list = new ArrayList(payTypeMap.values());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line,list);

        spinner.setAdapter(adapter);

        act_pay_type.setAdapter(adapter);
        act_pay_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    act_pay_type.showDropDown();

            }
        });

        checkbox_sign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println(b);
            }
        });

        //
        debounce(act_product, searchQueryProduct());
        productFilteredAdapter = new AdapterSaleProductFiltered(getContext(), android.R.layout.simple_dropdown_item_1line, productsRespList);
        act_product.setAdapter(productFilteredAdapter);
        act_product.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    act_product.showDropDown();

            }
        });
        act_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (productsRespList.get(i) != null) {
                    productId = productsRespList.get(i).getId() + "";
                    act_total.setText(productsRespList.get(i).getPrice() + "");
                    productPriceToVerify=productsRespList.get(i).getPrice();
                }
            }
        });

        debounce(act_client, searchQueryClient());
        customerFilteredAdapter = new AdapterSaleCustomerFiltered(getContext(), android.R.layout.simple_dropdown_item_1line, customersRespList);
        act_client.setAdapter(customerFilteredAdapter);
        act_client.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    act_client.showDropDown();

            }
        });
        act_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (customersRespList.get(i) != null) {
                    customerId = customersRespList.get(i).getId();

                    if (!Tools.isStringNil(customersRespList.get(i).getSignature())) {
                        checkbox_sign.setVisibility(View.VISIBLE);
                        checkbox_sign.setChecked(true);
                    } else {
                        checkbox_sign.setVisibility(View.INVISIBLE);
                        checkbox_sign.setChecked(false);
                    }
                }
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatingSale();
            }
        });

        if (!Tools.isConnected(getContext()) && KeyStoreLocal.getInstance(getContext()).getOfflineClients() != null && !KeyStoreLocal.getInstance(getContext()).getOfflineClients().isEmpty()) {
            customerFilteredAdapter.addAll(KeyStoreLocal.getInstance(getContext()).getOfflineClients());
            customerFilteredAdapter.notifyDataSetChanged();
        }
    }

    private void validatingSale() {
        try {
            String pay_type = paymentSelected;
            String clientName = act_client.getText().toString();
            String productName = act_product.getText().toString();
            String region = act_region.getText().toString();
            String neighborhood = act_area.getText().toString();
            String city_block = act_quart.getText().toString();
            String house_number = act_nr_house.getText().toString();
            String reference_point = act_ref_street.getText().toString();
            String act_pay_mpesa_code_value=act_pay_mpesa_code.getText().toString();
            double first_prestation = Double.parseDouble(act_installments.getText().toString());
            double total = (saleType == SaleType.NEXT_PREST || act_total.getText() == null || act_total.getText().toString().trim().isEmpty()) ? 0 : Double.parseDouble(act_total.getText().toString());
            boolean check_for_first_pay = !productId.equals("") && first_prestation > 0 && first_prestation <= total && !customerId.equals("");
            boolean check_for_next_prest = saleType == SaleType.NEXT_PREST && first_prestation > 0 && first_prestation <= soldItem.getRemain();
            double lat = 0;
            double lng = 0;
            boolean pass_verification=false;
            if(pay_type.equals("M-Pesa")){
                act_pay_mpesa_code_lyt.setVisibility(View.VISIBLE);
            }else {
                act_pay_mpesa_code_lyt.setVisibility(View.GONE);
            }

            double aux=Double.parseDouble(act_total.getText().toString());

            if(first_prestation > total){
                Log.d("check", "first_prestation: "+first_prestation);
//                Log.d("check", "total: "+aux);
                act_installments.setError("Valor deve ser menor que a prestacao em falta");
                pass_verification=false;
            }else {
                pass_verification=true;
            }
            if (Tools.getLatLng(getContext()) != null) {
                lat = Tools.getLatLng(getContext()).getLatitude();
                lng = Tools.getLatLng(getContext()).getLongitude();
            }

            if (!Tools.isStringNil(pay_type) && (pass_verification || check_for_first_pay || check_for_next_prest || (!Tools.isConnected(getContext()) && !Tools.isStringNil(clientName) && !Tools.isStringNil(productName)))) {


                AddSaleModel addSaleModel = new AddSaleModel(
                        KeyStoreLocal.getInstance(getContext()).getUser() != null
                                && KeyStoreLocal.getInstance(getContext()).getUser().getId() != null ? KeyStoreLocal.getInstance(getContext()).getUser().getId() : KeyStoreLocal.getInstance(getContext()).getUserId(),
                        (Tools.isConnected(getContext()) && !customerId.isEmpty()) || (KeyStoreLocal.getInstance(getContext()).getOfflineClients() != null && !KeyStoreLocal.getInstance(getContext()).getOfflineClients().isEmpty() && !customerId.isEmpty()) ? customerId : clientName,
                        Tools.isConnected(getContext()) ? productId : productName,total,
                        first_prestation,
                        Tools.getMapKey(payTypeMap, pay_type) + "",
                        lat,
                        lng,
                        region,
                        neighborhood,
                        city_block,
                        house_number,
                        reference_point,
                        act_pay_mpesa_code_value
                );

                Object saleData =  addSaleModel;

                if (saleType == SaleType.NEXT_PREST) {
                    saleData = new AddSalePrestModel(
                            Integer.parseInt(soldItem.getId().replace(".0", "")),
                            first_prestation,
                            Tools.getMapKey(payTypeMap, pay_type)
                    );
                }

                if (Tools.isConnected(getContext())) {
                    progressDialog.setMessage(getString(R.string.loading_sale_fragment));
                    progressDialog.show();

                    if (saleType != SaleType.NEXT_PREST) {
                        if (!checkbox_sign.isChecked()) {
                            progressDialog.hide();
                            callback.navigateToCustomerSignSale(Tools.convertObjToMap(saleData), saleType);
                        } else {
                            processSale(Tools.convertObjToMap(saleData));
                        }
                    } else {
                        processSale(Tools.convertObjToMap(saleData));
                    }
                } else {
                    KeyStoreLocal.getInstance(getContext()).setOfflineSales(addSaleModel);

                    Tools.alertDialogSimpleOk((AppCompatActivity) getActivity(), getContext().getString(R.string.info_alert_offline_sale_data), isDismissed -> {
                        if (isDismissed != null && Boolean.parseBoolean(isDismissed.toString())) {
                            getActivity().onBackPressed();
                        }
                    });
                }

            } else {
                progressDialog.hide();
                Snackbar.make(parent_view, getString(R.string.missing_data_sale_fragment), Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            progressDialog.hide();
            e.printStackTrace();
            Log.d("TAG", "validatingSale: "+e.getMessage());
            Log.d("TAG", "validatingSale: "+e.getStackTrace());
            if (!Tools.isGPS_ON(getContext())) {
                Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail_gps), Snackbar.LENGTH_LONG).show();
            } else {
                Toasty.error(getActivity(), getString(R.string.error_sale_fragment_fail)+" "+e.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQueryClient() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (Tools.isConnected(getContext())) {
                    GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
                    Call<ResponseModel<List<CustomerResponseModel>>> call = service.getClientFilteredList(textViewTextChangeEvent.getText().toString());

                    call.enqueue(new Callback<ResponseModel<List<CustomerResponseModel>>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<List<CustomerResponseModel>>> call, Response<ResponseModel<List<CustomerResponseModel>>> response) {
                            customerFilteredAdapter.clear();

                            if (response.isSuccessful()) {
                                if (!response.body().getResponse().isEmpty() && response.body().getResponse() instanceof ArrayList) {
                                    for (CustomerResponseModel customerResponseModel:
                                            response.body().getResponse()) {
                                        customerFilteredAdapter.add(customerResponseModel);
                                    }
                                }else {
                                    Toasty.error(getContext(), "Selecione o cliente sugerido!", Toast.LENGTH_SHORT).show();
                                    act_client.setError("Por favor adicione o novo cliente (+)");
                                }
                            } else {
                                Snackbar.make(parent_view, getString(R.string.error_sale_fragment) +" "+ response.message(), Snackbar.LENGTH_LONG).show();
                            }
                            customerFilteredAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                            customerFilteredAdapter.clear();
                            customerId = "";
                            Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail)+" "+t.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQueryProduct() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (Tools.isConnected(getContext())) {
                    GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
                    Call<ResponseModel<List<ProductResponseModel>>> call = service.getProductFilteredList(textViewTextChangeEvent.getText().toString());

                    call.enqueue(new Callback<ResponseModel<List<ProductResponseModel>>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<List<ProductResponseModel>>> call, Response<ResponseModel<List<ProductResponseModel>>> response) {
                            productFilteredAdapter.clear();

                            if (response.isSuccessful()) {
                                if (!response.body().getResponse().isEmpty() && response.body().getResponse() instanceof ArrayList) {
                                    for (ProductResponseModel productResponseModel:
                                            response.body().getResponse()) {
                                        productFilteredAdapter.add(productResponseModel);
                                    }
                                }else {
                                    Toasty.error(getContext(), "Produto nao disponivel no stock!", Toast.LENGTH_LONG).show();
                                    act_product.setError("Solicite a adicao do produto ao stock!");
                                }
                            } else {
                                Snackbar.make(parent_view, getString(R.string.error_sale_fragment), Snackbar.LENGTH_LONG).show();
                            }
                            productFilteredAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<List<ProductResponseModel>>> call, Throwable t) {
                            productFilteredAdapter.clear();
                            productId = "";
                            Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail)+" "+t.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
                
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void debounce(AutoCompleteTextView v, DisposableObserver<TextViewTextChangeEvent> searchQuery) {
        disposable.add(
                RxTextView.textChangeEvents(v)
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery));
    }


    private void processSale(Map<String, String> map) {
        progressDialog.setMessage(getString(R.string.loading_sale_fragment));
        progressDialog.show();

        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);

        if (saleType == SaleType.FIRST_PAY) {
            Call<SaleAddedResponseModel> call = service.postSale(map);

            call.enqueue(new Callback<SaleAddedResponseModel>() {
                @Override
                public void onResponse(Call<SaleAddedResponseModel> call, Response<SaleAddedResponseModel> response) {
                    progressDialog.hide();

                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toasty.success(getActivity(), getString(R.string.success_sale_fragment), Toast.LENGTH_SHORT, true).show();
                        Log.d("PDF", "onResponse: "+response.body().getUrl());
                        callback.renderWebView(response.body().getUrl());

                        // callback.renderWebView(response.body().getResponse().getId() + "");
                    } else {
                        Log.d("Sale", "onResponse: "+response.body());
                        Log.d("Sale", "onResponse: "+response.message());

                        Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed)+" "+response.body(), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SaleAddedResponseModel> call, Throwable t) {
                    progressDialog.hide();
                    Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed)+" "+t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Call<AddSalePrestResponseModel> call = service.postNextPrestSale(map);

            call.enqueue(new Callback<AddSalePrestResponseModel>() {
                @Override
                public void onResponse(Call<AddSalePrestResponseModel> call, Response<AddSalePrestResponseModel> response) {
                    progressDialog.hide();

                    if (response.isSuccessful() && response.body() != null) {
//                        Snackbar.make(parent_view, getString(R.string.success_sale_next_prest_fragment), Snackbar.LENGTH_LONG).show();
                        Toasty.success(getContext(), getString(R.string.success_sale_next_prest_fragment), Toast.LENGTH_LONG).show();

                        getActivity().onBackPressed();
                    } else {
                        Snackbar.make(parent_view, getString(R.string.error_sale_next_prest_fragment_failed), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AddSalePrestResponseModel> call, Throwable t) {
                    progressDialog.hide();
                    Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed)+" "+t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(parent_view, getString(R.string.no_data_available), Snackbar.LENGTH_LONG).show();
            } else {
                Log.d("QR", "onActivityResult GetContents vvv: "+result.getContents());
//                try {
                    act_product.setText(result.getContents());
                    Snackbar.make(parent_view, "Scaneado com Sucesso!", Snackbar.LENGTH_LONG).show();

//                    JSONObject obj = new JSONObject(result.getContents());
                   // Log.d("QR", "onActivityResult obj: "+obj.toString());
                    //Log.d("QR", "onActivityResult Names: "+obj.names());

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Snackbar.make(parent_view, getString(R.string.error_get_qr_code_data), Snackbar.LENGTH_LONG).show();
//                }
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}