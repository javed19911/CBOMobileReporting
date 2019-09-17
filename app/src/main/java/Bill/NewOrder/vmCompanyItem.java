package Bill.NewOrder;

import android.app.Activity;

import saleOrder.ViewModel.CBOViewModel;

public class vmCompanyItem  extends CBOViewModel<IFBillNewOrder> {
    @Override
    public void onUpdateView(Activity context, IFBillNewOrder view) {
        view.getCompanyCode();
        view.getCompanyCode();
    }
}
