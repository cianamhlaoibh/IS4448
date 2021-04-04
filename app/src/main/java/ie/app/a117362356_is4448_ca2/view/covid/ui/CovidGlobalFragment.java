package ie.app.a117362356_is4448_ca2.view.covid.ui;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.GlobalSummary;
import ie.app.a117362356_is4448_ca2.model.covid.StatSummary;
import ie.app.a117362356_is4448_ca2.view.covid.adapter.CountryAdapter;

public class CovidGlobalFragment extends Fragment {

    RecyclerView rvCountries;
    List<StatSummary> stats;
    CountryAdapter adapter;
    ProgressBar pbLoad;
    TextView tvNewConfirmed, tvTotalConfirmed, tvNewDeaths, tvTotalDeaths, tvNewRecovered, tvTotalRecovered;

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
        CovidDao dao = new CovidDao();
        dao.selectGlobalSummary(getGlobalCallBack);
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
        pbLoad = root.findViewById(R.id.pbLoad);
        pbLoad.setVisibility(View.VISIBLE);

        rvCountries = root.findViewById(R.id.rvCountries);
        rvCountries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        stats = new ArrayList<>();
        adapter = new CountryAdapter(stats, getContext());
        rvCountries.setAdapter(adapter);
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
            pbLoad.setVisibility(View.GONE);
        }
    };
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
}
