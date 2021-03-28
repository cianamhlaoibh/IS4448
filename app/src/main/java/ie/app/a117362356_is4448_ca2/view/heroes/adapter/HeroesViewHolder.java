package ie.app.a117362356_is4448_ca2.view.heroes.adapter;

import android.os.TestLooperManager;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ie.app.a117362356_is4448_ca2.R;

public class HeroesViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvRealName, tvTeam;
    RatingBar rbRating;
    public HeroesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvRealName = itemView.findViewById(R.id.tvRealName);
        tvTeam = itemView.findViewById(R.id.tvTeam);
        rbRating = itemView.findViewById(R.id.rbRating);
    }
}
