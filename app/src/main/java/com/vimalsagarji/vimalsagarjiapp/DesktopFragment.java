package com.vimalsagarji.vimalsagarjiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class DesktopFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private LinearLayout lin_info;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.desktop_fragment, container, false);
        findID();
        idClick();
        return rootview;
    }

    private void findID() {
        lin_info = (LinearLayout) rootview.findViewById(R.id.lin_info);
        lin_event = (LinearLayout) rootview.findViewById(R.id.lin_event);
        lin_audio = (LinearLayout) rootview.findViewById(R.id.lin_audio);
        lin_video = (LinearLayout) rootview.findViewById(R.id.lin_video);
        lin_thought = (LinearLayout) rootview.findViewById(R.id.lin_thought);
        lin_gallery = (LinearLayout) rootview.findViewById(R.id.lin_gallery);
        lin_qa = (LinearLayout) rootview.findViewById(R.id.lin_qa);
        lin_comp = (LinearLayout) rootview.findViewById(R.id.lin_comp);
        lin_op = (LinearLayout) rootview.findViewById(R.id.lin_op);
        lin_bypeople = (LinearLayout) rootview.findViewById(R.id.lin_bypeople);
    }

    private void idClick() {
        lin_info.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_info:
                intent = new Intent(getActivity(), InformationCategory.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_event:
                intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_audio:
                intent = new Intent(getActivity(), AudioCategory.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_video:
                intent = new Intent(getActivity(), VideoCategory.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_thought:
                intent = new Intent(getActivity(), ThoughtsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_gallery:
                intent = new Intent(getActivity(), Gallery_All_Category.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_qa:
                intent = new Intent(getActivity(), QuestionAnswerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_comp:
                intent = new Intent(getActivity(), CompetitionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_op:
                intent = new Intent(getActivity(), OpinionPoll.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.lin_bypeople:
                intent = new Intent(getActivity(), ByPeople.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
