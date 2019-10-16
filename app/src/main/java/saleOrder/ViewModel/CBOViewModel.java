package saleOrder.ViewModel;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

/**
 * Created by cboios on 04/03/19.
 */

public abstract class CBOViewModel<T> extends ViewModel {
    private int count = 0;
    protected T view;
    public void setView(AppCompatActivity context, T view){
        this.view = view;
        if (view != null){
            onUpdateView(context,view);
        }
    }

    public Boolean isLoaded(){
        count++;
        return count >1;
    }
    public int getCount(){
        count++;
        return count;
    }
    public abstract void onUpdateView(AppCompatActivity context, T view);
}
