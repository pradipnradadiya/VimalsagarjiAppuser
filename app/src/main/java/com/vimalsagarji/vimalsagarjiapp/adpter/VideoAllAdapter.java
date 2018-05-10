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
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.VideoFullActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.YoutubePlayActivity;
import com.vimalsagarji.vimalsagarjiapp.model.VideoAllItem;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurCommonUrl;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;

public class VideoAllAdapter extends RecyclerView.Adapter<VideoAllAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<VideoAllItem> itemArrayList;

    public VideoAllAdapter(Activity activity, ArrayList<VideoAllItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_all_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        VideoAllItem videoAllItem = itemArrayList.get(i);
        holder.textView.setText(videoAllItem.getVideolink());
        holder.txt_video_title.setText(videoAllItem.getTitle());

        if (!videoAllItem.getVideolink().equalsIgnoreCase("")) {
            String[] str = videoAllItem.getVideolink().split("/");
            final String v_vode = str[3];
            Log.e("code", "----------------" + v_vode);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, YoutubePlayActivity.class);
                    intent.putExtra("vcode", v_vode);
                    activity.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView, txt_video_title;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_video);
            textView = (TextView) itemView.findViewById(R.id.txt_video_link);
            txt_video_title = (TextView) itemView.findViewById(R.id.txt_video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("onClick", "------------");

            if (itemArrayList.get(getAdapterPosition()).getId().equalsIgnoreCase("eid")) {
                if (itemArrayList.get(getAdapterPosition()).getVideo().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Video not available click on video link.", Toast.LENGTH_LONG).show();
                } else {
                    video_play_url = AllOurCommonUrl.videopath + "eventvideo/" + itemArrayList.get(getAdapterPosition()).getVideo();
                    Intent intent = new Intent(activity, VideoFullActivity.class);
                    activity.startActivity(intent);
                }

            }
            if (itemArrayList.get(getAdapterPosition()).getId().equalsIgnoreCase("bid")) {
                if (itemArrayList.get(getAdapterPosition()).getVideo().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Video not available click on video link.", Toast.LENGTH_LONG).show();
                } else {
                    video_play_url = AllOurCommonUrl.videopath + "bypeoplevideo/" + itemArrayList.get(getAdapterPosition()).getVideo();
                    Intent intent = new Intent(activity, VideoFullActivity.class);
                    activity.startActivity(intent);
                }

            }


        }


    }


}
