package ie.app.a117362356_is4448_ca2.view.covid.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import ie.app.a117362356_is4448_ca2.R;

public class CountryViewHolder extends RecyclerView.ViewHolder {
    TextView tvCountry;
    ConstraintLayout clCountry;
    public CountryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCountry = itemView.findViewById(R.id.tvCountry);
        clCountry = itemView.findViewById(R.id.clCountry);
    }
}

