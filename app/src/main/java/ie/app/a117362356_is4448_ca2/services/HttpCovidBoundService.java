package ie.app.a117362356_is4448_ca2.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.dao.CovidDao;
import ie.app.a117362356_is4448_ca2.dao.HeroDao;
import ie.app.a117362356_is4448_ca2.model.CovidStats;
import ie.app.a117362356_is4448_ca2.model.Hero;

public class HttpCovidBoundService extends Service {

    public HttpCovidBoundService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new HttpCovidBoundService.BackGroundBinder();
    }

    public class BackGroundBinder extends Binder {

        private ThreadGroup myThreadGroup = new ThreadGroup("Covid");

        public void getCovidStats(final String country, final Handler handler) {
            new Thread(myThreadGroup, new Runnable() {
                @Override
                public void run() {
                    ArrayList<CovidStats> covidStatsList = CovidDao.selectCountryStats(country);
                    Message msg = new Message();
                    msg.obj = covidStatsList;
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }
}
