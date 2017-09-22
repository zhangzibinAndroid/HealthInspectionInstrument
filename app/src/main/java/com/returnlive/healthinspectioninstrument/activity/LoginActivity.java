package com.returnlive.healthinspectioninstrument.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.application.HealthApplication;
import com.returnlive.healthinspectioninstrument.base.BaseActivity;
import com.returnlive.healthinspectioninstrument.bean.LoginBean;
import com.returnlive.healthinspectioninstrument.constant.Code;
import com.returnlive.healthinspectioninstrument.constant.InstanceUrl;
import com.returnlive.healthinspectioninstrument.constant.UserMessage;
import com.returnlive.healthinspectioninstrument.gson.GsonParsing;
import com.returnlive.healthinspectioninstrument.version_update.UpdateInfo;
import com.returnlive.healthinspectioninstrument.version_update.UpdateInfoService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.USER_LOGIN;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.img_loging_delect)
    ImageView userDelect;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.img_loging_password_delect)
    ImageView passwordDelect;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_registered)
    TextView tvRegistered;
    private Unbinder unbinder;
    private static final String TAG = "LoginActivity";
    // 更新版本要用到的一些信息
    private UpdateInfo info;
    private ProgressDialog pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //获取Activity的Preferences对象
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        //从Preferences对象中获取登陆信息并显示到EditText中
        edt_username.setText(sp.getString("loginName", ""));
        edt_password.setText(sp.getString("password", ""));
        HealthApplication.addActivity(this);
        edt_username.setHintTextColor(Color.argb(125, 255, 255, 255));
        edt_password.setHintTextColor(Color.argb(125, 255, 255, 255));
        edt_username.setTextColor(Color.argb(125, 255, 255, 255));
        edt_password.setTextColor(Color.argb(125, 255, 255, 255));
        //设置控件获取焦点监听
        edt_username.addTextChangedListener(textWatcher);
        edt_password.addTextChangedListener(textWatcher);

        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    UpdateInfoService updateInfoService = new UpdateInfoService(
                            getApplicationContext());
                    info = updateInfoService.getUpDateInfo();
                    versionHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();

    }


    @OnClick({R.id.btn_login, R.id.tv_registered})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                final String name = edt_username.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String pwds = edt_password.getText().toString().trim();
                if (TextUtils.isEmpty(pwds)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                showIOSLodingDialog("正在登陆");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginInstance(name,pwds);
                    }
                }).start();

                break;
            case R.id.tv_registered:
                JumpActivity(RegisterActivity.class);

                break;
        }
    }

    private void loginInstance(String name, String pwds) {
        //定义SharedPreferences.Editor接口对象
        SharedPreferences.Editor edit = getPreferences(0).edit();
        //吧登陆信息保存到preferences中
        edit.putString("loginName", name);
        edit.putString("password", pwds);
        //提交信息
        edit.commit();
        OkHttpUtils.post().url(USER_LOGIN)
                .addParams("phone",name)
                .addParams("passwd",pwds)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissIOSDialog();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        loginHandler.sendMessage(msg);
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /**
             * "登录"设置控件“删除”按钮是否显示(控件是否为空)
             */

            if (edt_username.length() == 0) {
                userDelect.setVisibility(View.GONE);
            } else {
                userDelect.setVisibility(View.VISIBLE);
                userDelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_username.setText("");
                    }
                });
            }
            if (edt_password.length() == 0) {
                passwordDelect.setVisibility(View.GONE);
            } else {
                passwordDelect.setVisibility(View.VISIBLE);
                passwordDelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_password.setText("");
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(Code.SUCCESS) > 0) {
                try {
                    LoginBean loginBean = GsonParsing.getLoginMessage(result);
                    InstanceUrl.mid = loginBean.getInfo().getId();
                    InstanceUrl.uid = loginBean.getInfo().getE_id();
                    UserMessage.phone = loginBean.getInfo().getPhone();
                    JumpActivity(MenuActivity.class);
                    dismissIOSDialog();

                } catch (Exception e) {
                    Log.e(TAG, "获取用户信息解析失败: "+e.getMessage() );
                }
            }else {
                dismissIOSDialog();
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "短信验证解析异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());

            }

        }
    };



    @SuppressLint("HandlerLeak")
    private Handler versionHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (isNeedUpdate()) {
                showUpdateDialog();
            }
        };
    };


    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("有新版本" + info.getVersion());
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getUrl());
                } else {
                    Toast.makeText(getApplicationContext(), "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.create().show();
    }

    private boolean isNeedUpdate() {
        String v = info.getVersion(); // 最新版本的版本号
//        Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
        if (v.equals(getVersion())) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";

        }
    }

    void downFile(final String url) {
        pBar = new ProgressDialog(this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCanceledOnTouchOutside(false);
        pBar.setTitle("正在更新");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory()+"/health" , "health.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        down();
                    }


                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                        int mtotal = (int) total;
                        pBar.setMax(mtotal);
                        pBar.setProgress((int)(mtotal* progress));
                    }
                });
            }
        }).start();
    }

    void down() {
        versionHandler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory()+"/health", "health.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
