package com.example.shizhan.customfront.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by gehui on 16/8/3.
 */
public class User implements Serializable { //定义User和User中要用的字段，与数据库中保持一致。
    private int id;
    String user_name;
    String password;


    String email;


    String tel;


    String image;
    String brithday;
    String signature;

    public User(int id, String user_name, String password, String email, String tel, String image, String brithday, String signature, int sex) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.image = image;
        this.brithday = brithday;
        this.signature = signature;
        this.sex = sex;
    }

//    public User ConUser(User u){
//        User newUser;
//        this.User(u.getId(),u,getUser_name(),u.getPassword(),u.getEmail(),u.getTel(),u.getImage(),u.getBrithday(),u.getSignature(),u.getSex());
//    }

    private int sex;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }  //自动生成方法，右击Generate选择 Getter and Setter 然后选择定义的实体自动生成

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


}
