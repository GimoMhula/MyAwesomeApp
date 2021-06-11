package dev.visum.demoapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterGridItemCategory;
import dev.visum.demoapp.data.DataGenerator;
import dev.visum.demoapp.fragment.AddSaleFragment;
import dev.visum.demoapp.fragment.CustomerSignSaleFragment;
import dev.visum.demoapp.fragment.ListSoldItemsFragment;
import dev.visum.demoapp.fragment.ProductGridFragment;
import dev.visum.demoapp.model.BaseActivity;
import dev.visum.demoapp.model.ItemCategory;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.model.SoldItem;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;

public class MainActivity extends BaseActivity implements AddSaleFragment.OnAddSaleSelectedListener
        , ListSoldItemsFragment.OnListSoldItemsSelectedListener
        , CustomerSignSaleFragment.OnCustomerSignSaleListener {

    private RecyclerView recyclerView;
    private AdapterGridItemCategory mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        List<ItemCategory> items = new ArrayList<>();
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.image = R.drawable.ic_image_black_24dp;
        itemCategory.title = "Produtos";
        itemCategory.imageDrw = AppCompatResources.getDrawable(this, itemCategory.image);
        items.add(itemCategory);

        ItemCategory itemCategorySales = new ItemCategory();
        itemCategorySales.image = R.drawable.ic_image_black_24dp;
        itemCategorySales.title = "Vendas";
        itemCategorySales.imageDrw = AppCompatResources.getDrawable(this, itemCategorySales.image);
        items.add(itemCategorySales);
        //set data and list dev.visum.demoapp.adapter
        mAdapter = new AdapterGridItemCategory(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridItemCategory.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemCategory obj, int position) {
               // Toast.makeText(MainActivity.this,  "Item " + obj.title + " clicked", Toast.LENGTH_SHORT).show();

               switch (obj.title) {
                   case "Produtos":
                       goToFragment(new ProductGridFragment());
                       break;
                   case "Vendas":
                       goToFragment(new ListSoldItemsFragment()); // goToFragment(new AddSaleFragment());
                       break;
                   default:Bundle bundle = new Bundle();
                       bundle.putString("content",obj.title);
                       startActivity(ContentActivity.class,bundle,null);
                       break;
               }
            }
        });
    }

    private void goToFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_view, f);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
        getSupportActionBar().setTitle("Dashboard");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof AddSaleFragment) {
            AddSaleFragment addSaleFragment = (AddSaleFragment) fragment;
            addSaleFragment.setCallback(this);
        } else if (fragment instanceof ListSoldItemsFragment) {
            ListSoldItemsFragment listSoldItemsFragment = (ListSoldItemsFragment) fragment;
            listSoldItemsFragment.setCallback(this);
        } else if (fragment instanceof CustomerSignSaleFragment) {
            CustomerSignSaleFragment customerSignSaleFragment = (CustomerSignSaleFragment) fragment;
            customerSignSaleFragment.setCallback(this);
        }
    }

    // fragments interfaces

    @Override
    public void navigateToCustomerSignSale(Map<String, String> addSaleMap, SaleType saleType) {
        goToFragment(new CustomerSignSaleFragment().newInstance(addSaleMap, saleType));
    }

    @Override
    public void navigateToAddSale(SaleType saleType, SoldItem soldItem) {
        goToFragment(new AddSaleFragment().newInstance(saleType, soldItem));
    }

    @Override
    public void renderWebView(String saleId) {
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("page finished loading " + url);
                createWebPrintJob(view);
            }
        });
        this.setContentView(webView);
        webView.loadUrl(Constants.getInstance().API + Constants.getInstance().INVOICE_PATH + "/" + saleId);
    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        // printJobs.add(printJob);
    }

}
