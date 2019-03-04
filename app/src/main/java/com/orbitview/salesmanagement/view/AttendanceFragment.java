package com.orbitview.salesmanagement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.controller.AppController;
import com.orbitview.salesmanagement.controller.MainActivity;
import com.orbitview.salesmanagement.model.Employee;

/**
 * Created by hamnaro on 05/11/18.
 */

public class AttendanceFragment extends Fragment{
    private static final String LOG_TAG = "AttendanceFragment";
    private EditText inputName;
    private EditText inputPhone;
    private static EditText inputPlace;
    private TextView tvFileName;
    private Button btnTakePicture;
    private Button btnTakeVideo;
    private AppController appController;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 100;
    private static RelativeLayout mapLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.attendance_fragment, container, false);

        inputName = (EditText)myView.findViewById(R.id.inputName);
        inputPhone = (EditText)myView.findViewById(R.id.inputPhone);
        inputPlace = (EditText)myView.findViewById(R.id.inputPlace);

        btnTakePicture = (Button)myView.findViewById(R.id.btnTakePicture);
        btnTakeVideo= (Button)myView.findViewById(R.id.btnTakeVideo);

        appController = (AppController)getActivity().getApplication();
        Employee employee = appController.employee;

        /*
        inputName.setText(employee.getFirstName()+" "+employee.getLastName());
        inputPhone.setText(employee.getPhoneNumber());
        */

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);*/
                ((MainActivity)getActivity()).takePicture(v);

            }
         });

        btnTakeVideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).takeVideo(v);
            }
        });
        return myView;
    }

    public static void setPlace(String place){
        Log.i(LOG_TAG,"Method setPlace is called");
        if(inputPlace != null)
            inputPlace.setText(place);
    }


}
