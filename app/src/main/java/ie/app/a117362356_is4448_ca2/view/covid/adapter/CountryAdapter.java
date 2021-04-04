package ie.app.a117362356_is4448_ca2.view.covid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.covid.StatSummary;
import ie.app.a117362356_is4448_ca2.model.heroes.Hero;
import ie.app.a117362356_is4448_ca2.view.heroes.adapter.HeroesViewHolder;

public class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> {
    private List<StatSummary> stats;
    private Context context;

    public CountryAdapter(List<StatSummary> stats, Context context) {
        this.stats = stats;
        this.context = context;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.holder_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        final StatSummary summary = stats.get(position);
        holder.tvCountry.setText(summary.getCountry());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void updateDataSet(List<StatSummary> stats) {
        this.stats.clear();
        this.stats.addAll(stats);
        notifyDataSetChanged();
    }
}
