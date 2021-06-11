package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterListSoldItems;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.MySaleKeyModel;
import dev.visum.demoapp.model.MySaleModel;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.model.SoldItem;
import dev.visum.demoapp.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListSoldItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSoldItemsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private View parent_view;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private AdapterListSoldItems mAdapter;
    private TextView empty_view;
    private ExtendedFloatingActionButton fab_add_sale;
    private List<SoldItem> soldItemList = new ArrayList<>();


    ListSoldItemsFragment.OnListSoldItemsSelectedListener callback;

    public void setCallback(ListSoldItemsFragment.OnListSoldItemsSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnListSoldItemsSelectedListener {
        public void navigateToAddSale(SaleType saleType, SoldItem soldItem);
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
    }

    private void init() {
        soldItemList = new ArrayList<>();
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

       GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
       Call<MySaleKeyModel> call = service.getMySales();

       call.enqueue(new Callback<MySaleKeyModel>() {
           @Override
           public void onResponse(Call<MySaleKeyModel> call, Response<MySaleKeyModel> response) {
               if (response.isSuccessful()) {
                   empty_view.setVisibility(View.GONE);
                   recyclerView.setVisibility(View.VISIBLE);

                   for (ArrayList<Object> saleResponseModel : response.body().getSales()) {

                       for (Object saleData : saleResponseModel) {
                           final ObjectMapper mapper = new ObjectMapper();
                           final Map<String, Object> convertValueSale = mapper.convertValue(saleData, new TypeReference<Map<String, Object>>() {});

                           if (convertValueSale != null) {
                               MySaleModel mySaleModel = convertMapToMySaleModel(convertValueSale);

                               String subtitle = ("Falta " + mySaleModel.getTotalPrest()).replace(".0", "") + " prestações de " + mySaleModel.getRemain() + "MT";
                               boolean containsPrest = true;

                               if (mySaleModel.getRemain() == 0.0) {
                                   subtitle = "Pagou um total de " + mySaleModel.getTotal() + "MT";
                                   containsPrest = false;
                               }

                               SoldItem soldItem = new SoldItem(mySaleModel.getId() + "",
                                       "Venda para " + mySaleModel.getName(),
                                       subtitle,
                                       mySaleModel.getSaleDate(),
                                       Constants.getInstance().API + mySaleModel.getImage(),
                                       mySaleModel.getRemain(),
                                       containsPrest
                               );
                               soldItemList.add(soldItem);
                               break;
                           }
                       }
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
           public void onFailure(Call<MySaleKeyModel> call, Throwable t) {
               progressDialog.dismiss();
               Snackbar.make(getView(), getString(R.string.error_get_sales), Snackbar.LENGTH_LONG).show();
           }
       });

        //set data and list adapter

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

    private MySaleModel convertMapToMySaleModel(Map<String, Object> convertValueSale) {
        return new MySaleModel(
                (double) convertValueSale.get("id"),
                (double) convertValueSale.get("total"),
                (double) convertValueSale.get("remain"),
                convertValueSale.get("image").toString(),
                convertValueSale.get("name").toString(),
                (double) convertValueSale.get("quant"),
                (double) convertValueSale.get("price"),
                (double) convertValueSale.get("completed"),
                convertValueSale.get("warehouse").toString(),
                convertValueSale.get("saleDate").toString(),
                (double) convertValueSale.get("totalPrest")
        );
    }

    /*
    *
            <LinearLayout
                android:id="@+id/ll_qty"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">
                <View
                    android:layout_width="0dp"
                    android:layout_height="@dimen/spacing_mlarge" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <com.google.android.material.textfield.TextInputLayout
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="2">

                        <AutoCompleteTextView
                            android:id="@+id/act_qty"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:hint="@string/act_qty"
                            android:maxLines="1"
                            android:singleLine="true"
                            android:inputType="number"
                            android:focusable="false" />

                    </com.google.android.material.textfield.TextInputLayout>
                </LinearLayout>

            </LinearLayout>
*/
}