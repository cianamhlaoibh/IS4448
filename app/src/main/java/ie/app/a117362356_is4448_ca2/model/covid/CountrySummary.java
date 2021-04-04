package ie.app.a117362356_is4448_ca2.model.covid;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.NumberFormat;
import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.dao.HeroDao;
import ie.app.a117362356_is4448_ca2.view.heroes.adapter.HeroesAdapter;
import ie.app.a117362356_is4448_ca2.view.heroes.ui.HeroesFragment;

public class CountrySummary extends Fragment {
    private static final String ARG_SUMMARY = "summary";
    private StatSummary summary;
    private ProgressBar pbLoad;
    private Toolbar toolbar;
    private CovidDao dao;
    ArrayList<CountryStats> stats;
    TextView tvNewConfirmed, tvTotalConfirmed, tvNewDeaths, tvTotalDeaths, tvNewRecovered, tvTotalRecovered;


    public CountrySummary() {
        // Required empty public constructor
    }

    public static CountrySummary newInstance(StatSummary summary) {
        CountrySummary fragment = new CountrySummary();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SUMMARY, summary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            summary = getArguments().getParcelable(ARG_SUMMARY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country_summary, container, false);
        //https://programming.vip/docs/add-toolbar-menu-to-fragment.html
        toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("COVID-19 - " + summary.getCountry());

        tvNewConfirmed = root.findViewById(R.id.tvNewConfirmed);
        tvNewDeaths = root.findViewById(R.id.tvNewDeaths);
        tvNewRecovered = root.findViewById(R.id.tvNewRecovered);
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeath);
        tvTotalRecovered = root.findViewById(R.id.tvTotalActive);
//        setHasOptionsMenu(true);
//        rvHeroes = root.findViewById(R.id.rvHeroes);
//        rvHeroes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        heroes = new ArrayList<>();
//        adapter = new HeroesAdapter(heroes, getContext());
//        rvHeroes.setAdapter(adapter);
        dao = new CovidDao();
//
//        spRating = root.findViewById(R.id.spRating);
//        spTeam = root.findViewById(R.id.spTeam);
//        spTeam.setOnItemSelectedListener(this);
//        spRating.setOnItemSelectedListener(this);
//
        pbLoad = root.findViewById(R.id.pbLoad);
        pbLoad.setVisibility(View.VISIBLE);

        initOverviewFigures();

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(rvHeroes);
        return root;
    }

    private void initOverviewFigures() {
        NumberFormat format = NumberFormat.getInstance();
        tvNewConfirmed.setText(format.format(summary.getNewConfirmed()));
        tvTotalConfirmed.setText(format.format(summary.getTotalConfirmed()));
        tvNewDeaths.setText(format.format(summary.getNewDeaths()));
        tvTotalDeaths.setText(format.format(summary.getTotalDeaths()));
        tvNewRecovered.setText(format.format(summary.getNewRecovered()));
        tvTotalRecovered.setText(format.format(summary.getTotalRecovered()));
    }


    @Override
    public void onResume() {
        super.onResume();
        dao.selectCountryStats(summary.getSlug(), getSummaryCallBack);
    }

    public final Handler getSummaryCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            stats = (ArrayList<CountryStats>) msg.obj;
            pbLoad.setVisibility(View.GONE);
        }
    };

}
