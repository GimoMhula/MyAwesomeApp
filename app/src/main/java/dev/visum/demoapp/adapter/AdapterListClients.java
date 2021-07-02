package dev.visum.demoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.model.ClientModel;
import dev.visum.demoapp.model.ClientModel;
import dev.visum.demoapp.utils.Tools;

public class AdapterListClients extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ClientModel> items = new ArrayList<>();

    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ClientModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListClients(Context context, List<ClientModel> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView contact;
        public TextView address;
        public TextView date;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            contact = v.findViewById(R.id.contact);
            address= v.findViewById(R.id.address);
            date = v.findViewById(R.id.date);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            ClientModel ClientModel = items.get(position);
            view.name.setText(ClientModel.getName());
            view.contact.setText(ClientModel.getContact());
            view.address.setText(ClientModel.getAddress());
            view.date.setText(ClientModel.getCreated_at());
//            Tools.displayImageOriginal(ctx, view.image, 0, ClientModel.getImgUrl());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}