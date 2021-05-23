package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterSaleCustomerFiltered;
import dev.visum.demoapp.adapter.AdapterSaleProductFiltered;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.model.SaleCreatedModel;
import dev.visum.demoapp.utils.Tools;
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
    private View parent_view;
    private ProgressDialog progressDialog;
    private AutoCompleteTextView act_product, act_client, act_qty, act_pay_type, bt_sale_date;
    private Button bt_submit;
    private CompositeDisposable disposable = new CompositeDisposable();

    private AdapterSaleCustomerFiltered customerFilteredAdapter;
    private ArrayList<CustomerResponseModel> customersRespList = new ArrayList<>();
    private String customerId = "";

    private AdapterSaleProductFiltered productFilteredAdapter;
    private ArrayList<ProductResponseModel> productsRespList = new ArrayList<>();
    private String productId = "";

    Map<Integer,String> payTypeMap = new HashMap<>();
    {
        payTypeMap.put(1, "A mão");
        payTypeMap.put(2, "M-Pesa");
        payTypeMap.put(3, "Conta Móvel");
        payTypeMap.put(4, "Transferência");
    }

    public AddSaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSaleFragment newInstance(String param1, String param2) {
        AddSaleFragment fragment = new AddSaleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent_view = inflater.inflate(R.layout.fragment_add_sale, container, false);

        initComponent();

        return parent_view;
    }

    private void initComponent() {
        progressDialog = new ProgressDialog(getActivity());
        act_client = parent_view.findViewById(R.id.act_client);
        act_pay_type = parent_view.findViewById(R.id.act_pay_type);
        act_product = parent_view.findViewById(R.id.act_product);
        act_qty = parent_view.findViewById(R.id.act_qty);
        bt_submit = parent_view.findViewById(R.id.bt_submit);
        bt_sale_date = parent_view.findViewById(R.id.bt_sale_date);

        List<String> list = new ArrayList(payTypeMap.values());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line,list);
        act_pay_type.setAdapter(adapter);
        act_pay_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    act_pay_type.showDropDown();

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
                }
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processSale();
            }
        });

        bt_sale_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePickerLight(view);
            }
        });

        // get latitude and longitude
        // get signature
    }

    private void dialogDatePickerLight(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date = calendar.getTimeInMillis();
                        ((EditText) v).setText(Tools.getFormattedDateShort(date));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getActivity().getFragmentManager(), getString(R.string.date_sale_fragment));
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQueryClient() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
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
                            }
                        } else {
                            Snackbar.make(parent_view, getString(R.string.error_sale_fragment), Snackbar.LENGTH_LONG).show();
                        }
                        customerFilteredAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                        customerFilteredAdapter.clear();
                        customerId = "";
                        Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail), Snackbar.LENGTH_LONG).show();
                    }
                });

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
                        Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail), Snackbar.LENGTH_LONG).show();
                    }
                });
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


    private void processSale() {
        progressDialog.setMessage(getString(R.string.loading_sale_fragment));
        progressDialog.show();

        try {
            String pay_type = act_pay_type.getText().toString();
            String qty = act_qty.getText().toString();
            String date = bt_sale_date.getText().toString();
            double lat = Tools.getLatLng(getContext()).getLatitude();
            double lng = Tools.getLatLng(getContext()).getLongitude();

            if (!Tools.isStringNil(pay_type)
                    && !Tools.isStringNil(date)
                    && !Tools.isStringNil(qty) && !customerId.equals("") && !productId.equals("")) {
                GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);

                Call<SaleAddedResponseModel> call = service.postSale(Tools.convertObjToMap(new SaleCreatedModel(
                        productId,
                        customerId,
                        KeyStoreLocal.getInstance(getContext()).getUserId(),
                        qty,
                        Tools.getMapKey(payTypeMap, pay_type) + "",
                        date,
                        lat,
                        lng
                )));

                call.enqueue(new Callback<SaleAddedResponseModel>() {
                    @Override
                    public void onResponse(Call<SaleAddedResponseModel> call, Response<SaleAddedResponseModel> response) {
                        progressDialog.hide();

                        if (response.isSuccessful() && response.body() != null) {
                            Snackbar.make(parent_view, getString(R.string.success_sale_fragment), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SaleAddedResponseModel> call, Throwable t) {
                        progressDialog.hide();
                        Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed), Snackbar.LENGTH_LONG).show();
                    }
                });

            } else {
                progressDialog.hide();
                Snackbar.make(parent_view, getString(R.string.missing_data_sale_fragment), Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.hide();
            if (!Tools.isGPS_ON(getContext())) {
                Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail_gps), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(parent_view, getString(R.string.error_sale_fragment_fail), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}