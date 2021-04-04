package ie.app.a117362356_is4448_ca2.view.covid.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.model.covid.CountryStats;


/**
 * https://www.linkedin.com/learning/search?keywords=android%20widget&u=92780010
 */
public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    Context context;
    AppWidgetManager appWidgetManager;
    int[] appWidgetIds;

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        CovidDao dao = new CovidDao();
        dao.selectCountryStats("ireland", getCallBack);
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;
    }

    public final Handler getCallBack = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            ArrayList<CountryStats> stats = (ArrayList<CountryStats>) msg.obj;
            long cases, deaths;
            int size;
            size = stats.size();
            CountryStats today = stats.get(size);
            CountryStats yesterday = stats.get(size - 1);
            cases = today.getConfirmed() - yesterday.getConfirmed();
            deaths = today.getDeaths() - yesterday.getDeaths();

            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(
                        context.getPackageName(), R.layout.widget_layout);
                remoteViews.setTextViewText(R.id.tvCases, String.valueOf(cases));
                remoteViews.setTextViewText(R.id.tvDeaths, String.valueOf(deaths));
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    };
}
