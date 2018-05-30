package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.EventCategoryItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerEventCategoryAdapter extends RecyclerView.Adapter<RecyclerEventCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<EventCategoryItem> itemArrayList;
    private String id;
    public static final ArrayList<String> audio_cat = new ArrayList<>();

    public RecyclerEventCategoryAdapter(Activity activity, ArrayList<EventCategoryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_category_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final EventCategoryItem audioCategoryItem = itemArrayList.get(i);
        holder.grid_txtTitle.setText(CommonMethod.decodeEmoji(audioCategoryItem.getName()));
        String path = CommonURL.ImagePath + "eventcategory/" + audioCategoryItem.getCategoryicon().replaceAll(" ", "%20");
        if (audioCategoryItem.getCategoryicon().equalsIgnoreCase("")) {
//            Picasso.with(activity).load(R.drawable.noimageavailable);
        } else {
//            Picasso.with(activity).load(path).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_audio_category);

            Log.e("path","-----------------"+path);
            Glide.with(activity).load(path
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.grid_img);

        }

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView grid_txtTitle;
        ImageView grid_img;
        public ViewHolder(View itemView) {
            super(itemView);

            grid_txtTitle = (TextView) itemView.findViewById(R.id.grid_txtTitle);
            grid_img = (ImageView) itemView.findViewById(R.id.grid_img);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), EventActivity.class);
            intent.putExtra("event_category_id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("icon", itemArrayList.get(getAdapterPosition()).getCategoryicon());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
