package com.orbitview.salesmanagement.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.orbitview.salesmanagement.R;
import com.orbitview.salesmanagement.model.DataItem;

import java.util.List;

public class AdapterReport extends BaseAdapter {
    List<DataItem> dataReport;
    Context context;
    LayoutInflater inflater;

    public AdapterReport(Context context, List<DataItem> dataReport) {
        this.dataReport = dataReport;
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataReport.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.layout_row, parent, false);
        }

        TableLayout tablelayoutid = (TableLayout) v.findViewById(R.id.tablelayoutid);
        // Inflate your row "template" and fill out the fields.

        ((TextView)v.findViewById(R.id.idnotabel)).setText(position);
        ((TextView)v.findViewById(R.id.employeeName)).setText(dataReport.get(position).getEmployeeName());
        ((TextView)v.findViewById(R.id.phoneNumber)).setText(dataReport.get(position).getPhoneNumber());
        ((TextView)v.findViewById(R.id.emailAddress)).setText(dataReport.get(position).getEmail());
        ((TextView)v.findViewById(R.id.employeeId)).setText(dataReport.get(position).getEmployeeId());
        ((TextView)v.findViewById(R.id.supervisor)).setText("");
        ((TextView)v.findViewById(R.id.customerName)).setText(dataReport.get(position).getEmployeeName());
        ((TextView)v.findViewById(R.id.customerPhone)).setText(dataReport.get(position).getCustomerPhone());
        ((TextView)v.findViewById(R.id.location)).setText(dataReport.get(position).getLocation());
        ((TextView)v.findViewById(R.id.dateSubmit)).setText(dataReport.get(position).getDate());
        ((TextView)v.findViewById(R.id.timeSubmit)).setText(dataReport.get(position).getTime());
        ((TextView)v.findViewById(R.id.photoCust)).setText(dataReport.get(position).getFileName());
        tablelayoutid.addView(v);

//        TextView idnotabel = (TextView) v.findViewById(R.id.idnotabel);
//        TextView employeeName = (TextView) v.findViewById(R.id.employeeName);
//        TextView phoneNumber = (TextView) v.findViewById(R.id.phoneNumber);
//        TextView emailAddress = (TextView) v.findViewById(R.id.emailAddress);
//        TextView employeeId = (TextView) v.findViewById(R.id.employeeId);
//        TextView supervisor = (TextView) v.findViewById(R.id.supervisor);
//        TextView customerName = (TextView) v.findViewById(R.id.customerName);
//        TextView customerPhone = (TextView) v.findViewById(R.id.customerPhone);
//        TextView location = (TextView) v.findViewById(R.id.location);
//        TextView dateSubmit = (TextView) v.findViewById(R.id.dateSubmit);
//        TextView timeSubmit = (TextView) v.findViewById(R.id.timeSubmit);
//        TextView photoCust = (TextView) v.findViewById(R.id.photoCust);

//        idnotabel.setText(position);
//        employeeName.setText(dataReport.get(position).getEmployeeName());
//        phoneNumber.setText(dataReport.get(position).getPhoneNumber());
//        emailAddress.setText(dataReport.get(position).getEmail());
//        employeeId.setText(dataReport.get(position).getEmployeeId());
//        supervisor.setText("");
//        customerName.setText(dataReport.get(position).getCustomerName());
//        customerPhone.setText(dataReport.get(position).getCustomerPhone());
//        location.setText(dataReport.get(position).getLocation());
//        dateSubmit.setText(dataReport.get(position).getDate());
//        timeSubmit.setText(dataReport.get(position).getTime());
//        photoCust.setText(dataReport.get(position).getFileName());

        return v;
    }
}
