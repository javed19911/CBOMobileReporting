package bill.BillReport;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bill.CompanySelecter.ICompany;
import bill.openingStock.OpeningStockActivity;
import saleOrder.MyOrderAPIService;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;
import utils_new.CustomDialog.Spinner_Dialog;

public class FBillFilter extends Fragment implements ICompany {
    LinearLayout Lname, LFDate, LTDate;
    Button name, FDateBtn, TDateBtn;
    Context context;
    ImageView spinImgName,spinImgMonth,img_missed_type;
    mCompany selectedCompany;
    Date TDateSelected, FDateSelected;

    private  ArrayList <mCompany> Companies= new ArrayList<>();
    private ArrayList<mPay> payModes = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_bill_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        name = view.findViewById(R.id.rptt_name);
        FDateBtn = view.findViewById(R.id.month_name);
        TDateBtn = view.findViewById(R.id.missed_type);

        Lname = view.findViewById(R.id.Lrptt_name);
        LFDate = view.findViewById(R.id.Lmonth_name);
        LTDate = view.findViewById(R.id.Lmissed_type);

        spinImgName =  view.findViewById(R.id.spinner_img_dcr_rpt_name);
        spinImgMonth = view.findViewById(R.id.spinner_img_dcr_rpt_month);
        img_missed_type =  view.findViewById(R.id.img_missed_type);
        context = getContext();



        name.setText("---Select---");
        FDateBtn.setText("---Select From Date---");
        TDateBtn.setText("---Select To Date---");

        setTDATE(new Date());
        setFDATE(new Date());


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Spinner_Dialog(context, Companies, item -> {

                    selectedCompany = (mCompany) item;
                    name.setText(item.getName());


                }).show();

            }
        });
        TDateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   new CustomDatePicker(context, null,null

                   ).Show(CustomDatePicker.getDate(TDateBtn.getText().toString(),  CustomDatePicker.ShowFormat)
                           , new CustomDatePicker.ICustomDatePicker() {
                               @Override
                               public void onDateSet(Date date) {
                                   setTDATE(date);
                               }
                           });

               } catch (ParseException e) {
                   e.printStackTrace();
               }

           }
       });
        FDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, null,null

                    ).Show(CustomDatePicker.getDate(FDateBtn.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {

                                    setFDATE(date);
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        Lname.setOnClickListener(view1 -> name.performClick());
        LFDate.setOnClickListener(view1 -> FDateBtn.performClick());
        LTDate.setOnClickListener(view1 -> TDateBtn.performClick());

        getCompanyDDl(getActivity());

    }

    private void getCompanyDDl(Context context) {
        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        new MyOrderAPIService(context)
                .execute(
                        new ResponseBuilder("BILLDDL_MOBILE", request)

                                .setTables(tables)
                                .setResponse(
                                        new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle bundle) {

                                                   onPartyListUpdated(Companies);

                                            }

                                            @Override
                                            public void onResponse(Bundle bundle) throws Exception {
                                                parser1(bundle);
                                                //"COMPANY_ID":"23","COMPANY_NAME":"ABN Medical Specialties"
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);                                          }
                                        }));


    }

    private void parser1(Bundle bundle) throws JSONException {
        String table0 = bundle.getString("Tables0");
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(table0);
        Companies.clear();
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject c = jsonArray.getJSONObject(i);
            Companies.add(new mCompany(c.getString("COMPANY_NAME"), c.getString("COMPANY_ID")));

        }


        String table1 = bundle.getString("Tables1");
        JSONArray jsonArray1  = new JSONArray(table1);
        payModes.clear();
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject c = jsonArray1.getJSONObject(i);
            payModes.add(new mPay(c.getInt("ID"), c.getString("PAYMENT_MODE")));

        }





    }


    public mCompany getSelectedCompany(){
        return selectedCompany;
    }

    public Date getTDateSelected(){
        return TDateSelected;
    }

    public Date getFDateSelected(){
        return FDateSelected;
    }

    public ArrayList<mCompany> getCompanies(){
        return Companies;
    }

    public ArrayList<mPay> getPayModes(){
        return payModes;
    }

    @Override
    public void onPartyListUpdated(ArrayList<mCompany> Companies) {
        //this.Companies.addAll(Companies);
        selectedCompany = Companies.get(Companies.size()-1);
        name.setText(selectedCompany.getName());

        if (getActivity().getClass() == BillActivity.class) {
            ((BillActivity) getActivity()).getBills();
        }else if(getActivity().getClass() == DashboardBill.class) {
            ((DashboardBill) getActivity()).getBills();
        }else if(getActivity().getClass() == OpeningStockActivity.class) {
            ((OpeningStockActivity) getActivity()).getBills();
        }


    }

    @Override
    public void setFDATE(Date date) {
        FDateBtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
        FDateSelected = date;
    }

    @Override
    public void setTDATE(Date date) {

        TDateBtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
        TDateSelected = date;
    }


}