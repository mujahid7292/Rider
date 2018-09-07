package com.sand_corporation.www.uthaopartner.PaymentGateWay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sand_corporation.www.uthaopartner.R;

public class PaymentBoxActivity extends AppCompatActivity {

    private ImageView ic_back_sign;
    private LinearLayout uthaoWalletPayment, addNewPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_box);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        uthaoWalletPayment = findViewById(R.id.uthaoWalletPayment);
        addNewPaymentMethod = findViewById(R.id.addNewPaymentMethod);

        uthaoWalletPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentBoxActivity.this, UthaoWalletActivity.class);
                startActivity(intent);
            }
        });

        addNewPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentBoxActivity.this, AddPaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}
