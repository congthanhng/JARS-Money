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

public class LTSSActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiLTSS,btnThemGhiChepLTSS;
    TextView txtTienBanDauLTSS,txtTienDaChiLTSS,txtTienConLaiLTSS;
    ListView listViewLTSS;
    GhiChepLTSSAdapter adapter;
    ArrayList<GhiChepLTSS> arrayGhiChepLTSS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ltss);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarLTSS);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepLTSS();

        //thêm ghi chép
        btnThemGhiChepLTSS.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(LTSSActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepLTSS() {
        arrayGhiChepLTSS=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepLTSSAdapter(LTSSActivity.this,R.layout.ghichep_ltss_custom,arrayGhiChepLTSS);
        listViewLTSS.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienLTSS = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'LTSS'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiLTSS.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'LTSS'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienLTSS = tongtien * phantram / 100;
        }
        long TongLTSSBanDau=(long)tienLTSS;
        //settext textView tổng tiền ban đầu
        txtTienBanDauLTSS.setText(format(TongLTSSBanDau)+ " VNĐ");
        conlai=TongLTSSBanDau-tongchi;
        txtTienConLaiLTSS.setText(format(conlai)+ " VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepLTSS= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'LTSS'");
        arrayGhiChepLTSS.clear();
        while(getdataGhiChepLTSS.moveToNext()){
            String mucchi=getdataGhiChepLTSS.getString(0);
            long sotien=getdataGhiChepLTSS.getLong(1);
            String ghichu=getdataGhiChepLTSS.getString(2);
            String ngaychi=getdataGhiChepLTSS.getString(3);
            arrayGhiChepLTSS.add(new GhiChepLTSS(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiLTSS=(Button)findViewById(R.id.buttonThemMucChiLTSS);
        btnThemGhiChepLTSS=(Button)findViewById(R.id.buttonThemGhiChepLTSS);
        txtTienBanDauLTSS=(TextView) findViewById(R.id.textViewTienBanDauLTSS);
        txtTienDaChiLTSS=(TextView)findViewById(R.id.textViewTienDaChiLTSS);
        txtTienConLaiLTSS=(TextView)findViewById(R.id.textViewTienConLaiLTSS);
        listViewLTSS=(ListView)findViewById(R.id.listGhiChepLTSS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
