package com.cong.thanh.jarsdemo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GhChepAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GhiChep> ghichepList;

    public GhChepAdapter(Context context, int layout, List<GhiChep> ghichepList) {
        this.context = context;
        this.layout = layout;
        this.ghichepList = ghichepList;
    }

    @Override
    public int getCount() {
        return ghichepList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //khai báo phương thức holder
    private class ViewHolder{
        ImageView HinhGhiChep;
        TextView TenMucChi,DienGiai,SoTien,Jars,dateSelect;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            //Ánh xạ
            holder.HinhGhiChep=(ImageView)convertView.findViewById(R.id.imgHinhghichep);
            holder.TenMucChi=(TextView)convertView.findViewById(R.id.txtVMucChi);
            holder.DienGiai=(TextView)convertView.findViewById(R.id.txtVDienGiai);
            holder.SoTien=(TextView)convertView.findViewById(R.id.txtVSoTien);
            holder.Jars=(TextView)convertView.findViewById(R.id.txtVJars);
            holder.dateSelect=(TextView)convertView.findViewById(R.id.textViewNgayChi);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        //gán giá trị
        GhiChep ghiChep =ghichepList.get(position);
//        holder.HinhGhiChep.setImageResource(ghiChep.getImgHinhDanhSach());
        holder.TenMucChi.setText(ghiChep.getNameMucChi());
        holder.DienGiai.setText(ghiChep.getDienGiai());
        holder.SoTien.setText(ghiChep.getSoTien());
        holder.Jars.setText(ghiChep.getJars());
        holder.dateSelect.setText(ghiChep.getNgaychi());
        return convertView;
    }
}
