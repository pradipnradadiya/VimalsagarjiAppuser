package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionItem;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerCompetitionQuestionAllAdapter extends RecyclerView.Adapter<RecyclerCompetitionQuestionAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionQuestionItem> itemArrayList;
    private String id;

    public ArrayList<String> qidlist = new ArrayList<>();
    public ArrayList<String> answerlist = new ArrayList<>();

    public RecyclerCompetitionQuestionAllAdapter(Activity activity, ArrayList<CompetitionQuestionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition_all, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final CompetitionQuestionItem competitionQuestionItem = itemArrayList.get(i);

//        holder.img_option.setVisibility(View.VISIBLE);
        holder.txt_answer.setVisibility(View.GONE);
        holder.txt_type.setVisibility(View.GONE);


        holder.txt_index.setText(String.valueOf(i + 1) + ") ");
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getQuestion()));
//        holder.txt_type.setText(competitionQuestionItem.getQtype());
//        holder.txt_answer.setText(competitionQuestionItem.getAnswer());

        if (competitionQuestionItem.getQtype().equalsIgnoreCase("yes_no")) {
            holder.lin.setVisibility(View.VISIBLE);

            holder.txt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.img_yes.setVisibility(View.VISIBLE);
                    holder.img_no.setVisibility(View.GONE);

                    if (qidlist.contains(competitionQuestionItem.getId())) {
                        qidlist.set(i, competitionQuestionItem.getId());
                        answerlist.set(i, "true");
                    } else {
                        qidlist.add(i, competitionQuestionItem.getId());
                        answerlist.add(i, "true");
                    }

                    Log.e("Question id array", "----------" + qidlist);
                    Log.e("Answer list array", "----------" + answerlist);

                }
            });

            holder.txt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.img_yes.setVisibility(View.GONE);
                    holder.img_no.setVisibility(View.VISIBLE);

                    if (qidlist.contains(competitionQuestionItem.getId())) {
                        qidlist.set(i, competitionQuestionItem.getId());
                        answerlist.set(i, "false");
                    } else {
                        qidlist.add(i, competitionQuestionItem.getId());
                        answerlist.add(i, "false");
                    }

                    Log.e("Question id array", "----------" + qidlist);
                    Log.e("Answer list array", "----------" + answerlist);


                }
            });

            holder.img_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.img_yes.setVisibility(View.VISIBLE);
                    holder.img_no.setVisibility(View.GONE);

                    if (qidlist.contains(competitionQuestionItem.getId())) {
                        qidlist.set(i, competitionQuestionItem.getId());
                        answerlist.set(i, "true");
                    } else {
                        qidlist.add(i, competitionQuestionItem.getId());
                        answerlist.add(i, "true");
                    }

                    Log.e("Question id array", "----------" + qidlist);
                    Log.e("Answer list array", "----------" + answerlist);

                }
            });

            holder.img_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.img_yes.setVisibility(View.GONE);
                    holder.img_no.setVisibility(View.VISIBLE);

                    if (qidlist.contains(competitionQuestionItem.getId())) {
                        qidlist.set(i, competitionQuestionItem.getId());
                        answerlist.set(i, "false");
                    } else {
                        qidlist.add(i, competitionQuestionItem.getId());
                        answerlist.add(i, "false");
                    }

                    Log.e("Question id array", "----------" + qidlist);
                    Log.e("Answer list array", "----------" + answerlist);


                }
            });

        } else {

            holder.lin.setVisibility(View.GONE);
            String[] options = competitionQuestionItem.getOptions().split(",");
            for (int j = 0; j < options.length; j++) {
//                holder.txt_option.append(options[j]+"\n");

                holder.radioButton = new RadioButton(activity);
                Log.e("id", "----------------" + holder.radioButton.getId());
                Log.e("option", "----------------" + options[j]);
                holder.radioButton.setText(options[j]);
                holder.radiogroup.addView(holder.radioButton);


                holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {


                        RadioButton rb = (RadioButton) activity.findViewById(checkedId);
                        String rbtext = (String) rb.getText();

                        Log.e("position", "---------------" + i);



                        if (qidlist.contains(competitionQuestionItem.getId())) {
                            qidlist.set(i, competitionQuestionItem.getId());
                            answerlist.set(i, rbtext);
                        } else {
                            qidlist.add(i, competitionQuestionItem.getId());
                            answerlist.add(i, rbtext);
                        }


                        Log.e("Question id array", "----------" + qidlist);
                        Log.e("Answer list array", "----------" + answerlist);


                    }
                });

            }

        }

    }


    @Override
    public int getItemCount() {

//        questionid=new String[itemArrayList.size()];
        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_index;
        final TextView txt_type;
        final TextView txt_answer;
        final TextView txt_option;
        final TextView txt_yes;
        final TextView txt_no;
        final LinearLayout lin;
        final ImageView img_view;
        final ImageView img_yes, img_no;
        RadioGroup radiogroup;
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_index = (TextView) itemView.findViewById(R.id.txt_index);
            txt_yes = (TextView) itemView.findViewById(R.id.txt_yes);
            txt_no = (TextView) itemView.findViewById(R.id.txt_no);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_answer = (TextView) itemView.findViewById(R.id.txt_answer);
            txt_option = (TextView) itemView.findViewById(R.id.txt_option);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            img_yes = (ImageView) itemView.findViewById(R.id.img_yes);
            img_no = (ImageView) itemView.findViewById(R.id.img_no);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);
            radiogroup = (RadioGroup) itemView.findViewById(R.id.radiogroup);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(v.getContext(), AudioDetailActivity.class);
//            intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
//            intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
//            v.getContext().startActivity(intent);
//            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
