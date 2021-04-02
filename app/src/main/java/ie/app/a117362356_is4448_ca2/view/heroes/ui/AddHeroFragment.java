package ie.app.a117362356_is4448_ca2.view.heroes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.dao.HeroDao;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.services.HttpBoundService;
import ie.app.a117362356_is4448_ca2.view.utils.ServiceReceiver;

/**
 * https://stackoverflow.com/questions/13067033/how-to-access-activity-variables-from-a-fragment-android
 */
public class AddHeroFragment extends Fragment implements View.OnClickListener{

    Toolbar toolbar;
    Button btnAdd, btnCanel;
    EditText etName, etRealName;
    RatingBar rbRating;
    Spinner spTeam;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddHeroFragment() {
    }

    public static AddHeroFragment newInstance() {
        AddHeroFragment fragment = new AddHeroFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_hero, container, false);
        //https://programming.vip/docs/add-toolbar-menu-to-fragment.html
        toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName = root.findViewById(R.id.etName);
        etRealName = root.findViewById(R.id.etRealName);
        rbRating = root.findViewById(R.id.rbRating);
        spTeam = root.findViewById(R.id.spTeam);
        btnAdd = root.findViewById(R.id.btnAdd);
        btnCanel = root.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(this);
        btnCanel.setOnClickListener(this);
        return root;
    }

    public final Handler myCallBack = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Hero h = (Hero) msg.obj;
            Toast.makeText(getContext(), h.getName() + " added!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //https://stackoverflow.com/questions/46175610/back-button-in-toolbar-not-working-in-android-fragment
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String name, realName, team;
                float rating;
                name = etName.getText().toString().trim();
                realName = etRealName.getText().toString().trim();
                rating = rbRating.getRating();
                team = spTeam.getSelectedItem().toString();
                Hero h = new Hero(name, realName, rating, team);
                HeroDao dao = new HeroDao();
                try {
                    dao.insertHero(h, insertHeroCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCancel:
                //https://stackoverflow.com/questions/20812922/how-to-close-the-current-fragment-by-using-button-like-the-back-button
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    public final Handler insertHeroCallback = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Boolean error = (Boolean) msg.obj;
            if (error == false) {
                getActivity().getFragmentManager().popBackStack();
                Toast.makeText(getContext(), "Hero added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Error occurred!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}