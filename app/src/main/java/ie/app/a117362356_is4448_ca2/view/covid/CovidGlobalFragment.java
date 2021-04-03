package ie.app.a117362356_is4448_ca2.view.covid;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.GlobalSummary;

public class CovidGlobalFragment extends Fragment {

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
//        tvCases = root.findViewById(R.id.tvCases);
//        tvDeaths = root.findViewById(R.id.tvDeaths);
//        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
//        tvTotalDeath = root.findViewById(R.id.tvTotalDeath);
//        tvTotalActive = root.findViewById(R.id.tvTotalActive);
//        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
//        lineChart = root.findViewById(R.id.lcCases);
//        rgTimeRange = root.findViewById(R.id.rgTimeRange);
//        rgTimeRange.setOnCheckedChangeListener(this);
//        configureLineChart();
//        pbLoad = root.findViewById(R.id.pbLoad);
//        pbLoad.setVisibility(View.VISIBLE);
        //pbLoad.setAnimation(android.animation.ValueAnimator.sDurationScale == 0.0f);
        return root;
    }

    public final Handler getGlobalCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            GlobalSummary gs = (GlobalSummary) msg.obj;
//            setDailyFigures(stats);
//            setOverviewToDate(stats);
//            setupChartData(stats, "All");
//            updateWidget(stats);
//            pbLoad.setVisibility(View.GONE);
        }
    };
}
