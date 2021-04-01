package ie.app.a117362356_is4448_ca2.view.covid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import ie.app.a117362356_is4448_ca2.R;


/**
 * https://www.linkedin.com/learning/search?keywords=android%20widget&u=92780010
 */
public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        //Special foodItem = new Special(R.string.pizza, R.drawable.pizza);
        //int imageId = foodItem.getImageId();
        //String foodName = context.getString(foodItem.getNameId());

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.tvCases, "410");
            remoteViews.setTextViewText(R.id.tvDeaths, "6");
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }

    }
}
