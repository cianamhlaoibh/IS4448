package ie.app.a117362356_is4448_ca2.view.heroes.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.Hero;

/**
 * https://www.youtube.com/watch?v=sJ-Z9G0SDhc
 */
public class HeroesAdapter extends RecyclerView.Adapter<HeroesViewHolder> implements Filterable {
    private List<Hero> heroes;
    private List<Hero> heroesAll;
    private List<Hero> heroesSpinnerFiltered;
    private Context contx;
    Boolean isFiltered;
    String filterTeam;
    float filterRating;

    public HeroesAdapter(List<Hero> heroes, Context contx) {
        this.heroes = heroes;
        heroesAll = new ArrayList<>(heroes);
        heroesSpinnerFiltered = new ArrayList<>();
        this.isFiltered = false;
        this.contx = contx;
    }

    @NonNull
    @Override
    public HeroesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contx).inflate(R.layout.holder_heroes, parent, false);
        return new HeroesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HeroesViewHolder holder, int position) {
        final Hero hero = heroes.get(position);
        holder.tvName.setText(hero.getName());
        holder.tvRealName.setText(hero.getRealName());
        holder.rbRating.setRating(hero.getRating());
        holder.tvTeam.setText(hero.getTeam());
    }


    @Override
    public int getItemCount() {
        return heroes.size();
    }

    public void updateDataSet(ArrayList<Hero> heroes) {
        this.heroes.clear();
        this.heroes.addAll(heroes);
        this.heroesAll.addAll(heroes);
        notifyDataSetChanged();
    }

    public void itemRemoved(int postion) {
        this.heroes.remove(postion);
        notifyItemRemoved(postion);
    }

    public void itemAdded(Hero hero, int postion) {
        this.heroes.add(postion, hero);
        notifyItemInserted(postion);
    }

    @Override
    public Filter getFilter() {
        return heroFilter;
    }

    private Filter heroFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Hero> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //if search field empty return all
                if (isFiltered) {
                    filteredList.addAll(heroesSpinnerFiltered);
                } else {
                    filteredList.addAll(heroesAll);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                if(isFiltered) {
                    for (Hero h : heroesSpinnerFiltered) {
                        if (h.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(h);
                        }
                    }
                }else{
                    for (Hero h : heroesAll) {
                        if (h.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(h);
                        }
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            heroes.clear();
            heroes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void spinnerFilter(final String team, final String rating, final Handler handler) {
        final List<Hero> filteredList = new ArrayList<>();
        if (team.equals("Teams") && rating.equals("Ratings")) {
            isFiltered = false;
            filteredList.addAll(heroesAll);
            heroes.clear();
            heroes.addAll(filteredList);
            notifyDataSetChanged();
        } else if (!team.equals("Teams") && rating.equals("Ratings")) {
            for (Hero h : heroesAll) {
                if (h.getTeam().toLowerCase().contains(team.toLowerCase())) {
                    filteredList.add(h);
                }
            }
            isFiltered = true;
            heroes.clear();
            heroes.addAll(filteredList);
            heroesSpinnerFiltered.addAll(filteredList);
            notifyDataSetChanged();
        } else if (team.equals("Teams") && !rating.equals("Ratings")) {
            float ratingF = covertToFloat(rating);
            for (Hero h : heroesAll) {
                if (h.getRating() == ratingF) {
                    filteredList.add(h);
                }
            }
            isFiltered = true;
            heroes.clear();
            heroes.addAll(filteredList);
            heroesSpinnerFiltered.addAll(filteredList);
            notifyDataSetChanged();
        } else if (!team.equals("Teams") && !rating.equals("Ratings")) {
            float ratingF = covertToFloat(rating);
            for (Hero h : heroesAll) {
                if (h.getTeam().toLowerCase().contains(team.toLowerCase()) && h.getRating() == ratingF) {
                    filteredList.add(h);
                }
            }
            isFiltered = true;
            heroes.clear();
            heroes.addAll(filteredList);
            heroesSpinnerFiltered.addAll(filteredList);
            notifyDataSetChanged();
        }
    }

    private float covertToFloat(String rating) {
        float ratingF;
        switch (rating) {
            case "1-star":
                ratingF = 1;
                break;
            case "2-stars":
                ratingF = 2;
                break;
            case "3-stars":
                ratingF = 3;
                break;
            case "4-stars":
                ratingF = 4;
                break;
            case "5-stars":
                ratingF = 5;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rating);
        }
        return ratingF;
    }

}

