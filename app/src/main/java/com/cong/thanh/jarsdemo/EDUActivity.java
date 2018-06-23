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

public class EDUActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiEDU,btnThemGhiChepEDU;
    TextView txtTienBanDauEDU,txtTienDaChiEDU,txtTienConLaiEDU;
    ListView listViewEDU;
    GhiChepEDUAdapter adapter;
    ArrayList<GhiChepEDU> arrayGhiChepEDU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarEDU);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepEDU();

        //thêm ghi chép
        btnThemGhiChepEDU.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(EDUActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepEDU() {
        arrayGhiChepEDU=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepEDUAdapter(EDUActivity.this,R.layout.ghichep_edu_custom,arrayGhiChepEDU);
        listViewEDU.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienEDU = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'EDU'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiEDU.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'EDU'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienEDU = tongtien * phantram / 100;
        }
        long TongEDUBanDau=(long)tienEDU;
        //settext textView tổng tiền ban đầu
        txtTienBanDauEDU.setText(format(TongEDUBanDau)+ " VNĐ");
        conlai=TongEDUBanDau-tongchi;
        txtTienConLaiEDU.setText(format(conlai)+ " VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepEDU= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'EDU'");
        arrayGhiChepEDU.clear();
        while(getdataGhiChepEDU.moveToNext()){
            String mucchi=getdataGhiChepEDU.getString(0);
            long sotien=getdataGhiChepEDU.getLong(1);
            String ghichu=getdataGhiChepEDU.getString(2);
            String ngaychi=getdataGhiChepEDU.getString(3);
            arrayGhiChepEDU.add(new GhiChepEDU(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiEDU=(Button)findViewById(R.id.buttonThemMucChiEDU);
        btnThemGhiChepEDU=(Button)findViewById(R.id.buttonThemGhiChepEDU);
        txtTienBanDauEDU=(TextView) findViewById(R.id.textViewTienBanDauEDU);
        txtTienDaChiEDU=(TextView)findViewById(R.id.textViewTienDaChiEDU);
        txtTienConLaiEDU=(TextView)findViewById(R.id.textViewTienConLaiEDU);
        listViewEDU=(ListView)findViewById(R.id.listGhiChepEDU);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
