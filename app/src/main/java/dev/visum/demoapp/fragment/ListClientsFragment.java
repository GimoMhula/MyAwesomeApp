package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.visum.demoapp.R;
import dev.visum.demoapp.activity.MainActivity;
import dev.visum.demoapp.adapter.AdapterListClients;
import dev.visum.demoapp.adapter.AdapterListSoldItems;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.AddClientModel;
import dev.visum.demoapp.model.ClientModel;
import dev.visum.demoapp.model.ClientResponseModel;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.DataUpdateActivityToFragment;
import dev.visum.demoapp.model.MySaleModel;
import dev.visum.demoapp.model.ResponseAddClientModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.model.SoldItem;
import dev.visum.demoapp.utils.Constants;
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
 * Use the {@link ListClientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListClientsFragment extends Fragment implements DataUpdateActivityToFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private AutoCompleteTextView act_search;
    private View parent_view;
    private RecyclerView recyclerView;
    private ProgressBar progress_search;
    private ProgressDialog progressDialog;
    private AdapterListClients mAdapter;
    private TextView empty_view;
    private ExtendedFloatingActionButton fab_add_client;
    private List<ClientModel> clientItemList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    ListClientsFragment.OnListSoldItemsSelectedListener callback;

    public void setCallback(ListClientsFragment.OnListSoldItemsSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public void pubData(String input) {
//        fetchSales(input);
    }

    public interface OnListSoldItemsSelectedListener {
        void navigateToAddSale(SaleType saleType, SoldItem soldItem);
    }

    public ListClientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListSoldItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListClientsFragment newInstance(String param1, String param2) {
        ListClientsFragment fragment = new ListClientsFragment();
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
        parent_view = inflater.inflate(R.layout.fragment_list_client_items, container, false);

        initToolbar();
        init();

        return parent_view;
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.list_clients_title));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    private void init() {
        ((MainActivity) getActivity()).setCallback(this);
        clientItemList = new ArrayList<>();
        act_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.act_search);
        progress_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.progress_search);
        empty_view = parent_view.findViewById(R.id.empty_view);
        fab_add_client = parent_view.findViewById(R.id.fab_add_client);
        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                fab_add_client.shrink();
            }
        }, 1200);

        mAdapter = new AdapterListClients(getContext(), clientItemList, R.layout.item_client_horizontal);
        recyclerView.setAdapter(mAdapter);

       if (Tools.isConnected(getContext())) {
           GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
           Call<ResponseModel<List<CustomerResponseModel>>> call = service.getClientFilteredList("");

           call.enqueue(new Callback<ResponseModel<List<CustomerResponseModel>>>() {
               @Override
               public void onResponse(Call<ResponseModel<List<CustomerResponseModel>>> call, Response<ResponseModel<List<CustomerResponseModel>>> response) {
                   if (response.isSuccessful()) {
                       Log.d("TAG", "onResponse: "+response.body());
                       empty_view.setVisibility(View.GONE);
                       recyclerView.setVisibility(View.VISIBLE);

                       for (CustomerResponseModel clientsResponseModel : response.body().getResponse()) {

                           ClientModel client = new ClientModel(clientsResponseModel.getId(),
                                    clientsResponseModel.getName(),
                                   clientsResponseModel.getAddress(),
                                   clientsResponseModel.getEmail(),
                                   clientsResponseModel.getContact(),
                                   clientsResponseModel.getSignature(),
                                   clientsResponseModel.getCreated_at(),
                                   clientsResponseModel.getUpdated_at()

                           );
                           clientItemList.add(client);
                       }

                       recyclerView.getAdapter().notifyDataSetChanged();
                   } else {
                       System.out.println("message: " + response.message());
                       try {
                           System.out.println("error body: " + response.errorBody().string().toString());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       empty_view.setVisibility(View.VISIBLE);
                       recyclerView.setVisibility(View.GONE);
                   }
                   progressDialog.dismiss();
               }

               @Override
               public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                   progressDialog.dismiss();
                   t.printStackTrace();
                   Snackbar.make(getView(), getString(R.string.error_get_clients), Snackbar.LENGTH_LONG).show();
               }
           });
       } else {
           progressDialog.dismiss();
           empty_view.setVisibility(View.VISIBLE);
           recyclerView.setVisibility(View.GONE);
       }
        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListClients.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ClientModel obj, int position) {
                // Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();

//                if (obj.isContainsPrest()) {
//                    callback.navigateToAddSale(SaleType.NEXT_PREST, obj);
//                }
            }

        });


        // empty_view.setVisibility(View.VISIBLE);
        // recyclerView.setVisibility(View.GONE);
        fab_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    act_client.setText("");
                    // final Dialog dialog = new Dialog(getActivity());

                    final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                    dialog.setTitle(R.string.dialog_title_add_client);
                    final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_client_alert_dialog, null);
                    dialog.setView(dialogView);

                    AutoCompleteTextView act_name = dialogView.findViewById(R.id.act_name);
                    AutoCompleteTextView act_address = dialogView.findViewById(R.id.act_address);
                    AutoCompleteTextView act_email = dialogView.findViewById(R.id.act_email);
                    AutoCompleteTextView act_contact = dialogView.findViewById(R.id.act_contact);

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
//                            pb_loading.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            try {
                                String name = act_name.getText().toString();
                                String address = act_address.getText().toString();
                                String email = act_email.getText().toString();
                                String contact = act_contact.getText().toString();

                                if (!Tools.isStringNil(name)
                                        && !Tools.isStringNil(address)
                                        && !Tools.isStringNil(email)
                                        && !Tools.isStringNil(contact)) {

                                    GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
                                    Call<ResponseAddClientModel> call = service.postAddClient(Tools.convertObjToMap(new AddClientModel(name, email, address, contact)));

                                    call.enqueue(new Callback<ResponseAddClientModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseAddClientModel> call, Response<ResponseAddClientModel> response) {
                                            if (response.isSuccessful()) {
                                                Snackbar.make(getView(), getString(R.string.create_client_ok), Snackbar.LENGTH_LONG).show();
//                                                customerFilteredAdapter.clear();
//                                                customerFilteredAdapter.add(response.body().getData());
//                                                customerFilteredAdapter.notifyDataSetChanged();
//                                                act_client.showDropDown();
                                            } else {
                                                bt_create_client.setEnabled(true);
                                                System.out.println(response.message());
                                                Snackbar.make(getView(), getString(R.string.failed_client), Snackbar.LENGTH_LONG).show();
                                            }

                                          //  pb_loading.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseAddClientModel> call, Throwable t) {
                                            System.out.println(t.getMessage());
                                            bt_create_client.setEnabled(true);
                                          //  pb_loading.setVisibility(View.GONE);
                                            Snackbar.make(getView(), getString(R.string.error_client), Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    bt_create_client.setEnabled(true);
                                    //pb_loading.setVisibility(View.GONE);
                                    Snackbar.make(getView(), getString(R.string.fields_invalid_client), Snackbar.LENGTH_LONG).show();
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                bt_create_client.setEnabled(true);
                                //pb_loading.setVisibility(View.GONE);
                                Snackbar.make(getView(), getString(R.string.error_client), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show();



            }
        });

    }

    private void debounce(AutoCompleteTextView act, DisposableObserver<TextViewTextChangeEvent> searchQuery) {
        disposable.add(
                RxTextView.textChangeEvents(act)
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery));
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQuerySales() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                progress_search.setVisibility(View.VISIBLE);
                fetchSales(textViewTextChangeEvent.getText().toString());
            }

            @Override
            public void onError(Throwable e) {
                progress_search.setVisibility(View.GONE);
                e.printStackTrace();
                Snackbar.make(getView(), getString(R.string.failed_get_sales), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void fetchSales(String input) {
        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<ResponseModel<List<CustomerResponseModel>>> call = service.getClientFilteredList(input);

        call.enqueue(new Callback<ResponseModel<List<CustomerResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CustomerResponseModel>>> call, Response<ResponseModel<List<CustomerResponseModel>>> response) {
                progress_search.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        clientItemList.clear();
                        for (CustomerResponseModel clientsResponseModel : response.body().getResponse()) {


                            ClientModel client = new ClientModel(clientsResponseModel.getId(),
                                    clientsResponseModel.getName(),
                                    clientsResponseModel.getAddress(),
                                    clientsResponseModel.getEmail(),
                                    clientsResponseModel.getContact(),
                                    clientsResponseModel.getSignature(),
                                    clientsResponseModel.getCreated_at(),
                                    clientsResponseModel.getUpdated_at()

                            );
                            clientItemList.add(client);
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                progress_search.setVisibility(View.GONE);
                Snackbar.make(getView(), getString(R.string.failed_get_sales), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_sales, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search_sales) {
            if (!disposable.isDisposed() && act_search != null) {
                debounce(act_search, searchQuerySales());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}