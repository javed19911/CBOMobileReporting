package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection;

import androidx.appcompat.app.AppCompatActivity;

import saleOrder.ViewModel.CBOViewModel;

public class vmSelection extends CBOViewModel<iSelection> {

    @Override
    public void onUpdateView(AppCompatActivity context, iSelection view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setTile(view.getActivityTitle());
        }
    }
}
