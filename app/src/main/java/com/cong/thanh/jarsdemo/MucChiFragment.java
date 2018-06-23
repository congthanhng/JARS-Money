package com.cong.thanh.jarsdemo;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MucChiFragment extends Fragment {

    List<String>listdataHeader ;
    HashMap<String,List<String>> listdataChild;
    MucChiAdapter adapter;

    ExpandableListView elv;
    Button btnThemMucChi;
    int vitriChild=-1;
    int vitriGroup=-1;
    public MucChiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_muc_chi, container, false);
        //khai báo ánh xạ
        elv=(ExpandableListView)view.findViewById(R.id.elvList);
        btnThemMucChi=(Button)view.findViewById(R.id.buttonThemMucChi);
        //khai báo và thiết lập list
        AddControl();
        //sự kiện click khi chọn 1 item con
        elv.setOnChildClickListener(mOnChildClickListener);
        //sự kiện khi click vào thêm mục chi
        btnThemMucChi.setOnClickListener(mOnClickListener);

        return view;
    }

    //sự kiện khi click vào thêm mục chi
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogThemMucChi();
        }

        private void dialogThemMucChi() {
            final Dialog DialogThemMucChi=new Dialog(getActivity());
            DialogThemMucChi.setContentView(R.layout.dialog_them_mucchi);
            DialogThemMucChi.show();

            //ánh xạ
            RadioGroup radioGroupJars=(RadioGroup)DialogThemMucChi.findViewById(R.id.radioGroup);
            final RadioButton radioButtonNEC=(RadioButton)DialogThemMucChi.findViewById(R.id.rdNEC);
            final RadioButton radioButtonLTSS=(RadioButton)DialogThemMucChi.findViewById(R.id.rdLTSS);
            final RadioButton radioButtonEDU=(RadioButton)DialogThemMucChi.findViewById(R.id.rdEDU);
            final RadioButton radioButtonGIVE=(RadioButton)DialogThemMucChi.findViewById(R.id.rdGIVE);
            final RadioButton radioButtonPLAY=(RadioButton)DialogThemMucChi.findViewById(R.id.rdPLAY);
            final RadioButton radioButtonFFA=(RadioButton)DialogThemMucChi.findViewById(R.id.rdFFA);
            final EditText edtMucChi=(EditText)DialogThemMucChi.findViewById(R.id.editTextTenMucChi);
            Button btnHuy=(Button)DialogThemMucChi.findViewById(R.id.button_huy_mucchi);
            Button btnLuu=(Button)DialogThemMucChi.findViewById(R.id.button_luu_mucchi);

            //sự kiện chọn item trong radio group
//            radioGroupJars.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    switch (checkedId){
//                        case R.id.rdNEC: ;break;
//                        case R.id.rdLTSS: break;
//                        case R.id.rdEDU: break;
//                        case R.id.rdGIVE: break;
//                        case R.id.rdPLAY: break;
//                        case R.id.rdFFA: break;
//
//                    }
//
//                }
//            });

            //sự kiện lưu
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mucchi=edtMucChi.getText().toString().trim();
                    if(mucchi.length()!=0){
                        if(radioButtonNEC.isChecked())  MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', '"+mucchi+"')");
                        if(radioButtonLTSS.isChecked()) MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'LTSS', '"+mucchi+"')");
                        if(radioButtonEDU.isChecked())  MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', '"+mucchi+"')");
                        if(radioButtonGIVE.isChecked()) MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', '"+mucchi+"')");
                        if(radioButtonPLAY.isChecked()) MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', '"+mucchi+"')");
                        if(radioButtonFFA.isChecked())  MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'FFA', '"+mucchi+"')");
                        GetDaTaMucChi();
                        adapter.notifyDataSetChanged();
                        DialogThemMucChi.cancel();
                    }else Toast.makeText(getActivity(),"Vui lòng không để trống tên mục chi",Toast.LENGTH_SHORT).show();
                }
            });
            //sự kiện khi ấn hủy
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemMucChi.cancel();
                }
            });
        }
    };
    //sự kiện khi chọn item child sẽ đến dialog chỉnh sửa
    ExpandableListView.OnChildClickListener mOnChildClickListener=new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            vitriChild=childPosition;
            vitriGroup=groupPosition;
            DialogChinhSuaMucChi();
            return true;
        }

        private void DialogChinhSuaMucChi() {
            final Dialog dialogChinhSuaMucChi=new Dialog(getActivity());
            dialogChinhSuaMucChi.setContentView(R.layout.dialog_chinhsua_mucchi);
            dialogChinhSuaMucChi.show();

            //ánh xạ

            final EditText edtchinhsuaMucChi=(EditText)dialogChinhSuaMucChi.findViewById(R.id.editTextChinhsuaMucChi);
            Button btnXoa=(Button)dialogChinhSuaMucChi.findViewById(R.id.button_xoa_mucchi);
            Button btnLuu=(Button)dialogChinhSuaMucChi.findViewById(R.id.button_luu_mucchi);

            final String muccon=listdataChild.get(listdataHeader.get(vitriGroup)).get(vitriChild).toString().trim();
            edtchinhsuaMucChi.setText(muccon);


            //sự kiện xóa
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogXoa();

                }

                private void DialogXoa() {
                    AlertDialog.Builder dialogXoa = new AlertDialog.Builder(getActivity());
                    dialogXoa.setMessage("Bạn có muốn xóa mục chi này?");
                    dialogXoa.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.database.QueryData("DELETE FROM MucChi WHERE MucCon = '"+muccon+"'");
                            GetDaTaMucChi();
                            adapter.notifyDataSetChanged();
                            dialogChinhSuaMucChi.cancel();
                            Toast.makeText(getActivity(),"Đã xóa mục chi",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogXoa.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogXoa.show();
                }
            });
            // sự kiện lưu chỉnh sửa
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edtchinhsuaMucChi.length()==0) Toast.makeText(getActivity(),"Tên mục chi không được bỏ trống",Toast.LENGTH_SHORT).show();
                    else {
                        MainActivity.database.QueryData("UPDATE MucChi SET MucCon = '"+edtchinhsuaMucChi.getText().toString().trim()+"' WHERE MucCon = '"+muccon+"'");
                        GetDaTaMucChi();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"Đã lưu chỉnh sửa",Toast.LENGTH_SHORT).show();
                        dialogChinhSuaMucChi.cancel();
                    }

                }
            });
        }
    };
    private void GetDaTaMucChi(){
        Cursor dataMucChi = MainActivity.database.GetData("SELECT * FROM MucChi ORDER BY MucCon");
        listdataChild.get(listdataHeader.get(0)).clear();
        listdataChild.get(listdataHeader.get(1)).clear();
        listdataChild.get(listdataHeader.get(2)).clear();
        listdataChild.get(listdataHeader.get(3)).clear();
        listdataChild.get(listdataHeader.get(4)).clear();
        listdataChild.get(listdataHeader.get(5)).clear();
        while(dataMucChi.moveToNext()){
            String muccha= dataMucChi.getString(1);
            String muccon=dataMucChi.getString(2);
            if(listdataHeader.get(0).equals(muccha)) listdataChild.get(listdataHeader.get(0)).add(muccon);
            if(listdataHeader.get(1).equals(muccha)) listdataChild.get(listdataHeader.get(1)).add(muccon);
            if(listdataHeader.get(2).equals(muccha)) listdataChild.get(listdataHeader.get(2)).add(muccon);
            if(listdataHeader.get(3).equals(muccha)) listdataChild.get(listdataHeader.get(3)).add(muccon);
            if(listdataHeader.get(4).equals(muccha)) listdataChild.get(listdataHeader.get(4)).add(muccon);
            if(listdataHeader.get(5).equals(muccha)) listdataChild.get(listdataHeader.get(5)).add(muccon);
        }
    }
    private void AddControl() {

        listdataHeader=new ArrayList<>();
        listdataChild=new HashMap<String,List<String>>();
        //tạo bảng cơ sở dữ liệu mục chi nếu chưa tồn tại
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS MucChi(Id INTEGER PRIMARY KEY AUTOINCREMENT, MucCha VARCHAR(5), MucCon VARCHAR(100))");
        //Thêm dữ liệu vào bảng csdl Mục chi
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Ăn uống')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Hóa đơn tiền Điện')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Hóa đơn tiền Nước')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền điện thoại')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền Internet')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'GAS')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền cáp truyền hình')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Đi chợ/Siêu thị')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Xe cộ/Đi lại')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Dụng cụ sinh hoạt')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'Sửa chữa/Bảo trì thiết bị')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'NEC', 'y tế/Sức khỏe')");
//
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua xe')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua nhà')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua điện thoại')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua vàng')");
//
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', 'Sách')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', 'Khóa học kỹ năng')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', 'Học phí')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', 'Làm đẹp')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'EDU', 'Giao Lưu/Quan hệ')");
//
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', 'Cưới Hỏi')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', 'Ma Chay')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', 'Từ Thiện')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', 'Biếu Tặng')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'GIVE', 'Thăm Hỏi')");
//
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', 'Du Lịch')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', 'Vui Chơi Giải Trí')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', 'Phim Ảnh')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', 'Ca Nhạc')");
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'PLAY', 'Thể Thao')");
//
//        MainActivity.database.QueryData("INSERT INTO MucChi VALUES(null, 'FFA', 'Đầu Tư')");

        //tạo dữ liệu cho header
        listdataHeader.add("NEC");
        listdataHeader.add("LTSS");
        listdataHeader.add("EDU");
        listdataHeader.add("GIVE");
        listdataHeader.add("PLAY");
        listdataHeader.add("FFA");


        //tạo dữ liệu cho child
        List<String>NEC=new ArrayList<String>();
        List<String>LTSS=new ArrayList<String>();
        List<String>EDU=new ArrayList<String>();
        List<String>GIVE=new ArrayList<String>();
        List<String>PLAY=new ArrayList<String>();
        List<String>FFA=new ArrayList<String>();
        //đưa dữ liệu của child vào
        listdataChild.put(listdataHeader.get(0),NEC);
        listdataChild.put(listdataHeader.get(1),LTSS);
        listdataChild.put(listdataHeader.get(2),EDU);
        listdataChild.put(listdataHeader.get(3),GIVE);
        listdataChild.put(listdataHeader.get(4),PLAY);
        listdataChild.put(listdataHeader.get(5),FFA);
        GetDaTaMucChi();
//        MainActivity.database.QueryData("DROP TABLE MucChi");
        adapter=new MucChiAdapter(getActivity(),listdataHeader,listdataChild);
        elv.setAdapter(adapter);

    }

}
