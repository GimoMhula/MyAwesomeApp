package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import dev.visum.demoapp.R;
import dev.visum.demoapp.activity.MainActivity;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.UserAgentBodyModel;
import dev.visum.demoapp.model.UserAgentResponseModel;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

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
    private FloatingActionButton loginBtn;
    private TextInputEditText passwordEditText, emailEditText;
    private final Pattern sPatternPassword = Pattern.compile(Constants.getInstance().PASSWORD_REGEX);
    private final Pattern sPatternEmail = Pattern.compile(Constants.getInstance().EMAIL_REGEX);

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        parent_view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return parent_view;
    }


    // init
    private void init() {
        loginBtn = parent_view.findViewById(R.id.loginBtn);
        passwordEditText = parent_view.findViewById(R.id.passwordEditText);
        emailEditText = parent_view.findViewById(R.id.emailEditText);
        progressDialog = new ProgressDialog(getActivity());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
    //end

    // login action
    private void loginUser() {
      //  getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        progressDialog.setMessage(getString(R.string.sign_in_msg));
        progressDialog.show();
        loginBtn.setAlpha(0f);
       // UserAgentResponseModel userAgent=new UserAgentResponseModel("5","Gimo","gimo.mhula@gmail.com","sdfsdfsdf");
//        KeyStoreLocal.getInstance(getActivity()).setUser(userAgent);

        if (passwordEditText.getText().toString().trim() != null && !passwordEditText.getText().toString().trim().isEmpty() && isValid(emailEditText.getText().toString().trim(), sPatternEmail)) { // isValid(passwordEditText.getText(), sPatternPassword)
            GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);

            Call<ResponseModel<UserAgentResponseModel>> call = service.loginUser(Tools.convertObjToMap(new UserAgentBodyModel(emailEditText.getText().toString().trim(), passwordEditText.getText().toString())));
            call.enqueue(new Callback<ResponseModel<UserAgentResponseModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<UserAgentResponseModel>> call, Response<ResponseModel<UserAgentResponseModel>> response) {
                    progressDialog.dismiss();
                    loginBtn.setAlpha(1f);
                    Log.d("Login", "response.isSuccessful: "+response.isSuccessful());
                    if (response.code() > 100 && response.code() < 399 && response.body() != null && response.body().getResponse() != null && response.body().getResponse().getToken() != null) {
                        Snackbar.make(parent_view, getString(R.string.success_login_msg), Snackbar.LENGTH_SHORT).show();
                        // Save user model
                        UserAgentResponseModel userAgent = response.body().getResponse();
                        userAgent.setToken("Bearer " + userAgent.getToken());
                        KeyStoreLocal.getInstance(getActivity()).setUser(userAgent);
                        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        Snackbar.make(parent_view, getString(R.string.user_credentials_invalid_msg), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<UserAgentResponseModel>> call, Throwable t) {
                    Log.d("Login", "onFailure: "+t.getMessage());
                    Log.d("Login", "onFailure: "+t.toString());
                    Log.d("Login", "onFailure: "+call.toString());
                    progressDialog.dismiss();
                    loginBtn.setAlpha(1f);
                    Snackbar.make(parent_view, getString(R.string.failed_sign_in_msg), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            progressDialog.dismiss();
            loginBtn.setAlpha(1f);
            Snackbar.make(parent_view, getString(R.string.wrong_text_input), Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isValid(CharSequence s, @NotNull Pattern pattern) {
        return pattern.matcher(s).matches();
    }
}