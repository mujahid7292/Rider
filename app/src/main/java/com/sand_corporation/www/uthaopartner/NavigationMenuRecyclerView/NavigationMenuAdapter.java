package com.sand_corporation.www.uthaopartner.NavigationMenuRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.AccountsActivity.AccountActivity;
import com.sand_corporation.www.uthaopartner.HelpBox.HelpBoxActivity;
import com.sand_corporation.www.uthaopartner.HomeActivity.Home;
import com.sand_corporation.www.uthaopartner.NotificationBox.NotificationBoxActivity;
import com.sand_corporation.www.uthaopartner.PaymentGateWay.PaymentBoxActivity;
import com.sand_corporation.www.uthaopartner.PromotionBox.PromotionBoxActivity;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RideHistoryRecyclerView.RideHistoryRecyclerViewActivity;
import com.sand_corporation.www.uthaopartner.SettingsActivity.SettingsActivity;

import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by HP on 6/16/2017.
 */

public class NavigationMenuAdapter extends RecyclerView.Adapter <NavigationMenuAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<NavigationMenuItem> data = Collections.emptyList();
    //We initialize List with Collections.emptyList(), so that we don not have null pointer exception in our code.

    public NavigationMenuAdapter(Context context, List<NavigationMenuItem> data ){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_navigation_menu_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        NavigationMenuItem current = data.get(position);
        holder.title.setText(current.navigation_title);
        holder.icon.setImageResource(current.navigation_icon_id);
        holder.singleNavigationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menuText = holder.title.getText().toString();
                Log.i("Check","Menu Selected position: " + holder.getAdapterPosition() + "\n" +
                "Menu Selected Text: " + menuText);

                if (0 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), RideHistoryRecyclerViewActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (1 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), PromotionBoxActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (2 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), NotificationBoxActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (3 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), PaymentBoxActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (4 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), AccountActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (5 == holder.getAdapterPosition()){
                    Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (6 == holder.getAdapterPosition()){
                Intent intent = new Intent(view.getContext(), HelpBoxActivity.class);
                view.getContext().startActivity(intent);//Without context we will not be able to startActivity.

                } else if (7 == holder.getAdapterPosition()){
                    if (menuText.equals("বাংলা")){
                        Paper.book().write("language","bn");
                        Intent intent = new Intent(view.getContext(), Home.class);
                        view.getContext().startActivity(intent);//Without context we will not be able to startActivity.
                        ((Activity)context).finish();
                    } else if (menuText.equals("English")){
                        Paper.book().write("language","en");
                        Intent intent = new Intent(view.getContext(), Home.class);
                        view.getContext().startActivity(intent);//Without context we will not be able to startActivity.
                        ((Activity)context).finish();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView title;
        private LinearLayout singleNavigationMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.navigationMenuIcon);
            title = (TextView) itemView.findViewById(R.id.navigationMenuTitle);
            singleNavigationMenu = (LinearLayout) itemView.findViewById(R.id.singleNavigationMenu);
        }

    }
}
