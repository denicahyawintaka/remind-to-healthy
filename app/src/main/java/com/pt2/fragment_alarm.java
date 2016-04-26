package com.pt2;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_alarm extends DialogFragment {
    ListView listView;
    Button OKE;
    static String DialogboxTitle;



    public interface alarmListener   {
       ArrayList<HashMap> getObat();
    }

    //---empty constructor required
    public fragment_alarm() {


    }

    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.activity_fragment_alarm, container);
             listView = (ListView) view.findViewById(R.id.listObat);
        OKE= (Button) view.findViewById(R.id.dismis);

        //---get the EditText and Button views


                //---gets the calling activity
             alarmListener activity = (alarmListener) getActivity();
       ArrayList<HashMap> obat = activity.getObat();
        adapterModelObat adpter = new adapterModelObat(view.getContext(), obat);
        listView.setAdapter(adpter);
        //---dismiss the alert

        OKE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
              //  dismiss();


        //---set the title for the dialog
       // getDialog().setTitle(DialogboxTitle);

        return view;

}
}

