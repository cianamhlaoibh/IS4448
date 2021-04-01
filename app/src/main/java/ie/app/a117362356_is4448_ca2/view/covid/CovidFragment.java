package ie.app.a117362356_is4448_ca2.view.covid;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.CovidStats;
import ie.app.a117362356_is4448_ca2.services.HttpBoundService;
import ie.app.a117362356_is4448_ca2.view.utils.ServiceReceiver;

/**
 * https://www.java67.com/2015/06/how-to-format-numbers-in-java.html#:~:text=In%20order%20to%20print%20numbers,number%20starting%20from%20the%20right.
 *
 * https://learntodroid.com/how-to-display-a-line-chart-in-your-android-app/
 */
public class CovidFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    TextView tvCases, tvDeaths, tvTotalConfirmed, tvTotalDeath, tvTotalActive, tvTotalRecovered;
    RadioGroup rgTimeRange;
    private LineChart lineChart;
    private ServiceReceiver serviceReceiver;
    protected HttpBoundService.BackGroundBinder httpBinder;
    ArrayList<CovidStats> stats;
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
        serviceReceiver = (ServiceReceiver) context;
    }

    @Override
    public void onResume() {
        super.onResume();
//        httpBinder = serviceReceiver.getBinder();
////        if (httpBinder != null) {
////            httpBinder.getCovidStats("ireland", getCallBack);
////        }
        CovidDao dao = new CovidDao();
        dao.selectCountryStats("ireland", getCallBack);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_covid, container, false);
        tvCases = root.findViewById(R.id.tvCases);
        tvDeaths = root.findViewById(R.id.tvDeaths);
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeath = root.findViewById(R.id.tvTotalDeath);
        tvTotalActive = root.findViewById(R.id.tvTotalActive);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        lineChart = root.findViewById(R.id.lcCases);
        rgTimeRange = root.findViewById(R.id.rgTimeRange);
        rgTimeRange.setOnCheckedChangeListener(this);
        configureLineChart();
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
            stats = (ArrayList<CovidStats>) msg.obj;
            setDailyFigures(stats);
            setOverviewToDate(stats);
            setupChartData(stats, "All");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupChartData(List<CovidStats> stats, String timeRange) {
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
                size= stats.size();
                List<CovidStats> pastMonthsStats = stats.subList(size - 29, size);
                for (int i = 1; i <= 28; i++) {
                    CovidStats stat = pastMonthsStats.get(i);
                    Date statDate = stat.getDate();
                    long statTime = statDate.getTime();
                    x = statTime;
                    y = stat.getConfirmed() - pastMonthsStats.get(i-1).getConfirmed();
                    dailyConfirmedCases.add(new Entry(x, y));
                }
                break;
            case "Last Week":
                size = stats.size();
                List<CovidStats> pastWeeksStats = stats.subList(size - 8, size);
                for (int i = 1; i <= 7; i++) {
                    CovidStats stat = pastWeeksStats.get(i);
                    Date statDate = stat.getDate();
                    long statTime = statDate.getTime();
                    x = statTime;
                    y = stat.getConfirmed() - pastWeeksStats.get(i-1).getConfirmed();
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
    private void setOverviewToDate(ArrayList<CovidStats> stats) {
        CovidStats statsToday = stats.get(stats.size() - 1);
        NumberFormat format = NumberFormat.getInstance();
        tvTotalConfirmed.setText(format.format(statsToday.getConfirmed()));
        tvTotalDeath.setText(format.format(statsToday.getDeaths()));
        tvTotalActive.setText(format.format(statsToday.getActive()));
        tvTotalRecovered.setText(format.format(statsToday.getRecovered()));
    }

    private void setDailyFigures(ArrayList<CovidStats> stats) {
        int size = stats.size();
        NumberFormat format = NumberFormat.getInstance();
        CovidStats today = stats.get(size - 1);
        CovidStats yesterday = stats.get(size - 2);
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
}
