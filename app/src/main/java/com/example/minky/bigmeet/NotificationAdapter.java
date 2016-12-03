package com.example.minky.bigmeet;

import android.app.Notification;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.dift.ui.SwipeToAction;

/**
 * Created by minky on 29/06/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Nottification> items;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Nottification item = items.get(position);
        NotificationHolder vh = (NotificationHolder) holder;
/*        vh.titleView.setText(item.getTitle());
        vh.authorView.setText(item.getAuthor());
        vh.imageView.setImageURI(Uri.parse(item.getImageUrl()));*/
        vh.titleView.setText(item.getTitle());
        vh.bodyView.setText(item.getBody());
        vh.data = item;
    }

    public class NotificationHolder extends SwipeToAction.ViewHolder<Nottification> {
        public TextView titleView;
        public TextView bodyView;

        public NotificationHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.title);
            bodyView = (TextView) v.findViewById(R.id.body);
        }
    }

    public NotificationAdapter(List<Nottification> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
