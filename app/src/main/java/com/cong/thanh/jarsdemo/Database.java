package com.cong.thanh.jarsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //truy vấn không trả về kết quả: CREAT, INSERT, UPDATE, DELETE
    public  void QueryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }
    //truy vấn có trả kết quả: SELECT
    public Cursor GetData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    //hàm này sẽ được gọi khi database được khởi tạo lần đầu|| dùng để insert các dữ liệu ban đầu
    @Override
    public void onCreate(SQLiteDatabase db) {
        //tạo bảng csdl MucChi
        db.execSQL("CREATE TABLE IF NOT EXISTS MucChi(Id INTEGER PRIMARY KEY AUTOINCREMENT, MucCha VARCHAR(5), MucCon VARCHAR(100))");
        // tạo bảng csdl tài khoản
        db.execSQL("CREATE TABLE IF NOT EXISTS TaiKhoan(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenST VARCHAR(200), SoTienST MONEY, GhiChuSoTien VARCHAR(200))");
        //tạo bảng csdl Ghi chép
        db.execSQL("CREATE TABLE IF NOT EXISTS GhiChep(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SoTienGhiChep MONEY," +
                " GhiChuGhiChep VARCHAR(200)," +
                " MucCha VARCHAR(5)," +
                " MucCon VARCHAR(100)," +
                " DateSelect VARCHAR(15))");
        //tạo bảng csdl hũ jars
        db.execSQL("CREATE TABLE IF NOT EXISTS HuJARS(JARS VARCHAR(5) PRIMARY KEY, PhanTram INTEGER, TienBD MONEY, TienCL MONEY)");
        //tạo bảng csdl cho ngày bắt đầu và kết thúc
        db.execSQL("CREATE TABLE IF NOT EXISTS DateBegin(begindate Date)");
        db.execSQL("CREATE TABLE IF NOT EXISTS DateEnd(enddate Date)");
        //thêm dữ liệu mặc định vào bảng DateBeginEnd
        db.execSQL("INSERT INTO DateBegin VALUES('2018-02-01')");
        db.execSQL("INSERT INTO DateEnd VALUES('2018-03-01')");
        //thêm dữ liệu mặc định vào bảng MucChi
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Ăn uống')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Hóa đơn tiền Điện')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Hóa đơn tiền Nước')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền điện thoại')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền Internet')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'GAS')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Tiền cáp truyền hình')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Đi chợ/Siêu thị')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Xe cộ/Đi lại')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Dụng cụ sinh hoạt')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'Sửa chữa/Bảo trì thiết bị')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'NEC', 'y tế/Sức khỏe')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua xe')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua nhà')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua điện thoại')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'LTSS', 'Mua vàng')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'EDU', 'Sách')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'EDU', 'Khóa học kỹ năng')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'EDU', 'Học phí')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'EDU', 'Làm đẹp')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'EDU', 'Giao Lưu/Quan hệ')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'GIVE', 'Cưới Hỏi')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'GIVE', 'Ma Chay')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'GIVE', 'Từ Thiện')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'GIVE', 'Biếu Tặng')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'GIVE', 'Thăm Hỏi')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'PLAY', 'Du Lịch')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'PLAY', 'Vui Chơi Giải Trí')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'PLAY', 'Phim Ảnh')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'PLAY', 'Ca Nhạc')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'PLAY', 'Thể Thao')");
        db.execSQL("INSERT INTO MucChi VALUES(null, 'FFA', 'Đầu Tư')");
        //thêm dữ liệu mặc định vào bảng hũ JARS
        db.execSQL("INSERT INTO HuJARS VALUES('NEC','55','0','0')");
        db.execSQL("INSERT INTO HuJARS VALUES('LTSS','10','0','0')");
        db.execSQL("INSERT INTO HuJARS VALUES('EDU','10','0','0')");
        db.execSQL("INSERT INTO HuJARS VALUES('GIVE','5','0','0')");
        db.execSQL("INSERT INTO HuJARS VALUES('PLAY','10','0','0')");
        db.execSQL("INSERT INTO HuJARS VALUES('FFA','10','0','0')");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
