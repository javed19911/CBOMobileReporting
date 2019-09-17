package Bill.BillReport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.F_TeamMonthDivision;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMissedFilter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import Bill.CompanySelecter.ICompany;
import Bill.CompanySelecter.vmCompany;
import utils.model.DropDownModel;
import utils_new.CustomDatePicker;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.CustomError;
import utils_new.Custom_Variables_And_Method;

public class FBillFilter extends Fragment implements ICompany {
    LinearLayout Lname, Lmonth, Lmissed_type, Ldivision;
    Button name, month, missed_type, division;
    vmCompany vmcompany;
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    public ProgressDialog progress1;
    ImageView spinImgName,spinImgMonth,img_missed_type;
    mCompany company;

    private  ArrayList <mCompany> Partylist= new ArrayList<>();
    @Override
    public void onStart() {
        super.onStart();
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(getContext());
        cboDbHelper = new CBO_DB_Helper(getContext());
       /* vmcompany= ViewModelProviders.of(getActivity()).get(VM_TeamMonthDivision.class);
        vmcompany.setListner(this);*/

        getViewModel();


       




    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_bill_filter, container, false);

        name = retView.findViewById(R.id.rptt_name);
        month = retView.findViewById(R.id.month_name);
        missed_type = retView.findViewById(R.id.missed_type);

        Lname = retView.findViewById(R.id.Lrptt_name);
        Lmonth = retView.findViewById(R.id.Lmonth_name);
        Lmissed_type = retView.findViewById(R.id.Lmissed_type);

        spinImgName =  retView.findViewById(R.id.spinner_img_dcr_rpt_name);
        spinImgMonth = retView.findViewById(R.id.spinner_img_dcr_rpt_month);
        img_missed_type =  retView.findViewById(R.id.img_missed_type);
        context = getContext();



        name.setText("---Select---");
        month.setText("---Select FromDate---");
        missed_type.setText("---Select ToDate---");


          name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Spinner_Dialog(context, Partylist, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {

                        mCompany company = new mCompany();
                        company.setId(item.getId());
                        company.setName(item.getName());

                        //vmCompany.setParty(party);
                        name.setText(item.getName());
                        vmcompany.setComponeyId(item.getId());


                    }
                }).show();

            }
        });
       month.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   new CustomDatePicker(context, null,null

                   ).Show(CustomDatePicker.getDate(month.getText().toString(),  CustomDatePicker.ShowFormat)
                           , new CustomDatePicker.ICustomDatePicker() {
                               @Override
                               public void onDateSet(Date date) {
                                   month.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                   try {
                                       vmcompany.setFDATE(CustomDatePicker.getDate(month.getText().toString(),CustomDatePicker.ShowFormat));
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }
                                   //  vmcompany.setFDATE(CustomDatePicker.formatDate(,CustomDatePicker.CommitFormat));
                                  // mIddate=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                               }
                           });

               } catch (ParseException e) {
                   e.printStackTrace();
               }

           }
       });
        missed_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, null,null

                    ).Show(CustomDatePicker.getDate(missed_type.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    missed_type.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));

                                    try {
                                        vmcompany.setTDATE(CustomDatePicker.getDate(missed_type.getText().toString(),CustomDatePicker.ShowFormat));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    // mIddate=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        Lname.setOnClickListener(view -> name.performClick());
        Lmonth.setOnClickListener(view -> month.performClick());
        Lmissed_type.setOnClickListener(view -> missed_type.performClick());

        return  retView;
    }

    public vmCompany getViewModel(){
        if ( vmcompany == null) {
            vmcompany = ViewModelProviders.of(getActivity()).get(vmCompany.class);
            vmcompany.setView(getActivity(),this);
            vmcompany.setListner(this);

        }
        return vmcompany;
    }


    @Override
    public void getReferencesById() {

    }


    @Override
    public void onPartyListUpdated(ArrayList<mCompany> billlist) {
        Partylist.addAll(billlist);
        name.setText(billlist.get(billlist.size()-1).getName());
       vmcompany.setComponeyId("");
        //add deafult

    }

    @Override
    public void setFDATE() {
        month.setText(CustomDatePicker.currentDate(CustomDatePicker.ShowFormat));
        try {
            vmcompany.setFDATE(CustomDatePicker.getDate(month.getText().toString(),CustomDatePicker.ShowFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTDATE() {
        missed_type.setText(CustomDatePicker.currentDate(CustomDatePicker.ShowFormat));
        try {
            vmcompany.setTDATE(CustomDatePicker.getDate(missed_type.getText().toString(),CustomDatePicker.ShowFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}