package com.cong.thanh.jarsdemo;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    GridView gvJARS;
    ArrayList<GridViewJARS> arrayJARS;
    GridView_Adapter adapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    Button ThemTaiKhoan;
    TextView txtSoTien,txtDateBegin,txtDateEnd;
    ImageView imgDateBegin,imgDateEnd;
    Calendar calendarBegin,calendarEnd,calendarcurrent;
    SimpleDateFormat formatIn,formatOut;
    long conlai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);

        //Ánh xạ xml từ fragment_home
        ThemTaiKhoan=(Button)view.findViewById(R.id.buttonThemTaiKhoan);
        txtSoTien=(TextView)view.findViewById(R.id.textViewSoTien) ;
        txtDateBegin=(TextView)view.findViewById(R.id.textViewdatebegin);
        txtDateEnd=(TextView)view.findViewById(R.id.textViewdateend);
        imgDateBegin=(ImageView)view.findViewById(R.id.imageDateBegin);
        imgDateEnd=(ImageView)view.findViewById(R.id.imageDateEnd);
        gvJARS=(GridView)view.findViewById(R.id.GridVJARS);

        //khai báo calendar
        calendarBegin=Calendar.getInstance();
        calendarEnd=Calendar.getInstance();
        calendarcurrent=Calendar.getInstance();

        //lấy thời gian hiện tại
        int ngay=calendarcurrent.get(calendarcurrent.DATE);
        int thang=calendarcurrent.get(calendarcurrent.MONTH);
        int nam=calendarcurrent.get(calendarcurrent.YEAR);

        //khai báo định dạng SimpleDateFormat
        formatIn=new SimpleDateFormat("yyyy-MM-dd");
        formatOut=new SimpleDateFormat("dd/MM/yyyy");

        //thêm các item vào gridView và lấy dữ liệu từ csdl
        AddJARS();
        // lấy dữ liệu date từ csdl và hàm xử lý sự kiện chọn textVewDateBegin và textViewDateEnd để thay đổi ngày bắt đầu và ngày kết thúc
        GetDataDate();
        resetDate();
        //sự kiện chọn ngày khi click vào textviewBegin
        txtDateBegin.setOnClickListener(mDateTextViewBeginOnClickListener);
        //sự kiện chọn ngày khi click vào textviewEnd
        txtDateEnd.setOnClickListener(mDateTextViewEndOnClickListener);
        //sự kiện chọn ngày sau khi click vào imgDateBegin tương tự như textviewBegin
        imgDateBegin.setOnClickListener(mDateTextViewBeginOnClickListener);
        //sự kiện chọn ngày khi click vào imgDateEnd tương tự như textviewEnd
        imgDateEnd.setOnClickListener(mDateTextViewEndOnClickListener);

        //sự kiện chọn item trong gridview
        gvJARS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),arrayJARS.get(position).getNameJars().toString(),Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0: Intent intentNEC = new Intent(getActivity(),NECActivity.class);startActivity(intentNEC);break;
                    case 1: Intent intentLTSS = new Intent(getActivity(),LTSSActivity.class);startActivity(intentLTSS);break;
                    case 2: Intent intentEDU = new Intent(getActivity(),EDUActivity.class);startActivity(intentEDU);break;
                    case 3: Intent intentGIVE = new Intent(getActivity(),GIVEActivity.class);startActivity(intentGIVE);break;
                    case 4: Intent intentPLAY = new Intent(getActivity(),PLAYActivity.class);startActivity(intentPLAY);break;
                    case 5: Intent intentFFA = new Intent(getActivity(),FFAActivity.class);startActivity(intentFFA);break;
                }
            }
        });

        //sự kiện click khi nhấn vào Thêm tài khoản, hiển thị 1 dialog
        ThemTaiKhoan.setOnClickListener(mOnClickListener);
        return view;
    }
//reset lại các hũ khi thời gian kết thúc bằng với thời gian hiện tại
    private void resetDate() {
        if (calendarcurrent.getTimeInMillis()>=calendarEnd.getTimeInMillis()){
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("Ngày kết thúc thời hạn hiện tại đã đến bạn có muốn RESET lại các hũ không\n" +
                    "Nếu bạn chọn CÓ. Bạn sẽ phải thiết lập thời hạn tiếp theo cho các hũ\n" +
                    "Và nếu số tiền trong các hũ còn dư thì sẽ được dồn lại và thêm vào thời hạn tiếp theo\n" +
                    "Sau khi chọn CÓ bạn hãy chọn ngày kết thúc cho thời hạn tiếp theo.");

            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //thời gian bắt đầu sẽ là thời gian hiện tại
                    MainActivity.database.QueryData("UPDATE DateBegin SET begindate = '"+formatIn.format(calendarcurrent.getTime())+"'");
                    //thời gian kết thúc sẽ do người dùng chọn ngày trong datePickerDialog
                    DatePickerDialog datePickerDialog1=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendarEnd.set(year,month,dayOfMonth);
                            if(calendarEnd.getTimeInMillis()<calendarBegin.getTimeInMillis())Toast.makeText(getActivity(),"Ngày kết thúc phải lớn hơn ngày bắt đầu",Toast.LENGTH_LONG).show();
                            else {
                                MainActivity.database.QueryData("UPDATE DateEnd SET enddate = '"+formatIn.format(calendarEnd.getTime())+"'");
                                GetDataDate();
                            }
                        }
                    },calendarEnd.get(calendarEnd.YEAR),calendarEnd.get(calendarEnd.MONTH),calendarEnd.get(calendarEnd.DATE));
                    datePickerDialog1.show();
                    //reset lại các tài khoản
                    MainActivity.database.QueryData("DELETE FROM TaiKhoan");
                    //reset lại các ghi chép
                    MainActivity.database.QueryData("DELETE FROM GhiChep");
                    //số dư trong hũ sẽ được chuyển vào thời hạn mới
                    if(conlai!=0) MainActivity.database.QueryData("INSERT INTO TaiKhoan VALUES(null,'Tiền dư trong các hũ','"+conlai+"','Thời hạn cũ')");
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(),"Bạn chọn Không",Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
    }

    // lấy dữ liệu ngày bắt đầu và kết thúc từ csdl
    private void GetDataDate() {
        Cursor getDateBegin= MainActivity.database.GetData("SELECT * FROM DateBegin");
        while (getDateBegin.moveToNext()){
            String dateBegin= getDateBegin.getString(0);
            try {
                calendarBegin.setTime(formatIn.parse(dateBegin));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Cursor getDateEnd=MainActivity.database.GetData("SELECT * FROM DateEnd");
        while (getDateEnd.moveToNext()){
            String dateEnd= getDateEnd.getString(0);
            try {
                calendarEnd.setTime(formatIn.parse(dateEnd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txtDateBegin.setText("Từ:"+formatOut.format(calendarBegin.getTime()));
        txtDateEnd.setText("Đến:"+formatOut.format(calendarEnd.getTime()));
    }
    //sự kiện khi click vào txtDateBegin và txtDateEnd, hiển thị một DatePickerDialog để chọn ngày
    TextView.OnClickListener mDateTextViewBeginOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //hiển thị datePickerDialog để cho người dùng chọn ngày
            DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendarBegin.set(year,month,dayOfMonth);
                    if (calendarEnd.getTimeInMillis()< calendarBegin.getTimeInMillis()) Toast.makeText(getActivity(),"Ngày bắt đầu phải nhỏ hơn ngày kết thúc",Toast.LENGTH_LONG).show();
                    else {
                        MainActivity.database.QueryData("UPDATE DateBegin SET begindate = '"+formatIn.format(calendarBegin.getTime())+"'");
                        GetDataDate();
                    }
                }
            },calendarBegin.get(calendarBegin.YEAR),calendarBegin.get(calendarBegin.MONTH),calendarBegin.get(calendarBegin.DATE));
            datePickerDialog.show();

        }
    };
    TextView.OnClickListener mDateTextViewEndOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog1=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendarEnd.set(year,month,dayOfMonth);
                    if(calendarEnd.getTimeInMillis()<calendarBegin.getTimeInMillis())Toast.makeText(getActivity(),"Ngày kết thúc phải lớn hơn ngày bắt đầu",Toast.LENGTH_LONG).show();
                    else {
                        MainActivity.database.QueryData("UPDATE DateEnd SET enddate = '"+formatIn.format(calendarEnd.getTime())+"'");
                        GetDataDate();
                    }
                }
            },calendarEnd.get(calendarEnd.YEAR),calendarEnd.get(calendarEnd.MONTH),calendarEnd.get(calendarEnd.DATE));
            datePickerDialog1.show();
        }
    };
    //sự kiện click khi thêm tài khoản
    private Button.OnClickListener mOnClickListener=new View.OnClickListener() {
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

                //format số nhập vào thành dạng #,###,###,###
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

                            Long longval;
                            if (originalString.contains(",")) {
                                originalString = originalString.replaceAll(",", "");
                            }
                            longval = Long.parseLong(originalString);

//                            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                            formatter.applyPattern("#,###,###,###");
//                            String formattedString = formatter.format(longval);

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
                            MainActivity.database.QueryData("INSERT INTO TaiKhoan VALUES(null, '"+ten+"', '"+st+"', '"+ghichu+"')");

                            getDaTa();

//                            Bundle bundle=new Bundle();
//                            bundle.putString("tentaikhoan",(String) edtNhapTenTaiKhoan.getText().toString() );
//                            bundle.putString("sotien",(String) edtNhapSoTien.getText().toString());
//                            bundle.putString("ghichu",(String) edtNhapGhiChu.getText().toString());
//                            TaiKhoanFragment taiKhoanFragment=new TaiKhoanFragment();
//                            taiKhoanFragment.setArguments(bundle);

                            dialogThemTaiKhoan.cancel();
                        }

                    }
                });

            }

        };
    private String format(long s){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }
    private void getDaTa(){
        long tongchi=0;
        long tongtien=0;
        conlai=0;
        double tienNEC = 0,tienLTSS=0,tienEDU=0,tienGIVE=0,tienPLAY=0,tienFFA=0;
        long dachiNEC,dachiLTSS,dachiEDU,dachiGIVE,dachiPLAY,dachiFFA;
        dachiNEC=dachiLTSS=dachiEDU=dachiGIVE=dachiPLAY=dachiFFA=0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //thiết lập cho txt số tiền
        txtSoTien.setText(format(conlai)+" VNĐ");
        if(txtSoTien.length()>=12) txtSoTien.setTextSize(24);
        else txtSoTien.setTextSize(36);
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT JARS,PhanTram FROM HuJARS");
        while (getDataPhantram.moveToNext()) {
            String hujars = getDataPhantram.getString(0);
            int phantram = getDataPhantram.getInt(1);
            switch (hujars) {
                case "NEC":
                    tienNEC = tongtien * phantram / 100;
                    break;
                case "LTSS":
                    tienLTSS = tongtien * phantram / 100;
                    break;
                case "EDU":
                    tienEDU = tongtien * phantram / 100;
                    break;
                case "GIVE":
                    tienGIVE = tongtien * phantram / 100;
                    break;
                case "PLAY":
                    tienPLAY = tongtien * phantram / 100;
                    break;
                case "FFA":
                    tienFFA = tongtien * phantram / 100;
                    break;
            }
        }
        //số tiền có trong hũ bằng số tiền trong hũ ban đầu trừ cho số tiền đã chi
        Cursor dataDaChiJars=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep), MucCha FROM GhiChep GROUP BY MucCha");
        while (dataDaChiJars.moveToNext()){
            if (dataDaChiJars.getString(1).equals("NEC")) {
                dachiNEC=dataDaChiJars.getLong(0);
                tienNEC-=dachiNEC;}
            if (dataDaChiJars.getString(1).equals("LTSS")) {
                dachiLTSS=dataDaChiJars.getLong(0);
                tienLTSS-=dachiLTSS;}
            if (dataDaChiJars.getString(1).equals("EDU")) {
                dachiEDU=dataDaChiJars.getLong(0);
                tienEDU-=dachiEDU;}
            if (dataDaChiJars.getString(1).equals("GIVE")) {
                dachiGIVE=dataDaChiJars.getLong(0);
                tienGIVE-=dachiGIVE;}
            if (dataDaChiJars.getString(1).equals("PLAY")){
                dachiPLAY=dataDaChiJars.getLong(0);
                tienPLAY-=dachiPLAY;}
            if (dataDaChiJars.getString(1).equals("FFA")) {
                dachiFFA=dataDaChiJars.getLong(0);
                tienFFA-=dachiFFA;}
        }
        //cập nhật lại cơ sở dữ liệu
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienNEC+"',TienCL = '"+dachiNEC+"' WHERE JARS = 'NEC'");
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienLTSS+"',TienCL = '"+dachiLTSS+"' WHERE JARS = 'LTSS'");
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienEDU+"',TienCL = '"+dachiEDU+"' WHERE JARS = 'EDU'");
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienGIVE+"',TienCL = '"+dachiGIVE+"' WHERE JARS = 'GIVE'");
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienPLAY+"',TienCL = '"+dachiPLAY+"' WHERE JARS = 'PLAY'");
        MainActivity.database.QueryData("UPDATE HuJARS SET TienBD = '"+tienFFA+"',TienCL = '"+dachiFFA+"' WHERE JARS = 'FFA'");

        //lay ra du lieu và thêm vào các hũ
        Cursor datajars=MainActivity.database.GetData("SELECT * FROM HuJARS");
        arrayJARS.clear();
        while (datajars.moveToNext()) {
            String tienbandau=format(datajars.getLong(2));
            String tienconlai=format(datajars.getLong(3));
            if(datajars.getString(0).equals("NEC"))
            { arrayJARS.add(new GridViewJARS("NEC",datajars.getInt(1)+"%", R.drawable.nec_icon2, tienbandau, tienconlai));}

            if (datajars.getString(0).equals("LTSS"))
            { arrayJARS.add(new GridViewJARS("LTSS",datajars.getInt(1)+"%", R.drawable.ltss_icon2, tienbandau, tienconlai));}

            if (datajars.getString(0).equals("EDU"))
            { arrayJARS.add(new GridViewJARS("EDU",datajars.getInt(1)+"%", R.drawable.edu_icon2, tienbandau, tienconlai));}

            if (datajars.getString(0).equals("GIVE"))
            { arrayJARS.add(new GridViewJARS("GIVE",datajars.getInt(1)+"%", R.drawable.give_icon2, tienbandau, tienconlai));}

            if (datajars.getString(0).equals("PLAY"))
            { arrayJARS.add(new GridViewJARS("PLAY",datajars.getInt(1)+"%", R.drawable.play_icon2, tienbandau, tienconlai));}

            if (datajars.getString(0).equals("FFA"))
            { arrayJARS.add(new GridViewJARS("FFA",datajars.getInt(1)+"%", R.drawable.ffa_icon2, tienbandau, tienconlai));}
        }

    }
    private void AddJARS() {

        arrayJARS =new ArrayList<>();
//        MainActivity.database.QueryData("DROP TABLE GhiChep");
//        MainActivity.database.QueryData("DROP TABLE SoTien");
        // lấy từ csdl và truyền dữ liệu vào Textview số tiền
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS SoTien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenST VARCHAR(200), SoTienST MONEY, GhiChuSoTien VARCHAR(200))");
//        //do ghi chép có truy xuất vào csdl của Mục chi nên phải tạo bảng mục chi trước tránh trường hợp ngoại lệ: bảng mục chi chưa có
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS MucChi(Id INTEGER PRIMARY KEY AUTOINCREMENT, MucCha VARCHAR(5), MucCon VARCHAR(100))");
//        //tạo bảng csdl Ghi chép
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS GhiChep(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                " SoTienGhiChep MONEY," +
//                " GhiChuGhiChep VARCHAR(200)," +
//                " MucCha VARCHAR(5)," +
//                " MucCon VARCHAR(100))");
////        MainActivity.database.QueryData("DROP TABLE HuJARS");
//        MainActivity.database.QueryData("CREATE TABLE IF NOT EXISTS HuJARS(JARS VARCHAR(5) PRIMARY KEY, PhanTram INTEGER, TienBD MONEY, TienCL MONEY)");

//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('NEC','55','0','0')");
//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('LTSS','10','0','0')");
//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('EDU','10','0','0')");
//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('GIVE','5','0','0')");
//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('PLAY','10','0','0')");
//        MainActivity.database.QueryData("INSERT INTO HuJARS VALUES('FFA','10','0','0')");
        //xu ly du lieu
        getDaTa();
        adapter=new GridView_Adapter(getActivity(),R.layout.gridview_jars_custom,arrayJARS);
        gvJARS.setAdapter(adapter);
    }

}
