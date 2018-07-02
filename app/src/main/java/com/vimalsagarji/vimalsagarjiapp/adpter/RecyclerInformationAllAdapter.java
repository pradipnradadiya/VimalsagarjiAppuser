package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.InformationItem;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtItem;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerInformationAllAdapter extends RecyclerView.Adapter<RecyclerInformationAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<InformationItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;


    public RecyclerInformationAllAdapter(Activity activity, ArrayList<InformationItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.thismonth_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {


        InformationItem informationItem=itemArrayList.get(i);

        holder.txt_Title.setText(CommonMethod.decodeEmoji(informationItem.getTitle()));
        holder.txt_Description.setText(CommonMethod.decodeEmoji(informationItem.getDescription()));
        holder.txt_Date.setText(CommonMethod.decodeEmoji(informationItem.getDate()));
        holder.txt_Address.setText(CommonMethod.decodeEmoji(informationItem.getAddress()));
        holder.txt_views.setText(CommonMethod.decodeEmoji(informationItem.getView()));


        if (informationItem.getIs_viewed().equalsIgnoreCase("true")) {
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

        TextView txt_ID, txt_Title, txt_Description, txt_Address, txt_Date, txt_views;
        private ImageView img_new;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txt_Title = (TextView) itemView.findViewById(R.id.txtTitle);
            txt_Description = (TextView) itemView.findViewById(R.id.txtDescription);
            txt_Date = (TextView) itemView.findViewById(R.id.txtDate);
            txt_Address = (TextView) itemView.findViewById(R.id.txt_address);
            img_new = (ImageView) itemView.findViewById(R.id.img_new);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            InformationItem informationItem = itemArrayList.get(getAdapterPosition());
            informationItem.setIs_viewed("true");
            notifyItemChanged(getAdapterPosition());

            Intent intent = new Intent(activity, InformationDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("listTitle", informationItem.getTitle());
            intent.putExtra("listID", informationItem.getID());
            intent.putExtra("listDescription", informationItem.getDescription());
            intent.putExtra("listDate", informationItem.getDate());
            intent.putExtra("time", informationItem.getDate());
            intent.putExtra("view", informationItem.getView());
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
