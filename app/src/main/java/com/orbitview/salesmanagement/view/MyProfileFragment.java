package com.orbitview.salesmanagement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.controller.AppController;
import com.orbitview.salesmanagement.model.Employee;

/**
 * Created by hamnaro on 05/11/18.
 */

public class MyProfileFragment extends Fragment {
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPhone;
    private EditText inputEmail;
    private EditText inputAddress;
    private Button btnEditProfile;
    private Button logoutButton;
    private AppController appController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.my_profile_fragment, container, false);


        AppController appController = (AppController)getActivity().getApplication();
        Employee employee = appController.employee;

        View myView = inflater.inflate(R.layout.my_profile_fragment, container, false);

        inputFirstName = (EditText)myView.findViewById(R.id.inputFirstName);
        inputLastName = (EditText)myView.findViewById(R.id.inputLastName);
        inputPhone = (EditText)myView.findViewById(R.id.inputPhone);
        inputEmail = (EditText)myView.findViewById(R.id.inputEmail);
        inputAddress = (EditText)myView.findViewById(R.id.inputAddress);

        //Make height same
        //inputLastName.setHeight(inputFirstName.getHeight());

        //Set value according to database
        inputFirstName.setText(employee.getFirstName());
        inputLastName.setText(employee.getLastName());
        inputPhone.setText(employee.getPhoneNumber());
        inputEmail.setText(employee.getEmail());
        inputAddress.setText(employee.getAddress());

        logoutButton = (Button)myView.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog();
                dialog.showDialog(getActivity(), "Are you sure want to logged out?");

                /*
                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                    getActivity().finishAffinity();
                    System.exit(0);
                } else if (Build.VERSION.SDK_INT >= 21) {
                    getActivity().finishAndRemoveTask();
                    System.exit(0);
                }*/
            }
        });

        btnEditProfile = (Button)myView.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEditProfile.getTag().equals("edit")){
                    inputFirstName.setEnabled(true);
                    inputLastName.setEnabled(true);
                    inputPhone.setEnabled(true);
                    inputEmail.setEnabled(true);
                    inputAddress.setEnabled(true);
                    btnEditProfile.setText("Save");
                    btnEditProfile.setTag("save");
                }else{
                    //Dummy save to DB
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully save profile", Toast.LENGTH_SHORT).show();
                    inputFirstName.setEnabled(false);
                    inputLastName.setEnabled(false);
                    inputPhone.setEnabled(false);
                    inputEmail.setEnabled(false);
                    inputAddress.setEnabled(false);
                    btnEditProfile.setText("Edit Profile");
                    btnEditProfile.setTag("edit");
                }
            }
        });

        return myView;
    }
}
