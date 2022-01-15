package dev.visum.demoapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.model.Province;


public class ProvinceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private List<Province> routes;

    public ProvinceSpinnerAdapter(Context context, List<Province> routes) {
        this.routes = routes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_province, null);
        TextView textView = view.findViewById(R.id.province_desc);
        textView.setText(routes.get(position).getName());
        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view = View.inflate(context, R.layout.item_province, null);
        final TextView textView = view.findViewById(R.id.province_desc);
        textView.setText(routes.get(position).getName());

        return view;
    }
}
