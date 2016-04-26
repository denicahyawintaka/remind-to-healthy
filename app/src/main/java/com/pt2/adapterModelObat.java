package com.pt2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterModelObat extends ArrayAdapter<HashMap> {
    Context c;
    ArrayList<HashMap> obat;
    LayoutInflater inflater;

    public adapterModelObat(Context context, ArrayList<HashMap> obat) {
        super(context, R.layout.activity_adapter_model_obat, obat);

        this.c=context;
        this.obat=obat;
    }

    public class ViewHolder{
        TextView nameObat, dosisPeggunaan;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.activity_adapter_model_obat, null);
        }

        ViewHolder holder=new ViewHolder();

        holder.nameObat = (TextView) convertView.findViewById(R.id.namaObat);
        holder.dosisPeggunaan = (TextView) convertView.findViewById(R.id.dosisPenggunaan);

        holder.nameObat.setText(obat.get(position).get("nama_obat").toString());
       String penggunaan =  obat.get(position).get("dosis").toString()+" "+ obat.get(position).get("penggunaan").toString();
        holder.dosisPeggunaan.setText(penggunaan);
        return convertView;
    }
}
