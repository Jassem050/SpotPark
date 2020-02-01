package amits.parking.com.parking;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GenerateQRCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GenerateQRCodeActivity";

    private TextView bookingId;
    private ImageView qrCodeImageView;
    private Button generateButton;
    private SessionManager sessionManager;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void init(){
        sessionManager = new SessionManager(this);
        bookingId = (TextView) findViewById(R.id.booking_id);
        bookingId.setText(sessionManager.getBid());
        qrCodeImageView = (ImageView) findViewById(R.id.qr_image_view);
        generateButton = (Button) findViewById(R.id.qr_generate_btn);
        generateButton.setOnClickListener(this);
    }

    private void qrCodeGenerate(){
        String text=sessionManager.getBid() + "," + sessionManager.getId() ;// Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setVisibility(View.VISIBLE);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qr_generate_btn:
                Log.d(TAG, "onClick: called");
                qrCodeImageView.setVisibility(View.VISIBLE);
                qrCodeGenerate();
        }
    }
}
