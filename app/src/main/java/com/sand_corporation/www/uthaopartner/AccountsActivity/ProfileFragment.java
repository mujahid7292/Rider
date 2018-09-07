package com.sand_corporation.www.uthaopartner.AccountsActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePic;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private String TAG = "AccountActivity";
    private AccountActivityViewModel mViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"ProfileFragment: onCreateView");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"ProfileFragment: onActivityCreated");
        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this)
                .get(AccountActivityViewModel.class);
        putObserverInRideHistoryFromRoom();
    }



    private void putObserverInRideHistoryFromRoom() {

        mViewModel.mBasicInfo.observe(this, new Observer<BasicInfo>() {
            @Override
            public void onChanged(@Nullable BasicInfo basicInfo) {
                if (basicInfo != null){
                    String Full_name = basicInfo.getFull_name();
                    String Email = basicInfo.getEmail();
                    String Mobile = basicInfo.getMobile();
                    String Home_address = basicInfo.getHome_address();
                    String Nid = basicInfo.getNid();
                    String Driving_license = basicInfo.getDriving_license();
                    String vehicle_reg_number = basicInfo.getVehicle_registration_number();
                    String vehicle_reg_authority = basicInfo.getVehicle_registration_authority();
                    String Registration_Number = vehicle_reg_authority + " " + vehicle_reg_number;
                    String Vehicle_fitness_number = basicInfo.getVehicle_fitness_number();
                    String Vehicle_tax_token_number = basicInfo.getVehicle_tax_token_number();
                    String ServiceType = basicInfo.getServiceType();
                    String ServiceArea = basicInfo.getRegistration_city();
                    String manufacturer = basicInfo.getVehicle_manufacturer();
                    String model = basicInfo.getVehicle_model();
                    String production_year = basicInfo.getVehicle_production_year();
                    String Vehicle_Model = manufacturer + " " + model + " (" + production_year + ")";
                    Log.i(TAG,"driverName: " + Full_name);

                    TextView txtFullName = getActivity().findViewById(R.id.txtFullName);
                    TextView txtServiceType = getActivity().findViewById(R.id.txtServiceType);
                    TextView txtServiceArea = getActivity().findViewById(R.id.txtServiceArea);
                    TextView txtVehicleRegistration = getActivity().findViewById(R.id.txtVehicleRegistration);
                    TextView txtVehicleModel = getActivity().findViewById(R.id.txtVehicleModel);
                    TextView txtEmail = getActivity().findViewById(R.id.txtEmail);
                    TextView txtMobile = getActivity().findViewById(R.id.txtMobile);
                    TextView txtHomeAddress = getActivity().findViewById(R.id.txtHomeAddress);
                    TextView txtNID = getActivity().findViewById(R.id.txtNID);
                    TextView txtDriverLicense = getActivity().findViewById(R.id.txtDriverLicense);
                    TextView txtFitnessCertificate = getActivity().findViewById(R.id.txtFitnessCertificate);
                    TextView txtTaxToken = getActivity().findViewById(R.id.txtTaxToken);


                    txtFullName.setText(Full_name);
                    txtServiceType.setText(ServiceType);
                    txtServiceArea.setText(ServiceArea);
                    txtVehicleRegistration.setText(Registration_Number);
                    txtVehicleModel.setText(Vehicle_Model);
                    txtEmail.setText(Email);
                    txtMobile.setText(Mobile);
                    txtHomeAddress.setText(Home_address);
                    txtNID.setText(Nid);
                    txtDriverLicense.setText(Driving_license);
                    txtFitnessCertificate.setText(Vehicle_fitness_number);
                    txtTaxToken.setText(Vehicle_tax_token_number);

                }
            }
        });

        mViewModel.mProfilePic.observe(this, new Observer<ProfilePic>() {
            @Override
            public void onChanged(@Nullable ProfilePic profilePic) {
                if (profilePic != null){
                    CircleImageView driverProfilePic = getActivity().findViewById(R.id.driverProfilePic);
                    driverProfilePic.setImageBitmap(getImage(profilePic.getProfile_Pic()));
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"ProfileFragment: onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"ProfileFragment: onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"ProfileFragment: onDestroy");
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        FirebaseCrash.log("Home:getImage.called");
        Crashlytics.log("Home:getImage.called");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
