package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterGridProductCard;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.ProductModel;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;
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
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private AdapterGridProductCard mAdapter;
    private AppCompatActivity activity;
    private TextView empty_view;
    private List<ProductModel> items = new ArrayList<>();

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista de Produtos");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
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
                        items.add(new ProductModel(productResponseModel.getName(), Double.toString(productResponseModel.getPrice()) + "MT", Constants.getInstance().API + productResponseModel.getImage()));
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
                Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
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
}