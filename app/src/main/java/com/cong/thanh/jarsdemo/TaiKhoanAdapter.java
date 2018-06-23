package com.cong.thanh.jarsdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaiKhoanAdapter extends BaseAdapter {

    DeleteDataTaiKhoan deleteDataTaiKhoan;
    private Context context;
    private int layout;
    private List<TaiKhoan> taiKhoanList;

    public TaiKhoanAdapter(Context context, int layout, List<TaiKhoan> taiKhoanList) {
        this.context = context;
        this.layout = layout;
        this.taiKhoanList = taiKhoanList;
    }

    @Override
    public int getCount() {
        return taiKhoanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //khai báo holder
    private class ViewHolder{
        TextView txtTenTaiKhoan,txtSoTien,txtGhichu;
        ImageView imgDelete;

    }
    //gán ViewHolder (không phải load lại những View đã load trước đó rồi, giúp tiết kiệm và giảm thời gian xử lý)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        deleteDataTaiKhoan= (DeleteDataTaiKhoan) context;
        ViewHolder holder;
        if(convertView==null){

            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            //Ánh xạ View
            holder.txtTenTaiKhoan=(TextView)convertView.findViewById(R.id.tvTenTK);
            holder.txtSoTien=(TextView)convertView.findViewById(R.id.txtSotien);
            holder.txtGhichu=(TextView)convertView.findViewById(R.id.txtGhichu);
            holder.imgDelete=(ImageView)convertView.findViewById(R.id.imageCancel);

            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }

        //gán giá trị
        final TaiKhoan taiKhoan=taiKhoanList.get(position);
        holder.txtTenTaiKhoan.setText("Tên tài khoản: " + taiKhoan.getTenTK());
        holder.txtSoTien.setText("Số tiền: "+taiKhoan.getSoTien()+" VNĐ");
        holder.txtGhichu.setText("Ghi chú: "+taiKhoan.getGhiChu());

        //bắt sự kiện xóa khi nhấn vào imangCancel
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogXoa(taiKhoan.getId(), context);
            }
            //hiển thị dialog xóa
            private void DialogXoa(final int id, final Context context) {
                    final AlertDialog.Builder dialogxoa=new AlertDialog.Builder(context);
                    dialogxoa.setMessage("Bạn muốn xóa tài khoản này ?");

                    //thực hiện xóa khi chọn ok
                    dialogxoa.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //xóa dữ liệu từ csdl
                            MainActivity.database.QueryData("DELETE FROM TaiKhoan WHERE Id = '"+id+"'");
                            Toast.makeText(context,"Tài khoản này đã được xóa",Toast.LENGTH_SHORT).show();
                            //truyền biến và tham chiếu đến interface ở MainActivity
                            deleteDataTaiKhoan.giatrixoa(true);

                        }
                    });
                    //thực hiện hủy khi chọn cancel
                    dialogxoa.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDataTaiKhoan.giatrixoa(false);

                        }
                    });
                    dialogxoa.show();
            }
        });

        return convertView;
    }
}
