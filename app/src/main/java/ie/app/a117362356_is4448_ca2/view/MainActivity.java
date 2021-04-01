package ie.app.a117362356_is4448_ca2.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.services.HttpBoundService;
import ie.app.a117362356_is4448_ca2.view.covid.CovidFragment;
import ie.app.a117362356_is4448_ca2.view.heroes.ui.AddHeroFragment;
import ie.app.a117362356_is4448_ca2.view.heroes.ui.HeroesFragment;
import ie.app.a117362356_is4448_ca2.view.utils.ServiceReceiver;

//https://androidwave.com/bottom-navigation-bar-android-example/

public class MainActivity extends AppCompatActivity implements ServiceReceiver {

    BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
                AddHeroFragment fragment = AddHeroFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
            }
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_heros:
                            openFragment(HeroesFragment.newInstance("", ""));
                            fabAdd.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.navigation_covid:
                            openFragment(CovidFragment.newInstance("", ""));
                            fabAdd.setVisibility(View.GONE);
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected boolean isBound = false;
    protected HttpBoundService.BackGroundBinder httpBinder;
    protected ServiceConnection conn = new BackGroundServiceConnection();

    @Override
    public HttpBoundService.BackGroundBinder getBinder() {
        if(isBound) {
            return httpBinder;
        }
        return null;
    }

    protected class BackGroundServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            httpBinder = (HttpBoundService.BackGroundBinder) service;
            openFragment(HeroesFragment.newInstance("", ""));
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound= false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent boundServiceIntent = new Intent(this, HttpBoundService.class);
        bindService(boundServiceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the background service
        if (isBound) {
            unbindService(conn);
            isBound = false;
        }
    }
}