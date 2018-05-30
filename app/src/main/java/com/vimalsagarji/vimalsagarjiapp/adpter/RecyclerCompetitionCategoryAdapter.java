package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionRulesActivity;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerCompetitionCategoryAdapter extends RecyclerView.Adapter<RecyclerCompetitionCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionItem> itemArrayList;
    private String id;
    public static final ArrayList<String> compcatid = new ArrayList<>();
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;

    public RecyclerCompetitionCategoryAdapter(Activity activity, ArrayList<CompetitionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition, viewGroup, false);
        totalTimeCountInMilliseconds = 60 * 1000;      // time count for 3 minutes = 180 seconds
        timeBlinkInMilliseconds = 30 * 1000;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {


        final CompetitionItem competitionItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionItem.getTitle()));
        holder.txt_timer.setText(CommonMethod.decodeEmoji(competitionItem.getTotal_minute() + " : minutes"));
        String[] datesarray=competitionItem.getDate().split("-");


        holder.txt_last_date.setText(CommonMethod.decodeEmoji(datesarray[2]+"-"+datesarray[1]+"-"+datesarray[0]));
        holder.txt_time.setText(CommonMethod.decodeEmoji(competitionItem.getTime()));
        holder.txt_total_question.setText("Number of question : "+CommonMethod.decodeEmoji(competitionItem.getTotal_question()));


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_timer;
        final TextView txt_time;
        final TextView txt_last_date;
        final TextView txt_total_question;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_timer = (TextView) itemView.findViewById(R.id.txt_timer);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_last_date = (TextView) itemView.findViewById(R.id.txt_last_date);
            txt_total_question = (TextView) itemView.findViewById(R.id.txt_total_question);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), CompetitionRulesActivity.class);
            intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("rules", itemArrayList.get(getAdapterPosition()).getRules());
            intent.putExtra("minute", itemArrayList.get(getAdapterPosition()).getTotal_minute());
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
