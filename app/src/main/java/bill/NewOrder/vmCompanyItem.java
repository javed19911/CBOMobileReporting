package bill.NewOrder;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import saleOrder.ViewModel.CBOViewModel;

public class vmCompanyItem  extends CBOViewModel<IFBillNewOrder> {
    @Override
    public void onUpdateView(AppCompatActivity context, IFBillNewOrder view) {
        view.getCompanyCode();
        view.getCompanyCode();
    }
}
