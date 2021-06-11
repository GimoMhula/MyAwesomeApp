package dev.visum.demoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.model.SurveyQuestionsModel;
import dev.visum.demoapp.model.SurveyQuestionsModel;

public class AdapterListSurveyQuestions extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  LinearLayout lyt_radio_group,lyt_text_plain,lyt_spinner;
    private List<SurveyQuestionsModel> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterListSurveyQuestions(Context context, List<SurveyQuestionsModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView description;
        public RadioButton more;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            more = (RadioButton) v.findViewById(R.id.check_question);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            lyt_text_plain = (LinearLayout) v.findViewById(R.id.lyt_text_plain);
            lyt_radio_group = (LinearLayout) v.findViewById(R.id.lyt_radio_group);
            lyt_spinner = (LinearLayout) v.findViewById(R.id.lyt_spinner);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey_question_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final SurveyQuestionsModel p = items.get(position);
            view.title.setText(p.title);
            view.description.setText(p.description);

            if(p.type==1){
                lyt_radio_group.setVisibility(View.GONE);
                lyt_spinner.setVisibility(View.GONE);
                lyt_text_plain.setVisibility(View.VISIBLE);
            }
            if(p.type==2){
                lyt_radio_group.setVisibility(View.VISIBLE);
                lyt_spinner.setVisibility(View.GONE);
                lyt_text_plain.setVisibility(View.GONE);
            }
            if(p.type==3){
                lyt_radio_group.setVisibility(View.GONE);
                lyt_spinner.setVisibility(View.VISIBLE);
                lyt_text_plain.setVisibility(View.GONE);
            }
//            Tools.displayImageOriginal(ctx, view.image, p.image, p.url);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

//            view.more.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onMoreButtonClickListener == null) return;
////                    onMoreButtonClick(view, p);
//                }
//            });
        }
    }

    private void onMoreButtonClick(final View view, final SurveyQuestionsModel p) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_product_more);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, SurveyQuestionsModel obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, SurveyQuestionsModel obj, MenuItem item);
    }

}