package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterListSurveyCard;
import dev.visum.demoapp.adapter.AdapterSaleCustomerFiltered;
import dev.visum.demoapp.adapter.AdapterSaleProductFiltered;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.BaseActivity;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.SurveyModel;
import dev.visum.demoapp.model.SurveyResponseModel;
import dev.visum.demoapp.model.ResponseModel;
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
 * Use the {@link SurveyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveyFragment extends Fragment {

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
    private AdapterListSurveyCard mAdapter;
    private AppCompatActivity activity;
    private TextView empty_view;
    private List<SurveyModel> items = new ArrayList<>();
    private FragmentActivity myContext;


    public SurveyFragment() {
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
    public static SurveyFragment newInstance(String param1, String param2) {
        SurveyFragment fragment = new SurveyFragment();
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

         //initToolbar();
        initComponent();

        return parent_view;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) parent_view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
       activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Inqueritos");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(getActivity());
    }

    private void initComponent() {
        empty_view = parent_view.findViewById(R.id.empty_view);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();

        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<ResponseModel<List<SurveyResponseModel>>> call = service.getSurveysList();

        call.enqueue(new Callback<ResponseModel<List<SurveyResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<SurveyResponseModel>>> call, Response<ResponseModel<List<SurveyResponseModel>>> response) {

                Log.d("tag", "onResponse: "+response.toString());
                Log.d("tag", "onResponse Message: "+response.message());
                Log.d("tag", "onResponse Body: "+response.body());
                if (response.isSuccessful()) {
                    empty_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    for (SurveyResponseModel surveyResponseModel : response.body().getResponse()) {
                        items.add(new SurveyModel(surveyResponseModel.getId(),surveyResponseModel.getTitle(),surveyResponseModel.getDescription(),surveyResponseModel.getStatus()+""));
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel<List<SurveyResponseModel>>> call, Throwable t) {
                Log.d("tag", "onFailure Body: "+t.getStackTrace());
                Log.d("tag", "onFailure Body: "+t.getMessage());
                Log.d("tag", "onFailure Body: "+t.getCause());
                progressDialog.dismiss();

            }
        });

        recyclerView = parent_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getContext(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //set data and list adapter
        mAdapter = new AdapterListSurveyCard(getContext(), items);
        recyclerView.setAdapter(mAdapter);



        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSurveyCard.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SurveyModel obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();

                SurveyQuestionFragment fragment=new SurveyQuestionFragment();
                Log.d("TAG", "passVal"+obj.id+"");
                goToFragment(fragment.newInstance(obj.id+"",""));

            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterListSurveyCard.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, SurveyModel obj, MenuItem item) {
                Snackbar.make(parent_view, obj.title + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        // TODO: missing refresh list and load more items
    }
    private void goToFragment(Fragment f) {
//        ll_search_bar.setVisibility(View.GONE);
//
//        if (toolbar.getMenu().findItem(R.id.action_logout) != null) {
//            toolbar.getMenu().findItem(R.id.action_logout).setVisible(false);
//        }
//
//        if (menu_search != null) {
//            menu_search.setVisible(true);
//            toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
//        }
        FragmentTransaction fragmentTransaction = BaseActivity.getInstance().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_view, f);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.menu_cart_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}