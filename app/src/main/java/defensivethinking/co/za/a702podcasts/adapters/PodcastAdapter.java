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
 * Created by Profusion on 2015-12-28.
 */
public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.BindingHolder>  {

    private List<Podcast> mPodcasts;

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
