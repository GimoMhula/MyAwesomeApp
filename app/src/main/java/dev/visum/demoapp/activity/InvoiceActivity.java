package dev.visum.demoapp.activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

import dev.visum.demoapp.R;
import dev.visum.demoapp.model.BaseActivity;

public class InvoiceActivity extends BaseActivity {
    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
    final String fileName="Test.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        
        Intent intent = getIntent();

        String id =intent.getStringExtra("invoice_id");


        WebView  webView = findViewById(R.id.webview_invoice);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
         webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://3.10.223.89/final_invoice/"+id);
        webView.setWebViewClient(new WebViewClient());



    }




}