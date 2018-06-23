package com.cong.thanh.jarsdemo;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class GhiChepFragment extends ListFragment {

    //Khai báo
    ArrayList<GhiChep> arrayGhiChep;
    GhChepAdapter adapter;
    int vitri=-1;

    Button   btnThemGhiChep;
    TextView txtTongTiendaChi;
    TextView txtHienCo;
    Calendar calendar;
    public GhiChepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ghi_chep, container, false);

        //Khai báo và ánh xạ Button
        btnThemGhiChep  =(Button)view.findViewById(R.id.buttonThemGhiChep);
        txtTongTiendaChi=(TextView)view.findViewById(R.id.textViewTongtiendachi);
        txtHienCo       =(TextView)view.findViewById(R.id.textViewHienCo);

        //sự kiện thêm Ghi chép
        btnThemGhiChep.setOnClickListener(mOnClickListener);
        //khai báo calendar
        calendar=Calendar.getInstance();

        //khai báo và thiết lập ListView
        AddGhiChep();
        return view;
    }
    //sự kiện khi click vào thêm ghi chép
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogThemGhiChep();
        }

        //hàm gọi dialog Them ghi chép và xử lý sự kiện trong dialog
        private void DialogThemGhiChep() {
            final int ngay    =calendar.get(calendar.DATE);
            final int thang   =calendar.get(calendar.MONTH);
            final int nam     =calendar.get(calendar.YEAR);
            final Dialog dialogThemGhiChep=new Dialog(getActivity());
            dialogThemGhiChep.setContentView(R.layout.dialog_them_ghichep);
            dialogThemGhiChep.show();

            //ánh xạ xml dialog_them_ghichep
            final EditText edtNhapSoTien=(EditText)dialogThemGhiChep.findViewById(R.id.editTextNhapTien);
            final EditText edtNhapGhiChu=(EditText)dialogThemGhiChep.findViewById(R.id.eTextNhapGhiChu);
            final TextView txtMucChi    =(TextView) dialogThemGhiChep.findViewById(R.id.texViewMucChi) ;
            final TextView txtDataChange=(TextView)dialogThemGhiChep.findViewById(R.id.textViewDateChange);
            ImageView imgDatePicker     =(ImageView)dialogThemGhiChep.findViewById(R.id.imageDatePicker);
            Button btnHuy               =(Button)dialogThemGhiChep.findViewById(R.id.button_huy_them_ghichep);
            Button btnLuu               =(Button)dialogThemGhiChep.findViewById(R.id.button_luu_them_ghichep);

            final Button btnSeclectMucChi=(Button)dialogThemGhiChep.findViewById(R.id.buttonSelectMucChi);

            //set thời gian cho textView datachange
            txtDataChange.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
            //sự kiện khi nhấn vào imgDataPicker sẽ xuất hiện datapickerDialog
            imgDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year,month,dayOfMonth);
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                            txtDataChange.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    },nam, thang,ngay);
                    datePickerDialog.show();
                }
            });
            //sự kiện khi ấn vào nút hủy
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogThemGhiChep.cancel();
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

            //sự kiện click khi nhập vào số tiền
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateSelect=txtDataChange.getText().toString();
//                    SimpleDateFormat formatInput= new SimpleDateFormat("dd/MM/yyyy");
//                    SimpleDateFormat formatOutput= new SimpleDateFormat("yyyy-MM-dd");
//                    try {
//                        calendar.setTime(formatInput.parse(dateSelect));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    String newDate=formatOutput.format(calendar.getTime());
//                    Toast.makeText(getActivity(),newDate,Toast.LENGTH_SHORT).show();
                    if(btnSeclectMucChi.getText().toString().equals("Chọn Hũ")) Toast.makeText(getActivity(),"Hãy chọn mục bạn cần chi trong chọn hũ",Toast.LENGTH_SHORT).show();
                    else if(edtNhapSoTien.length()==0||edtNhapSoTien.getText().toString().equals("0")) Toast.makeText(getActivity(),"Số tiền không được bỏ trống hoặc bằng 0",Toast.LENGTH_SHORT).show();
                         else if(edtNhapSoTien.length()>17)Toast.makeText(getActivity(),"Số tiền không vượt quá 13 chữ số",Toast.LENGTH_SHORT).show();
                              else {
                                    String sotien=edtNhapSoTien.getText().toString().trim().replaceAll(",","");
                                    long st = 0;
                                    try {
                                        st=Long.parseLong(sotien);
                                    }catch (NumberFormatException abt){Toast.makeText(getActivity(),"Nhập lại",Toast.LENGTH_SHORT).show(); }
                                    String ghichu=edtNhapGhiChu.getText().toString().trim();
                                    String muccha=btnSeclectMucChi.getText().toString().trim();
                                    String muccon=txtMucChi.getText().toString().trim();

                                    MainActivity.database.QueryData("INSERT INTO GhiChep VALUES(null, '"+st+"', '"+ghichu+"','"+muccha+"','"+muccon+"','"+dateSelect+"')");
                                    GetDataGhiChep();
                                    adapter.notifyDataSetChanged();
                                    settext();
                                    Toast.makeText(getActivity(),"Đã thêm ghi chép",Toast.LENGTH_SHORT).show();
                                    dialogThemGhiChep.cancel(); }

                }
            });
            //lựa chọn Mục chi khi click chọn buttonSelectMucChi
            btnSeclectMucChi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialogSelectMucChi = new Dialog(getActivity());
                    //set view cho dialog
                    dialogSelectMucChi.setContentView(R.layout.dilog_select_mucchi);
                    dialogSelectMucChi.show();
                    //dialog có expanable view nên phải thêm adapter và các thuộc tính để tạo thành 1 list view
                    final List<String> listdataHeader =new ArrayList<>();
                    final HashMap<String,List<String>> listdataChild=new HashMap<String,List<String>>();
                    ExpandableListView selecMucchi=(ExpandableListView)dialogSelectMucChi.findViewById(R.id.selecList);

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
                    //lấy dữ liệu từ bảng MucChi thêm vào dialog
                    Cursor dataMucChi = MainActivity.database.GetData("SELECT * FROM MucChi ORDER BY MucCon");
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
                    MucChiAdapter adapter=new MucChiAdapter(getActivity(),listdataHeader,listdataChild);
                    selecMucchi.setAdapter(adapter);
                    //sau khi chọn child sẽ gán giá trị cho button và textview select mục chi
                    selecMucchi.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            btnSeclectMucChi.setText(listdataHeader.get(groupPosition).toString());
                            txtMucChi.setText(listdataChild.get(listdataHeader.get(groupPosition)).get(childPosition).toString());
                            dialogSelectMucChi.cancel();
                            return true;
                        }
                    });
                }
            });

        }
    };

    ImageView.OnClickListener imgOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    //sự kiện chọn item của listView sẽ tiến hành đi vào dialog chỉnh sửa
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        vitri=position;
        DialogChinhSuaGhiChep(arrayGhiChep.get(position).getId());
    }
    private void DialogChinhSuaGhiChep(final int id) {
        final Dialog dialogChinhSuaGhiChep=new Dialog(getActivity());
        dialogChinhSuaGhiChep.setContentView(R.layout.dialog_chinhsua_ghichep);
        dialogChinhSuaGhiChep.show();
        final int ngay    =calendar.get(calendar.DATE);
        final int thang   =calendar.get(calendar.MONTH);
        final int nam     =calendar.get(calendar.YEAR);
        //ánh xạ xml dialog chỉnh sửa ghi chep
        final EditText edtNhapSoTien    =(EditText)dialogChinhSuaGhiChep.findViewById(R.id.editTextNhapTien);
        final EditText edtNhapGhiChu    =(EditText)dialogChinhSuaGhiChep.findViewById(R.id.eTextNhapGhiChu);
        final TextView txtMucChi        =(TextView)dialogChinhSuaGhiChep.findViewById(R.id.texVMucchi) ;
        Button btnXoa                   =(Button)dialogChinhSuaGhiChep.findViewById(R.id.button_xoa_ghichep);
        Button btnLuu                   =(Button)dialogChinhSuaGhiChep.findViewById(R.id.button_luu_ghichep);
        final Button btnSeclectMucChi   =(Button)dialogChinhSuaGhiChep.findViewById(R.id.buttonSelectMucChi);
        final TextView txtChangeDate    =(TextView)dialogChinhSuaGhiChep.findViewById(R.id.textViewDateChange);
        ImageView imgDatePicker         =(ImageView)dialogChinhSuaGhiChep.findViewById(R.id.imageDatePicker);

        //gán dữ liệu đã có
        btnSeclectMucChi.setText(arrayGhiChep.get(vitri).getJars().toString());
        txtMucChi.setText(arrayGhiChep.get(vitri).getNameMucChi().toString());
        edtNhapSoTien.setText(arrayGhiChep.get(vitri).getSoTien().toString().trim());
        edtNhapGhiChu.setText(arrayGhiChep.get(vitri).getDienGiai().toString().trim());
        txtChangeDate.setText(arrayGhiChep.get(vitri).getNgaychi().toString().trim());
//        Toast.makeText(getActivity(),txtChangeDate.getText().toString(),Toast.LENGTH_SHORT).show();
        //sự kiện chọn ngày sau khi click vào image
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        txtChangeDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },nam,thang,ngay);
                datePickerDialog.show();
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
        //sự kiện khi ấn vào nút xóa
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogxoa=new AlertDialog.Builder(getActivity());
                dialogxoa.setMessage("Bạn có muốn xóa ghi chép này không?");
                dialogxoa.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.database.QueryData("DELETE FROM GhiChep WHERE ID = '"+id+"'");
                        GetDataGhiChep();
                        adapter.notifyDataSetChanged();
                        settext();
                        Toast.makeText(getActivity(),"Đã xóa ghi chép",Toast.LENGTH_SHORT).show();
                        dialogChinhSuaGhiChep.cancel();
                    }
                });
                dialogxoa.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogxoa.show();
                //hoặc dialogThemTaiKhoan.dismiss();
            }
        });
        //sự kiện click lưu lại chỉnh sửa
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNhapSoTien.length()==0||edtNhapSoTien.getText().toString().equals("0")) Toast.makeText(getActivity(),"Số tiền không được bỏ trống hoặc bằng 0",Toast.LENGTH_SHORT).show();
                else if(edtNhapSoTien.length()>17)Toast.makeText(getActivity(),"Số tiền không vượt quá 13 chữ số",Toast.LENGTH_SHORT).show();
                else {
                    String sotien=edtNhapSoTien.getText().toString().trim().replaceAll(",","");
                    long st = 0;
                    try {
                        st=Long.parseLong(sotien);
                    }catch (NumberFormatException abt){Toast.makeText(getActivity(),"Nhập lại",Toast.LENGTH_SHORT).show(); }
                    String ghichu=edtNhapGhiChu.getText().toString().trim();
                    String muccha=btnSeclectMucChi.getText().toString().trim();
                    String muccon=txtMucChi.getText().toString().trim();
                    String dateSelect = txtChangeDate.getText().toString().trim();
                    MainActivity.database.QueryData("UPDATE GhiChep SET SoTienGhiChep = '"+st+"', GhiChuGhiChep = '"+ghichu+"', MucCha = '"+muccha+"', MucCon = '"+muccon+"', DateSelect = '"+dateSelect+"' WHERE ID = '"+id+"'");
                    GetDataGhiChep();
                    adapter.notifyDataSetChanged();
                    settext();
                    Toast.makeText(getActivity(),"Đã lưu chỉnh sửa",Toast.LENGTH_SHORT).show();
                    dialogChinhSuaGhiChep.cancel();
                }

            }
        });
        //lựa chọn Mục chi khi click chọn buttonSelectMucChi
        btnSeclectMucChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogSelectMucChi = new Dialog(getActivity());
                //set view cho dialog
                dialogSelectMucChi.setContentView(R.layout.dilog_select_mucchi);
                dialogSelectMucChi.show();
                //dialog có expanable view nên phải thêm adapter và các thuộc tính để tạo thành 1 list view
                final List<String> listdataHeader =new ArrayList<>();
                final HashMap<String,List<String>> listdataChild=new HashMap<String,List<String>>();
                ExpandableListView selecMucchi=(ExpandableListView)dialogSelectMucChi.findViewById(R.id.selecList);

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
                //lấy dữ liệu từ bảng MucChi thêm vào dialog
                Cursor dataMucChi = MainActivity.database.GetData("SELECT * FROM MucChi ORDER BY MucCon");
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
                MucChiAdapter adapter=new MucChiAdapter(getActivity(),listdataHeader,listdataChild);
                selecMucchi.setAdapter(adapter);
                //sau khi chọn child sẽ gán giá trị cho button và textview select mục chi
                selecMucchi.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        btnSeclectMucChi.setText(listdataHeader.get(groupPosition).toString());
                        txtMucChi.setText(listdataChild.get(listdataHeader.get(groupPosition)).get(childPosition).toString());
                        dialogSelectMucChi.cancel();
                        return true;
                    }
                });
            }
        });
    }

    private void AddGhiChep() {
        arrayGhiChep=new ArrayList<>();
        //do ghi chép có truy xuất vào csdl của Mục chi nên phải tạo bảng mục chi trước tránh trường hợp ngoại lệ: bảng mục chi chưa có
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS MucChi(Id INTEGER PRIMARY KEY AUTOINCREMENT, MucCha VARCHAR(5), MucCon VARCHAR(100))");
//        //tạo bảng csdl Ghi chép
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS GhiChep(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                " SoTienGhiChep MONEY," +
//                " GhiChuGhiChep VARCHAR(200)," +
//                " MucCha VARCHAR(5)," +
//                " MucCon VARCHAR(100))");
          GetDataGhiChep();
          settext();
//        arrayGhiChep.add(new GhiChep("Ăn Tiệm","NEC","15,000 đ", "ăn tối"));
//        arrayGhiChep.add(new GhiChep("xe cộ","NEC","2,000 đ", "gửi xe"));
//        arrayGhiChep.add(new GhiChep("điện","NEC","55,000 đ", "tiền điện tháng 5"));
//        arrayGhiChep.add(new GhiChep("sách","EDU","15,000 đ", "99"));
        adapter=new GhChepAdapter(getActivity(),R.layout.ghi_chep_custom,arrayGhiChep);
        setListAdapter(adapter);


    }
    //format string to date

    //format number
    private String format(long s){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }
    //hàm settext cho textView Tong Tien đã chi
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
        txtTongTiendaChi.setText("Tổng tiền đã chi: "+format(tongchi)+ " VNĐ");
        txtHienCo.setText("Tổng còn: "+format(conlai)+" VNĐ");

    }
    private void GetDataGhiChep() {
        Cursor dataGhiChep=MainActivity.database.GetData("SELECT * FROM GhiChep");
        //xóa mảng trước khi load để tránh trường hợp mảng cũ vẫn còn lại chồng lên mảng mới
        arrayGhiChep.clear();
        while (dataGhiChep.moveToNext()){
            int id=dataGhiChep.getInt(0);
            Long sotien=dataGhiChep.getLong(1);
            String ghichu=dataGhiChep.getString(2);
            String muccha=dataGhiChep.getString(3);
            String muccon=dataGhiChep.getString(4);
            String dateselect=dataGhiChep.getString(5);
            arrayGhiChep.add(new GhiChep(id,format(sotien),ghichu,muccha,muccon,dateselect));
        }
    }

}
