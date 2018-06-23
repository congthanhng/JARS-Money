package com.cong.thanh.jars;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cong.thanh.jarsdemo.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ThemTaiKhoanActivity extends AppCompatActivity {
    //khai báo
    TextView txtTenTK,txtGhiChu;
    EditText edtTenTK,edtSoTien,edtGhiChu;
    Button btnLuuTK,btnHuyTK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tai_khoan);
        AnhXa();
        edtSoTien.addTextChangedListener(onTextChangedListener());
        //sự kiên click khi ấn hủy, sẽ trờ về màn hình trước
        btnHuyTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //sự kiện click khi ấn lưu
        btnLuuTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra người dùng đã nhập trước khi lưu hay chưa
                if( edtSoTien.length()==0) Toast.makeText(ThemTaiKhoanActivity.this,"Bạn chưa nhập số tiền",Toast.LENGTH_SHORT).show();
                if (edtSoTien.length()>17)Toast.makeText(ThemTaiKhoanActivity.this,"Số tiền không vượt quá 13 chữ số",Toast.LENGTH_SHORT).show();
                else{
                    //sau khi click lưu thì trờ về màn hình trước
                    Intent Luu=new Intent();
                    //gửi kèm dữ liệu khi trờ về màn hình trước
//                long a= Long.parseLong(edtSoTien.getText().toString());
                    String a=edtSoTien.getText().toString();
                    Luu.putExtra("dulieu",a);
                    setResult(RESULT_OK,Luu);
                    finish();

                }
            }
        });
    }
    //Format number in EditText after realtime editing -
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtSoTien.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edtSoTien.setText(formattedString);
                    edtSoTien.setSelection(edtSoTien.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edtSoTien.addTextChangedListener(this);
            }
        };

    }


    private void AnhXa() {
        //ánh xạ TextView
        txtTenTK=(TextView)findViewById(R.id.textViewTenTK);
        txtGhiChu=(TextView)findViewById(R.id.textViewGhiChu);
        //ánh xạ Edit Text
        edtTenTK=(EditText)findViewById(R.id.editTextTenTK);
        edtGhiChu=(EditText)findViewById(R.id.editTextGhiChu);
        edtSoTien=(EditText)findViewById(R.id.editTextSoTien);
        //ánh xạ Button
        btnHuyTK=(Button)findViewById(R.id.buttonHuyTK);
        btnLuuTK=(Button)findViewById(R.id.buttonLuuTK);
    }

    public ThemTaiKhoanActivity() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
