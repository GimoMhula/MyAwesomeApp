package dev.visum.demoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.visum.demoapp.model.CustomerResponseModel;

public class AdapterSaleCustomerFiltered extends ArrayAdapter<CustomerResponseModel> {
    private ArrayList<CustomerResponseModel> items;
    private ArrayList<CustomerResponseModel> itemsAll;
    private ArrayList<CustomerResponseModel> suggestions;
    private int viewResourceId;

    @SuppressWarnings("unchecked")
    public AdapterSaleCustomerFiltered(Context context, int viewResourceId,
                                       ArrayList<CustomerResponseModel> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<CustomerResponseModel>) items.clone();
        this.suggestions = new ArrayList<CustomerResponseModel>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        CustomerResponseModel customerResponseModel = items.get(position);
        if (customerResponseModel != null) {
            TextView customerResponseModelLabel = (TextView)  v.findViewById(android.R.id.text1);
            if (customerResponseModelLabel != null) {
                customerResponseModelLabel.setText(customerResponseModel.getName().concat("-").concat(customerResponseModel.getContact()));
                customerResponseModelLabel.setPadding(25, 20, 0, 40);
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
            String str = ((CustomerResponseModel) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CustomerResponseModel customerResponseModel : itemsAll) {
                    if (customerResponseModel.getName().concat("-").concat(customerResponseModel.getContact()).toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customerResponseModel);
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
            ArrayList<CustomerResponseModel> filteredList = (ArrayList<CustomerResponseModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CustomerResponseModel c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
