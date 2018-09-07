package com.sand_corporation.www.uthaopartner.Animation;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.R;


/**
 * Created by HP on 1/1/2018.
 */

public class TabAnimation implements TabHost.OnTabChangeListener  {

    private Context context;
    private static final int ANIMATION_TIME = 240;
    private TabHost tabHost;
    private View previousView;
    private View currentView;
    private int currentTab;

    public TabAnimation(Context context, TabHost tabHost) {
        this.context = context;
        this.tabHost = tabHost;
        this.previousView = tabHost.getCurrentView();
    }

    @Override
    public void onTabChanged(String tabNumber) {

        //Change Tab title color
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000")); // unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
//        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#0000FF")); // selected
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#ffffff"));

        //Change tab underline indicator color
        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tvv = (TextView)v.findViewById(android.R.id.title);
            if(tvv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
        }

        //Tab Animation part
        currentView = tabHost.getCurrentView();
        if (tabHost.getCurrentTab() > currentTab){
            previousView.setAnimation(outTOLeftAnimation());
            currentView.setAnimation(inFromRightAnimation());
        } else {
            previousView.setAnimation(outTORightAnimation());
            currentView.setAnimation(inFromLeftAnimation());
        }

        previousView = currentView;
        currentTab = tabHost.getCurrentTab();
    }

    private Animation inFromLeftAnimation() {

        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                -1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );

        return setProperties(inFromLeft);
    }

    private Animation setProperties(Animation animation) {
        animation.setDuration(ANIMATION_TIME);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }

    private Animation outTORightAnimation() {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );

        return setProperties(outToRight);
    }

    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );

        return setProperties(inFromRight);
    }

    private Animation outTOLeftAnimation() {
        Animation outTOLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                -1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );

        return setProperties(outTOLeft);
    }
}
