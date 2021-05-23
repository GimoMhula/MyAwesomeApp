package dev.visum.demoapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import dev.visum.demoapp.R;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.fragment.LoginFragment;
import dev.visum.demoapp.model.BaseActivity;

public class LoginActivity extends BaseActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);

        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startApp() {
        if (KeyStoreLocal.getInstance(this).getToken() != null && KeyStoreLocal.getInstance(this).getUserId() != null) {
            startActivity(MainActivity.class, null, null);
        } else {
            FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, new LoginFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            Context context;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Permissões");
            alertDialog.setMessage("É necessário dar permissão para determinarmos a localização do utilizador no momento da venda!");
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
}
