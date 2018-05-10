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

import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.AudioDetail;
import com.vimalsagarji.vimalsagarjiapp.activity.ByPeopleDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.EventDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.NotificationItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionList;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<NotificationItem> itemArrayList;

    public NotificationListAdapter(Activity activity, ArrayList<NotificationItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nitification, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationItem notificationItem = itemArrayList.get(position);

        Log.e("table", "------------" + notificationItem.getTable());
        if (notificationItem.getTable().equalsIgnoreCase("Information")) {
            holder.notification_image.setImageResource(R.drawable.infromation);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));


        } else if (notificationItem.getTable().equalsIgnoreCase("Events")) {
            holder.notification_image.setImageResource(R.drawable.event);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("Audio")) {
            holder.notification_image.setImageResource(R.drawable.audio);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("Video")) {
            holder.notification_image.setImageResource(R.drawable.video);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("Thoughts")) {
            holder.notification_image.setImageResource(R.drawable.thoughts);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("ByPeople")) {
            holder.notification_image.setImageResource(R.drawable.bypeople);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("QuestionAnswer")) {
            holder.notification_image.setImageResource(R.drawable.qa);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("CompetitionMain")) {
            holder.notification_image.setImageResource(R.drawable.competition);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(notificationItem.getDescription()));
        } else if (notificationItem.getTable().equalsIgnoreCase("OpinionPollMain")) {
            holder.notification_image.setImageResource(R.drawable.opinionpoll);
            holder.txt_title.setText(CommonMethod.decodeEmoji(notificationItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(notificationItem.getDate()));

            if (notificationItem.getDescription().equalsIgnoreCase("")) {
                holder.txt_content.setText("Yes & No Answer.");
            } else {
                holder.txt_content.setText(notificationItem.getDescription());
            }

        }
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txt_title, txt_content, txt_date;
        ImageView notification_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            notification_image = (ImageView) itemView.findViewById(R.id.notification_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Information")) {
                Intent intent = new Intent(v.getContext(), InformationDetailActivity.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Events")) {
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Audio")) {
                Intent intent = new Intent(v.getContext(), AudioDetail.class);
                intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Video")) {
                Intent intent = new Intent(v.getContext(), VideoDetailActivity.class);
                intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Thoughts")) {
                Intent intent = new Intent(v.getContext(), ThoughtsDetailActivity.class);
                intent.putExtra("thoughtid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("ByPeople")) {
                Intent intent = new Intent(v.getContext(), ByPeopleDetailActivity.class);
                intent.putExtra("postId", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("QuestionAnswer")) {
                Intent intent = new Intent(v.getContext(), AllQuestionDetail.class);
                intent.putExtra("qid", itemArrayList.get(getAdapterPosition()).getId());
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("CompetitionMain")) {
                Intent intent = new Intent(v.getContext(), CompetitionList.class);
                intent.putExtra("categoryID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("listTitle", itemArrayList.get(getAdapterPosition()).getTitle());
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("OpinionPollMain")) {
                Intent intent = new Intent(v.getContext(), OpinionPoll.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

}
