package Bill.Cart;

import android.app.Activity;

import saleOrder.ViewModel.CBOViewModel;

public class vmCompanyCart extends CBOViewModel<ICompanyCart> {
    @Override
    public void onUpdateView(Activity context, ICompanyCart view) {

        view.getReferencesById();
        view.getActvityttitle();
        view.setTitle(view.getActvityttitle());

    }

   /* public mOrder getOrder(){
        return order;
    }
    public void setOrder(mOrder order){
        this.order = order;
    }*/
}
