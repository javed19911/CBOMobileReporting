package bill.Cart;

import android.app.Activity;

import bill.mBillOrder;
import saleOrder.ViewModel.CBOViewModel;

public class vmCompanyCart extends CBOViewModel<ICompanyCart> {

    private mBillOrder order;

    @Override
    public void onUpdateView(Activity context, ICompanyCart view) {

        view.getReferencesById();
        view.getActvityttitle();
        view.setTitle(view.getActvityttitle());

    }

    public mBillOrder getOrder(){
        return order;
    }
    public void setOrder(mBillOrder order){
        this.order = order;
    }
}
