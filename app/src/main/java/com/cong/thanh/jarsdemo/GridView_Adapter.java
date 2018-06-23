package com.cong.thanh.jarsdemo;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridView_Adapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GridViewJARS> listJars;

    public GridView_Adapter(Context context, int layout, List<GridViewJARS> listJars) {
        this.context = context;
        this.layout = layout;
        this.listJars = listJars;
    }

    @Override
    public int getCount() {
        return listJars.size();
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
        TextView txtJARS;
        TextView txtPhanTram;
        ImageView imgHinhJars;
        TextView txtBanDau;
        TextView txtConLai,txtChangeNameJARS;
    }
    //gán ViewHolder (không phải load lại những View đã load trước đó rồi, giúp tiết kiệm và giảm thời gian xử lý)

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            //ánh xạ để xủ lý
            holder.txtJARS          =(TextView)convertView.findViewById(R.id.textVjars);
            holder.txtPhanTram      =(TextView)convertView.findViewById(R.id.texVPhantram);
            holder.imgHinhJars      =(ImageView) convertView.findViewById(R.id.imgHinhjars);
            holder.txtBanDau        =(TextView) convertView.findViewById(R.id.textVSoTienBanDau);
            holder.txtConLai        =(TextView) convertView.findViewById(R.id.textVSoTienConLai);
            holder.txtChangeNameJARS=(TextView)convertView.findViewById(R.id.textViewchangeJARS);
            convertView.setTag(holder);

        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        //gán giá trị
        GridViewJARS gridViewJARS=listJars.get(position);
        switch (gridViewJARS.getNameJars())
        {
            case "NEC"  : holder.txtChangeNameJARS.setText("(Cần thiết)");break;
            case "LTSS" : holder.txtChangeNameJARS.setText("(Dài hạn)");break;
            case "EDU"  : holder.txtChangeNameJARS.setText("(Học tập)");break;
            case "GIVE" : holder.txtChangeNameJARS.setText("(Cho đi)");break;
            case "PLAY" : holder.txtChangeNameJARS.setText("(Hưởng thụ)");break;
            case "FFA"  : holder.txtChangeNameJARS.setText("(Đầu tư)");break;
        }
        holder.txtJARS.setText(gridViewJARS.getNameJars());
        holder.txtPhanTram.setText(gridViewJARS.getPhanTram());
        holder.imgHinhJars.setImageResource(gridViewJARS.getHinhjars());
        holder.txtBanDau.setText(gridViewJARS.getTienBanDau());
        holder.txtConLai.setText(gridViewJARS.getTienConLai());
        return convertView;
    }
}
