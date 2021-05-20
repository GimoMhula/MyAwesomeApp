package dev.visum.demoapp.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import dev.visum.demoapp.R;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.fragment.LoginFragment;
import dev.visum.demoapp.model.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (KeyStoreLocal.getInstance(this).getToken() != null) {
            startActivity(MainActivity.class, null, null);
        } else {
            FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, new LoginFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
