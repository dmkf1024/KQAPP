package com.hznu.kaoqin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hznu.kaoqin.R;
import com.hznu.kaoqin.net.Net;
import com.hznu.kaoqin.pojo.Constant;
import com.hznu.kaoqin.proxy.SPProxy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IPSettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.et_ip1)
    EditText etIp1;
    @BindView(R.id.et_ip2)
    EditText etIp2;
    @BindView(R.id.et_ip3)
    EditText etIp3;
    @BindView(R.id.et_ip4)
    EditText etIp4;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.et_port)
    EditText etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsettings);
        ButterKnife.bind(this);

        // 初始化标题栏
        initToolbar();
        // 初始化网络信息
        initNetInfo();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化IP地址
     */
    private void initNetInfo() {
        // 获取网络信息
        String ip = SPProxy.getIp(IPSettingsActivity.this);
        String port = SPProxy.getPort(IPSettingsActivity.this);

        String subIp = ip.substring(0, ip.indexOf("."));
        etIp1.setHint(subIp);

        ip = ip.substring(ip.indexOf(".") + 1);
        subIp = ip.substring(0, ip.indexOf("."));
        etIp2.setHint(subIp);

        ip = ip.substring(ip.indexOf(".") + 1);
        subIp = ip.substring(0, ip.indexOf("."));
        etIp3.setHint(subIp);

        ip = ip.substring(ip.indexOf(".") + 1);
        etIp4.setHint(ip);

        etPort.setHint(port);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                // 是否合法
                boolean isIPValid = true;
                boolean isPortValid = true;
                // 获取ip
                String ip1 = etIp1.getText().toString().trim();
                String ip2 = etIp2.getText().toString().trim();
                String ip3 = etIp3.getText().toString().trim();
                String ip4 = etIp4.getText().toString().trim();
                // 规范格式
                ip1 = TextUtils.isEmpty(ip1) ? etIp1.getHint().toString() : ip1;
                ip2 = TextUtils.isEmpty(ip2) ? etIp2.getHint().toString() : ip2;
                ip3 = TextUtils.isEmpty(ip3) ? etIp3.getHint().toString() : ip3;
                ip4 = TextUtils.isEmpty(ip4) ? etIp4.getHint().toString() : ip4;
                // 规范大小
                if (Integer.parseInt(ip1) > 255) {
                    isIPValid = false;
                } else if (Integer.parseInt(ip2) > 255) {
                    isIPValid = false;
                } else if (Integer.parseInt(ip3) > 255) {
                    isIPValid = false;
                } else if (Integer.parseInt(ip4) > 255) {
                    isIPValid = false;
                }

                // 获取端口号
                String port = etPort.getText().toString().trim();
                port = TextUtils.isEmpty(port) ? etPort.getHint().toString() : port;
                int portNum = Integer.parseInt(port);
                // 判断端口号是否在合理范围内
                if (portNum < 1 || portNum > 65535) {
                    isPortValid = false;
                }


                if (isIPValid && isPortValid) {
                    String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
                    Log.i(Constant.Tag.NET, "saveIp is " + ip);
                    // 保存IP和端口号
                    SPProxy.saveIp(IPSettingsActivity.this, ip);
                    SPProxy.savePort(IPSettingsActivity.this, port);
                    finish();
                } else if (!isIPValid) {
                    Toast.makeText(IPSettingsActivity.this, INVALID_IP, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IPSettingsActivity.this, INVALID_PORT, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

