package ie.app.a117362356_is4448_ca2.view.heroes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.a117362356_is4448_ca2.view.heroes.ui.EditHeroFragment;
import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.Hero;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesViewHolder> {
    private List<Hero> heroes;
    private Context contx;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;

    public HeroesAdapter(List<Hero> heroes, Context contx) {
        this.heroes = heroes;
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
        notifyDataSetChanged();
    }
}

