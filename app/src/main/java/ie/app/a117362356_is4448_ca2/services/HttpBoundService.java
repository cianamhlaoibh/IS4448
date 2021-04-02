package ie.app.a117362356_is4448_ca2.services;

/**
 * Created by Michael Gleeson on 14/02/2019
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class HttpBoundService extends Service {

    public HttpBoundService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new BackGroundBinder();
    }

    public class BackGroundBinder extends Binder {

//        private ThreadGroup HeroThreadGroup = new ThreadGroup("Hero");
//        public void selectHeroes(final Handler handler) {
//            new Thread(HeroThreadGroup, new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<Hero> heroList = HeroDao.selectHeroes();
//                    Message msg = new Message();
//                    msg.obj = heroList;
//                    handler.sendMessage(msg);
//                }
//            }).start();
//        }
//
//        public void createHero(final Hero hero, final Handler handler){
//            new Thread(HeroThreadGroup, new Runnable() {
//                @Override
//                public void run() {
//                    Hero h = HeroDao.insertHero(hero);
//                    Message msg = new Message();
//                    msg.obj = h;
//                    handler.sendMessage(msg);
//                }
//            }).start();
//        }
//
//        public void updateHero(final Hero hero, final Handler handler) {
//            new Thread(HeroThreadGroup, new Runnable() {
//                @Override
//                public void run() {
//                    Hero h = HeroDao.updateHero(hero);
//                    Message msg = new Message();
//                    msg.obj = h;
//                    handler.sendMessage(msg);
//                }
//            }).start();
//        }
//
//        public void deleteHero(final int heroId, final Handler handler) {
//            new Thread(HeroThreadGroup, new Runnable() {
//                @Override
//                public void run() {
//                    Boolean b = HeroDao.deleteHero(heroId);
//                    Message msg = new Message();
//                    msg.obj = b;
//                    handler.sendMessage(msg);
//                }
//            }).start();
//        }

//        private ThreadGroup CovidThreadGroup = new ThreadGroup("Covid");
//        public void getCovidStats(final String country, final Handler handler) {
//            new Thread(CovidThreadGroup, new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<CovidCountryStats> covidStatsList = CovidDao.selectCountryStats(country);
//                    Message msg = new Message();
//                    msg.obj = covidStatsList;
//                    handler.sendMessage(msg);
//                }
//            }).start();
//        }


    }
}