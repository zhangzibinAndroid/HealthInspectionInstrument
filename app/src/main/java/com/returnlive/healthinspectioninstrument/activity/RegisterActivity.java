package com.returnlive.healthinspectioninstrument.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseActivity;
import com.returnlive.healthinspectioninstrument.bean.ReturnCode;
import com.returnlive.healthinspectioninstrument.constant.Code;
import com.returnlive.healthinspectioninstrument.gson.GsonParsing;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.USER_REGISTER;
import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.VERIFICATION_CODE;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edt_phone_register)
    EditText edtPhone;
    @BindView(R.id.edt_write_code)
    EditText edtWriteCode;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    private ReturnCode returnCode;
    private static final long TIME = 120;
    private long mTimeRemaining;
    private CountDownTimer mCountDownTimer;
    private String phone, sendCode;
    private static final String TAG = "RegisterActivity";
    private Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unBinder = ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        edtPhone.setText("");
        edtWriteCode.setText("");
        edtPhone.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) edtPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtPhone, InputMethodManager.SHOW_FORCED);
    }

    @OnClick({R.id.toolbar, R.id.btn_send_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.btn_send_code:
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendCodeInterface(phone);
                    }
                }).start();

                break;
            case R.id.btn_register:
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendCode = edtWriteCode.getText().toString();
                if (TextUtils.isEmpty(sendCode)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerInterface(phone, sendCode);
                    }
                }).start();
                break;
        }
    }


    //短信验证倒计时
    private void startTime(long time) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time * 1000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                if (!RegisterActivity.this.isFinishing()) {
                    mTimeRemaining = ((millisUnitFinished / 1000) + 1);
                    btnSendCode.setText("已发送:" + mTimeRemaining);
                    btnSendCode.setClickable(false);
                    btnSendCode.setBackground(getResources().getDrawable(R.drawable.shape_color_pwd));
                } else {
                    Log.e(TAG, "onTick: 执行中.....");
                }

            }

            @Override
            public void onFinish() {
                btnSendCode.setBackground(getResources().getDrawable(R.drawable.shape_color_pwd2));
                btnSendCode.setText("重新获取");
                btnSendCode.setClickable(true);
            }
        };
        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            btnSendCode.setClickable(true);
        }
        unBinder.unbind();
    }


    //短信验证码接口
    private void sendCodeInterface(String phone) {
        OkHttpUtils.post().url(VERIFICATION_CODE)
                .addParams("phone", phone)
                .addParams("type", "3")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        runOnUiToast(getResources().getString(R.string.net_error));
                        Log.e(TAG, "获取短信验证码接口请求失败：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        sendCodeHandler.sendMessage(msg);
                    }
                });
    }

    private Handler sendCodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;

            if (result.indexOf(Code.SUCCESS) > 0) {
                startTime(TIME);
                Toast.makeText(RegisterActivity.this, "短信验证码已发送", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "短信验证解析异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());
            }
        }
    };


    private void registerInterface(String phone, String sendCode) {
        OkHttpUtils.post().url(USER_REGISTER)
                .addParams("phone", phone)
                .addParams("msgcode", sendCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        runOnUiToast(getResources().getString(R.string.net_error));
                        Log.e(TAG, "注册接口请求失败: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        registerHandler.sendMessage(msg);
                    }
                });
    }

    private Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(Code.SUCCESS) > 0) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "短信验证解析异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());
            }

        }
    };

}
