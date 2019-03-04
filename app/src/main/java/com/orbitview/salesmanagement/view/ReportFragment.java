package com.orbitview.salesmanagement.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.config.Config;
import com.orbitview.salesmanagement.model.DataItem;
import com.orbitview.salesmanagement.model.ReportResponse;
import com.orbitview.salesmanagement.network.ApiInterface;
import com.orbitview.salesmanagement.network.InitRetrofitReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment implements View.OnClickListener {

    private EditText startDate;
    private EditText endDate;
    private Button btnSearch;
    private Button btnExport;
//    private Button btnImage;
    private TextView loc;
    private TextView filePhoto;
    private TableLayout tablelayoutid;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    public String strStartDate, strEndDate;
    List<DataItem> data_report;

    private View myView;
    private Boolean flagExport = false;

    Dialog settingsDialog;
    private ProgressDialog pdPreview;
    private ProgressDialog exDialog;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private Calendar calendar;
    private SimpleDateFormat currentFormat;
    private String currentDate;

    int count = 0;
    int rowImage;
    int id = 1;
    String token_key = "eyJ0eXWcl55i9h7Q";

    private static final String TAG = "###ReportFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_report, container, false);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();
        settingsDialog = new Dialog(getContext());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setCancelable(false);
        settingsDialog.setContentView(R.layout.preview_layout);

        setDateTimeField();

        return myView;
    }

    private void setDateTimeField() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void findViewsById() {
        mContext = this.getContext();

        startDate = (EditText) myView.findViewById(R.id.startDate);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();

        endDate = (EditText) myView.findViewById(R.id.endDate);
        endDate.setInputType(InputType.TYPE_NULL);

        btnSearch = (Button) myView.findViewById(R.id.btnSearchReport);
        btnSearch.setOnClickListener(this);

        btnExport = (Button) myView.findViewById(R.id.btnExport);
        btnExport.setOnClickListener(this);

//        btnImage = (Button) myView.findViewById(R.id.btnImage);
//        btnImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == startDate) {
            fromDatePickerDialog.show();
        } else if(view == endDate) {
            toDatePickerDialog.show();
        } else if(view == btnSearch) {
            strStartDate = startDate.getText().toString();
            strEndDate = endDate.getText().toString();

            final SimpleDateFormat defaultFormat = new SimpleDateFormat("dd-MM-yyyy");
            final SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");
            String outputDateStrStart = "";
            String outputDateStrEnd = "";

            outputDateStrStart = parseDate(strStartDate, defaultFormat, ymdFormat);
            outputDateStrEnd = parseDate(strEndDate, defaultFormat, ymdFormat);
//            Log.i("output_string", outputDateStr);

            if(!strStartDate.equals("") && !strEndDate.equals("")) {
                if(Integer.valueOf(outputDateStrStart) <= Integer.valueOf(outputDateStrEnd)) {
                    getDataReport(strStartDate, strEndDate);
                } else {
                    showMessage("Start date must be smaller than end date");
                }
            } else {
                showMessage("Choose date first");
            }
        } else if(view == btnExport) {
            if (flagExport) {
                exportToExcel(strStartDate, strEndDate);
            } else {
                showMessage("There is no data to export");
            }
        }
//        else if(view == btnImage) {
//            getRetrofitImage("test_20181122_123000.jpg");
//        }
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    private void getRetrofitImage(final String nameFile, final String flag, final String sDate, final String eDate) {
        ApiInterface apiInterface = InitRetrofitReport.getInstance();

        Call<ResponseBody> imageCall = apiInterface.getImageDetails(token_key,nameFile);
        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, response.message());
                    if(!response.isSuccessful()){
                        Log.e(TAG, "Something's gone wrong");
                        // TODO: show error message
                        return;
                    }

                    if (flag.equals("preview")) {
                        PreviewFileAsyncTask previewFileAsyncTask = new PreviewFileAsyncTask(nameFile);
                        previewFileAsyncTask.execute(response.body().byteStream());
                    } else {
                        DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask(nameFile, sDate, eDate);
                        downloadFileAsyncTask.execute(response.body().byteStream());
//                        DownloadAllImage(response.body(),nameFile);
                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

//    private void DownloadAllImage(ResponseBody body, String filename) {
//        String appDirectoryName = "OrbitView/Images";
//        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), appDirectoryName);
//
//        File file = new File(imageRoot, filename);
//
//        //create directory if not exist
//        if(!imageRoot.isDirectory()){
//            imageRoot.mkdirs();
//        }
//
//        InputStream inputStream = body.byteStream();
//        OutputStream output = null;
//        try {
//            //create directory if not exist
//            if(!imageRoot.isDirectory()){
//                imageRoot.mkdirs();
//            }
//
//            output = new FileOutputStream(file);
//
//            byte[] buffer = new byte[1024]; // or other buffer size
//            int read;
//
//            Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
//            while ((read = inputStream.read(buffer)) != -1) {
//                output.write(buffer, 0, read);
//                Log.v(TAG, "Writing to buffer to output stream.");
//            }
//            Log.d(TAG, "Flushing output stream.");
//            output.flush();
//            Log.d(TAG, "Output flushed.");
//        } catch (IOException e) {
//            Log.e(TAG, "IO Exception: " + e.getMessage());
//            e.printStackTrace();
//            return;
//        } finally {
//            try {
//                if (output != null) {
//                    output.close();
//                    Log.d(TAG, "Output stream closed sucessfully.");
//                }
//                else{
//                    Log.d(TAG, "Output stream is null");
//                }
//            } catch (IOException e){
//                Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
//                e.printStackTrace();
//                return;
//            }
//
//            boolean ext = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+(\\.(?i)(jpe?g|png))$)", filename);
//            boolean extError = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+$)", filename);
//            Log.d(TAG, "regex: "+ext);
//
//            // Check file 0 Byte
//            double bytes = file.length();
//            int mem = Integer.valueOf((int) bytes);
//            if (mem == 0) {
//                file.delete();
//            }
//        }
//    }

    private class DownloadFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {

        String filename, sDate, eDate;

        public DownloadFileAsyncTask(String filename, String sDate, String eDate){
            this.filename = filename;
            this.sDate = sDate;
            this.eDate = eDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getContext());
            mBuilder.setContentTitle("Exporting Report")
                    .setContentText("Export in progress")
                    .setSmallIcon(R.drawable.ic_export_white_24dp);
            // Start a the operation in a background thread
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            // Do the "lengthy" operation 20 times
                            count = 0;
                            while (count < rowImage) {
                                // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                                mBuilder.setProgress(rowImage, count, false);
                                // Displays the progress bar for the first time.
                                mNotifyManager.notify(id, mBuilder.build());
                                // Sleeps the thread, simulating an operation
                                try {
                                    // Sleep for 1 second
                                    Thread.sleep(1*1000);
                                } catch (InterruptedException e) {
                                    Log.d("TAG", "sleep failure");
                                }
                            }
                            // When the loop is finished, updates the notification
                            mBuilder.setContentText("Export completed")
                                    // Removes the progress bar
                                    .setProgress(0,0,false);
                            mNotifyManager.notify(id, mBuilder.build());
                        }
                    }
                    // Starts the thread by calling the run() method in its Runnable
            ).start();
        }

        @Override
        protected Boolean doInBackground(InputStream... params) {
            String endPath = "/Report_"+sDate+"_"+eDate;
            calendar = Calendar.getInstance();
            currentFormat = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = currentFormat.format(calendar.getTime());

            String appDirectoryName = "OrbitView/"+currentDate+endPath;
            File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), appDirectoryName);

            InputStream inputStream = params[0];
            File file = new File(imageRoot, filename);
            OutputStream output = null;
            try {
                //create directory if not exist
                if(!imageRoot.isDirectory()){
                    imageRoot.mkdirs();

                    // fix
                    imageRoot.setExecutable(true);
                    imageRoot.setReadable(true);
                    imageRoot.setWritable(true);

                    // initiate media scan and put the new things into the path array to
                    // make the scanner aware of the location and the files you want to see
                    MediaScannerConnection.scanFile(getContext(), new String[] {imageRoot.toString()}, null, null);

                    Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri fileContentUri = Uri.fromFile(imageRoot);
                    mediaScannerIntent.setData(fileContentUri);
                    mContext.sendBroadcast(mediaScannerIntent);
                }

                output = new FileOutputStream(file);

                byte[] buffer = new byte[1024]; // or other buffer size
                int read;

                Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    Log.v(TAG, "Writing to buffer to output stream.");
                }
                Log.d(TAG, "Flushing output stream.");
                output.flush();
                Log.d(TAG, "Output flushed.");
            } catch (IOException e) {
                Log.e(TAG, "IO Exception: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                        Log.d(TAG, "Output stream closed sucessfully.");
                    }
                    else{
                        Log.d(TAG, "Output stream is null");
                    }
                } catch (IOException e){
                    Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }

                boolean ext = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+(\\.(?i)(jpe?g|png))$)", filename);
                boolean extError = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+$)", filename);
                Log.d(TAG, "regex: "+ext);

                // Check file 0 Byte
                double bytes = file.length();
                int mem = Integer.valueOf((int) bytes);
                if (mem == 0 || mem < 10000) {
                    file.delete();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d(TAG, "Download success: " + result);
            count=count+1;
            if (count == rowImage) {
                exDialog.dismiss();
                showMessage("Report has been export");
            }
        }
    }

    private class PreviewFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {

        String appDirectoryName = "OrbitView/Images";
        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), appDirectoryName);
        String filename;

        public PreviewFileAsyncTask(String filename){
            this.filename = filename;
        }

        @Override
        protected Boolean doInBackground(InputStream... params) {
            InputStream inputStream = params[0];
            File file = new File(imageRoot, filename);
            OutputStream output = null;
            try {
                //create directory if not exist
                if(!imageRoot.isDirectory()){
                    imageRoot.mkdirs();

                    // fix
                    imageRoot.setExecutable(true);
                    imageRoot.setReadable(true);
                    imageRoot.setWritable(true);

                    // initiate media scan and put the new things into the path array to
                    // make the scanner aware of the location and the files you want to see
                    MediaScannerConnection.scanFile(getContext(), new String[] {imageRoot.toString()}, null, null);

                    Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri fileContentUri = Uri.fromFile(imageRoot);
                    mediaScannerIntent.setData(fileContentUri);
                    mContext.sendBroadcast(mediaScannerIntent);
                }

                output = new FileOutputStream(file);

                byte[] buffer = new byte[1024]; // or other buffer size
                int read;

                Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    Log.v(TAG, "Writing to buffer to output stream.");
                }
                Log.d(TAG, "Flushing output stream.");
                output.flush();
                Log.d(TAG, "Output flushed.");
            } catch (IOException e) {
                Log.e(TAG, "IO Exception: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                        Log.d(TAG, "Output stream closed sucessfully.");
                    }
                    else{
                        Log.d(TAG, "Output stream is null");
                    }
                } catch (IOException e){
                    Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Log.d(TAG, "Download success: " + result);
            // TODO: show a snackbar or a toast
            String appDirectoryName = "OrbitView/Images";
            File directory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), appDirectoryName);

            //file path
            File file = new File(directory, filename);

            VideoView vidView = settingsDialog.findViewById(R.id.vidPreview);
            ImageView imgView = settingsDialog.findViewById(R.id.imgPreview);
            ImageView btnClose = (ImageView) settingsDialog.findViewById(R.id.closeDialogImg);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsDialog.dismiss();
                }
            });

            boolean ext = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+(\\.(?i)(jpe?g|png))$)", filename);
            boolean extError = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+$)", filename);
            Log.d(TAG, "regex: "+ext);

            // Check file 0 Byte
            double bytes = file.length();
            int mem = Integer.valueOf((int) bytes);
            if (mem == 0 || mem < 10000) {
                file.delete();
            }

            if(ext) {
                pdPreview.dismiss();
                if (file.exists()){
                    int width, height;
                    Bitmap bMap = BitmapFactory.decodeFile(String.valueOf(file));
                    width = bMap.getWidth();
                    height = bMap.getHeight();
                    Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
                    settingsDialog.show();
                    vidView.setVisibility(View.GONE);
                    imgView.setVisibility(View.VISIBLE);
                    imgView.setImageBitmap(bMap2);
                } else {
                    showMessage("File doesn't exist!");
                }
            } else if(extError) {
                pdPreview.dismiss();
                showMessage("File cannot be opened!");
            } else {
                pdPreview.dismiss();
                if (file.exists()) {
                    settingsDialog.show();
                    vidView.setVisibility(View.VISIBLE);
                    imgView.setVisibility(View.GONE);
                    vidView.setVideoPath(String.valueOf(file));
                    vidView.start();
                } else {
                    showMessage("File doesn't exist!");
                }
            }
        }
    }

    private void exportToExcel(String sDate, String eDate) {
        exDialog = new ProgressDialog(ReportFragment.this.getContext());
        exDialog.setIndeterminate(true);
        exDialog.setCancelable(false);
        exDialog.setMessage("Exporting report...");
        exDialog.show();

        // File Path
        calendar = Calendar.getInstance();
        currentFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = currentFormat.format(calendar.getTime());
        String endPath = "/Report_"+sDate+"_"+eDate;
        String photoDirectoryName = "OrbitView/"+currentDate+endPath;
        File directoryPhoto = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), photoDirectoryName);

        //Excel Path
        final String fileName = "Report_"+sDate+"_"+eDate+".xls";
        String endPathExcel = "/Report_"+sDate+"_"+eDate;

        //Saving file in external storage
        String appDirectoryName = "OrbitView/"+currentDate+endPathExcel;
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), appDirectoryName);

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();

            // fix
            directory.setExecutable(true);
            directory.setReadable(true);
            directory.setWritable(true);

            // initiate media scan and put the new things into the path array to
            // make the scanner aware of the location and the files you want to see
            MediaScannerConnection.scanFile(getContext(), new String[] {directory.toString()}, null, null);

            Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri fileContentUri = Uri.fromFile(directory);
            mediaScannerIntent.setData(fileContentUri);
            mContext.sendBroadcast(mediaScannerIntent);
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(sDate+"_"+eDate, 0);

            try {
                sheet.addCell(new Label(0, 0, "No")); // column and row
                sheet.addCell(new Label(1, 0, "Employee Name"));
                sheet.addCell(new Label(2, 0, "Phone Number"));
                sheet.addCell(new Label(3, 0, "Email"));
                sheet.addCell(new Label(4, 0, "Employee Id"));
                sheet.addCell(new Label(5, 0, "Supervisor"));
                sheet.addCell(new Label(6, 0, "Customer Name"));
                sheet.addCell(new Label(7, 0, "Customer Phone"));
                sheet.addCell(new Label(8, 0, "Location"));
                sheet.addCell(new Label(9, 0, "Date"));
                sheet.addCell(new Label(10, 0, "Time"));
                sheet.addCell(new Label(11, 0, "Photo/Video"));

                for(int i = 0; i < data_report.size(); i++){
                    String no = String.valueOf(i+1);
                    String employeeName = data_report.get(i).getEmployeeName();
                    String phoneNumber = data_report.get(i).getPhoneNumber();
                    String email = data_report.get(i).getEmail();
                    String employeeId = data_report.get(i).getEmployeeId();
                    String supervisor = "";
                    String customerName = data_report.get(i).getCustomerName();
                    String customerPhone = data_report.get(i).getCustomerPhone();
                    String location = data_report.get(i).getLocation();
                    String date = data_report.get(i).getDate();
                    String time = data_report.get(i).getTime();
                    String photo = data_report.get(i).getFileName();

                    sheet.addCell(new Label(0, i+1, no));
                    sheet.addCell(new Label(1, i+1, employeeName));
                    sheet.addCell(new Label(2, i+1, phoneNumber));
                    sheet.addCell(new Label(3, i+1, email));
                    sheet.addCell(new Label(4, i+1, employeeId));
                    sheet.addCell(new Label(5, i+1, supervisor));
                    sheet.addCell(new Label(6, i+1, customerName));
                    sheet.addCell(new Label(7, i+1, customerPhone));
                    sheet.addCell(new Label(8, i+1, location));
                    sheet.addCell(new Label(9, i+1, date));
                    sheet.addCell(new Label(10, i+1, time));
//                    sheet.addCell(new Label(11, i+1, photo));
                    //file path
                    File filePhoto = new File(directoryPhoto, photo);
                    URL url = new URL(Config.REPORT_URL[Config.BUILD_TYPE]+"image?key=eyJ0eXWcl55i9h7Q&id="+photo);

                    WritableHyperlink wh = new WritableHyperlink(11, i+1, 11,i+1, url, "Click to see file");
//                    WritableImage wi = new WritableImage(11, i+1, 300, 300, filePhoto);
                    sheet.addHyperlink(wh);
                }

                rowImage = data_report.size();

                for(int i = 0; i < rowImage; i++) {
//                    DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask(data_report.get(i).getFileName());
//                    downloadFileAsyncTask.execute();
                    getRetrofitImage(data_report.get(i).getFileName(), "download",sDate,eDate);
                }
            } catch (RowsExceededException e) {
                e.printStackTrace();
                exDialog.dismiss();
            } catch (WriteException e) {
                e.printStackTrace();
                exDialog.dismiss();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
                exDialog.dismiss();
            }
        } catch (IOException e) {
            e.printStackTrace();
            exDialog.dismiss();
        }
    }

    private void getDataReport(String startDateS, String endDateS) {
        ApiInterface apiInterface = InitRetrofitReport.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(ReportFragment.this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<ReportResponse> reportCall = apiInterface.getReport(startDateS, endDateS);
        reportCall.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                Log.d("report", String.valueOf(response.body()));
                // Pasikan response Sukses
                if (response.isSuccessful()){
                    Log.d("response api", response.body().toString());
                    data_report = response.body().getData();

                    tablelayoutid = (TableLayout) myView.findViewById(R.id.tablelayoutid);
                    // Inflate your row "template" and fill out the fields.

                    while (tablelayoutid.getChildCount() > 1) {
                        TableRow row =  (TableRow)tablelayoutid.getChildAt(1);
                        tablelayoutid.removeView(row);
                        int j=tablelayoutid.getChildCount();
                    }

                    int jumRow = data_report.size();

                    if (jumRow > 0) {
                        flagExport = true;
                    } else {
                        flagExport = false;
                    }

                    for(int i = 0; i < jumRow; i++){
                        final TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.layout_row, null);
                        ((TextView)row.findViewById(R.id.idnotabel)).setText(String.valueOf(i+1));
                        ((TextView)row.findViewById(R.id.employeeName)).setText(data_report.get(i).getEmployeeName());
                        ((TextView)row.findViewById(R.id.phoneNumber)).setText(data_report.get(i).getPhoneNumber());
                        ((TextView)row.findViewById(R.id.emailAddress)).setText(data_report.get(i).getEmail());
                        ((TextView)row.findViewById(R.id.employeeId)).setText(data_report.get(i).getEmployeeId());
                        ((TextView)row.findViewById(R.id.supervisor)).setText("");
                        ((TextView)row.findViewById(R.id.customerName)).setText(data_report.get(i).getCustomerName());
                        ((TextView)row.findViewById(R.id.customerPhone)).setText(data_report.get(i).getCustomerPhone());
                        loc = ((TextView)row.findViewById(R.id.location));
                        loc.setText(data_report.get(i).getLocation());
                        loc.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                // The row the view is in
                                TableRow row = (TableRow) view.getParent();
                                // It's index
                                int index = tablelayoutid.indexOfChild(row);

                                double latitude = Double.valueOf(data_report.get(index-1).getLatitude());
                                double longitude = Double.valueOf(data_report.get(index-1).getLongitude());
                                String label = "Captured Location";
                                String uriBegin = "geo:" + latitude + "," + longitude;
                                String query = latitude + "," + longitude + "(" + label + ")";
                                String encodedQuery = Uri.encode(query);
                                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                Uri uri = Uri.parse(uriString);
                                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                startActivity(mapIntent);

//                                Toast.makeText(getContext(), "Lat:"+data_report.get(index-1).getLatitude()+"Lng:"+data_report.get(index-1).getLongitude(),
//                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        ((TextView)row.findViewById(R.id.dateSubmit)).setText(data_report.get(i).getDate());
                        ((TextView)row.findViewById(R.id.timeSubmit)).setText(data_report.get(i).getTime());

                        filePhoto = ((TextView)row.findViewById(R.id.photoCust));
                        filePhoto.setText(data_report.get(i).getFileName());
                        filePhoto.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                // The row the view is in
                                TableRow row = (TableRow) view.getParent();
                                // It's index
                                int index = tablelayoutid.indexOfChild(row);
                                String fileName = data_report.get(index-1).getFileName();

                                pdPreview = new ProgressDialog(ReportFragment.this.getContext());
                                pdPreview.setIndeterminate(true);
                                pdPreview.setCancelable(false);
                                pdPreview.setMessage("Downloading...");
                                pdPreview.show();

                                String appDirectoryName = "OrbitView/Images";
                                File directory = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS), appDirectoryName);

                                //file path
                                File file = new File(directory, fileName);

                                // Check file 0 Byte
                                double bytes = file.length();
                                int mem = Integer.valueOf((int) bytes);
                                if (mem == 0 || mem < 10000) {
                                    file.delete();
                                }

                                if(!file.exists()) {
                                    getRetrofitImage(fileName,"preview", "","");
                                } else {
                                    VideoView vidView = settingsDialog.findViewById(R.id.vidPreview);
                                    ImageView imgView = settingsDialog.findViewById(R.id.imgPreview);
                                    ImageView btnClose = (ImageView) settingsDialog.findViewById(R.id.closeDialogImg);
                                    btnClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            settingsDialog.dismiss();
                                        }
                                    });

                                    boolean ext = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+(\\.(?i)(jpe?g|png))$)", fileName);
                                    boolean extError = Pattern.matches("([a-zA-Z0-9\\s?\\_?]+$)", fileName);
                                    Log.d(TAG, "regex: "+ext);

                                    if(ext) {
                                        pdPreview.dismiss();
                                        if(file.exists()){
                                            int width = 300;
                                            int height = 300;
                                            Bitmap bMap = BitmapFactory.decodeFile(String.valueOf(file));
                                            width = bMap.getWidth();
                                            height = bMap.getHeight();
                                            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
                                            settingsDialog.show();
                                            vidView.setVisibility(View.GONE);
                                            imgView.setVisibility(View.VISIBLE);
                                            imgView.setImageBitmap(bMap2);
                                        } else {
                                            showMessage("File doesn't exist!");
                                        }
                                    } else if(extError) {
                                        pdPreview.dismiss();
                                        showMessage("File cannot be opened!");
                                    } else {
                                        pdPreview.dismiss();
                                        if(file.exists()) {
                                            settingsDialog.show();
                                            vidView.setVisibility(View.VISIBLE);
                                            imgView.setVisibility(View.GONE);
                                            vidView.setVideoPath(String.valueOf(file));
                                            vidView.start();
                                        } else {
                                            showMessage("File doesn't exist!");
                                        }
                                    }
                                }

                            }
                        });
                        tablelayoutid.addView(row);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

//    private void goDirection(String latitude, String longitude) {
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        context.startActivity(intent);
//    }

    private void showMessage(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
        toastContentView.addView(imageView, 0);
        toast.show();

    }

}
