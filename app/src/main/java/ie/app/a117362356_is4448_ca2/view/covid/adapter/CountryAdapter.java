package ie.app.a117362356_is4448_ca2.view.covid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.covid.StatSummary;
import ie.app.a117362356_is4448_ca2.model.heroes.Hero;
import ie.app.a117362356_is4448_ca2.view.heroes.adapter.HeroesViewHolder;
import ie.app.a117362356_is4448_ca2.view.heroes.ui.EditHeroFragment;
import ie.app.a117362356_is4448_ca2.view.utils.OnClickCallBack;

public class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> implements Filterable {
    private List<StatSummary> stats;
    private List<StatSummary> statsAll;
    private Context context;
    private OnClickCallBack callBack;

    public CountryAdapter(List<StatSummary> stats, Context context, OnClickCallBack callBack) {
        this.stats = stats;
        statsAll = new ArrayList<>(stats);
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.holder_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, final int position) {
        final StatSummary summary = stats.get(position);
        holder.tvCountry.setText(summary.getCountry());
        holder.clCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callBack.OnClickCallback(summary, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void updateDataSet(List<StatSummary> stats) {
        this.stats.clear();
        this.stats.addAll(stats);
        this.statsAll.addAll(stats);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StatSummary> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(statsAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (StatSummary s : statsAll) {
                    if (s.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(s);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stats.clear();
            stats.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
