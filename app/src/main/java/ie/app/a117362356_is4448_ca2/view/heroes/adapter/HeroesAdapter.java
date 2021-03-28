package ie.app.a117362356_is4448_ca2.view.heroes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.Hero;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesViewHolder> {
    private List<Hero> heroes;
    private Context contx;

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
    public void onBindViewHolder(@NonNull HeroesViewHolder holder, int position) {
        Hero hero = heroes.get(position);
        holder.tvName.setText(hero.getName());
        holder.tvRealName.setText(hero.getRealName());
        holder.rbRating.setNumStars(hero.getRating());
        holder.tvTeam.setText(hero.getTeam());
    }

    @Override
    public int getItemCount() {
        return heroes.size();
    }
}

