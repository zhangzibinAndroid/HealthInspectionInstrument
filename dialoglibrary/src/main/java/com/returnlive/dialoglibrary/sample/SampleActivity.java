package com.returnlive.dialoglibrary.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.dialoglibrary.ActionSheetDialog;
import com.returnlive.dialoglibrary.ActionSheetDialog.OnSheetItemClickListener;
import com.returnlive.dialoglibrary.ActionSheetDialog.SheetItemColor;
import com.returnlive.dialoglibrary.AlertDialog;
import com.returnlive.dialoglibrary.HintDialog;
import com.returnlive.dialoglibrary.IOSLoadingDialog;
import com.returnlive.dialoglibrary.LoadingDialog;
import com.returnlive.dialoglibrary.PhotoDialog;
import com.returnlive.dialoglibrary.R;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/21 0021
 * 时间： 下午 3:18
 * 描述： 使用方法
 */
public class SampleActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btn_clear_message, btn_send_picture, btn_item, btn_exit, btn_tongzhi;
    private TextView btn_loading_android, btn_loading_ios, btn_prompt_ios, btn_prompt_one_ios, btn_phone;
    private HintDialog hintDialog = new HintDialog(); // 提示框
    private HintDialog singleHintDialog = new HintDialog(); // 单个提示框
    private PhotoDialog photoDialog = new PhotoDialog(); // 拍照 选择相册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        initView();
    }

    private void initView() {
        btn_clear_message = (TextView) findViewById(R.id.btn_clear_message);
        btn_send_picture = (TextView) findViewById(R.id.btn_send_picture);
        btn_item = (TextView) findViewById(R.id.btn_item);
        btn_exit = (TextView) findViewById(R.id.btn_exit);
        btn_tongzhi = (TextView) findViewById(R.id.btn_tongzhi);

        btn_loading_android = (TextView) findViewById(R.id.btn_loading_android);
        btn_loading_ios = (TextView) findViewById(R.id.btn_loading_ios);
        btn_prompt_ios = (TextView) findViewById(R.id.btn_prompt_ios);
        btn_prompt_one_ios = (TextView) findViewById(R.id.btn_prompt_one_ios);
        btn_phone = (TextView) findViewById(R.id.btn_phone);

        btn_clear_message.setOnClickListener(this);
        btn_send_picture.setOnClickListener(this);
        btn_item.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_tongzhi.setOnClickListener(this);

        btn_loading_android.setOnClickListener(this);
        btn_loading_ios.setOnClickListener(this);
        btn_prompt_ios.setOnClickListener(this);
        btn_prompt_one_ios.setOnClickListener(this);
        btn_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_clear_message) {
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("清空消息列表", SheetItemColor.Red,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            }).show();
        } else if (i == R.id.btn_send_picture) {
            new ActionSheetDialog(this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("发送给好友", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            })
                    .addSheetItem("转载到空间相册", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            })
                    .addSheetItem("上传到群相册", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            })
                    .addSheetItem("保存到手机", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            })
                    .addSheetItem("收藏", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            })
                    .addSheetItem("查看聊天图片", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {

                                }
                            }).show();
        } else if (i == R.id.btn_item) {
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("请选择操作")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("条目一", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目二", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目三", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目四", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目五", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目六", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目七", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目八", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目九", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addSheetItem("条目十", SheetItemColor.Blue,
                            new OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(SampleActivity.this,
                                            "item" + which, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }).show();
        }else if (i == R.id.btn_exit){
            new AlertDialog(this).builder().setTitle("退出当前账号")
                    .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                    .setPositiveButton("确认退出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();

        }else if (i == R.id.btn_tongzhi){
            new AlertDialog(this).builder()
                    .setMsg("你现在无法接收到新消息提醒。请到系统-设置-通知中开启消息提醒")
                    .setNegativeButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }else if (i == R.id.btn_loading_android){
            LoadingDialog loadingDialog = new LoadingDialog().setOnTouchOutside(true);
            loadingDialog.show(getFragmentManager(), "loadingDialog");

        }else if (i == R.id.btn_loading_ios){
            IOSLoadingDialog iosLoadingDialog = new IOSLoadingDialog().setOnTouchOutside(true);
            iosLoadingDialog.show(getFragmentManager(), "iosLoadingDialog");

        }else if (i == R.id.btn_prompt_ios){
            hintDialog.setContent("确定要离开吗？");
            hintDialog.setOnConfirmClickListener(new HintDialog.HintConfirmCallback() {
                @Override
                public void onClick() {
                    hintDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击确定", Toast.LENGTH_SHORT).show();
                }
            });
            hintDialog.setOnCancelClickListener(new HintDialog.HintCancelCallback() {
                @Override
                public void onClick() {
                    hintDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
                }
            });
            hintDialog.show(getFragmentManager(), "hintDialog");

        }else if (i == R.id.btn_prompt_one_ios){
            singleHintDialog.setContent("请认真填写相关信息，谢谢合作~").setIsSingleButton(true);
            singleHintDialog.setOnSingleClickListener(new HintDialog.HintSingleCallback() {
                @Override
                public void onClick() {
                    singleHintDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击确认~", Toast.LENGTH_SHORT).show();
                }
            });
            singleHintDialog.show(SampleActivity.this.getFragmentManager(), "singleHintDialog");

        }else if (i == R.id.btn_phone){
            photoDialog.setOnCameraClickListener(new PhotoDialog.PhotoCameraCallback() {
                @Override
                public void onClick() {
                    photoDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击拍照", Toast.LENGTH_SHORT).show();
                }
            });
            photoDialog.setOnChoosePhotoClickListener(new PhotoDialog.ChoosePhotoCallback() {
                @Override
                public void onClick() {
                    photoDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击选取相册", Toast.LENGTH_SHORT).show();
                }
            });
            photoDialog.setOnCancleClickListener(new PhotoDialog.PhoneCancelCallback() {
                @Override
                public void onClick() {
                    photoDialog.dismiss();
                    Toast.makeText(SampleActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
                }
            });
            photoDialog.show(getFragmentManager(), "");
        }


    }
}
