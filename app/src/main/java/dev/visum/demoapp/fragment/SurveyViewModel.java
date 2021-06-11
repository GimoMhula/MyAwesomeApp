package dev.visum.demoapp.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SurveyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SurveyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Area de pagamento");
    }

    public LiveData<String> getText() {
        return mText;
    }
}