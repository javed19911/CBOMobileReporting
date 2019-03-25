package saleOrder;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

/**
 * Created by cboios on 04/03/19.
 */

public abstract class CBOViewModel<T> extends ViewModel {
    protected T view;
    public void setView(Activity context, T view){
        this.view = view;
        if (view != null){
            onUpdateView(context,view);
        }
    }
    public abstract void onUpdateView(Activity context, T view);
}
