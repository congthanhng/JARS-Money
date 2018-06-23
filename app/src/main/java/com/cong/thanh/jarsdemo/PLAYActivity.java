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

public class PLAYActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiPLAY,btnThemGhiChepPLAY;
    TextView txtTienBanDauPLAY,txtTienDaChiPLAY,txtTienConLaiPLAY;
    ListView listViewPLAY;
    GhiChepPLAYAdapter adapter;
    ArrayList<GhiChepPLAY> arrayGhiChepPLAY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarPLAY);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepPLAY();

        //thêm ghi chép
        btnThemGhiChepPLAY.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(PLAYActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepPLAY() {
        arrayGhiChepPLAY=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepPLAYAdapter(PLAYActivity.this,R.layout.ghichep_play_custom,arrayGhiChepPLAY);
        listViewPLAY.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienPLAY = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'PLAY'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiPLAY.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'PLAY'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienPLAY = tongtien * phantram / 100;
        }
        long TongPLAYBanDau=(long)tienPLAY;
        //settext textView tổng tiền ban đầu
        txtTienBanDauPLAY.setText(format(TongPLAYBanDau)+ " VNĐ");
        conlai=TongPLAYBanDau-tongchi;
        txtTienConLaiPLAY.setText(format(conlai)+ "VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepPLAY= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'PLAY'");
        arrayGhiChepPLAY.clear();
        while(getdataGhiChepPLAY.moveToNext()){
            String mucchi=getdataGhiChepPLAY.getString(0);
            long sotien=getdataGhiChepPLAY.getLong(1);
            String ghichu=getdataGhiChepPLAY.getString(2);
            String ngaychi=getdataGhiChepPLAY.getString(3);
            arrayGhiChepPLAY.add(new GhiChepPLAY(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiPLAY=(Button)findViewById(R.id.buttonThemMucChiPLAY);
        btnThemGhiChepPLAY=(Button)findViewById(R.id.buttonThemGhiChepPLAY);
        txtTienBanDauPLAY=(TextView) findViewById(R.id.textViewTienBanDauPLAY);
        txtTienDaChiPLAY=(TextView)findViewById(R.id.textViewTienDaChiPLAY);
        txtTienConLaiPLAY=(TextView)findViewById(R.id.textViewTienConLaiPLAY);
        listViewPLAY=(ListView)findViewById(R.id.listGhiChepPLAY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
