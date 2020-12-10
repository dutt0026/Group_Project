package com.example.finalgroupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EventListAdapter extends BaseAdapter {
    private List<Event> eventList;
    private Context context;
    private LayoutInflater inflater;

    public EventListAdapter(List<Event> events, Context context){
        this.eventList = events;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Event getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.event,parent,false);

        TextView eventName = (TextView) view.findViewById(R.id.eventName);
        eventName.setText(getItem(position).getEventTitle());

        return view;
    }
}
