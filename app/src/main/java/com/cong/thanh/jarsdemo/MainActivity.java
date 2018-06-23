package com.cong.thanh.jarsdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements DeleteDataTaiKhoan {

    DrawerLayout mDrawerLayout;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    public static Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //tạo database
         database=new Database(this,"jars",null,1);
//        //tạo bảng
//        database.QueryData("CREATE TABLE IF NOT EXISTS SoTien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenST VARCHAR(200), SoTienST VARCHAR(200), GhiChuSoTien VARCHAR(200))");
            //thêm vào bảng
//        //database.QueryData("INSERT INTO SoTien VALUES(null, 'tên ST', '20000', 'ghi chú')");
        //duyệt bảng
//        Cursor dataSoTien=database.GetData("SELECT * FROM SoTien");
//        while (dataSoTien.moveToNext()){
//            String ten=dataSoTien.getString(1);
//            Toast.makeText(MainActivity.this,ten,Toast.LENGTH_SHORT).show();
//        }

        //ánh xạ các view
        Anhxa();
        // sự kiện trong navigationViewDrawer
        navigationView.setNavigationItemSelectedListener(nOnNavigationDrawerItemSelectedListener);
        //sự kiện chọn fragment mặc định khi khởi động là HomeFragment
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_navigation,new HomeFragment()).commit();

    }
    private NavigationView.OnNavigationItemSelectedListener nOnNavigationDrawerItemSelectedListener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.itemInfo: ItemInforNavigationDrawer();mDrawerLayout.closeDrawers(); return true;
                    case R.id.itemThoat:  finish(); System.exit(0);
            }
            return true;
        }
    };

    //sự kiện chọn item từ bottomm navigationView, mỗi item tương ứng với 1 fragment
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();


            switch (item.getItemId()) {
                case R.id.item_navigation_home:
                    fragmentTransaction.replace(R.id.layout_navigation,new HomeFragment()).commit();
                    return true;
                case R.id.item_navigation_taikhoan:
                    fragmentTransaction.replace(R.id.layout_navigation,new TaiKhoanFragment()).commit();
                    return true;
                case R.id.item_navigation_muchi:
                    fragmentTransaction.replace(R.id.layout_navigation,new MucChiFragment()).commit();
                    return true;
                case R.id.item_navigation_ghichep:
                    fragmentTransaction.replace(R.id.layout_navigation,new GhiChepFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    //ánh xạ các view
    private void Anhxa() {
        mDrawerLayout=findViewById(R.id.containerDrawer);
        //ánh xạ bottom navigation
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_bar);
        //remove animation when items more than 3
        removeShiftMode(bottomNavigationView);
        frameLayout=(FrameLayout) findViewById(R.id.layout_navigation);
        //khỏi tạo toolbar
        toolbar= (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        //ánh xạ navigationDrawer
        navigationView=(NavigationView)findViewById(R.id.navigationView_main);
        //bỏ title mặc định
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //button gọi menu navigation khi ấn vào, đây là hàm có sẵn trong android
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }
// thêm menu toolbar vào thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }
    // chọn item từ menu trên toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemThetlap: ItemThietLapPhanTram();return true;
            case R.id.itemHuongdan:ItemHuongDan();return true;
            case R.id.itemCaiDat: Toast.makeText(MainActivity.this,"Tính năng cài đặt hiện đang được phát triển thêm. \n" +
                    "Sẽ có trong thời gian sớm nhất",Toast.LENGTH_SHORT).show();return true;
            //gọi ra menu navigation khi nhấn vào, ID này là ID mặc định có sẵn trong android
            case android.R.id.home: mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //xử lí interface, ở sau khi chọn ok ở dialog xóa thì refesh fragment tài khoản
    @Override
    public void giatrixoa(boolean delete) {
        //nhận dữ liêu từ TaiKhoanAdapter truyền vào và xử lý
        //load lại TaiKhoanfragment sau khi thực hiện xóa một dữ liệu
        if(delete) {
            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.layout_navigation,new TaiKhoanFragment()).commit();
        }
    }
    private void ItemThietLapPhanTram(){
        final Dialog dialogThietLapPhanTram=new Dialog(MainActivity.this);
        dialogThietLapPhanTram.setContentView(R.layout.dialog_menuitem_thietlap_phantram);
        dialogThietLapPhanTram.show();
        //ánh xạ
        final EditText edtNEC=(EditText)dialogThietLapPhanTram.findViewById(R.id.NECedt);
        final EditText edtLTSS=(EditText)dialogThietLapPhanTram.findViewById(R.id.LTSSedt);
        final EditText edtEDU=(EditText)dialogThietLapPhanTram.findViewById(R.id.EDUedt);
        final EditText edtGIVE=(EditText)dialogThietLapPhanTram.findViewById(R.id.GIVEedt);
        final EditText edtPLAY=(EditText)dialogThietLapPhanTram.findViewById(R.id.PLAYedt);
        final EditText edtFFA=(EditText)dialogThietLapPhanTram.findViewById(R.id.FFAedt);
        Button btnMacDinh=(Button)dialogThietLapPhanTram.findViewById(R.id.button_macdinh);
        Button btnHuy=(Button)dialogThietLapPhanTram.findViewById(R.id.button_huy);
        Button btnLuu=(Button)dialogThietLapPhanTram.findViewById(R.id.button_luu);

        //lấy ra dữ liệu từ table HuJARS trong csdl và truyền vào các EditText
        Cursor editDataJars =database.GetData("SELECT JARS,PhanTram FROM HuJARS");
        while (editDataJars.moveToNext()){
            String huars=editDataJars.getString(0);
            int phantram=editDataJars.getInt(1);
            switch (huars){
                case "NEC":edtNEC.setText(phantram+""); break;
                case "LTSS":edtLTSS.setText(phantram+"");break;
                case "EDU":edtEDU.setText(phantram+"");break;
                case "GIVE":edtGIVE.setText(phantram+"");break;
                case "PLAY":edtPLAY.setText(phantram+"");break;
                case "FFA":edtFFA.setText(phantram+"");break;
            }

        }
        //sự kiện button hủy trong dialog
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThietLapPhanTram.cancel();
            }
        });
        //sự kiện của button mặc định trong dialog
        btnMacDinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNEC.setText("55");
                edtLTSS.setText("10");
                edtEDU.setText("10");
                edtGIVE.setText("5");
                edtPLAY.setText("10");
                edtFFA.setText("10");
            }
        });
        //sự kiện button lưu trong dialog
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtNEC.getText().toString().equals("")||edtLTSS.getText().toString().equals("")||edtEDU.getText().toString().equals("")||
                        edtGIVE.getText().toString().equals("")||edtPLAY.getText().toString().equals("")||edtFFA.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this,"Không được để trống bất kì mục nào",Toast.LENGTH_SHORT).show();
                else {
                        int nec=Integer.parseInt(edtNEC.getText().toString());
                        int ltss=Integer.parseInt(edtLTSS.getText().toString());
                        int edu=Integer.parseInt(edtEDU.getText().toString());
                        int give=Integer.parseInt(edtGIVE.getText().toString());
                        int play=Integer.parseInt(edtPLAY.getText().toString());
                        int ffa=Integer.parseInt(edtFFA.getText().toString());
                        int tong=nec+ltss+edu+give+play+ffa;
                        if (tong!=100) Toast.makeText(MainActivity.this,"Tổng % từ các hũ phải bằng 100%",Toast.LENGTH_SHORT).show();
                        else {
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+nec+"' WHERE JARS = 'NEC'");
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+ltss+"' WHERE JARS = 'LTSS'");
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+edu+"' WHERE JARS = 'EDU'");
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+give+"' WHERE JARS = 'GIVE'");
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+play+"' WHERE JARS = 'PLAY'");
                            database.QueryData("UPDATE HuJARS SET PhanTram = '"+ffa+"' WHERE JARS = 'FFA'");
                            dialogThietLapPhanTram.cancel();
                            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.layout_navigation,new HomeFragment()).commit();
                        }
                }
            }
        });
    }
    private void ItemHuongDan(){
        Dialog dialogHuongdan=new Dialog(MainActivity.this);
        dialogHuongdan.setContentView(R.layout.dialog_menuitem_huongdan);
        dialogHuongdan.show();
        //ánh xạ các button hướng dẫn
        Button btnHuongdanNEC=(Button)dialogHuongdan.findViewById(R.id.huongdanNEC);
        Button btnHuongdanLTSS=(Button)dialogHuongdan.findViewById(R.id.huongdanLTSS);
        Button btnHuongdanEDU=(Button)dialogHuongdan.findViewById(R.id.huongdanEDU);
        Button btnHuongdanFFA=(Button)dialogHuongdan.findViewById(R.id.huongdanFFA);
        Button btnHuongdanPLAY=(Button)dialogHuongdan.findViewById(R.id.huongdanPLAY);
        Button btnHuongdanGIVE=(Button)dialogHuongdan.findViewById(R.id.huongdanGIVE);

        //button NEC
        btnHuongdanNEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hũ NEC giúp bạn đảm bảo nhu cầu thiết yếu của cuộc sống đối với bạn: ăn uống, sinh hoạt, các hóa đơn điện, nước, internet, cafe mỗi sáng, sửa chữa các thiết bị trong nhà,....\n" +
                        " Nó là tất cả những khoản" +
                        "chi mà bạn thường xuyên sử dụng và cần thiết trong cuộc sống của bạn, bạn càng kiếm nhiều tiền thì nhu cầu về cuộc sống của bạn càng cao và càng nhiều. \n" +
                        "Hũ này này giúp bạn điều chỉnh được các khoản chi tiêu một cách hợp lý hơn và xây dựng được khả năng hạn chế chi tiêu hoang phí\n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com mọi thắc mắc sẽ được giải đáp nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        //button LTSS
        btnHuongdanLTSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hũ LTSS là chiếc hũ đựng những khoản tiết kiệm của bạn hằng ngày, hằng tuần hoặc hàng tháng.\n" +
                        " Hũ này dành cho những kế hoạch lâu dài của bản thân bạn như mua nhà,mua xe, làm đám cưới, chuẩn bị sinh con, đầu tư, trả nợ,…" +
                        " giúp bạn tiết kiệm và nắm bắt được mục đích bản thân cần giữ gìn tiền bạc và đẩy mạnh tiết kiệm hơn\n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com mọi thắc mắc sẽ được giải đáp nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        //button EDU
        btnHuongdanEDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hũ EDU này sẽ được sử dụng cho mục đích nâng cao kiến thức, kĩ năng của bạn thông qua các khóa học cần thiết mà bạn dự định tham gia, phát triển bản thân bằng cách học tập từ người khác thông qua giao lưu- quan hệ: bạn mời người ta bằng một chầu cafe và họ chia sẻ cho mình kiến thức, như vậy tiền bạn uống cafe đó sẽ thuộc hũ EDU" +
                        "Bạn mua sách vở, dụng cụ học tập,.... \n" +
                        "Việc nâng cao kiến thức không bao giờ là thừa thãi và sẽ giúp ích rất nhiều cho công việc của bạn trong tương lai.\n "  +
                        "Bên cạnh đó, đối với những cá nhân đã có gia đình thì khoản tiền này đặc biệt cần thiết cho con cái của bạn khi đang ở độ tuổi đi học.\n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com mọi thắc mắc sẽ được giải đáp nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        //button FFA
        btnHuongdanFFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hũ FFA là hũ bạn để dành để đầu tư sinh lời, không hoàn toàn có nghĩa ta phải dành một số tiền cho các khoản đầu tư như mua cổ phiếu, bất động sản, kinh doanh… mà là tận dụng số tiền này để sinh lời theo cách thuận tiện nhất.\n" +
                        "Nếu số tiền để dành đầu tư của bạn còn quá ít, ta có thể chọn lựa hình thức gửi tiết kiệm có kì hạn để lấy lãi suất.\n" +
                        " Hoặc nếu có điều kiện và kiến thức kinh doanh hơn," +
                        " ta có thể chọn lựa các hình thức đầu tư có quy mô khác để sinh lời.\n" +
                        " Chính vì vậy, chiếc hũ thứ 4 mới có tên gọi là Financial Freedom, tạo cơ hội cho bạn làm giàu theo mong muốn của bản thân. \n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com mọi thắc mắc sẽ được giải đáp nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        //Button PLAY
        btnHuongdanPLAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hũ PLAY là hũ dùng để quan tâm đến bản thân bằng cách dành ít nhất một khoản thu nhất định để chi tiêu cho những mong muốn của chính mình. \n" +
                        "Ta có thể dùng chúng cho các nhu cầu mua sắm, ăn uống, vui chơi, đi du lịch, hay bạn muốn thưởng thức:đồ uống, món ăn,… \n" +
                        "Hũ này giúp bạn có thể giải tỏa căng thẳng sau thời gian làm việc vất vả, cũng như tạo thêm hứng khởi để tiếp tục làm việc, kiếm tiền.\n" +
                        "Thậm chí phương pháp JARS còn khuyên bạn nên đặt khoản chi này vào danh mục bắt buộc phải thực hiện, và nên dùng hết. Bạn cũng có thể tận dụng khoản chi này nhiều tháng một lần, thành một số tiền lớn và lên kế hoạch cho một chuyến du lịch thật thú vị để tự thưởng cho bản thân.\n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com mọi thắc mắc sẽ được giải đáp nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        //Button GIVE
        btnHuongdanGIVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Cuộc sống là phải biết cho và nhận, do đó chiếc hũ thứ 6 sẽ giúp bạn có thể dành được dùng cho những hoạt động chia sẻ, quan tâm đến những người xung quanh. Ta có thể để khoản tiền này để dành ra cho người thân trong gia đình, làm từ thiện, giúp đỡ người khác khi cần. \n" +
                        "Việc cho đi sẽ luôn là cơ hội để chúng ta nhận lại những điều quý báu và cơ hội được người khác giúp đỡ khi cần.\n" +
                        "\n"+
                        "Nếu bạn có thắc mắc, câu hỏi hãy gửi mail về hòm thư: nhocchoda@gmail.com thường sẽ trả lời nhanh nhất trong vòng 24h");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
    }
    //remove animation when bottomnavigation have more than 3 item
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
    }
    //item info in toolbar
    private void ItemInforNavigationDrawer(){
        Dialog dialogItemInfoNavigation=new Dialog(MainActivity.this);
        dialogItemInfoNavigation.setContentView(R.layout.dialog_info_itemnavigationdrawer);
        dialogItemInfoNavigation.show();
    }
}
