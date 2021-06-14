package dev.visum.demoapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.jakewharton.rxbinding4.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterSaleCustomerFiltered;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SurveyFragmentMenu extends Fragment {
    private FragmentActivity myContext;
    private SurveyViewModel galleryViewModel;
    private TextView next_text;

    private enum State {
        SHIPPING,
        PAYMENT,
        CONFIRMATION
    }

    State[] array_state = new State[]{State.SHIPPING, State.PAYMENT, State.CONFIRMATION};
    private View line_first, line_second;
    private ImageView image_shipping, image_payment, image_confirm;
    private TextView tv_shipping, tv_payment, tv_confirm;
    private int idx_state = 0;
    private AutoCompleteTextView act_client;
    private AdapterSaleCustomerFiltered customerFilteredAdapter;
    private ArrayList<CustomerResponseModel> customersRespList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(SurveyViewModel.class);
        View root = inflater.inflate(R.layout.activity_survey_step, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        initComponent(root);
        return root;
    }
    private void initComponent(View v) {
        act_client = v.findViewById(R.id.act_client);
        line_first = (View) v.findViewById(R.id.line_first);
        line_second = (View) v.findViewById(R.id.line_second);
        image_shipping = (ImageView) v.findViewById(R.id.image_shipping);
        image_payment = (ImageView) v.findViewById(R.id.image_payment);
        image_confirm = (ImageView) v.findViewById(R.id.image_confirm);

        tv_shipping = (TextView) v.findViewById(R.id.tv_shipping);
        tv_payment = (TextView) v.findViewById(R.id.tv_payment);
        tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        next_text=(TextView) v.findViewById(R.id.lyt_next_text);
        displayFragment(array_state[idx_state]);

        image_payment.setColorFilter(getResources().getColor(R.color.grey_10), PorterDuff.Mode.SRC_ATOP);
        image_confirm.setColorFilter(getResources().getColor(R.color.grey_10), PorterDuff.Mode.SRC_ATOP);

        (v.findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idx_state == array_state.length - 1) return;
                idx_state++;
                if(idx_state==1){
                    next_text.setText("Submeter");
                    if(next_text.getText().toString().equals("Submeter")){

                    }
                }if(idx_state==2){
                    showCustomDialog();
                }else {
                    next_text.setText("Proximo");
                }

                displayFragment(array_state[idx_state]);
            }
        });


        (v.findViewById(R.id.lyt_previous)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idx_state < 1) return;
                idx_state--;
                displayFragment(array_state[idx_state]);
            }
        });


        try {
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
        }catch (IllegalArgumentException e){
            Log.d("TAG", "initComponent: "+e.getMessage());
        }

    }

    private void displayFragment(State state) {
        FragmentManager fragmentManager = myContext.getSupportFragmentManager(); //If using fragments from support v4
//        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        refreshStepTitle();

        if (state.name().equalsIgnoreCase(State.SHIPPING.name())) {
            fragment = new SurveyFragment();
            tv_shipping.setTextColor(getResources().getColor(R.color.grey_90));
            image_shipping.clearColorFilter();
        } else if (state.name().equalsIgnoreCase(State.PAYMENT.name())) {
            fragment = new SurveyQuestionFragment();
            line_first.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            image_shipping.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            image_payment.clearColorFilter();
            tv_payment.setTextColor(getResources().getColor(R.color.grey_90));
            //TODO

        } else if (state.name().equalsIgnoreCase(State.CONFIRMATION.name())) {
//            fragment = new FragmentConfirmation();
            line_second.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            image_payment.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            image_confirm.clearColorFilter();
            tv_confirm.setTextColor(getResources().getColor(R.color.grey_90));
        }

        if (fragment == null) return;
        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.commit();
    }

    private void refreshStepTitle() {
        tv_shipping.setTextColor(getResources().getColor(R.color.grey_20));
        tv_payment.setTextColor(getResources().getColor(R.color.grey_20));
        tv_confirm.setTextColor(getResources().getColor(R.color.grey_20));
    }
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    @Override
    public void onAttach(@NonNull Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
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
                            Snackbar.make(getView(), getString(R.string.error_sale_fragment), Snackbar.LENGTH_LONG).show();
                        }
                        customerFilteredAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<List<CustomerResponseModel>>> call, Throwable t) {
                        customerFilteredAdapter.clear();
//                        customerId = "";
                        Snackbar.make(getView(), getString(R.string.error_sale_fragment_fail), Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
