package dev.visum.demoapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import dev.visum.demoapp.R;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.model.AddSaleResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.model.SaleType;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.ProgressRequestBody;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerSignSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerSignSaleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Map<String, String> addSaleMap;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //
    private View parent_view;
    private ProgressDialog progressDialog;
    private ProgressBar progressBarSignature;
    Button clear_sign_btn, sign_btn;
    LinearLayout mContent;
    signature mSignature;
    Bitmap bitmap;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    //File DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY +"/"+ pic_name + ".png";

    private SaleType saleType;

    OnCustomerSignSaleListener callback;

    public void setCallback(OnCustomerSignSaleListener callback) {
        this.callback = callback;
    }

    public interface OnCustomerSignSaleListener {
        public void renderWebView(String invoiceUrl);
    }

    public CustomerSignSaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param addSaleMap Parameter add sale map.
     * @param saleType Parameter pass sale type - full, first prest or next prest
     * @return A new instance of fragment CustomerSignSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerSignSaleFragment newInstance(Map<String, String> addSaleMap, SaleType saleType) {
        CustomerSignSaleFragment fragment = new CustomerSignSaleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) addSaleMap);
        args.putSerializable(ARG_PARAM2, saleType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addSaleMap = (Map<String, String>) getArguments().getSerializable(ARG_PARAM1);
            saleType = (SaleType) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent_view = inflater.inflate(R.layout.fragment_customer_sign_sale, container, false);
        initToolbar();
        init();
        return parent_view;
    }

    //
    private void initToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Assinatura da Venda");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        progressDialog = new ProgressDialog(getActivity());
        progressBarSignature = parent_view.findViewById(R.id.progressBarSignature);
        mContent = parent_view.findViewById(R.id.linearLayout);
        clear_sign_btn = parent_view.findViewById(R.id.clear_sign_btn);
        sign_btn = parent_view.findViewById(R.id.submit_signature_btn);
        mSignature = new signature(getContext(), null);

        changeSignSaveBtn(false);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        clear_sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignature.clear();
                changeSignSaveBtn(false);
            }
        });

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory = new File(DIRECTORY);
                if (!directory.exists()){
                    directory.mkdir();
//                     If you require it to make the entire directory path including parents,
//                     use directory.mkdirs(); here instead.
                }

                File signFile = mSignature.save(mContent, StoredPath);

                if (signFile != null) {
                    upload(signFile);
                }else{
                    signFile=null;
                }
            }
        });
    }

    private void upload(File file) {
        Log.d("Venda", "upload: "+file.toString());
        progressBarSignature.setVisibility(View.VISIBLE);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "Image", new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                progressBarSignature.setProgress(percentage);
            }

            @Override
            public void onError() {
                Log.d("Venda", "onError: "+"Error");
                progressBarSignature.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                Log.d("Venda", "onFinish: "+"Finished");
                progressBarSignature.setVisibility(View.GONE);
            }
        });
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("signature", file.getName(), fileBody);

        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);
        Call<JsonObject> call = service.uploadDigitalSignatureImage(filePart, addSaleMap.get("customer_id"));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Venda", "response.isSuccessful(): "+response);
                    processSale();
                } else {
                    System.out.println("Failed " + response.message());
                    Log.d("Venda", "Failed: "+response.message());
                    try {
                        System.out.println("Failed " + response.errorBody().string().toString());
                        Log.d("Venda", "Failed: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBarSignature.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Venda", "onFailure: "+t.getMessage());
                t.printStackTrace();
                progressBarSignature.setVisibility(View.GONE);
            }
        });
    }

    public void changeSignSaveBtn(boolean isEnable) {
        sign_btn.setEnabled(isEnable);
    }

    private void processSale() {
        progressDialog.setMessage(getString(R.string.loading_sale_fragment));
        progressDialog.show();
        GetDataService service = MozCarbonAPI.getRetrofit(getContext()).create(GetDataService.class);

        Call<SaleAddedResponseModel> call = service.postSale(addSaleMap);

        call.enqueue(new Callback<SaleAddedResponseModel>() {
            @Override
            public void onResponse(Call<SaleAddedResponseModel> call, Response<SaleAddedResponseModel> response) {
                progressDialog.hide();

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Snackbar.make(parent_view, getString(R.string.success_sale_fragment), Snackbar.LENGTH_LONG).show();
                    callback.renderWebView(response.body().getUrl());
                    Log.d("Sale", "onResponse: "+response.body());
                    Log.d("Sale", "onResponse: "+response.message());
                    // callback.renderWebView(response.body().getResponse().getId() + "");
                } else {
                    Log.d("Sale", "onResponse: "+response.body());
                    Log.d("Sale", "onResponse: "+response.message());
                    Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SaleAddedResponseModel> call, Throwable t) {
                progressDialog.hide();

                Snackbar.make(parent_view, getString(R.string.error_sale_fragment_failed), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }
/**
        * Checks if the app has permission to write to device storage
 *
         * If the app does not has permission then the user will be prompted to grant permissions
 *
         * @param activity
 */
        public  void verifyStoragePermissions(Activity activity) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Sem permissao ", Toast.LENGTH_SHORT).show();
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }else {
               // Toast.makeText(getContext(), "Com permissao ", Toast.LENGTH_SHORT).show();
            }
        }
        public File save(View v, String StoredPath) {

            Log.d("Venda", "save: "+StoredPath);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }

            verifyStoragePermissions((AppCompatActivity) getActivity());
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file

                File profileImageFile =  new File(StoredPath);
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

                Uri fileUri = Uri.fromFile(profileImageFile);
                Log.d("Venda", "ProfileImage: "+profileImageFile.getName());
                return profileImageFile;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            changeSignSaveBtn(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
