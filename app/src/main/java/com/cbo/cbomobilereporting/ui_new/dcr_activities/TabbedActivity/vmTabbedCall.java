package com.cbo.cbomobilereporting.ui_new.dcr_activities.TabbedActivity;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import saleOrder.ViewModel.CBOViewModel;

public class vmTabbedCall extends CBOViewModel<iTabbedCall> {

    @Override
    public void onUpdateView(AppCompatActivity context, iTabbedCall view) {
        if (view != null) {
            view.getReferencesById();
            //setStatus("P");
            view.setTile(view.getActivityTitle());
        }

    }
}
