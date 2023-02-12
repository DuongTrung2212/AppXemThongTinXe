package com.endproject.endproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;

import api.APIServices;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {


    View settingView;
    CircleImageView avatar_info;
    TextView tv_nameInfo,tv_emailInfor,tv_phoneInfo,tv_sexInfo, txtWarn,txtWarnChangeInfo, btnShowChangePassFrom,
            btnShowChangeInfo, btnClear, btnLogout;
    MainActivity mainActivity;
    Button btnChangePassword, btnChangeInfo, btnAction;
    EditText ed_oldPassword, ed_newPassword, ed_reNewPassword, ed_name, ed_phone;
    RadioButton rd_male, rd_female;
    LinearLayout viewActionList, formChangePassword, formChangeInfo;
    String sexChange="Nam";
    boolean isActionListShow = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingView=inflater.inflate(R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment

        String idUser=mainActivity.idUser;
        avatar_info=settingView.findViewById(R.id.avatar_info);
        tv_nameInfo=settingView.findViewById(R.id.tv_nameInfo);
        tv_emailInfor=settingView.findViewById(R.id.tv_emailInfor);
        tv_phoneInfo=settingView.findViewById(R.id.tv_phoneInfo);
        tv_sexInfo=settingView.findViewById(R.id.tv_sexInfo);

        btnAction = settingView.findViewById(R.id.btnAction);
        viewActionList = settingView.findViewById(R.id.viewActionList);
        btnShowChangePassFrom = settingView.findViewById(R.id.btnShowChangePassFrom);
        btnShowChangeInfo =settingView.findViewById(R.id.btnShowChangeInfo);
        btnClear = settingView.findViewById(R.id.btnClear);
        btnLogout = settingView.findViewById(R.id.btnLogout);

        formChangePassword = settingView.findViewById(R.id.formChangePassword);
        formChangeInfo = settingView.findViewById(R.id.formChangeInfo);

        btnChangePassword = settingView.findViewById(R.id.btnChangePassword);
        ed_oldPassword = settingView.findViewById(R.id.ed_oldPassword);
        ed_newPassword = settingView.findViewById(R.id.ed_newPassword);
        ed_reNewPassword = settingView.findViewById(R.id.ed_reNewPassword);
        txtWarn = settingView.findViewById(R.id.txtWarn);

        ed_name = settingView.findViewById(R.id.ed_name);
        ed_phone = settingView.findViewById(R.id.ed_phone);
        txtWarnChangeInfo = settingView.findViewById(R.id.txtWarnChangeInfo);
        btnChangeInfo = settingView.findViewById(R.id.btnChangeInfo);
        rd_male = settingView.findViewById(R.id.rd_male);
        rd_female = settingView.findViewById(R.id.rd_female);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity.context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtWarnChangeInfo.setText("");
                String name = ed_name.getText().toString().trim();
                String phone = ed_phone.getText().toString().trim();

                if(name.isEmpty() || phone.isEmpty()){
                    txtWarnChangeInfo.setText("Cần nhập đủ thông tin!");
                } else {
                    APIServices.apiServices.updateUser(idUser, name, phone, sexChange).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());

                                int code = jsonObject.getInt("code");
                                if(code == 201) {
                                    notificationChangeInfo();
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    formChangeInfo.setVisibility(View.GONE);
                                    tv_nameInfo.setText(jsonObjectData.getString("name"));
                                    tv_phoneInfo.setText(jsonObjectData.getString("phone_number"));
                                    tv_sexInfo.setText(jsonObjectData.getString("sex"));
                                } else {
                                    txtWarnChangeInfo.setText("Lỗi hệ thống!");
                                }
                            } catch (Exception e) {
                                Log.d("loii", e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        rd_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexChange = "Nam";
            }
        });
        rd_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexChange = "Nữ";
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formChangeInfo.setVisibility(View.GONE);
                formChangePassword.setVisibility(View.GONE);
            }
        });
        btnShowChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formChangeInfo.setVisibility(View.VISIBLE);
                formChangePassword.setVisibility(View.GONE);
            }
        });
        btnShowChangePassFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formChangeInfo.setVisibility(View.GONE);
                formChangePassword.setVisibility(View.VISIBLE);
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isActionListShow == false) {
                    viewActionList.setVisibility(View.VISIBLE);
                    isActionListShow = true;
                } else {
                    viewActionList.setVisibility(View.GONE);
                    isActionListShow = false;
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = ed_oldPassword.getText().toString().trim();
                String newPass = ed_newPassword.getText().toString().trim();
                String reNewPass = ed_reNewPassword.getText().toString().trim();
                txtWarn.setText("");
                if(oldPass.isEmpty() || newPass.isEmpty() || reNewPass.isEmpty()) {
                    txtWarn.setText("Cần nhập đủ thông tin!");
                } else if(oldPass.equals(newPass)) {
                    txtWarn.setText("Mật khẩu mới giống mật khẩu cũ!");
                } else if(!newPass.equals(reNewPass)) {
                    txtWarn.setText("Nhập lại mật khẩu mới không đúng!");
                } else {
                    APIServices.apiServices.changePassword(idUser, oldPass, newPass ).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                Log.d("res", jsonObject.toString());
                                String code = jsonObject.getString("code");
                                if(code.equals("201")) {
                                    notification();
                                } else if(code.equals("320")) {
                                    txtWarn.setText("Mật khẩu cũ không đúng!");
                                }
                                else {
                                    txtWarn.setText("Đổi mật khẩu không thành công!");
                                }
                            } catch (Exception e) {

                                Log.d("loi", e.toString());
                                txtWarn.setText("Lỗi hệ thống!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }



            }
        });

        APIServices.apiServices.getUser(idUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject dataUser=new JSONObject(response.body().string());
                    String id=dataUser.getString("id");
                    String name=dataUser.getString("name");
                    String email=dataUser.getString("email");
                    String phone_number=dataUser.getString("phone_number");
                    String avatar=dataUser.getString("avatar");
                    String sex=dataUser.getString("sex");
                    Log.d("trung",name);
                    Glide.with(mainActivity.context).load("http://"+avatar).error(R.drawable.anime_bg1).into(avatar_info);
                    tv_nameInfo.setText(name);
                    tv_emailInfor.setText(email);
                    tv_phoneInfo.setText(phone_number);
                    tv_sexInfo.setText(sex);
                    ed_name.setText(name);
                    ed_phone.setText(phone_number);
                    if(sex.equals("Nữ")){
                        rd_female.setChecked(true);
                    } else {
                        rd_male.setChecked(true);
                    }


                } catch (Exception e) {
                    Log.d("trung","Erro");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




        return settingView;
    }
    public void notification(){
        NotificationManager mNotificationManager =
                (NotificationManager) mainActivity.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mainActivity.context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.anime_bg1) // notification icon
                .setContentTitle("Thành công") // title for notification
                .setContentText("Thay đổi mật khẩu thành công")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(mainActivity.context, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mainActivity.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
    public void notificationChangeInfo(){
        NotificationManager mNotificationManager =
                (NotificationManager) mainActivity.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mainActivity.context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.anime_bg1) // notification icon
                .setContentTitle("Thành công") // title for notification
                .setContentText("Thay đổi thông tin thành công")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(mainActivity.context, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mainActivity.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}