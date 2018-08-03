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
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.QuestionAllItem;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.fcm.MyFirebaseMessagingService.questionid;

@SuppressWarnings("ALL")
public class RecyclerQuestionAllAdapter extends RecyclerView.Adapter<RecyclerQuestionAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<QuestionAllItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;


    public RecyclerQuestionAllAdapter(Activity activity, ArrayList<QuestionAllItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_answer_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        QuestionAllItem questionAllItem=itemArrayList.get(i);

        holder.txt_views.setText(questionAllItem.getView());
        holder.txtQuestion.setText("Q : "+ CommonMethod.decodeEmoji(questionAllItem.getQuestion()));
        holder.txtAnswer.setText("A : "+CommonMethod.decodeEmoji(questionAllItem.getAnswer()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(questionAllItem.getDate()));
        holder.txt_postby.setText("Question By:" + CommonMethod.decodeEmoji(questionAllItem.getName()));


        if (questionAllItem.getIs_viewed().equalsIgnoreCase("true")){
            holder.img_new.setVisibility(View.GONE);
        }
        else{
            holder.img_new.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txtQuestion, txtAnswer, txt_date, txt_postby, txt_views;
        ImageView img_new;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            txtAnswer = (TextView) itemView.findViewById(R.id.txtAnswer);
            txt_date = (TextView) itemView.findViewById(R.id.txt_datess);
            txt_postby = (TextView) itemView.findViewById(R.id.txt_postby);
            img_new = (ImageView) itemView.findViewById(R.id.img_new);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            QuestionAllItem questionAllItem=itemArrayList.get(getAdapterPosition());
            questionAllItem.setIs_viewed("true");
            notifyItemChanged(getAdapterPosition());

            Intent intent = new Intent(activity, AllQuestionDetail.class);
            Log.e("Question", questionAllItem.getQuestion());
            Log.e("Answer", questionAllItem.getAnswer());
            intent.putExtra("Question", questionAllItem.getQuestion());
            intent.putExtra("Answer", questionAllItem.getAnswer());
            intent.putExtra("view", questionAllItem.getView());
            intent.putExtra("qid",questionAllItem.getID());
            questionid=questionAllItem.getID();

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
