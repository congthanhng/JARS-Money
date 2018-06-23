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

public class GhiChepPLAYAdapter extends BaseAdapter {
    private  Context context;
    private int layout;
    List<GhiChepPLAY> listGhiChepPLAY;

    public GhiChepPLAYAdapter(Context context, int layout, List<GhiChepPLAY> listGhiChepPLAY) {
        this.context = context;
        this.layout = layout;
        this.listGhiChepPLAY = listGhiChepPLAY;
    }

    @Override
    public int getCount() {
        return listGhiChepPLAY.size();
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
        GhiChepPLAY ghiChepPLAY= listGhiChepPLAY.get(position);
        holder.txtMucChi.setText(ghiChepPLAY.getMucChi());
        holder.txtDienGiai.setText(ghiChepPLAY.getGhiChu());
        holder.txtSoTien.setText(ghiChepPLAY.getSoTien());
        holder.txtNgayChi.setText(ghiChepPLAY.getNgayChi());
        return convertView;
    }
}
