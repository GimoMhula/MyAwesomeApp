package dev.visum.demoapp.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by visum.dev .
 */

public abstract class BaseFragment extends Fragment {
    private String TAG;

    public String getTAG() {
        if (TAG == null || TAG.trim().isEmpty())
            return getClass().getSimpleName();

        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public <activity extends BaseActivity> void startActivity(Class<activity> target) {
        startActivity(target, null, null);
    }

    public <activity extends BaseActivity> void startActivity(Class<activity> target, @Nullable Bundle bundle, @Nullable Parcelable data) {
        Intent intent = new Intent(getActivity(), target);

        if (bundle != null) {
            intent.putExtra("Bundle", bundle);
        }

        if (data != null) {
            intent.putExtra("Parcelable", data);
        }

        startActivity(intent);
    }


    public void swapFragment(@IdRes int container, BaseFragment baseFragment) {
        swapFragment(container, baseFragment, null);
    }

    public void swapFragment(@IdRes int container, BaseFragment baseFragment, @Nullable String tag) {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.swapFragment(container, baseFragment, tag);
        }
    }

    public void swapFragmentAndAddToBackStack(int container, BaseFragment baseFragment, @Nullable String stackName) {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.swapFragmentAndAddToBackStack(container, baseFragment, baseFragment.getTAG(), stackName);
        }
    }

    public void callImageFromStorage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escolha a Imagem"), requestCode);

    }




    public String getText(TextView textInput) {
        return textInput.getText().toString().trim();
    }
}
