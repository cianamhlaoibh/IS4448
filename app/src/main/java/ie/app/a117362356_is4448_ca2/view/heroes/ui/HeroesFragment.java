package ie.app.a117362356_is4448_ca2.view.heroes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.view.heroes.adapter.HeroesAdapter;

public class HeroesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView rvHeroes;
    HeroesAdapter adapter;
    ArrayList<Hero> heroes;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_heroes, container, false);
        rvHeroes = root.findViewById(R.id.rvHeroes);
        rvHeroes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        heroes = new ArrayList<>();
        Hero hero = new Hero("SpiderMan", "Peter Parker", 5, "Avengers");
        heroes.add(hero);
        adapter = new HeroesAdapter(heroes, getContext());
        rvHeroes.setAdapter(adapter);
        return root;
    }
}
