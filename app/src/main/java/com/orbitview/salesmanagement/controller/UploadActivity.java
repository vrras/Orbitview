package com.orbitview.salesmanagement.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.config.Config;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.orbitview.salesmanagement.helper.FilePath.getPath;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private AppController appController;
    private String customerName;
    private String customerPhone;
    private Uri pictureFile;
    private Uri videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        appController = (AppController)this.getApplication();
        RelativeLayout pictureLayout = findViewById(R.id.pictureLayout);
        VideoView videoView = findViewById(R.id.videoView);
        TextView locationTextView = findViewById(R.id.locationTextView);

        double latitude = appController.latitude;
        double longitude = appController.longitude;
        String tagLocation = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            this.customerName = bundle.getString("customerName");
            this.customerPhone = bundle.getString("customerPhone");
            tagLocation = bundle.getString("tagLocation");
            this.pictureFile = (Uri) bundle.get("pictureFile");
            this.videoFile = (Uri) bundle.get("videoFile");
        }

        if(appController.currentPlace != null)
            locationTextView.setText(appController.currentPlace);
        if(this.pictureFile != null) {
            String timeStamp = new SimpleDateFormat("MMM d, yyyy h:mm a",Locale.ENGLISH).format(new Date());
            String coordinate = latitude +"," + longitude;
            String[] strText = {timeStamp,coordinate, tagLocation};
            Bitmap bitmap = drawTextToBitmap(this,this.pictureFile.getEncodedPath(),strText);

            if(bitmap == null)
                Toast.makeText(getApplicationContext(), "Display Picture Failed. Please Allow Read Storage Permission", Toast.LENGTH_SHORT).show();
            else
                pictureLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }else if(this.videoFile != null){
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(this.videoFile);
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
        }
    }

    public Bitmap drawTextToBitmap(Context mContext,String pathName,  String[] mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(pathName);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            }catch (Exception e){
                Log.i(TAG,"Exception: "+e.getMessage());
                Toast.makeText(getApplicationContext(), "Please Re-Login and Allow Read Storage Permission", Toast.LENGTH_SHORT).show();
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setTextSize((int) (40 * scale));

            //Initialize text coordinate
            int x = 150;
            int y = 200;

            Log.i(TAG,"X coordinate: "+x);
            Log.i(TAG,"Y coordinate: "+y);

            canvas.drawText(mText[0], x, y, paint);
            canvas.drawText(mText[1], x, y+150, paint);
            canvas.drawText(mText[2], x, y+300, paint);
            FileOutputStream ostream = new FileOutputStream(pathName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);
            ostream.close();
            return bitmap;
        } catch (Exception e) {
            Log.i(TAG,"Error Message:"+e.getMessage());
            return null;
        }
    }

    public void btnYesAction(View view) {
        if (this.pictureFile != null)
            this.uploadMultipart(this.pictureFile);
        else if(this.videoFile != null)
           this.uploadMultipart(this.videoFile);
    }


    public void btnNoAction(View view) {
        finish();
    }

    public void uploadMultipart(Uri uri) {
        //getting the actual path of the image
        String path = getPath(this, uri);

        Log.i(TAG,"File Path: "+path);
        if (path == null) {
            Toast.makeText(this, "Empty File Path", Toast.LENGTH_LONG).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            //Uploading code
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                String fileName;
                if(this.videoFile != null)
                    fileName = this.customerName +"_"+timeStamp + ".mp4";
                else
                    fileName = this.customerName +"_"+timeStamp + ".jpg";
                //Creating a multi part request
                new MultipartUploadRequest(this.getApplicationContext(), Config.UPLOAD_FILE_URL[Config.BUILD_TYPE])
                        .addFileToUpload(path, "userfile",fileName) //Adding file
                        .addParameter("username",appController.userName)//Adding text parameter to the request
                        .addParameter("customer_name", this.customerName)
                        .addParameter("customer_phone", this.customerPhone)
                        .addParameter("latitude", String.valueOf(appController.latitude))
                        .addParameter("longitude", String.valueOf(appController.longitude))
                        .addParameter("longitude", String.valueOf(appController.longitude))
                        .addParameter("place", String.valueOf(appController.currentPlace))
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                UploadActivity.this.showMessage("Failed to upload file to server - "+exception.getMessage());
                                progressDialog.hide();
                                finish();
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                if(serverResponse.getHttpCode() == 200){
                                    Log.i(TAG,"Server Response:"+serverResponse.getBodyAsString());
                                    try {
                                        JSONObject obj = new JSONObject(serverResponse.getBodyAsString());
                                        //Toast.makeText(UploadActivity.this.getApplicationContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        UploadActivity.this.showMessage(obj.getString("message"));
                                        progressDialog.hide();
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        }).startUpload();

            } catch (Exception exc) {
                Log.i(TAG,"Exception: "+exc.getMessage());
            }
        }
    }

    public void showMessage(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
        toastContentView.addView(imageView, 0);
        toast.show();
    }
}