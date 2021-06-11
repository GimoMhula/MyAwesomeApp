package dev.visum.demoapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import dev.visum.demoapp.R;


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
}
