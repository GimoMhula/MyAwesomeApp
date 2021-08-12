package dev.visum.demoapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.asksira.webviewsuite.WebViewSuite;
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
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment {

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
    WebViewSuite myWebView;
    private WebView mWebView;

    public FormFragment() {
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
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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
        parent_view = inflater.inflate(R.layout.content_inbox_webview, container, false);
        init();
        return parent_view;
    }


    // init
    private void init() {
        loginBtn = parent_view.findViewById(R.id.loginBtn);
        passwordEditText = parent_view.findViewById(R.id.passwordEditText);
        emailEditText = parent_view.findViewById(R.id.emailEditText);
        progressDialog = new ProgressDialog(getActivity());
/*
        myWebView =parent_view.findViewById(R.id.webview_inbox);
//        myWebView.startLoading(getResources().getString(R.string.txapita_home_web_page_url));


        myWebView.startLoading("forms.gle/R4bMsp65xwZongPTA");
        myWebView.interfereWebViewSetup(webView -> {
            WebSettings webSettings = webView.getSettings();
            //Change your WebView settings here
            webSettings.setLoadsImagesAutomatically(true);
//            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setAppCacheEnabled(true); // Disable while debugging
        });

*/
        mWebView = (WebView) parent_view.findViewById(R.id.webview_inbox);
String url="https://docs.google.com/forms/d/e/1FAIpQLSdP9KQMYPiTwZ0zSL5xXWJhecPxICiiQZgH_zInLRtXAnnFiw/viewform?usp=sf_link";

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity( intent );
                return true;
            }
        });
        mWebView.loadUrl(url);
    }
    //end

    // login action

    private boolean isValid(CharSequence s, @NotNull Pattern pattern) {
        return pattern.matcher(s).matches();
    }
}