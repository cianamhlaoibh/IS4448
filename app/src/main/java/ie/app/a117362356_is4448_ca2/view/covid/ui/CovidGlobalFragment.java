package ie.app.a117362356_is4448_ca2.view.covid.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.GlobalSummary;
import ie.app.a117362356_is4448_ca2.model.covid.StatSummary;
import ie.app.a117362356_is4448_ca2.view.covid.adapter.CountryAdapter;
import ie.app.a117362356_is4448_ca2.view.utils.ErrorCallback;
import ie.app.a117362356_is4448_ca2.view.utils.OnClickCallBack;

public class CovidGlobalFragment extends Fragment implements OnClickCallBack, ErrorCallback, View.OnClickListener {

    RecyclerView rvCountries;
    List<StatSummary> stats;
    CountryAdapter adapter;
    ProgressBar pbLoad;
    ImageButton btnRefresh;
    TextView tvNewConfirmed, tvTotalConfirmed, tvNewDeaths, tvTotalDeaths, tvNewRecovered, tvTotalRecovered, tvOverview;
    SearchView searchView;
    CovidDao dao;

    public CovidGlobalFragment() {
        // Required empty public constructor
    }

    public static CovidGlobalFragment newInstance() {
        CovidGlobalFragment fragment = new CovidGlobalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_covid_global, container, false);
        tvNewConfirmed = root.findViewById(R.id.tvNewConfirmed);
        tvNewDeaths = root.findViewById(R.id.tvNewDeaths);
        tvNewRecovered = root.findViewById(R.id.tvNewRecovered);
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeath);
        tvTotalRecovered = root.findViewById(R.id.tvTotalActive);
        tvOverview = root.findViewById(R.id.tvOverview);
        pbLoad = root.findViewById(R.id.pbLoad);
        pbLoad.setVisibility(View.VISIBLE);
        btnRefresh = root.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

        rvCountries = root.findViewById(R.id.rvCountries);
        rvCountries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        stats = new ArrayList<>();
        adapter = new CountryAdapter(stats, getContext(), this);
        rvCountries.setAdapter(adapter);
        dao = new CovidDao(this);
        dao.selectGlobalSummary(getGlobalCallBack);

        searchView = root.findViewById(R.id.svCountries);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return root;
    }

    public final Handler getGlobalCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            GlobalSummary gs = (GlobalSummary) msg.obj;
            setGlobalFigures(gs.getGlobal());
            stats = gs.getCountries();
            adapter.updateDataSet(stats);
            setOverviewDate();
            pbLoad.setVisibility(View.GONE);
        }
    };

    private void setOverviewDate() {
        Date date = stats.get(stats.size()-1).getDate();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = df.format(date);
        tvOverview.setText("Overview as of " + date1);
    }

    //https://www.java67.com/2015/06/how-to-format-numbers-in-java.html#:~:text=In%20order%20to%20print%20numbers,number%20starting%20from%20the%20right.
    private void setGlobalFigures(StatSummary global) {
        NumberFormat format = NumberFormat.getInstance();
        tvNewConfirmed.setText(format.format(global.getNewConfirmed()));
        tvNewDeaths.setText(format.format(global.getNewDeaths()));
        tvNewRecovered.setText(format.format(global.getNewRecovered()));
        tvTotalConfirmed.setText(format.format(global.getTotalConfirmed()));
        tvTotalDeaths.setText(format.format(global.getTotalDeaths()));
        tvTotalRecovered.setText(format.format(global.getTotalRecovered()));
    }

    @Override
    public void OnClickCallback(StatSummary stat, int position) {
        CountrySummary fragment = CountrySummary.newInstance(stat);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onDataAccessError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRefresh:
                dao.selectGlobalSummary(getGlobalCallBack);
                btnRefresh.setVisibility(View.GONE);
                pbLoad.setVisibility(View.VISIBLE);
                break;
        }
    }
}
