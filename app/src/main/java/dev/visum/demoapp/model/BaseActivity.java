package dev.visum.demoapp.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private String lastUsedFragmentTag;
    private final String INTENT_EXTRA_BUNDLE = "Bundle";
    private final String INTENT_EXTRA_PARCELABLE = "Parcelable";


    private State state;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        state = State.FOREGROUND;

    }

    public void startMyActivity(Class<?> target) {
        startActivity(new Intent(this, target));
    }

    public <activity extends BaseActivity> void startActivity(Class<activity> target, @Nullable Bundle bundle, @Nullable Parcelable data) {
        Intent intent = new Intent(this, target);

        if (bundle != null) {
            intent.putExtra(INTENT_EXTRA_BUNDLE, bundle);
        }

        if (data != null) {
            intent.putExtra(INTENT_EXTRA_PARCELABLE, data);
        }

        startActivity(intent);
    }

    public Bundle getBundle() {
        return getBundle(INTENT_EXTRA_BUNDLE);
    }

    public Bundle getBundle(String key) {
        if (!TextUtils.isEmpty(key))
            return getIntent().getBundleExtra(key);
        return getIntent().getBundleExtra(INTENT_EXTRA_BUNDLE);
    }

    public Parcelable getParcelable(String key) {
        if (!TextUtils.isEmpty(key))
            return getIntent().getParcelableExtra(key);
        return getIntent().getParcelableExtra(INTENT_EXTRA_BUNDLE);
    }

    public Parcelable getParcelable() {
        return getParcelable(INTENT_EXTRA_PARCELABLE);
    }

    public <Frag extends BaseFragment> void swapFragment(@IdRes int container, @NonNull Frag fragment, @Nullable String tag) {
        if (tag == null || tag.isEmpty())
            tag = fragment.getTAG();

        lastUsedFragmentTag = tag;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, tag)
                .commit();
    }

    public String getText(@NonNull TextView textSource) {
        return textSource.getText().toString().trim();
    }

    public <Frag extends BaseFragment> void swapFragmentAndAddToBackStack(@IdRes int container, @NonNull Frag fragment, @Nullable String tag, @Nullable String stackName) {
        if (tag == null || tag.isEmpty())
            tag = fragment.getTAG();

        lastUsedFragmentTag = tag;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(stackName)
                .commit();
    }

    public Double getNumber(TextView textView) {
        Double number;

        String text = getText(textView);

        number = Double.parseDouble(text);


        return number;
    }






    public String getLastUsedFragmentTag() {
        return lastUsedFragmentTag;
    }






/*    public void sendNotification(String title, String subject, SubData data, String to) {
        VolleyRequestQueuer
                .init(this)
                .sendDataNotification(title, subject, data, to)
                .execute(response -> Timber.d("response: %s", response), Timber::d);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state = State.FOREGROUND;
        mInstance = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        state = State.BACKGROUND;
    }

    @Override
    protected void onStart() {
        super.onStart();

        isAppInBackground(State.FOREGROUND);
    }

    @Override
    protected void onPause() {
        super.onPause();

        isAppInBackground(State.BACKGROUND);
    }

    public enum State {
        BACKGROUND, FOREGROUND
    }


    private void isAppInBackground(State state) {
        this.state = state;
    }

    public synchronized State getCurrentState() {
        return state;
    }


    private static BaseActivity mInstance;

    public static BaseActivity getInstance() {
        return mInstance;
    }

}
