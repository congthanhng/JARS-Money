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

public class GhiChepEDUAdapter extends BaseAdapter {
    private  Context context;
    private int layout;
    List<GhiChepEDU> listGhiChepEDU;

    public GhiChepEDUAdapter(Context context, int layout, List<GhiChepEDU> listGhiChepEDU) {
        this.context = context;
        this.layout = layout;
        this.listGhiChepEDU = listGhiChepEDU;
    }

    @Override
    public int getCount() {
        return listGhiChepEDU.size();
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
        GhiChepEDU ghiChepEDU= listGhiChepEDU.get(position);
        holder.txtMucChi.setText(ghiChepEDU.getMucChi());
        holder.txtDienGiai.setText(ghiChepEDU.getGhiChu());
        holder.txtSoTien.setText(ghiChepEDU.getSoTien());
        holder.txtNgayChi.setText(ghiChepEDU.getNgayChi());
        return convertView;
    }
}
