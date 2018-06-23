package com.cong.thanh.jarsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class GhiChepNECAdapter extends BaseAdapter {
    private  Context context;
    private int layout;
    List<GhiChepNEC> listGhiChepNEC;

    public GhiChepNECAdapter(Context context, int layout, List<GhiChepNEC> listGhiChepNEC) {
        this.context = context;
        this.layout = layout;
        this.listGhiChepNEC = listGhiChepNEC;
    }

    @Override
    public int getCount() {
        return listGhiChepNEC.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtMucChi,txtSoTien,txtDienGiai,txtNgayChi;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            //ánh xạ
            holder.txtMucChi=(TextView)convertView.findViewById(R.id.txtVMucChi);
            holder.txtDienGiai=(TextView)convertView.findViewById(R.id.txtVDienGiai);
            holder.txtSoTien=(TextView)convertView.findViewById(R.id.txtVSoTien);
            holder.txtNgayChi=(TextView)convertView.findViewById(R.id.textViewNgayChi);

            convertView.setTag(holder);
        } else holder=(ViewHolder)convertView.getTag();
        //gán giá trị
        GhiChepNEC ghiChepNEC= listGhiChepNEC.get(position);
        holder.txtMucChi.setText(ghiChepNEC.getMucChi());
        holder.txtDienGiai.setText(ghiChepNEC.getGhiChu());
        holder.txtSoTien.setText(ghiChepNEC.getSoTien());
        holder.txtNgayChi.setText(ghiChepNEC.getNgayChi());
        return convertView;
    }
}
