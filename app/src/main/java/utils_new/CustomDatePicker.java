package utils_new;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDatePicker implements DatePickerDialog.OnDateSetListener{
// This is the listener for the activity
    private ICustomDatePicker listener = null;
    private Date minDate;
    private Date maxDate;
    private Context context;

    public static String ShowFirstDayOfMonthFormat = "01-MMM-yyyy";
    public static String CommitFirstDayOfMonthFormat = "MM-01-yyyy";
    public static String ShowFormat = "dd-MMM-yyyy";
    public static String CommitFormat = "MM/dd/yyyy";
    

    public static Date getDate(String date,String format) throws java.text.ParseException {
        SimpleDateFormat format_new = new SimpleDateFormat(format);
        Date date1 = format_new.parse(date);
        return date1;
    }

    public static String formatDate(Date date,String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String  currentDate = dateFormat.format(date);
        return currentDate;
    }


    public static String currentDate() {
        return currentDate("MM/dd/yyyy");
    }

    public static String currentDate(String format) {
        Date todayDate = new Date();
        return formatDate(todayDate,format);
    }


    public void Show(Date date){
        Show(date,listener);
    }
    public void Show(Date date,ICustomDatePicker listener) {
// Get the current date to start the CustomDatePicker with
        this.listener = listener;
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);


       // return new DatePickerDialog(getActivity(), this, year, month, day);
        DatePickerDialog dpDialog = new DatePickerDialog(context,this , year, month, day);
        DatePicker datePicker = dpDialog.getDatePicker();
        if (minDate != null) {
            datePicker.setMinDate(minDate.getTime());
        }
        if (maxDate != null) {
            datePicker.setMaxDate(maxDate.getTime());
        }
         dpDialog.show();

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = (month+1);
        String date = ""+dayOfMonth;
        date += "/"+ month;
        date += "/"+ year;

        try {
            listener.onDateSet(getDate( date,"dd/MM/yyyy"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public CustomDatePicker(Context context,Date FromDate, Date ToDate,ICustomDatePicker listener) {
        this.context = context;
        minDate = FromDate;
        maxDate = ToDate;
        this.listener = listener;
    }

    public CustomDatePicker(Context context,Date FromDate, Date ToDate) {
        this.context = context;
        minDate = FromDate;
        maxDate = ToDate;
        this.listener = null;
       // new CustomDatePicker(context,FromDate,ToDate,null);
    }
    public CustomDatePicker(Context context,Date FromDate) {
        this.context = context;
        minDate = FromDate;
        maxDate = null;
        this.listener = null;
        //new CustomDatePicker(context,FromDate,null,null);
    }

    public CustomDatePicker(Context context) {
        this.context = context;
        minDate = null;
        maxDate = null;
        this.listener = null;
       // new CustomDatePicker(context,null,null,null);
    }

    // This is the method from the CustomDatePicker fragment to implement in the Main Activity
    public interface ICustomDatePicker{
         void onDateSet(Date date);
    }


}
