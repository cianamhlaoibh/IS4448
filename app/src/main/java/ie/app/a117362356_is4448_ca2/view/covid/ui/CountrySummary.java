package ie.app.a117362356_is4448_ca2.view.covid.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.CountryDetail;
import ie.app.a117362356_is4448_ca2.model.covid.CountryStats;
import ie.app.a117362356_is4448_ca2.model.covid.StatSummary;
import ie.app.a117362356_is4448_ca2.view.utils.ErrorCallback;

/**
 * https://www.youtube.com/watch?v=118wylgD_ig
 * <p>
 * IS4447GoogleMapApp
 */
public class CountrySummary extends Fragment implements OnMapReadyCallback, ErrorCallback, View.OnClickListener {
    private static final String ARG_SUMMARY = "summary";
    private StatSummary summary;
    private ProgressBar pbLoad;
    private Toolbar toolbar;
    private TextView tvOverview;
    ImageButton btnRefresh;
    private CovidDao dao;
    ArrayList<CountryStats> stats;
    TextView tvNewConfirmed, tvTotalConfirmed, tvNewDeaths, tvTotalDeaths, tvNewRecovered, tvTotalRecovered;
    GoogleMap map;
    String dateTo;
    String dateFrom;

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    CountryDetail detail;
    private Bundle mapViewBundle;

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
        tvOverview =root.findViewById(R.id.tvOverview);
//        setHasOptionsMenu(true);

        dao = new CovidDao(this);
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateTo = df.format(today);
        dateFrom = df.format(yesterday());
        dao.selectCountryDetails(summary.getSlug(), dateFrom, dateTo, getDetailsCallback);

        pbLoad = root.findViewById(R.id.pbLoad);
        pbLoad.setVisibility(View.VISIBLE);

        btnRefresh = root.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

        initOverviewFigures();
        mMapView = root.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

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

    public final Handler getDetailsCallback = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            dao.selectCountryStats(summary.getSlug(), getSummaryCallBack);
            List<CountryDetail> details = (ArrayList<CountryDetail>) msg.obj;
            if (details.size() >= 1)
                detail = details.get(details.size() - 1);
        }
    };

    public final Handler getSummaryCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            stats = (ArrayList<CountryStats>) msg.obj;
            try {
                setOverviewDate();
                setCountryConfirmedCasesMarkers();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pbLoad.setVisibility(View.GONE);
        }
    };

    private void setOverviewDate() {
        Date date = stats.get(stats.size()-1).getDate();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = df.format(date);
        tvOverview.setText("Overview as of " + date1);
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
//                ActivityCompat#requestPermissions
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.
//            return;
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        this.map = map;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCountryConfirmedCasesMarkers() throws ParseException {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(detail.getLat(), detail.getLon()), 5));

        NumberFormat format = NumberFormat.getInstance();
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(today);
        List<CountryStats> todaysStats = new ArrayList<>();
        for (int i = stats.size() - 1; i >= 0; i--) {
            String date1 = df.format(stats.get(i).getDate());
            if (date1.equals(date)) {
                todaysStats.add(stats.get(i));
            } else {
                break;
            }
        }
        if (todaysStats.size() > 1) {
            //sort today's stats by number of confirmed cases in increasing order
            Comparator<CountryStats> comparator = new Comparator<CountryStats>() {
                @Override
                public int compare(CountryStats o1, CountryStats o2) {
                    return Float.compare(o1.getConfirmed(), o2.getConfirmed());
                }
            };
            todaysStats.sort(comparator);
            int count = 1;
            for (CountryStats stat : todaysStats) {
                // Add a marker in UCC and move the camera
                LatLng marker = new LatLng(stat.getLat(), stat.getLon());

                map.addMarker(new MarkerOptions()
                        .position(marker)
                        .title(stat.getProvince())
                        .snippet("Ranking: " + count + "/" + todaysStats.size() + ", Total Cases: " + format.format(stat.getConfirmed()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                double c = (double) stat.getConfirmed() / (double) summary.getTotalConfirmed();
                double factor = (c * 10.00);

                CircleOptions circleOptions = createCircle(marker, factor);
                map.addCircle(circleOptions);
                count++;
            }
        } else if (todaysStats.size() == 1) {
            CountryStats stat = todaysStats.get(0);
            // Add a marker in UCC and move the camera
            LatLng marker = new LatLng(detail.getLat(), detail.getLon());

            map.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(detail.getCountry())
                    .snippet("Total Confirmed Cases: " + format.format(summary.getTotalConfirmed()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            CircleOptions circleOptions = createCircle(marker, 1);
            map.addCircle(circleOptions);
        }
    }

    public CircleOptions createCircle(LatLng marker, double factor) {
        CircleOptions circleOptions = new CircleOptions()
                .center(marker)
                .radius(10000 * factor)
                .fillColor(0x00000000)
                .strokeColor(0xff0000ff) //red outline
                .strokeWidth(2); //opaque red fill
        return circleOptions;
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    //https://stackoverflow.com/questions/11425236/get-yesterdays-date-using-date
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Override
    public void onDataAccessError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                dao.selectCountryDetails(summary.getSlug(), dateFrom, dateTo, getDetailsCallback);
                btnRefresh.setVisibility(View.GONE);
                pbLoad.setVisibility(View.VISIBLE);
                break;
        }
    }
}
