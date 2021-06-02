package dev.visum.demoapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterListSoldItems;
import dev.visum.demoapp.model.SoldItem;

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
    private AdapterListSoldItems mAdapter;
    private TextView empty_view;
    private ExtendedFloatingActionButton fab_add_sale;


    ListSoldItemsFragment.OnListSoldItemsSelectedListener callback;

    public void setCallback(ListSoldItemsFragment.OnListSoldItemsSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnListSoldItemsSelectedListener {
        public void navigateToAddSale();
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
        empty_view = parent_view.findViewById(R.id.empty_view);
        fab_add_sale = parent_view.findViewById(R.id.fab_add_sale);
        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                fab_add_sale.shrink();
            }
        }, 500);

        List<SoldItem> soldItemList = new ArrayList<>();
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));
        soldItemList.add(new SoldItem("a1", "Fogao X", "falta 3 prestações de 1000mt", "2021-05-25", "https://picsum.photos/200"));

        //set data and list adapter
        mAdapter = new AdapterListSoldItems(getContext(), soldItemList, R.layout.item_sold_item_horizontal);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSoldItems.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SoldItem obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });


        // empty_view.setVisibility(View.VISIBLE);
        // recyclerView.setVisibility(View.GONE);
        fab_add_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.navigateToAddSale();
            }
        });

    }
}