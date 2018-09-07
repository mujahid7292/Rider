package com.sand_corporation.www.uthaopartner.IntroSlider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.R;

/**
 * Created by HP on 12/4/2017.
 */

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.mContext = context;
    }

    //Now we will create an array of image for our 3 page intro slider.
    //As number of page is three our array size will be three.
    public int[] slide_images = {
//            R.drawable.ic_inside_logo,
//            R.drawable.ic_inside_logo,
//            R.drawable.ic_inside_logo
    };

    public String[] headers = {
            "Features",
            "Pricing",
            "Share"
    };

    public String[] description = {
            "- Real-time tracking of your journey \n\n" + "- Select your location and request a ride to be picked up.\n\n" + "- Hassle free cashless payment option\n\n",
            "- Get upfront pricing — see the trip cost before requesting a ride \n\n" +  "- Fare calculated by meter\n\n" + "- Apply promo codes to get discounts\n\n",
            "- If you care, please share\n\n" + "- Share promo codes with others to get discounts\n\n"
    };

    @Override
    public int getCount() {
        return headers.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout)object;
        //As our slide_layout.xml container is RelativeLayout
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //First we will inflate slide_layout.xml in the view
        LayoutInflater layoutInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container, false);

        //Now we will find out and link all the view from slide_layout.xml to java code
//        ImageView imageView = view.findViewById(R.id.imageView);
        TextView txtHeadLine = view.findViewById(R.id.txtHeadLine);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        //Now we will set the value in those views programmatically
//        imageView.setImageResource(slide_images[position]);
        txtHeadLine.setText(headers[position]);
        txtDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
        //As our slide_layout.xml container is RelativeLayout
    }
}
