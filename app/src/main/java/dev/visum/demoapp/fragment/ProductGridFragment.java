 package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterGridProductCard;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.DataUpdateActivityToFragment;
import dev.visum.demoapp.model.ProductModel;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductGridFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private View parent_view;
    private ProgressBar progress_search;
    private ProgressDialog progressDialog;
    private AutoCompleteTextView act_search;

    private RecyclerView recyclerView;
    private AdapterGridProductCard mAdapter;
    private AppCompatActivity activity;
    private TextView empty_view;
    private List<ProductModel> items = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ProductGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductGridFragment newInstance(String param1, String param2) {
        ProductGridFragment fragment = new ProductGridFragment();
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
        activity = ((AppCompatActivity)getActivity());
        parent_view = inflater.inflate(R.layout.fragment_product_grid, container, false);

        initToolbar();
        initComponent();

        return parent_view;
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.list_products));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    private void initComponent() {
        act_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.act_search);
        progress_search = getActivity().findViewById(R.id.toolbar).findViewById(R.id.progress_search);
        empty_view = parent_view.findViewById(R.id.empty_view);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();

        recyclerView = parent_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getContext(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //set data and list adapter
        mAdapter = new AdapterGridProductCard(getContext(), items);
        recyclerView.setAdapter(mAdapter);


        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<ResponseModel<List<ProductResponseModel>>> call = service.getProductsList();

        call.enqueue(new Callback<ResponseModel<List<ProductResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ProductResponseModel>>> call, Response<ResponseModel<List<ProductResponseModel>>> response) {
                if (response.isSuccessful()) {
                    empty_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    for (ProductResponseModel productResponseModel : response.body().getResponse()) {
                        items.add(new ProductModel(productResponseModel.getName(), productResponseModel.getCategory().getName(), Double.toString(productResponseModel.getPrice()) + "MT", Constants.getInstance().API + productResponseModel.getImage()));
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ProductResponseModel>>> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(getView(), getString(R.string.error_get_products), Snackbar.LENGTH_LONG).show();
            }
        });

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridProductCard.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ProductModel obj, int position) {
//                Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterGridProductCard.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, ProductModel obj, MenuItem item) {
                Snackbar.make(parent_view, obj.title + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        // TODO: missing refresh list and load more items
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_products, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search_products) {
            if (!disposable.isDisposed() && act_search != null) {
                debounce(act_search, searchQueryProducts());
            }
        }
        return super.onOptionsItemSelected(item);
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

    private void fetchProducts(String input) {
        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<ResponseModel<List<ProductResponseModel>>> call = service.getProductFilteredList(input);

        call.enqueue(new Callback<ResponseModel<List<ProductResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ProductResponseModel>>> call, Response<ResponseModel<List<ProductResponseModel>>> response) {
                progress_search.setVisibility(View.GONE);
                items.clear();
                if (response.isSuccessful() && response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                    empty_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    for (ProductResponseModel productResponseModel : response.body().getResponse()) {
                        items.add(new ProductModel(productResponseModel.getName(), productResponseModel.getCategory() == null ? "" : productResponseModel.getCategory().getName(), Double.toString(productResponseModel.getPrice()) + "MT", Constants.getInstance().API + productResponseModel.getImage()));
                    }

                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ProductResponseModel>>> call, Throwable t) {
                progress_search.setVisibility(View.GONE);
                Snackbar.make(getView(), getString(R.string.error_get_products), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQueryProducts() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                progress_search.setVisibility(View.VISIBLE);
                fetchProducts(textViewTextChangeEvent.getText().toString());
            }

            @Override
            public void onError(Throwable e) {
                progress_search.setVisibility(View.GONE);
                e.printStackTrace();
                Snackbar.make(getView(), getString(R.string.failed_get_products), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}