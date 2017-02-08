package com.dev.rsademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import rsa.Base64Utils;
import rsa.RSAUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button encryptBtn;
    private Button decryptBtn;
    private TextView showTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_text);
        encryptBtn = (Button) findViewById(R.id.btn_encrypt);
        decryptBtn = (Button) findViewById(R.id.btn_decode);
        showTv = (TextView) findViewById(R.id.tv_show);
        encryptBtn.setOnClickListener(this);
        decryptBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_encrypt:
                String source = editText.getText().toString().trim();
                if(source!=null){
                    try {
                        InputStream inputPublic = getResources().getAssets().open("rsa_public_key.pem");
                        PublicKey publicKey = RSAUtils.loadPublicKey(inputPublic);
                        //加密
                        byte[] encryptByte = RSAUtils.encryptData(source.getBytes(),publicKey);
                        String afterEncrypt = Base64Utils.encode(encryptByte);
                        showTv.setText(afterEncrypt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_decode:
                String encryptContent = showTv.getText().toString().trim();
                if(encryptContent!=null){
                    try {
                        InputStream inputPrivate = getResources().getAssets().open("pkcs8_rsa_private_key.pem");
                        PrivateKey privateKey = RSAUtils.loadPrivateKey(inputPrivate);
                        byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(encryptContent),privateKey);
                        String decryptStr = new String(decryptByte);
                        showTv.setText(decryptStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
