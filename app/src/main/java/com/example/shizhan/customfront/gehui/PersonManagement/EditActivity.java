package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditActivity extends Activity implements View.OnClickListener {
    private LinearLayout linearLayout;
    /**
     * DatePickerDialog,单击按钮调用setDate(),setDate()调用showDialog(int id)
     * showDialog(int id)调用onCreateDialog(int id)，onCreateDialog(int id)
     * 使用到回调函数setDateCallBack，setDateCallBack调用upDateDisplay()设置日期
     *
     */
    NativeData native_data;
    private AvatarImageView avatarImageView;
    String Image_Str = null;
    private static final int DIALOG_DATE_ID = 0;

    //当前系统的年月日
    private int mYear;
    private int mMonth;
    private int mDay;


    private TextView text_edit_nickname;
    private RadioButton radio_radio_man;
    private RadioButton radio_radio_woman;
    private RadioGroup radio_ed_radio_group;
    private int WMcheckId = -1;
    private TextView text_edit_signature;
    private TextView text_ed_birthday;


    LinearLayout button3;//用于返回


    protected boolean updateUserInfoFromPanel() {
        if ((native_data == null) && (native_data.isNull())) {
            return false;
        }
        String user_name = (String) text_edit_nickname.getText(); //用户名字
        int sex; //性别
        if (WMcheckId == radio_radio_woman.getId()) {
            sex = 1;
        } else {
            sex = 0;
        }
        String birthday = (String) text_ed_birthday.getText(); //
        String sigature = (String) text_edit_signature.getText();
        int uid = -1;
        //String password=null; // password实在密码窗口进行更新的
        String email = null;
        String tel = null;
        String image = "";

        if (!(Image_Str.equals(""))) {
            image = Image_Str;
        }

        native_data.getInfoFromWidget(uid, user_name, email, tel, sex, birthday, sigature, image);
        return true;
    }

    public void updateNativeAndInternetData() {
        User tmp_usr = new User(native_data.user.getId(),
                native_data.user.getUser_name(),
                native_data.user.getPassword(),
                native_data.user.getEmail(),
                native_data.user.getTel(),
                native_data.user.getImage(),
                native_data.user.getBrithday(),
                native_data.user.getSignature(),
                native_data.user.getSex());

        updateUserInfoFromPanel();
        boolean ret = native_data.changeUserInfomation();
        if (ret) {
            Toast.makeText(EditActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditActivity.this, "更新信息失败！", Toast.LENGTH_SHORT).show();
            native_data = new NativeData(tmp_usr);//事务回滚
        }


        setUserInfoOnPanel();


    }

    public boolean getDataFromNative() {
        Intent intent = this.getIntent();
        User user = null;
        user = (User) intent.getSerializableExtra("user");
        if (user == null) {
            return false;

        } else
            native_data = new NativeData(user);
        return true;
    }

    public void setHeadPortrait() {

        Image_Str = native_data.user.getImage();
        Bitmap bitm = NativeData.openBitmap(Image_Str);
        if (bitm != null)
            avatarImageView.setImageBitmap(bitm);

    }

    public void setBirth() {

        String str = native_data.user.getBrithday();
        if (str.equals(""))
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd"); //兼容两种格式
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                date = sdf.parse(str);
            } catch (ParseException ee) {

                ee.printStackTrace();
                return;
            }


        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


    }

    public boolean setUserInfoOnPanel() {

        if ((native_data == null))
            return false;

        if (native_data.isNull())
            return false;


        text_edit_nickname.setText(native_data.user.getUser_name());  //设定用户名
        if (native_data.user.getSex() == 1)                             //设定性别
            radio_radio_woman.setChecked(true);
        else
            radio_radio_man.setChecked(true);

        text_ed_birthday.setText(native_data.user.getBrithday());
        setBirth();
        setHeadPortrait();

        text_edit_signature.setText(native_data.user.getSignature());


        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.personal_edit);


        avatarImageView = (AvatarImageView) findViewById(R.id.avatarIv);


        avatarImageView.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo) {


                Image_Str = NativeData.saveBitmap(photo, "head.bmp");
                updateNativeAndInternetData();
                Toast.makeText(EditActivity.this, "设置新的头像成功", Toast.LENGTH_SHORT).show();
            }
        });


        ///////////触动对话框
        //    setContentView(R.layout.personal_edit); //不用这个设定view 的范围
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.setting_nickname);
        linearLayout.setOnClickListener(this);

//        setContentView(R.layout.personal_edit);
////        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.edit_birthday);
////        linearLayout2.setOnClickListener(this);
//        setContentView(R.layout.personal_edit);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.setting_sign);
        linearLayout3.setOnClickListener(this);
        //////////
        button3 = (LinearLayout) findViewById(R.id.edit_back);   /** 定义一个button，以便出动按钮时能通过ID找到按钮*/

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (native_data == null) {
                    return;

                }

                Intent intent = new Intent(EditActivity.this, SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", native_data.user);//序列化
                intent.putExtras(bundle);//发送数据
                startActivity(intent);//启动intent

                // Intent intent = new Intent(EditActivity.this, SettingActivity.class);
                //  startActivity(intent);
            }
        });
        LinearLayout button4 = (LinearLayout) findViewById(R.id.edit_password);   /** 定义一个button，以便出动按钮时能通过ID找到按钮*/

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (native_data == null) {
                    Toast.makeText(EditActivity.this, "没有网络连接不能修改密码", Toast.LENGTH_SHORT).show();
                    return;

                }

                Intent intent = new Intent(EditActivity.this, edit_password.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", native_data.user);//序列化
                intent.putExtras(bundle);//发送数据
                startActivity(intent);//启动intent

                //   Intent intent = new Intent(EditActivity.this, edit_password.class);
                //  startActivity(intent);
            }
        });


        text_edit_nickname = (TextView) findViewById(R.id.edit_nickname);
        radio_radio_man = (RadioButton) findViewById(R.id.radio_man);
        radio_radio_woman = (RadioButton) findViewById(R.id.radio_woman);
        radio_ed_radio_group = (RadioGroup) findViewById(R.id.ed_radio_group);
        text_edit_signature = (TextView) findViewById(R.id.edit_signature);
        text_ed_birthday = (TextView) findViewById(R.id.ed_birthday);


        if (getDataFromNative()) {
            setUserInfoOnPanel();
        } else {
            Toast.makeText(EditActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
        }


        //这个代码不能放在前面，因为初始化时候设置了按钮组，会触发事件
        radio_ed_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (native_data == null) {
                    Toast.makeText(EditActivity.this, "没有网络连接不能修改性别", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (native_data.isNull()) {
                    Toast.makeText(EditActivity.this, "没有数据不能修改性别", Toast.LENGTH_SHORT).show();
                    return;
                }

                WMcheckId = checkedId;
                if (checkedId == radio_radio_man.getId()) {
                    updateNativeAndInternetData();
                } else if (checkedId == radio_radio_woman.getId()) {
                    updateNativeAndInternetData();
                }
            }
        });


        //yang
        // native_data=new NativeData();
        //接收数据库信息


//        native_data.handler = new Handler() {
//            public void handleMessage(Message message) {
//                if (message.what == NativeData.SHOW_USER) {   //如果返回的是SHOW_USER
//                    text_edit_nickname.setText(native_data.user.getUser_name());  //设定用户名
//                    if (native_data.user.getSex() == 1)                             //设定性别
//                        radio_radio_woman.setChecked(true);
//                    else
//                        radio_radio_man.setChecked(true);
//                    text_ed_birthback.setText(native_data.user.getBrithday());
//                    text_edit_signature.setText(native_data.user.getSignature());
//
//                } else if (message.what == NativeData.SHOW_USER){
//                    text_edit_signature.setText("nibie");
//                } //这些都是接收组件的id
//            }
//        };
        //native_data.changeUserInfomation();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (native_data == null) {
            Toast.makeText(EditActivity.this, "没有网络连接不能修改头像", Toast.LENGTH_SHORT).show();
            return;
        }
        if (native_data.isNull()) {
            Toast.makeText(EditActivity.this, "没有数据不能修改头像", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (avatarImageView != null) {
            avatarImageView.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void updateDiaplay() {
        StringBuffer stringBuffer = new StringBuffer();

        String strYear = String.valueOf(mYear);
        mMonth++;
        String strMounth = String.valueOf(mMonth);
        if (strMounth.length() == 1) {
            strMounth = "0" + strMounth;
        }
        String strDay = String.valueOf(mDay);

        if (strDay.length() == 1) {
            strDay = "0" + strDay;
        }


        stringBuffer.append(strYear).append(strMounth).append(strDay);
        text_ed_birthday.setText(stringBuffer);
        updateNativeAndInternetData();
    }

    //单击按钮调用setDate()方法
    public void setDate(View v) {
        if (native_data == null) {
            Toast.makeText(EditActivity.this, "没有网络连接不能修改日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (native_data.isNull()) {
            Toast.makeText(EditActivity.this, "没有数据不能修改日期", Toast.LENGTH_SHORT).show();
            return;
        }

        setDate();
    }

    //setDate()方法调用showDialog(int id)方法，showDialog(int id)方法调用onCreateDialog(int id)
    private void setDate() {
        showDialog(DIALOG_DATE_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DATE_ID:
                //返回一个日期对话框
                return new DatePickerDialog(this, setDateCallBack, mYear, mMonth, mDay);
        }
        return super.onCreateDialog(id);
    }

    //回调函数，int year, int monthOfYear,int dayOfMonth三个参数为日期对话框设置的日期
    private OnDateSetListener setDateCallBack = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDiaplay();
        }
    };


    EditText editText;

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.setting_nickname:

                return;
//                if (native_data==null){
//                    Toast.makeText(EditActivity.this,"没有网络连接不能修改昵称",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(native_data.isNull()){
//                    Toast.makeText(EditActivity.this,"没有数据不能修改昵称",Toast.LENGTH_SHORT).show();
//                    return;
//                }

//
//                editText = new EditText(this);
//                new AlertDialog.Builder(this)
//                        .setTitle("请输入昵称：")
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setView(editText )
//                        .setPositiveButton("确认", new DialogInterface.
//                                OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                text_edit_nickname.setText(editText.getText().toString());
//                                updateNativeAndInternetData();
//                            }
//                        })
//                        .setNegativeButton("取消", null)
//                        .show();
//
//                        break;

            case R.id.edit_birthday:


                AlertDialog.Builder dialog2 = new AlertDialog.Builder
                        (EditActivity.this).setView(new EditText(this));
                dialog2.setTitle("请输入生日：");
                dialog2.setCancelable(false);
                dialog2.setPositiveButton("确认", new DialogInterface.
                        OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog2.setNegativeButton("取消", new DialogInterface.
                        OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog2.show();
                break;
            case R.id.setting_sign:

                if (native_data == null) {
                    Toast.makeText(EditActivity.this, "没有网络连接不能修改签名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (native_data.isNull()) {
                    Toast.makeText(EditActivity.this, "没有数据不能修改签名", Toast.LENGTH_SHORT).show();
                    return;
                }

                editText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入个性签名：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确认", new DialogInterface.
                                OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                text_edit_signature.setText(editText.getText().toString());
                                updateNativeAndInternetData();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


//                editText = new EditText(this);
//                AlertDialog.Builder dialog3 = new AlertDialog.Builder
//                        (EditActivity.this).setView(new EditText(this));
//                dialog3.setTitle("请输入个性签名：");
//                dialog3.setCancelable(false);
//                dialog3.setPositiveButton("确认", new DialogInterface.
//                        OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
//                dialog3.setNegativeButton("取消", new DialogInterface.
//                        OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
//                dialog3.show();
                break;
            default:
                break;
        }
    }


//simple_list_item_1
//        PersonAdapter adapter = new PersonAdapter(EditActivity.this, data);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                EditActivity.this, android.R.layout.simple_list_item_1, data);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);*/


//       ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//             EditActivity.this, android.R.layout.simple_list_item_1, data);
//        ListView listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(adapter);


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            button3.performClick();

        }

        return false;

    }


}