package dev.visum.demoapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.adapter.AdapterGridItemCategory;
import dev.visum.demoapp.data.DataGenerator;
import dev.visum.demoapp.fragment.ProductGridFragment;
import dev.visum.demoapp.model.BaseActivity;
import dev.visum.demoapp.model.ItemCategory;
import dev.visum.demoapp.utils.Tools;
import dev.visum.demoapp.widget.SpacingItemDecoration;

public class MainActivity extends BaseActivity {

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


        List<ItemCategory> items = DataGenerator.getItemCategory(this);
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.image = R.drawable.ic_image_black_24dp;
        itemCategory.title = "Produtos";
        itemCategory.imageDrw = AppCompatResources.getDrawable(this, itemCategory.image);
        items.add(itemCategory);
        //set data and list dev.visum.demoapp.adapter
        mAdapter = new AdapterGridItemCategory(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridItemCategory.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemCategory obj, int position) {
               Toast.makeText(MainActivity.this,  "Item " + obj.title + " clicked", Toast.LENGTH_SHORT).show();

               if (obj.title.equals("Produtos")) {
                   FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.add(R.id.parent_view, new ProductGridFragment());
                   fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
               } else {
                   Bundle bundle = new Bundle();
                   bundle.putString("content",obj.title);
                   startActivity(ContentActivity.class,bundle,null);
               }

            }
        });

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_10);
        Tools.setSystemBarLight(this);
    }

    @Override
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
    }
}
