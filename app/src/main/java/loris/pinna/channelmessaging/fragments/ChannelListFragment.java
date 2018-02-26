package loris.pinna.channelmessaging.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import loris.pinna.channelmessaging.ChannelListActivity;
import loris.pinna.channelmessaging.MessageListActivity;
import loris.pinna.channelmessaging.R;
import loris.pinna.channelmessaging.adapter.MySimpleArrayAdapter;
import loris.pinna.channelmessaging.classes.Channel;
import loris.pinna.channelmessaging.http.HttpPostHandler;
import loris.pinna.channelmessaging.http.JsonLoginResponse;
import loris.pinna.channelmessaging.http.PostRequest;
import loris.pinna.channelmessaging.listeners.OnDownloadListener;

public class ChannelListFragment extends Fragment {
    private ListView lvMyListView;
    private Button friends;
    private Button channels;
    private static final String PREFS_NAME = "access_token";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channellist,container);
        lvMyListView = (ListView)v.findViewById(R.id.channels);
        lvMyListView.setOnItemClickListener((ChannelListActivity)getActivity());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void fillList(ArrayList<Channel> list) {
        lvMyListView.setAdapter(new MySimpleArrayAdapter(getActivity(), list));
    }
}
