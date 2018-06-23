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

public class NECActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiNEC,btnThemGhiChepNEC;
    TextView txtTienBanDauNEC,txtTienDaChiNEC,txtTienConLaiNEC;
    ListView listViewNEC;
    GhiChepNECAdapter adapter;
    ArrayList<GhiChepNEC> arrayGhiChepNEC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nec);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarNEC);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepNEC();

        //thêm ghi chép
        btnThemGhiChepNEC.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(NECActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepNEC() {
        arrayGhiChepNEC=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepNECAdapter(NECActivity.this,R.layout.ghichep_nec_custom,arrayGhiChepNEC);
        listViewNEC.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienNEC = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'NEC'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiNEC.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'NEC'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienNEC = tongtien * phantram / 100;
        }
        long TongNECBanDau=(long)tienNEC;
        //settext textView tổng tiền ban đầu
        txtTienBanDauNEC.setText(format(TongNECBanDau)+ " VNĐ");
        conlai=TongNECBanDau-tongchi;
        txtTienConLaiNEC.setText(format(conlai)+ "VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepNEC= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'NEC'");
        arrayGhiChepNEC.clear();
        while(getdataGhiChepNEC.moveToNext()){
            String mucchi=getdataGhiChepNEC.getString(0);
            long sotien=getdataGhiChepNEC.getLong(1);
            String ghichu=getdataGhiChepNEC.getString(2);
            String ngaychi=getdataGhiChepNEC.getString(3);
            arrayGhiChepNEC.add(new GhiChepNEC(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiNEC=(Button)findViewById(R.id.buttonThemMucChiNEC);
        btnThemGhiChepNEC=(Button)findViewById(R.id.buttonThemGhiChepNEC);
        txtTienBanDauNEC=(TextView) findViewById(R.id.textViewTienBanDauNEC);
        txtTienDaChiNEC=(TextView)findViewById(R.id.textViewTienDaChiNEC);
        txtTienConLaiNEC=(TextView)findViewById(R.id.textViewTienConLaiNEC);
        listViewNEC=(ListView)findViewById(R.id.listGhiChepNEC);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
