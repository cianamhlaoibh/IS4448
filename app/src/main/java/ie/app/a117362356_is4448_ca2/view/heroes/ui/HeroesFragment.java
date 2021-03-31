package ie.app.a117362356_is4448_ca2.view.heroes.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.HeroDao;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.services.HttpBoundService;
import ie.app.a117362356_is4448_ca2.view.heroes.adapter.HeroesAdapter;
import ie.app.a117362356_is4448_ca2.view.utils.HeroServiceReceiver;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


/**
 * //https://androidwave.com/bottom-navigation-bar-android-example/
 * <p>
 * - Source - Swipe gestures in Recycler View | Android https://www.youtube.com/watch?v=rcSNkSJ624U
 * *    - Created By yoursTRULY
 * <p>
 * <p>
 * https://stackoverflow.com/questions/13067033/how-to-access-activity-variables-from-a-fragment-android
 */
public class HeroesFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rvHeroes;
    private HeroesAdapter adapter;
    private ArrayList<Hero> heroes;
    //private FloatingActionButton fabAdd;
    private Toolbar toolbar;
    private HeroDao dao;
    private HeroServiceReceiver serviceReceiver;
    protected HttpBoundService.BackGroundBinder httpBinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HeroesFragment() {
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
    public static HeroesFragment newInstance(String param1, String param2) {
        HeroesFragment fragment = new HeroesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        serviceReceiver = (HeroServiceReceiver) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_heroes, container, false);
        //https://programming.vip/docs/add-toolbar-menu-to-fragment.html
        toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        rvHeroes = root.findViewById(R.id.rvHeroes);
        rvHeroes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        heroes = new ArrayList<>();
        adapter = new HeroesAdapter(heroes, getContext());
        rvHeroes.setAdapter(adapter);
        dao = new HeroDao();

        //fabAdd = root.findViewById(R.id.fabAdd);
        //fabAdd.setOnClickListener(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvHeroes);
        return root;
    }

    public final Handler getCallBack = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            heroes = (ArrayList<Hero>) msg.obj;
            //notifing data set changed from within adapter has adapter.notifyDataSetChanged() was not working here
            adapter.updateDataSet(heroes);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        httpBinder = serviceReceiver.getBinder();
        if (httpBinder != null) {
            httpBinder.selectHeroes(getCallBack);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAdd:
                //https://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
                AppCompatActivity activity = (AppCompatActivity) getContext();
                AddHeroFragment fragment = AddHeroFragment.newInstance();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                break;
        }
    }

    //    https://www.youtube.com/watch?v=sJ-Z9G0SDhc
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return;
    }

    private int deletePostion;
    private Hero deleteHero;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    if (httpBinder != null) {
                        deletePostion = position;
                        deleteHero = heroes.get(deletePostion);
                        httpBinder.deleteHero(deleteHero.getId(), deleteCallBack);
                    }
                    break;
                case ItemTouchHelper.RIGHT:
                    //https://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    EditHeroFragment fragment = EditHeroFragment.newInstance(heroes.get(position));
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //Swipe Decorator library by Paolo Mantalto - https://github.com/xabaras/RecyclerViewSwipeDecorator
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public final Handler deleteCallBack = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Boolean result = (Boolean) msg.obj;
            if (result == true) {
                String message = "Are you sure you want to delete" + deleteHero.getName();
                Snackbar.make(rvHeroes, message, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                heroes.add(deletePostion, deleteHero);
                                adapter.notifyItemInserted(deletePostion);
                                httpBinder.createHero(deleteHero, createCallBack);
                            }
                        }).show();
            } else {
                Toast.makeText(getContext(), "Error occurred! Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public final Handler createCallBack = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };
}

