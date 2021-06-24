package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.visum.demoapp.R;
import dev.visum.demoapp.activity.MainActivity;
import dev.visum.demoapp.adapter.AdapterListSoldItems;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.DataUpdateActivityToFragment;
import dev.visum.demoapp.model.MySaleModel;
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
 * Use the {@link ListSoldItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSoldItemsFragment extends Fragment implements DataUpdateActivityToFragment {

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
    private AdapterListSoldItems mAdapter;
    private TextView empty_view;
    private ExtendedFloatingActionButton fab_add_sale;
    private List<SoldItem> soldItemList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    ListSoldItemsFragment.OnListSoldItemsSelectedListener callback;

    public void setCallback(ListSoldItemsFragment.OnListSoldItemsSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public void pubData(String input) {
        fetchSales(input);
    }

    public interface OnListSoldItemsSelectedListener {
        void navigateToAddSale(SaleType saleType, SoldItem soldItem);
    }

    public ListSoldItemsFragment() {
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
    public static ListSoldItemsFragment newInstance(String param1, String param2) {
        ListSoldItemsFragment fragment = new ListSoldItemsFragment();
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
        parent_view = inflater.inflate(R.layout.fragment_list_sold_items, container, false);

        initToolbar();
        init();

        return parent_view;
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.list_sold_items_title));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    private void init() {
        ((MainActivity) getActivity()).setCallback(this);
        soldItemList = new ArrayList<>();
        act_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.act_search);
        progress_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.progress_search);
        empty_view = parent_view.findViewById(R.id.empty_view);
        fab_add_sale = parent_view.findViewById(R.id.fab_add_sale);
        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                fab_add_sale.shrink();
            }
        }, 1200);

        mAdapter = new AdapterListSoldItems(getContext(), soldItemList, R.layout.item_sold_item_horizontal);
        recyclerView.setAdapter(mAdapter);

       if (Tools.isConnected(getContext())) {
           GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
           Call<ResponseModel<ArrayList<MySaleModel>>> call = service.getMySales("");

           call.enqueue(new Callback<ResponseModel<ArrayList<MySaleModel>>>() {
               @Override
               public void onResponse(Call<ResponseModel<ArrayList<MySaleModel>>> call, Response<ResponseModel<ArrayList<MySaleModel>>> response) {
                   if (response.isSuccessful()) {
                       empty_view.setVisibility(View.GONE);
                       recyclerView.setVisibility(View.VISIBLE);

                       for (MySaleModel saleResponseModel : response.body().getResponse()) {
                           String subtitle = ("Falta pagar " + Double.toString(saleResponseModel.getTotalPrice()).replace(".0", "") + "MT em prestações de " + Double.toString(saleResponseModel.getMissing()).replace(".0", "") + "MT");
                           boolean containsPrest = true;

                           if (saleResponseModel.getMissing() == 0) {
                               subtitle = "Pagou um total de " + saleResponseModel.getTotalPrice() + "MT";
                               containsPrest = false;
                           }

                           SoldItem soldItem = new SoldItem(saleResponseModel.getId(),
                                   "Venda para " + saleResponseModel.getProduct().getName(),
                                   subtitle,
                                   saleResponseModel.getCreated_at(),
                                   Constants.getInstance().API + saleResponseModel.getProduct().getImage(),
                                   saleResponseModel.getMissing(),
                                   containsPrest
                           );
                           soldItemList.add(soldItem);
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
               public void onFailure(Call<ResponseModel<ArrayList<MySaleModel>>> call, Throwable t) {
                   progressDialog.dismiss();
                   t.printStackTrace();
                   Snackbar.make(getView(), getString(R.string.error_get_sales), Snackbar.LENGTH_LONG).show();
               }
           });
       } else {
           progressDialog.dismiss();
           empty_view.setVisibility(View.VISIBLE);
           recyclerView.setVisibility(View.GONE);
       }
        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSoldItems.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SoldItem obj, int position) {
                // TODO: pass sale id and the last total price to pay and minimum
                // Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();

                if (obj.isContainsPrest()) {
                    callback.navigateToAddSale(SaleType.NEXT_PREST, obj);
                }
            }
        });


        // empty_view.setVisibility(View.VISIBLE);
        // recyclerView.setVisibility(View.GONE);
        fab_add_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.navigateToAddSale(SaleType.FIRST_PAY, null);
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
        Call<ResponseModel<ArrayList<MySaleModel>>> call = service.getMySales(input);

        call.enqueue(new Callback<ResponseModel<ArrayList<MySaleModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<MySaleModel>>> call, Response<ResponseModel<ArrayList<MySaleModel>>> response) {
                progress_search.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    System.out.println("loading...." + response.body().getResponse());
                    if (response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                        System.out.println("loading...." + response.body().getResponse().size());
                        soldItemList.clear();
                        for (MySaleModel saleResponseModel : response.body().getResponse()) {
                            String subtitle = ("Falta pagar " + Double.toString(saleResponseModel.getTotalPrice()).replace(".0", "") + "MT em prestações de " + Double.toString(saleResponseModel.getMissing()).replace(".0", "") + "MT");
                            boolean containsPrest = true;

                            if (saleResponseModel.getMissing() == 0) {
                                subtitle = "Pagou um total de " + saleResponseModel.getTotalPrice() + "MT";
                                containsPrest = false;
                            }

                            SoldItem soldItem = new SoldItem(saleResponseModel.getId(),
                                    "Venda para " + saleResponseModel.getProduct().getName(),
                                    subtitle,
                                    saleResponseModel.getCreated_at(),
                                    Constants.getInstance().API + saleResponseModel.getProduct().getImage(),
                                    saleResponseModel.getMissing(),
                                    containsPrest
                            );
                            soldItemList.add(soldItem);
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<MySaleModel>>> call, Throwable t) {
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