package dev.visum.demoapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.utils.Tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class OfflineSalesUnitTest {
    @Test
    public void send_multiples_sales() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // assertEquals(4, 2 + 2);

        AddSaleModel addSaleModel1 = new AddSaleModel(
                "1",
                "Gimo",
                "Produto X",
                500.0,
                "2",
                0,
                0,
                "dfdf",
                "neighborhood",
                "city_block",
                "house_number",
                "reference_point",
                "asdasd",
                2.0
        );

        AddSaleModel addSaleModel2 = new AddSaleModel(
                "1",
                "Elton",
                "Produto X",
                200.0,
                "",
                0,
                1,
                "neighborhood",
                "city_block",
                "house_number",
                "reference_point",
                "dsfsdf",
                "asdad",
                2.0
        );

        KeyStoreLocal.getInstance(appContext).setOfflineSales(addSaleModel1);
        KeyStoreLocal.getInstance(appContext).setOfflineSales(addSaleModel2);

        System.out.println(KeyStoreLocal.getInstance(appContext).getOfflineSales().toString());

        KeyStoreLocal.getInstance(appContext).clearOfflineSales();

        // assertNull(KeyStoreLocal.getInstance(appContext).getOfflineSales());
    }
}
