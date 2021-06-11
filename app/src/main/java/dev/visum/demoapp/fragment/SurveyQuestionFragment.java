package dev.visum.demoapp.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterListSurveyCard;
import dev.visum.demoapp.adapter.AdapterListSurveyQuestions;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SurveyModel;
import dev.visum.demoapp.model.SurveyQuestionResponseModel;
import dev.visum.demoapp.model.SurveyQuestionResponseModel;
import dev.visum.demoapp.model.SurveyQuestionsModel;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SurveyQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveyQuestionFragment extends Fragment {

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
    private AdapterListSurveyQuestions mAdapter;
    private AppCompatActivity activity;
    private TextView empty_view;
    private List<SurveyQuestionsModel> items = new ArrayList<>();
    private AutoCompleteTextView classification;

    public SurveyQuestionFragment() {
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
    public static SurveyQuestionFragment newInstance(String param1, String param2) {
        SurveyQuestionFragment fragment = new SurveyQuestionFragment();
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

        // initToolbar();
        initComponent();

        return parent_view;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) parent_view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        /*activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Produtos");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        Tools.setSystemBarColor(getActivity());
    }

    private void initComponent() {
        empty_view = parent_view.findViewById(R.id.empty_view);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();


        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<ResponseModel<List<SurveyQuestionResponseModel>>> call = service.getSurveyQuestionsList("1");

        call.enqueue(new Callback<ResponseModel<List<SurveyQuestionResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<SurveyQuestionResponseModel>>> call, Response<ResponseModel<List<SurveyQuestionResponseModel>>> response) {

                Log.d("tag", "onResponse: "+response.toString());
                Log.d("tag", "onResponse Message: "+response.message());
                Log.d("tag", "onResponse Body: "+response.body());
                if (response.isSuccessful()) {
                    empty_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    for (SurveyQuestionResponseModel SurveyQuestionResponseModel : response.body().getResponse()) {
                        items.add(new SurveyQuestionsModel(SurveyQuestionResponseModel.getId(),SurveyQuestionResponseModel.getTitle(),SurveyQuestionResponseModel.getDescription(),SurveyQuestionResponseModel.getType()));
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel<List<SurveyQuestionResponseModel>>> call, Throwable t) {
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
        mAdapter = new AdapterListSurveyQuestions(getContext(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSurveyQuestions.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SurveyQuestionsModel obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterListSurveyQuestions.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, SurveyQuestionsModel obj, MenuItem item) {
                Snackbar.make(parent_view, obj.title + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        // TODO: missing refresh list and load more items
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        classification = view.findViewById(R.id.autoComplete);

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Ma", "Boa","Excelente"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, R.id.tvItem, arrayList);

//        classification.setAdapter(adapter);

        /*
        classification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Escolha ")) {


                } else{

                }

//                    item4 = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.menu_cart_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}