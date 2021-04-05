package ie.app.a117362356_is4448_ca2.view.covid.ui;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.CountryStats;
import ie.app.a117362356_is4448_ca2.model.covid.GlobalSummary;
import ie.app.a117362356_is4448_ca2.view.utils.ErrorCallback;

/**
 * https://www.java67.com/2015/06/how-to-format-numbers-in-java.html#:~:text=In%20order%20to%20print%20numbers,number%20starting%20from%20the%20right.
 * <p>
 * https://learntodroid.com/how-to-display-a-line-chart-in-your-android-app/
 */
public class CovidFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ErrorCallback {

    TextView tvCases, tvDeaths, tvTotalConfirmed, tvTotalDeath, tvTotalActive, tvTotalRecovered;
    RadioGroup rgTimeRange;
    TextView tvOverview;
    Button btnGlobal;
    ImageButton btnRefresh;
    private LineChart lineChart;
    ProgressBar pbLoad;
    CovidDao dao;
    ArrayList<CountryStats> stats;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CovidFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CovidFragment newInstance(String param1, String param2) {
        CovidFragment fragment = new CovidFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_covid, container, false);
        dao = new CovidDao(this);
        dao.selectCountryStats("ireland", getCallBack);

        tvCases = root.findViewById(R.id.tvCases);
        tvDeaths = root.findViewById(R.id.tvDeaths);
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeath = root.findViewById(R.id.tvTotalDeath);
        tvTotalActive = root.findViewById(R.id.tvTotalActive);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        tvOverview = root.findViewById(R.id.tvOverview);
        lineChart = root.findViewById(R.id.lcCases);
        rgTimeRange = root.findViewById(R.id.rgTimeRange);
        rgTimeRange.setOnCheckedChangeListener(this);
        configureLineChart();
        pbLoad = root.findViewById(R.id.pbLoad);
        pbLoad.setVisibility(View.VISIBLE);
        btnGlobal = root.findViewById(R.id.btnGlobal);
        btnGlobal.setOnClickListener(this);
        btnRefresh = root.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        //pbLoad.setAnimation(android.animation.ValueAnimator.sDurationScale == 0.0f);
        return root;
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Covid-19 Cases Ireland");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });
    }

    public final Handler getCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            stats = (ArrayList<CountryStats>) msg.obj;
            setDailyFigures(stats);
            setOverviewToDate(stats);
            setupChartData(stats, "All");
            updateWidget(stats);
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

    private void updateWidget(ArrayList<CountryStats> stats) {
        Application app = getActivity().getApplication();
        Intent intent = new Intent(getContext(), WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getContext().sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupChartData(List<CountryStats> stats, String timeRange) {
        ArrayList<Entry> dailyConfirmedCases = new ArrayList<>();
        float x, y;
        int size;
        switch (timeRange) {
            case "All":
                for (int i = 0; i < stats.size() - 1; i++) {
                    Date date = stats.get(i).getDate();
                    x = date.getTime();
                    if (i == 0) {
                        y = 163057 - 159144; //setting the cases for day before api active for Ireland so graph does not start at 0 (https://www.worldometers.info/coronavirus/country/ireland/)
                    } else {
                        y = stats.get(i + 1).getConfirmed() - stats.get(i).getConfirmed();
                    }
                    dailyConfirmedCases.add(new Entry(x, y));
                }
                break;
            case "Last Month": //shows cases for past 28 days
                size = stats.size();
                List<CountryStats> pastMonthsStats = stats.subList(size - 29, size);
                for (int i = 1; i <= 28; i++) {
                    CountryStats stat = pastMonthsStats.get(i);
                    Date statDate = stat.getDate();
                    long statTime = statDate.getTime();
                    x = statTime;
                    y = stat.getConfirmed() - pastMonthsStats.get(i - 1).getConfirmed();
                    dailyConfirmedCases.add(new Entry(x, y));
                }
                break;
            case "Last Week":
                size = stats.size();
                List<CountryStats> pastWeeksStats = stats.subList(size - 8, size);
                for (int i = 1; i <= 7; i++) {
                    CountryStats stat = pastWeeksStats.get(i);
                    Date statDate = stat.getDate();
                    long statTime = statDate.getTime();
                    x = statTime;
                    y = stat.getConfirmed() - pastWeeksStats.get(i - 1).getConfirmed();
                    dailyConfirmedCases.add(new Entry(x, y));
                }
                break;
        }
        Comparator<Entry> comparator = new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return Float.compare(o1.getX(), o2.getX());
            }
        };
        dailyConfirmedCases.sort(comparator);
        setLineChartData(dailyConfirmedCases);
    }

    private void setLineChartData(ArrayList<Entry> dailyConfirmedCases) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet highLineDataSet = new LineDataSet(dailyConfirmedCases, "Confirmed Daily Cases");
        highLineDataSet.setDrawValues(false);
        highLineDataSet.setLineWidth(3);
        highLineDataSet.setColor(R.color.colorAccent);
        highLineDataSet.setCircleColor(R.color.colorAccent);
        dataSets.add(highLineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    //https://www.java67.com/2015/06/how-to-format-numbers-in-java.html#:~:text=In%20order%20to%20print%20numbers,number%20starting%20from%20the%20right.
    private void setOverviewToDate(ArrayList<CountryStats> stats) {
        CountryStats statsToday = stats.get(stats.size() - 1);
        NumberFormat format = NumberFormat.getInstance();
        tvTotalConfirmed.setText(format.format(statsToday.getConfirmed()));
        tvTotalDeath.setText(format.format(statsToday.getDeaths()));
        tvTotalActive.setText(format.format(statsToday.getActive()));
        tvTotalRecovered.setText(format.format(statsToday.getRecovered()));
    }

    private void setDailyFigures(ArrayList<CountryStats> stats) {
        int size = stats.size();
        NumberFormat format = NumberFormat.getInstance();
        CountryStats today = stats.get(size - 1);
        CountryStats yesterday = stats.get(size - 2);
        long cases = today.getConfirmed() - yesterday.getConfirmed();
        long deaths = today.getDeaths() - yesterday.getDeaths();
        tvCases.setText(format.format(cases));
        tvDeaths.setText(format.format(deaths));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbAll:
                setupChartData(stats, "All");
                break;
            case R.id.rbMonthly:
                setupChartData(stats, "Last Month");
                break;
            case R.id.rbWeekly:
                setupChartData(stats, "Last Week");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnGlobal:
                CovidGlobalFragment fragment = CovidGlobalFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case R.id.btnRefresh:
                dao.selectCountryStats("ireland", getCallBack);
                btnRefresh.setVisibility(View.GONE);
                pbLoad.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDataAccessError(String error) {
        pbLoad.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Error retrieving data. Please refresh!", Toast.LENGTH_SHORT).show();
        btnRefresh.setVisibility(View.VISIBLE);
    }
}
