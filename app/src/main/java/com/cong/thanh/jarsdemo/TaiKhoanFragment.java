package com.cong.thanh.jarsdemo;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaiKhoanFragment extends ListFragment{

    ArrayList<TaiKhoan> arrayTaiKhoan;
    TaiKhoanAdapter adapter;

//    Database database;

    Button ThemTaiKhoan;
    TextView TongTien,txtHienCo;

    int vitri=-1;
    long tong=0;

    public TaiKhoanFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tai_khoan, container, false);

        //ánh xạ
        ThemTaiKhoan=(Button)view.findViewById(R.id.btThemTK);
        TongTien=(TextView)view.findViewById(R.id.tvTongtien);
        txtHienCo=(TextView)view.findViewById(R.id.textViewTongcon);

        //sự kiện khi ấn thêm tài khoản
        ThemTaiKhoan.setOnClickListener(mOnClickListener);


        //khai báo list và thiết lập
        AddArrayTK();

        //xử lý settext cho Tong tien
        settext();
        return view;
    }


    //sự kiện thêm tài khoản khi ấn vào button thêm tài khoản
    private Button.OnClickListener mOnClickListener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogThemTaiKhoan();
        }
        //hàm gọi dialog Them tài khoản và xử lý sự kiện trong dialog
        private void DialogThemTaiKhoan() {
            final Dialog dialogThemTaiKhoan=new Dialog(getActivity());
            dialogThemTaiKhoan.setContentView(R.layout.dialog_them_taikhoan);
            dialogThemTaiKhoan.show();


            //ánh xạ xml dialog_them_taikhoan
            final EditText edtNhapTenTaiKhoan=(EditText)dialogThemTaiKhoan.findViewById(R.id.editTextNhapTenTaiKhoan);
            final EditText edtNhapSoTien=(EditText)dialogThemTaiKhoan.findViewById(R.id.editTextNhapSoTien);
            final EditText edtNhapGhiChu=(EditText)dialogThemTaiKhoan.findViewById(R.id.editTextNhapGhiChu);
            Button btnHuy=(Button)dialogThemTaiKhoan.findViewById(R.id.button_huy_them_taikhoan);
            Button btnLuu=(Button)dialogThemTaiKhoan.findViewById(R.id.button_luu_them_tai_khoan);

            //sự kiện khi ấn vào nút hủy
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogThemTaiKhoan.cancel();
                    //hoặc dialogThemTaiKhoan.dismiss();
                }
            });

            //format số nhập vào
            edtNhapSoTien.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    edtNhapSoTien.removeTextChangedListener(this);

                    try {
                        String originalString = s.toString();


                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }
                        Long longval;
                        longval = Long.parseLong(originalString);
//                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                        formatter.applyPattern("#,###,###,###");
//                        String formattedString = formatter.format(longval);

                        //setting text after format to EditText
                        edtNhapSoTien.setText(format(longval));
                        edtNhapSoTien.setSelection(edtNhapSoTien.getText().length());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    edtNhapSoTien.addTextChangedListener(this);

                }
            });

            //sự kiện click khi lưu thêm số tiền
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edtNhapSoTien.length()==0||edtNhapSoTien.getText().toString().equals("0")) Toast.makeText(getActivity(),"Số tiền không được bỏ trống hoặc bằng 0",Toast.LENGTH_SHORT).show();
                    else if(edtNhapSoTien.length()>17)Toast.makeText(getActivity(),"Số tiền không vượt quá 13 chữ số",Toast.LENGTH_SHORT).show();
                    else {
                        String ten=edtNhapTenTaiKhoan.getText().toString();
                        String sotien=edtNhapSoTien.getText().toString().replaceAll(",","");
                        long st = 0;
                        try {
                            st=Long.parseLong(sotien);
                        }catch (NumberFormatException abt){Toast.makeText(getActivity(),"Nhập lại",Toast.LENGTH_SHORT).show(); }
                        
                        String ghichu=edtNhapGhiChu.getText().toString();

                        //lưu vào csdl
                        MainActivity.database.QueryData("INSERT INTO TaiKhoan VALUES(null, '"+ten+"', '"+st+"', '"+ghichu+"')");
                        //cập nhật lại danh sách
                        //duyệt csdl và thêm vào listView
                        GetDataSoTien();
                        adapter.notifyDataSetChanged();
                        settext();
                        dialogThemTaiKhoan.cancel();


                    }

                }
            });

        }
    };

    //sự kiện chọn item từ list để chỉnh sửa item
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        vitri=position;
        DialogChinhSuaTaiKhoan(arrayTaiKhoan.get(vitri).getId());

    }

    private void DialogChinhSuaTaiKhoan(final int i) {
        final Dialog dialogChinhSuaTaiKhoan=new Dialog(getActivity());
        dialogChinhSuaTaiKhoan.setContentView(R.layout.dialog_chinhsua_taikhoan);


        //ánh xạ xml dialog_them_taikhoan
        final EditText edtNhapTenTaiKhoan=(EditText)dialogChinhSuaTaiKhoan.findViewById(R.id.editTextNhapTenTaiKhoan);
        final EditText edtNhapSoTien=(EditText)dialogChinhSuaTaiKhoan.findViewById(R.id.editTextNhapSoTien);
        final EditText edtNhapGhiChu=(EditText)dialogChinhSuaTaiKhoan.findViewById(R.id.editTextNhapGhiChu);
        Button btnXoa=(Button)dialogChinhSuaTaiKhoan.findViewById(R.id.button_xoa_taikhoan);
        Button btnLuu=(Button)dialogChinhSuaTaiKhoan.findViewById(R.id.button_luu_tai_khoan);
        //gán dữ liệu đã có
        edtNhapTenTaiKhoan.setText(arrayTaiKhoan.get(vitri).getTenTK().toString().trim());
        edtNhapSoTien.setText(arrayTaiKhoan.get(vitri).getSoTien().toString().trim());
        edtNhapGhiChu.setText(arrayTaiKhoan.get(vitri).getGhiChu().toString().trim());


        //format số nhập vào
        edtNhapSoTien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtNhapSoTien.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();


                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    Long longval;
                    longval = Long.parseLong(originalString);

//                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                    formatter.applyPattern("#,###,###,###");
//                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edtNhapSoTien.setText(format(longval));
                    edtNhapSoTien.setSelection(edtNhapSoTien.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edtNhapSoTien.addTextChangedListener(this);

            }
        });
        //sự kiện khi ấn vào nút hủy
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                arrayTaiKhoan.remove(vitri);
                dialogChinhSuaTaiKhoan.cancel();
                //hoặc dialogThemTaiKhoan.dismiss();
            }
        });


        //sự kiện click lưu thay đổi
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNhapSoTien.length()==0||edtNhapSoTien.getText().toString().equals("0")) Toast.makeText(getActivity(),"Số tiền không được bỏ trống",Toast.LENGTH_SHORT).show();
                else if(edtNhapSoTien.length()>17)Toast.makeText(getActivity(),"Số tiền không vượt quá 13 chữ số",Toast.LENGTH_SHORT).show();
                else {

                    //chỉnh sửa dữ liệu sau khi chọn button lưu
//                    arrayTaiKhoan.get(vitri).setSoTien(edtNhapSoTien.getText().toString().trim());
//                    arrayTaiKhoan.get(vitri).setTenTK(edtNhapTenTaiKhoan.getText().toString().trim());
//                    arrayTaiKhoan.get(vitri).setGhiChu(edtNhapGhiChu.getText().toString().trim());
                    String ten=edtNhapTenTaiKhoan.getText().toString();
                    String sotien=edtNhapSoTien.getText().toString().replaceAll(",","");
                    long st = 0;
                    try {
                        st=Long.parseLong(sotien);
                    }catch (NumberFormatException abt){Toast.makeText(getActivity(),"Nhập lại",Toast.LENGTH_SHORT).show();}
                    String ghichu=edtNhapGhiChu.getText().toString();
                    MainActivity.database.QueryData("UPDATE TaiKhoan SET TenST = '"+ten+"', SoTienST = '"+st+"',GhiChuSoTien = '"+ghichu+"'  WHERE Id = '"+i+"'");

                    GetDataSoTien();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"Đã lưu chỉnh sửa",Toast.LENGTH_SHORT).show();
                    //cập nhật lại danh sách
                    settext();
                    dialogChinhSuaTaiKhoan.cancel();


                }

            }

        });
        dialogChinhSuaTaiKhoan.show();
    }

    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    public void GetDataSoTien(){
        //select data
        Cursor dataSoTien=MainActivity.database.GetData("SELECT * FROM TaiKhoan");
        //xoa mảng trong app để khi duyệt xong csdl sẽ lấy bảng từ csdl, tránh tình trạng bảng từ csdl sẽ lồng vào bảng cũ của app
        arrayTaiKhoan.clear();
        //duyệt hết trong bảng đã chọn trong csdl
        while (dataSoTien.moveToNext()){
            int id=dataSoTien.getInt(0);
            String ten=dataSoTien.getString(1);
            long sotien=dataSoTien.getLong(2);
            String ghichu=dataSoTien.getString(3);
            arrayTaiKhoan.add(new TaiKhoan(id,ten,format(sotien),ghichu));
        }

    }
    //hàm settext cho textView Tong Tien
    private void settext(){
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        conlai=tongtien-tongchi;
        TongTien.setText("Tổng tiền ban đầu: "+format(tongtien)+ " VNĐ");
        txtHienCo.setText("Tổng còn: "+format(conlai)+" VNĐ");

    }

    private  void AddArrayTK(){
        arrayTaiKhoan = new ArrayList<>();
        //tạo database
//        database=new Database(getActivity(),"sotien.sqlite",null,1);
        //tạo bảng neu chua co tu database
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS SoTien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenST VARCHAR(200), SoTienST MONEY, GhiChuSoTien VARCHAR(200))");
        //database.QueryData("INSERT INTO SoTien VALUES(null, 'tên ST', '20000', 'ghi chú')");
        GetDataSoTien();
//        database.QueryData("DELETE FROM SoTien");

        //set adapter de chay
        adapter=new TaiKhoanAdapter(getContext(),R.layout.row_taikhoan,arrayTaiKhoan);
        setListAdapter(adapter);

    }


}
