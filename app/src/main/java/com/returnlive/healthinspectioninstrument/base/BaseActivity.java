package com.returnlive.healthinspectioninstrument.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.linktop.MonitorDataTransmissionManager;
import com.returnlive.dialoglibrary.IOSLoadingDialog;
import com.returnlive.healthinspectioninstrument.bean.ReturnCode;

import static com.returnlive.healthinspectioninstrument.constant.Code.DATA_EMPTY;
import static com.returnlive.healthinspectioninstrument.constant.Code.DATA_UPLOADING_FAILURE;
import static com.returnlive.healthinspectioninstrument.constant.Code.MID_NOTEXIT;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_CODE;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_ERROR;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_HAVE;
import static com.returnlive.healthinspectioninstrument.constant.Code.SENDCODE_ERROR;
import static com.returnlive.healthinspectioninstrument.constant.Code.UID_NOTEXIT;
import static com.returnlive.healthinspectioninstrument.constant.Code.USER_NOTEXIT;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 上午 11:58
 * 描述： Activity基类
 */

public class BaseActivity extends AppCompatActivity {
    private String[] permission = new String[1];
    protected MonitorDataTransmissionManager manager;
    protected IOSLoadingDialog iosLoadingDialog;
    protected ReturnCode returnCode;

    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    protected void setReplaceFragment(@IdRes int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment).commit();
    }

    public void initPermission(String[] permissions) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            int j = ContextCompat.checkSelfPermission(this, permissions[1]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                permission[0] = permissions[0];
                Log.e("TAG", "1permission[0]: "+permission[0] );
                startRequestPermission(permission, 1);
            }

            if (j != PackageManager.PERMISSION_GRANTED) {
                permission[0] = permissions[1];
                Log.e("TAG", "2permission[0]: "+permission[0] );
                startRequestPermission(permission, 2);
            }
        }
    }


    // 开始提交请求权限
    private void startRequestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        Toast.makeText(getApplicationContext(), "储存空间权限未开启，请去应用设置界面手动开启权限", Toast.LENGTH_SHORT).show();
                    }
                }

                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "权限获取成功", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }


        if (requestCode == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        Toast.makeText(getApplicationContext(), "位置信息权限未开启，请去应用设置界面手动开启权限", Toast.LENGTH_SHORT).show();
                    }
                }


                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "权限获取成功", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }



  protected void showIOSLodingDialog(String hintMsg){
      iosLoadingDialog = new IOSLoadingDialog().setOnTouchOutside(true);
      iosLoadingDialog.setOnTouchOutside(false);
      iosLoadingDialog.setHintMsg(hintMsg);
      iosLoadingDialog.show(getFragmentManager(), "iosLoadingDialog");
  }

  protected void dismissIOSDialog(){
      if (iosLoadingDialog!=null){
          iosLoadingDialog.dismiss();
      }
  }

    protected void runOnUiToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void getCodeStatus(int code){
        switch (code){
            case PHONE_CODE:
                runOnUiToast("请输入正确电话");
                break;
            case PHONE_HAVE:
                runOnUiToast("电话以存在请直接登录");
                break;
            case PHONE_ERROR:
                runOnUiToast("短信码获取失败");
                break;
            case SENDCODE_ERROR:
                runOnUiToast("短信码错误");
                break;
            case USER_NOTEXIT:
                runOnUiToast("用户不存在");
                break;
            case MID_NOTEXIT:
                runOnUiToast("m_id不可为空");
                break;
            case UID_NOTEXIT:
                runOnUiToast("uid不可为空");
                break;
            case DATA_EMPTY:
                runOnUiToast("数据不可为空");
                break;
            case DATA_UPLOADING_FAILURE:
                runOnUiToast("数据上传失败");
                break;
        }
    }



}
