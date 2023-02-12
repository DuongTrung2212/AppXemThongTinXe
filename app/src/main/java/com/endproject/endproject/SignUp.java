package com.endproject.endproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import api.APIServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    TextView ed_name, ed_email,ed_password, ed_rePassword, loginTxt,tv_error;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();


        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_error.setText("");
                String name = ed_name.getText().toString().trim();
                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
                String rePassword = ed_rePassword.getText().toString().trim();
                if(name.equals("") || email.equals("") || password.equals("") || rePassword.equals("")){
                    tv_error.setText("Cần nhập đủ thông tin!");
                } else if(!password.equals(rePassword)) {
                    tv_error.setText("Nhập lại mật khẩu không đúng!");
                } else {
                    Log.d("hihi", "hihi");
                    APIServices.apiServices.signUp(name, email, password).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                switch (jsonObject.getInt("code")){
                                    case 400:
                                        tv_error.setText("Có lỗi đã xảy ra");
                                        Toast.makeText(SignUp.this, "Có lỗi đã xảy ra", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 350:
                                        tv_error.setText("Email không đúng");
                                        Toast.makeText(SignUp.this, "Email không đúng", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 300:
                                        tv_error.setText("Email đã tồn tại");
                                        Toast.makeText(SignUp.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 201:
                                        tv_error.setText("Đăng ký thành công");
                                        Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        notification();

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 2000);
                                        break;
                                    default:
                                        tv_error.setText("Lỗi hệ thống");
                                        Toast.makeText(SignUp.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e){

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    public void notification(){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.anime_bg1) // notification icon
                .setContentTitle("Thành công") // title for notification
                .setContentText("Đăng ký thành công")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void initUI() {
        ed_name = findViewById(R.id.ed_name);
        ed_password = findViewById(R.id.ed_pass);
        ed_rePassword = findViewById(R.id.ed_rePass);
        ed_email = findViewById( R.id.ed_email);
        btnSignUp = findViewById( R.id.btn_signUp);
        loginTxt = findViewById(R.id.login);
        tv_error = findViewById( R.id.tv_error);
    }
}