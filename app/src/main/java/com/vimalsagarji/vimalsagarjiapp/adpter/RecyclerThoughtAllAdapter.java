package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.QuestionAllItem;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtItem;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerThoughtAllAdapter extends RecyclerView.Adapter<RecyclerThoughtAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<ThoughtItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;


    public RecyclerThoughtAllAdapter(Activity activity, ArrayList<ThoughtItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.thoughts_all_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        ThoughtItem questionAllItem = itemArrayList.get(i);

        holder.txt_views.setText(CommonMethod.decodeEmoji(questionAllItem.getView()));
        holder.txt_Title.setText(CommonMethod.decodeEmoji(questionAllItem.getTitle()));
        holder.txt_Description.setText(CommonMethod.decodeEmoji(questionAllItem.getDescription()));
        holder.txt_Date.setText(CommonMethod.decodeEmoji(questionAllItem.getDate()));

        if (questionAllItem.getIs_viewed().equalsIgnoreCase("true")) {
            holder.img_new.setVisibility(View.GONE);
        } else {
            holder.img_new.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txt_ID, txt_Title, txt_Description, txt_Date, txt_views;
        ImageView img_new;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txt_Title = (TextView) itemView.findViewById(R.id.txtTitle);
            txt_Description = (TextView) itemView.findViewById(R.id.txtDescription);
            txt_Date = (TextView) itemView.findViewById(R.id.txtDate);
            img_new = (ImageView) itemView.findViewById(R.id.img_new);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            ThoughtItem thoughtItem = itemArrayList.get(getAdapterPosition());
            thoughtItem.setIs_viewed("true");
            notifyItemChanged(getAdapterPosition());

            Intent intent = new Intent(activity, ThoughtsDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("thoughtid", thoughtItem.getID());
            intent.putExtra("listTitle", thoughtItem.getTitle());
            intent.putExtra("listDescription", thoughtItem.getDescription());
            intent.putExtra("listDate", thoughtItem.getDate());
            intent.putExtra("view", thoughtItem.getView());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}
