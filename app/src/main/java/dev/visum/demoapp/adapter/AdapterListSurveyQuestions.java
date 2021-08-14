package dev.visum.demoapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.model.SurveyQuestionsModel;

public class AdapterListSurveyQuestions extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private  LinearLayout lyt_radio_1,lyt_text_plain, lyt_radio_2;
    private List<SurveyQuestionsModel> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;
    AdapterListSurveyQuestions.OnListener callback;

    public interface OnListener {
        void setAnswer(String value, int position, int question_id);
    }

    public void setCallback(AdapterListSurveyQuestions.OnListener callback) {
        this.callback = callback; // new
    }
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
        private final RadioGroup radio_group_1,radio_group_2;
        public ImageView image;
        public TextView title;
        public TextView description;
        public RadioButton more;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextInputEditText) v.findViewById(R.id.description);
            more = (RadioButton) v.findViewById(R.id.check_question);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            lyt_text_plain = (LinearLayout) v.findViewById(R.id.lyt_text_plain);
            lyt_radio_1 = (LinearLayout) v.findViewById(R.id.lyt_radio_1);
            lyt_radio_2 = (LinearLayout) v.findViewById(R.id.lyt_radio_2);
            radio_group_1= (RadioGroup) v.findViewById(R.id.radio_group_1);
            radio_group_2= (RadioGroup) v.findViewById(R.id.radio_group_2);


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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final SurveyQuestionsModel p = items.get(position);
            view.title.setText(p.title);



            view.radio_group_1.getCheckedRadioButtonId();

            if(p.type==1){
                lyt_radio_1.setVisibility(View.GONE);
                lyt_radio_2.setVisibility(View.GONE);
                lyt_text_plain.setVisibility(View.VISIBLE);



                view.description.setText("");
                view.description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus){
                            callback.setAnswer(view.description.getText().toString(),position,p.id);
                            //TODO remove softkeyboard from plain text
                        }
                    }
                });


            }
            if(p.type==2){
                lyt_radio_1.setVisibility(View.VISIBLE);
                lyt_radio_2.setVisibility(View.GONE);
                lyt_text_plain.setVisibility(View.GONE);
                view.radio_group_1.getCheckedRadioButtonId();
            }
            if(p.type==3){
                lyt_radio_1.setVisibility(View.GONE);
                lyt_radio_2.setVisibility(View.VISIBLE);
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