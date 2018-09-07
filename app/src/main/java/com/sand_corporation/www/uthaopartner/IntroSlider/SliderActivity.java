package com.sand_corporation.www.uthaopartner.IntroSlider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.WelcomeActivity.Welcome;

public class SliderActivity extends AppCompatActivity {

    private LinearLayout pageStatusLayout;
    private ViewPager viewPagerIntro;
    private SliderAdapter adapter;
    //Now we will create below white dot using TextView
    private TextView[] mWhiteDots;
    private Button previousButton,nextButton;
    private int mCurrentPage;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String DEFAULT = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        preferences = getSharedPreferences("Driver_Basic_Info",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        editor = preferences.edit();

        pageStatusLayout = findViewById(R.id.pageStatusLayout);
        viewPagerIntro = findViewById(R.id.viewPagerIntro);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        adapter = new SliderAdapter(this);

        viewPagerIntro.setAdapter(adapter);
        addDotsIndicator(0);
        //Below we are attaching onPage change listener to our views
        viewPagerIntro.addOnPageChangeListener(viewListener);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerIntro.setCurrentItem(mCurrentPage - 1);
                //OnCLick of this button this viewpager will move back to
                //one page left.
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextButton.getText().toString().equals("NEXT")){
                    viewPagerIntro.setCurrentItem(mCurrentPage + 1);
                    //OnCLick of this button this viewpager will advance to
                    //one page right.
                } else if (nextButton.getText().toString().equals("FINISH")){
                    editor.putString("Sliding_Status","Done");
                    editor.commit();
                    Intent intent = new Intent(SliderActivity.this, Welcome.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    public void addDotsIndicator(int position){
        mWhiteDots = new TextView[3];
        //As number of page is 3
        pageStatusLayout.removeAllViews();
        //We are removing all views before adding new view
        for (int i = 0; i < mWhiteDots.length; i++){
            mWhiteDots[i] = new TextView(this);
            mWhiteDots[i].setText(Html.fromHtml("&#8226;"));
            //This above Html code make our 'mWhiteDots' as white
            mWhiteDots[i].setTextSize(35);
            mWhiteDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            //Adding new view
            pageStatusLayout.addView(mWhiteDots[i]);
        }

        //This below if statement will change our current page dot's color to white
        if (mWhiteDots.length > 0){
            mWhiteDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //From this 'position' value we will get in which page we are currently on
            //So we will pass this page number to 'addDotsIndicator()' method. So that
            //current page dot can be white
            addDotsIndicator(position);

            mCurrentPage = position;

            //Now we will show our button depending on the page
            if (position == 0){
                //This means we are in the first page
                previousButton.setEnabled(false);
                nextButton.setEnabled(true);
                previousButton.setVisibility(View.INVISIBLE);

                nextButton.setText("NEXT");
                previousButton.setText("");
            } else if (position == (mWhiteDots.length -1)){
                //This means we are in the last page
                previousButton.setEnabled(true);
                nextButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);

                nextButton.setText("FINISH");
                previousButton.setText("BACK");
            } else {
                //This means we are in the middle page
                previousButton.setEnabled(true);
                nextButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);

                nextButton.setText("NEXT");
                previousButton.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
