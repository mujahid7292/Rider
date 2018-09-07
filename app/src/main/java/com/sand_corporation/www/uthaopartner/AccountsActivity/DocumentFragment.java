package com.sand_corporation.www.uthaopartner.AccountsActivity;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sand_corporation.www.uthaopartner.AccountsActivity.CommunicationBetweenFragment.SelectedDocument;
import com.sand_corporation.www.uthaopartner.AccountsActivity.CommunicationBetweenFragment.SharedViewModel;
import com.sand_corporation.www.uthaopartner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {

    private static String TAG = "AccountActivity";
    private AccountActivityViewModel mViewModel;
    private SharedViewModel model;
    private ImageViewFragment imageViewFragment;
    private LinearLayout scannedDrivingLicenseLayout, scannedNidFrontLayout,
            scannedNidBackLayout, scannedRegistrationPaperLayout, scannedFitnessCertificate,
            scannedTaxTokenLayout;

    public DocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"DocumentFragment: onCreateView");
        return inflater.inflate(R.layout.fragment_document, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"DocumentFragment: onActivityCreated");

        //This below model for communicating between two fragments
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this)
                .get(AccountActivityViewModel.class);
        initializeAllTheViews();
    }

    private void initializeAllTheViews() {
        scannedDrivingLicenseLayout = getActivity().findViewById(R.id.scannedDrivingLicenseLayout);
        scannedNidFrontLayout = getActivity().findViewById(R.id.scannedNidFrontLayout);
        scannedNidBackLayout = getActivity().findViewById(R.id.scannedNidBackLayout);
        scannedRegistrationPaperLayout = getActivity().findViewById(R.id.scannedRegistrationPaperLayout);
        scannedFitnessCertificate = getActivity().findViewById(R.id.scannedFitnessCertificate);
        scannedTaxTokenLayout = getActivity().findViewById(R.id.scannedTaxTokenLayout);


        scannedDrivingLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Driving_License");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                ImageViewFragment imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();

            }
        });

        scannedNidFrontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Nid_Front");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        scannedNidBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Nid_Back");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                ImageViewFragment imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        scannedRegistrationPaperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Registration_Number");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                ImageViewFragment imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        scannedFitnessCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Fitness_Certificate");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                ImageViewFragment imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        scannedTaxTokenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewFragment = null;

                SelectedDocument selectedDocument = new SelectedDocument();
                selectedDocument.setSelectedImage("Tax_Token");
                model.select(selectedDocument);

                // Replace whatever is in the fragment_container view with this fragment,
                ImageViewFragment imageViewFragment = new ImageViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageViewFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"DocumentFragment: onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"DocumentFragment: onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"DocumentFragment: onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"DocumentFragment: onStop");
    }
}
