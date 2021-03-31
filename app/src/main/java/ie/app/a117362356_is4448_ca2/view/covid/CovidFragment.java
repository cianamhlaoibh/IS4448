package ie.app.a117362356_is4448_ca2.view.covid;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ie.app.a117362356_is4448_ca2.R;
import ie.app.a117362356_is4448_ca2.model.CovidStats;
import ie.app.a117362356_is4448_ca2.model.Hero;
import ie.app.a117362356_is4448_ca2.services.HttpBoundService;
import ie.app.a117362356_is4448_ca2.view.utils.HeroServiceReceiver;

public class CovidFragment extends Fragment {

    private HeroServiceReceiver serviceReceiver;
    protected HttpBoundService.BackGroundBinder httpBinder;
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
        serviceReceiver = (HeroServiceReceiver) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        httpBinder = serviceReceiver.getBinder();
        if (httpBinder != null) {
            httpBinder.getCovidStats("ireland",getCallBack);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_covid, container, false);
    }

    public final Handler getCallBack = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<CovidStats> stats = (ArrayList<CovidStats>) msg.obj;
        }
    };
}
