package saleOrder.ViewModel;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Views.iCart;

/**
 * Created by cboios on 06/03/19.
 */

public class vmCart extends CBOViewModel<iCart> {

    private mOrder order = new mOrder();;
    @Override
    public void onUpdateView(AppCompatActivity context, iCart view) {
        view.getReferencesById();
    }

    public mOrder getOrder(){
        return order;
    }
    public void setOrder(mOrder order){
        this.order = order;
    }



}
