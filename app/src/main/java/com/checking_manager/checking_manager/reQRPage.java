package com.checking_manager.checking_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class reQRPage extends AppCompatActivity {

    private String group_name;
    private String pos;
    private String stuff_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_q_r_page);


        group_name = getIntent().getExtras().getString("group_name");
        pos = getIntent().getExtras().getString("pos");
        stuff_name = getIntent().getExtras().getString("stuff_name");

        String sen = group_name + "_" + pos + "_" + stuff_name;

        ImageView IV = (ImageView)findViewById(R.id.qrCode);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(sen, BarcodeFormat.QR_CODE, 700, 700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            IV.setImageBitmap(bitmap);
        } catch (Exception e) {

        }
    }
}