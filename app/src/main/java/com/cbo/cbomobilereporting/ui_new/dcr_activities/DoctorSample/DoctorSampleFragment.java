package com.cbo.cbomobilereporting.ui_new.dcr_activities.DoctorSample;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Enum.CallType;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Views.iCart;
import utils.adapterutils.PobModel;
import utils_new.cboUtils.CBOGift;
import utils_new.cboUtils.CBOPOB;
import utils_new.cboUtils.CalledDoctorSinner;

public class DoctorSampleFragment extends Fragment {

    private vmDoctorSample mViewModel;

    private CBOGift cboGift;
    private CBOPOB cbopob;
    private CalledDoctorSinner calledDoctorSinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doctor_sample_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calledDoctorSinner = view.findViewById(R.id.docList);
        cboGift = view.findViewById(R.id.gift);
        cbopob = view.findViewById(R.id.pob);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CBOGift.GIFT_DIALOG:
                    ArrayList<PobModel> gifts = (ArrayList<PobModel>) data.getSerializableExtra("Items");
                    cboGift.updateDataList(gifts);
                    break;
                case CBOPOB.POB_DIALOG:
                    ArrayList<PobModel> pobs = (ArrayList<PobModel>) data.getSerializableExtra("Items");
                    cbopob.updateDataList(pobs);
                    break;

                default:

            }
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(vmDoctorSample.class);

        cboGift.setActivity(this, CallType.DOCTOR);
        cbopob.setActivity(this, CallType.DOCTOR);

    }

}
