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

public class FFAActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThemMucChiFFA,btnThemGhiChepFFA;
    TextView txtTienBanDauFFA,txtTienDaChiFFA,txtTienConLaiFFA;
    ListView listViewFFA;
    GhiChepFFAAdapter adapter;
    ArrayList<GhiChepFFA> arrayGhiChepFFA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffa);

        //thêm toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarFFA);
        setSupportActionBar(toolbar);
        //khởi tạo button back trên toolbar. đây là hàm có sẵn
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Anhxa();
        addArrayGhiChepFFA();

        //thêm ghi chép
        btnThemGhiChepFFA.setOnClickListener(mOnClickListener);
    }
    Button.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(FFAActivity.this,"Tính năng này sẽ cập nhật sau",Toast.LENGTH_SHORT).show();
        }
    };
    private void addArrayGhiChepFFA() {
        arrayGhiChepFFA=new ArrayList<>();
        GetDaTa();
        settext();
        adapter=new GhiChepFFAAdapter(FFAActivity.this,R.layout.ghichep_ffa_custom,arrayGhiChepFFA);
        listViewFFA.setAdapter(adapter);
    }

    private void settext() {
        long tongchi=0;
        long tongtien=0;
        long conlai=0;
        double tienFFA = 0;
        //lấy ra số tiền đã dùng ở từng hũ
        Cursor Tongchi=MainActivity.database.GetData("SELECT SUM(SoTienGhiChep) FROM GhiChep WHERE MucCha = 'FFA'");
        while (Tongchi.moveToNext()){
            long s=Tongchi.getLong(0);
            tongchi+=s;
        }
        //settext cho textVIew Tổng chi
        txtTienDaChiFFA.setText(format(tongchi)+" VNĐ");

        //lấy ra số tiền trong tài khoản
        Cursor Tongtien=MainActivity.database.GetData("SELECT SUM(SoTienST) FROM TaiKhoan ");
        while (Tongtien.moveToNext()){
            long s=Tongtien.getLong(0);
            tongtien+=s;
        }
        //só tiền còn lại bằng số tiền trong tài khoản trừ cho số tiền trong ghi chép
        conlai=tongtien-tongchi;
        //lấy dữ diệu phần trăm của các hũ
        Cursor getDataPhantram=MainActivity.database.GetData("SELECT PhanTram FROM HuJARS WHERE JARS = 'FFA'");
        while (getDataPhantram.moveToNext()) {
            int phantram = getDataPhantram.getInt(0);
            tienFFA = tongtien * phantram / 100;
        }
        long TongFFABanDau=(long)tienFFA;
        //settext textView tổng tiền ban đầu
        txtTienBanDauFFA.setText(format(TongFFABanDau)+ " VNĐ");
        conlai=TongFFABanDau-tongchi;
        txtTienConLaiFFA.setText(format(conlai)+ " VNĐ");

    }

    private void GetDaTa() {
        Cursor getdataGhiChepFFA= MainActivity.database.GetData("SELECT MucCon,SoTienGhiChep,GhiChuGhiChep,DateSelect FROM GhiChep WHERE MucCha = 'FFA'");
        arrayGhiChepFFA.clear();
        while(getdataGhiChepFFA.moveToNext()){
            String mucchi=getdataGhiChepFFA.getString(0);
            long sotien=getdataGhiChepFFA.getLong(1);
            String ghichu=getdataGhiChepFFA.getString(2);
            String ngaychi=getdataGhiChepFFA.getString(3);
            arrayGhiChepFFA.add(new GhiChepFFA(mucchi,format(sotien),ghichu,ngaychi));
        }
    }
    private String format(long s){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formatted=formatter.format(s);
        return formatted;
    }

    private void Anhxa() {
        btnThemMucChiFFA=(Button)findViewById(R.id.buttonThemMucChiFFA);
        btnThemGhiChepFFA=(Button)findViewById(R.id.buttonThemGhiChepFFA);
        txtTienBanDauFFA=(TextView) findViewById(R.id.textViewTienBanDauFFA);
        txtTienDaChiFFA=(TextView)findViewById(R.id.textViewTienDaChiFFA);
        txtTienConLaiFFA=(TextView)findViewById(R.id.textViewTienConLaiFFA);
        listViewFFA=(ListView)findViewById(R.id.listGhiChepFFA);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }
}
