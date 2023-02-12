package com.endproject.endproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import api.APIServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText ed_email,ed_pass;
    Button btn_login;
    TextView signup,tv_err;
    CheckBox cb_remember;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        Boolean check=preferences.getBoolean("remember",false);
        if(check==true){
            ed_email.setText(preferences.getString("email",""));
            ed_pass.setText(preferences.getString("pass",""));
            cb_remember.setChecked(true);
        }else{
            cb_remember.setChecked(false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();


        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("remember",b);
                    editor.putString("email",ed_email.getText().toString());
                    editor.putString("pass",ed_pass.getText().toString());
                    editor.apply();
                }else{
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("remember",b);
                    editor.putString("email","");
                    editor.putString("pass","");
                    editor.apply();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_email.getText().toString().trim().isEmpty()
                        ||ed_pass.getText().toString().trim().isEmpty()){
                    tv_err.setText("Vui lòng nhập đầy đủ các trường!!!!");
                }else{
                    check();
                    APIServices.apiServices.login(ed_email.getText().toString(),ed_pass.getText().toString()).enqueue(
                            new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body().string());
//                                    Log.d("trung",jsonObject.getString("code")+"");
                                        JSONObject dataUser=jsonObject.getJSONObject("data");
                                        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtra("idUser",dataUser.getString("id"));
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        tv_err.setText("Sai tài khoản hoặc mật khẩu!!!!");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("trung","Fail");
                                    tv_err.setText("Lỗi server");
                                }
                            });
                }


            }
        });



    }
    public void check(){
        if(cb_remember.isChecked()){
            SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putBoolean("remember",true);
            editor.putString("email",ed_email.getText().toString());
            editor.putString("pass",ed_pass.getText().toString());
            editor.apply();
        }else{
            SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putBoolean("remember",false);
            editor.putString("email","");
            editor.putString("pass","");
            editor.apply();
        }
    }
    private void initUI() {
        ed_email=findViewById(R.id.ed_email);
        ed_pass=findViewById(R.id.ed_pass);
        btn_login=findViewById(R.id.btn_login);
        cb_remember=findViewById(R.id.cb_remember);
        signup=findViewById(R.id.signup);
        tv_err=findViewById(R.id.tv_error);
    }
}