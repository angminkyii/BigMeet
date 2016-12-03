package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by minky on 23/06/2016.
 */
public class GroupDialog {

    ListView groupList;
    ArrayAdapter<String> lAdapter;
    List<Group> group;
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_group);

        groupList = (ListView) dialog.findViewById(R.id.listView3);
        lAdapter = new ArrayAdapter<>(dialog.getContext(),android.R.layout.simple_list_item_1);
        groupList.setAdapter(lAdapter);
        group = MainPageActivity.groupList;

        for(Group gp : group){
            lAdapter.add(gp.getName());
        }

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddEventActivity.mHandler.obtainMessage(AddEventActivity.GROUPSELECTED,position).sendToTarget();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

