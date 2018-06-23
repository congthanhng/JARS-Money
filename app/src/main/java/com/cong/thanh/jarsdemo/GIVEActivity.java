package com.cong.thanh.jarsdemo;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GIVEActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiGIVE,btnThemGhiChepGIVE;
    TextView txtTienBanDauGIVE,txtTienDaChiGIVE,txtTienConLaiGIVE;
    ListView listViewGIVE;
    GhiChepGIVEAdapter adapter;
    ArrayList<GhiChepGIVE> arrayGhiChepGIVE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarGIVE);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepGIVE();

        //thêm ghi chép
        btnThemGhiChepGIVE.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(GIVEActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepGIVE() {
        arrayGhiChepGIVE=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepGIVEAdapter(GIVEActivity.this,R.layout.ghichep_give_custom,arrayGhiChepGIVE);
        listViewGIVE.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienGIVE = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'GIVE'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiGIVE.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'GIVE'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienGIVE = tongtien * phantram / 100;
        }
        long TongGIVEBanDau=(long)tienGIVE;
        //settext textView tổng tiền ban đầu
        txtTienBanDauGIVE.setText(format(TongGIVEBanDau)+ " VNĐ");
        conlai=TongGIVEBanDau-tongchi;
        txtTienConLaiGIVE.setText(format(conlai)+ " VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepGIVE= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'GIVE'");
        arrayGhiChepGIVE.clear();
        while(getdataGhiChepGIVE.moveToNext()){
            String mucchi=getdataGhiChepGIVE.getString(0);
            long sotien=getdataGhiChepGIVE.getLong(1);
            String ghichu=getdataGhiChepGIVE.getString(2);
            String ngaychi=getdataGhiChepGIVE.getString(3);
            arrayGhiChepGIVE.add(new GhiChepGIVE(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiGIVE=(Button)findViewById(R.id.buttonThemMucChiGIVE);
        btnThemGhiChepGIVE=(Button)findViewById(R.id.buttonThemGhiChepGIVE);
        txtTienBanDauGIVE=(TextView) findViewById(R.id.textViewTienBanDauGIVE);
        txtTienDaChiGIVE=(TextView)findViewById(R.id.textViewTienDaChiGIVE);
        txtTienConLaiGIVE=(TextView)findViewById(R.id.textViewTienConLaiGIVE);
        listViewGIVE=(ListView)findViewById(R.id.listGhiChepGIVE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
