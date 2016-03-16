package defensivethinking.co.za.a702podcasts.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import defensivethinking.co.za.a702podcasts.BR;
import defensivethinking.co.za.a702podcasts.R;
import defensivethinking.co.za.a702podcasts.model.Podcast;

/**
 * Created by kgundula on 2015-12-28.
 */
public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.BindingHolder>  {

    private List<Podcast> mPodcasts;

    /*
        When not using DataBinding Library your BindingHolder will look like the commented code below.

        public static class BindingHolder extends RecyclerView.ViewHolder  {

            public TextView podcast_name;
            public TextView podcast_pub_date;

            BindingHolder(View itemView) {
                super(itemView);

                podcast_name = (TextView) itemView.findViewById(R.id.podcast_name);
                podcast_pub_date = (TextView)itemView.findViewById(R.id.podcast_pub_date);
            }

        }

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast, parent, false);
            return new BindingHolder(v);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {

            final Podcast podcast = mPodcasts.get(position);

            if ( podcast != null ) {
                holder.podcast_name.setText(podcast.getItemTitle());
                holder.podcast_description.setText(podcast.getItemPubDate());

             }

        }
    */

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
    public PodcastAdapter(List<Podcast> mPodcasts) {
        this.mPodcasts = mPodcasts;
    }

    @Override
    public PodcastAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast, parent, false);
        BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PodcastAdapter.BindingHolder holder, int position) {
        final Podcast podcast = mPodcasts.get(position);
        holder.getBinding().setVariable(BR.podcast, podcast);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mPodcasts.size();
    }
}
