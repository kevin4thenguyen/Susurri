package edu.hmc.willarcherkevin.susurri;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class ChatRoomFragment extends Fragment{

    ListView mainListView;
    private ChatAdapter mainAdapter;

    // for updating
    MyCustomReceiver updateReceiver;


    //default room defined here b/c fragments should have default constructors
    String room = "";

    //Fragments are required to have empty constructors
    //so use a setRoom method to set the room
    public void setRoom(String r){
        room = r;
    }
    public String getRoom(){ return room; }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(
                R.layout.activity_chat, container, false);

        // Access the ListView
        mainListView = (ListView) rootView.findViewById(R.id.main_listview);

        mainAdapter = new ChatAdapter(getActivity(), room);
        mainListView.setAdapter(mainAdapter);

        mainAdapter.loadObjects();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onPause();
        //unregister our receiver
        getActivity().unregisterReceiver(updateReceiver);
    }

    @Override
    public void onStart(){
        updateChat();
        super.onResume();
        updateReceiver = new MyCustomReceiver(this, room);
        IntentFilter intentFilter = new IntentFilter(
                "edu.hmc.willarcherkevin.susurri." + room.toUpperCase().replaceAll("\\s","_"));
        getActivity().registerReceiver(updateReceiver, intentFilter);
    }

    public void updateChat(){
        mainAdapter.loadObjects();
        mainAdapter.notifyDataSetChanged();
    }


}
