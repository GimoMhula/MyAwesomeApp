package dev.visum.demoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.visum.demoapp.model.ProductResponseModel;

public class AdapterSaleProductFiltered extends ArrayAdapter<ProductResponseModel> {
    private ArrayList<ProductResponseModel> items;
    private ArrayList<ProductResponseModel> itemsAll;
    private ArrayList<ProductResponseModel> suggestions;
    private int viewResourceId;

    @SuppressWarnings("unchecked")
    public AdapterSaleProductFiltered(Context context, int viewResourceId,
                                ArrayList<ProductResponseModel> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<ProductResponseModel>) items.clone();
        this.suggestions = new ArrayList<ProductResponseModel>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        ProductResponseModel product = items.get(position);
        if (product != null) {
            TextView productLabel = (TextView)  v.findViewById(android.R.id.text1);
            if (productLabel != null) {
                productLabel.setText(product.getName());
                productLabel.setPadding(25, 20, 0, 40);
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = ((ProductResponseModel) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ProductResponseModel product : itemsAll) {
                    if (product.getName().toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<ProductResponseModel> filteredList = (ArrayList<ProductResponseModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProductResponseModel c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
