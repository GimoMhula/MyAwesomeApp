package dev.visum.demoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterGridItemCategory;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.fragment.AddSaleFragment;
import dev.visum.demoapp.fragment.CustomerSignSaleFragment;
import dev.visum.demoapp.fragment.ListSoldItemsFragment;
import dev.visum.demoapp.fragment.ProductGridFragment;
import dev.visum.demoapp.fragment.SurveyFragmentMenu;
import dev.visum.demoapp.model.BaseActivity;
import dev.visum.demoapp.model.DataUpdateActivityToFragment;
import dev.visum.demoapp.model.ItemCategory;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.model.SoldItem;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements AddSaleFragment.OnAddSaleSelectedListener
        , ListSoldItemsFragment.OnListSoldItemsSelectedListener
        , CustomerSignSaleFragment.OnCustomerSignSaleListener {

    private AppCompatActivity main_activity;
    private RecyclerView recyclerView;
    private AdapterGridItemCategory mAdapter;
    private ImageButton bt_clear;
    private LinearLayout ll_search_bar;
    private MenuItem menu_search;
    private Toolbar toolbar;
    private IntentIntegrator qrScan;
    private View parentLayout;
    DataUpdateActivityToFragment callback;

    public void setCallback(DataUpdateActivityToFragment callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        main_activity = this;
        parentLayout = findViewById(R.id.parent_view);
        qrScan = new IntentIntegrator(this);
        bt_clear = findViewById(R.id.bt_clear);
        ll_search_bar = findViewById(R.id.ll_search_bar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_search_bar.setVisibility(View.GONE);
                menu_search.setVisible(true);
                toolbar.setBackgroundColor(getColor(R.color.colorPrimary));

                if (menu_search.getItemId() == R.id.menu_search_sales) {
                    toolbar.getMenu().findItem(R.id.menu_scan_qr_sales).setVisible(true);
                    toolbar.getMenu().findItem(R.id.menu_clear_qr_code_sale).setVisible(false);
                }
            }
        });


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

        ItemCategory itemCategorySurvey = new ItemCategory();
        itemCategorySurvey.image = R.drawable.ic_image_black_24dp;
        itemCategorySurvey.title = "Inqueritos";
        itemCategorySurvey.imageDrw = AppCompatResources.getDrawable(this, itemCategorySurvey.image);
        items.add(itemCategorySurvey);

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
                   case "Inqueritos":
                       goToFragment(new SurveyFragmentMenu());
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
        ll_search_bar.setVisibility(View.GONE);

        if (toolbar.getMenu().findItem(R.id.action_logout) != null) {
            toolbar.getMenu().findItem(R.id.action_logout).setVisible(false);
        }

        if (menu_search != null) {
            menu_search.setVisible(true);
            toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        }
        FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_view, f);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString((R.string.dashboard_title)));
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (toolbar.getTitle() == getString(R.string.dashboard_title)) {
            getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String oldTitle = ((AppCompatActivity) getInstance()).getSupportActionBar().getTitle().toString();
                if (oldTitle.equalsIgnoreCase(getString(R.string.list_sold_items_title))
                        || oldTitle.equalsIgnoreCase(getString(R.string.list_products))) {
                    dashboardHeader();
                }
                this.onBackPressed();
                break;
            case R.id.menu_search_sales:
                menu_search = item;
                item.setVisible(false);
                toolbar.getMenu().findItem(R.id.menu_scan_qr_sales).setVisible(false);
                toolbar.getMenu().findItem(R.id.menu_clear_qr_code_sale).setVisible(false);
                ll_search_bar.setVisibility(View.VISIBLE);
                toolbar.setBackgroundColor(getColor(R.color.mdtp_white));
                break;
            case R.id.menu_search_products:
                menu_search = item;
                item.setVisible(false);
                ll_search_bar.setVisibility(View.VISIBLE);
                toolbar.setBackgroundColor(getColor(R.color.mdtp_white));
                break;
            case R.id.action_logout:
                KeyStoreLocal.getInstance(getInstance().getBaseContext()).logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.menu_scan_qr_sales:
                qrScan.initiateScan();
                break;
            case R.id.menu_clear_qr_code_sale:
                hideClearAndShowQR();
                callback.pubData("");
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkProductInStock(String productName) {
        GetDataService service = MozCarbonAPI.getRetrofit(this).create(GetDataService.class);
        Call<ResponseModel<List<ProductResponseModel>>> call = service.getProductFilteredList(productName);

        call.enqueue(new Callback<ResponseModel<List<ProductResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ProductResponseModel>>> call, Response<ResponseModel<List<ProductResponseModel>>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getResponse().isEmpty() && response.body().getResponse() instanceof ArrayList) {
                        if (response.body().getResponse().get(0).getQty() > 0) {
                            Tools.alertDialogSimpleOk(main_activity, getString(R.string.product_not_sold));
                        } else {
                            showClearAndHideQR();
                            callback.pubData(response.body().getResponse().get(0).getName());
                        }
                    } else {
                        Tools.alertDialogSimpleOk(main_activity, getString(R.string.product_do_not_exists));
                    }
                } else {
                    Tools.alertDialogSimpleOk(main_activity, getString(R.string.product_not_found));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ProductResponseModel>>> call, Throwable t) {
                t.printStackTrace();
                Snackbar.make(parentLayout, getString(R.string.error_sale_fragment_fail), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void dashboardHeader() {
        ((AppCompatActivity) getInstance()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle(getString(R.string.dashboard_title));

        if (toolbar.getMenu().findItem(R.id.action_logout) != null) {
            toolbar.getMenu().findItem(R.id.action_logout).setVisible(true);
        }
    }

    private void hideClearAndShowQR() {
        toolbar.getMenu().findItem(R.id.menu_scan_qr_sales).setVisible(true);
        toolbar.getMenu().findItem(R.id.menu_clear_qr_code_sale).setVisible(false);
    }

    private void showClearAndHideQR() {
        toolbar.getMenu().findItem(R.id.menu_scan_qr_sales).setVisible(false);
        toolbar.getMenu().findItem(R.id.menu_clear_qr_code_sale).setVisible(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(parentLayout, getString(R.string.no_data_available), Snackbar.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    checkProductInStock(obj.getString("serie"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(parentLayout, getString(R.string.error_get_qr_code_data), Snackbar.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
    public void renderWebView(String invoiceUrl) {
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
        // this.setContentView(webView);
        webView.loadUrl(invoiceUrl);
        // webView.loadUrl(Constants.getInstance().API + Constants.getInstance().INVOICE_PATH + "/" + saleId);
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

        getInstance().getSupportFragmentManager().popBackStack(getInstance().getSupportFragmentManager().getFragments().get(0).getId(), 0);
        // Intent intent = getIntent();
        // finish();
        // startActivity(intent);
        // Save the job object for later status checking
        // printJobs.add(printJob);
    }
}
