package com.sand_corporation.www.uthaopartner.AccountsActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.AccountsActivity.EarningFragment.EarningFragment;
import com.sand_corporation.www.uthaopartner.R;

public class AccountActivity extends AppCompatActivity {

    //Activity Wide Variable
    private String TAG = "AccountActivity";
    private TextView headerText;
    private LinearLayout profile, document, earning, rating;
    private ImageView ic_back_sign;
    private FragmentTransaction fragmentTransaction;
    private ProfileFragment profileFragment;
    private DocumentFragment documentFragment;
    private EarningFragment earningFragment;
    private RatingFragment ratingFragment;

    //Profile Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        profileFragment = new ProfileFragment();
        fragmentTransaction.add(R.id.fragment_container, profileFragment);
        fragmentTransaction.commit();


        initializeActivityWideViews();

    }

    private void initializeActivityWideViews() {
        headerText = findViewById(R.id.headerText);

        profile = findViewById(R.id.profile);
        document = findViewById(R.id.document);
        earning = findViewById(R.id.earning);
        rating = findViewById(R.id.rating);


        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                documentFragment = null;
                earningFragment = null;
                ratingFragment = null;

                headerText.setText(R.string.Profile);
                profile.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.ic_bg_text_box));

                document.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                earning.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                rating.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));

                // Replace whatever is in the fragment_container view with this fragment,
                if (profileFragment == null){
                    profileFragment = new ProfileFragment();
                }
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment);

                // Commit the transaction
                fragmentTransaction.commit();

            }
        });

        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileFragment = null;
                earningFragment = null;
                ratingFragment = null;

                headerText.setText(R.string.documents);
                document.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.ic_bg_text_box));

                profile.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                earning.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                rating.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));

                // Replace whatever is in the fragment_container view with this fragment,
                documentFragment = new DocumentFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, documentFragment);

                // Commit the transaction
                fragmentTransaction.commit();

            }
        });

        earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileFragment = null;
                documentFragment = null;
                ratingFragment = null;

                headerText.setText(R.string.Earnings);
                earning.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.ic_bg_text_box));

                document.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                profile.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                rating.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));

                // Replace whatever is in the fragment_container view with this fragment,
                earningFragment = new EarningFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, earningFragment);

                // Commit the transaction
                fragmentTransaction.commit();

            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileFragment = null;
                documentFragment = null;
                earningFragment = null;

                headerText.setText(R.string.Ratings);
                rating.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.ic_bg_text_box));

                profile.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                document.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));
                earning.setBackground(ContextCompat.getDrawable(AccountActivity.this,
                        R.drawable.transparent_bg));

                // Replace whatever is in the fragment_container view with this fragment,
                ratingFragment = new RatingFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ratingFragment);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
    }


}
