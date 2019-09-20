package bill.NewOrder;

import android.app.Activity;
import android.content.Context;

import bill.Cart.Batch_Dialog;
import saleOrder.ViewModel.CBOViewModel;

public class vmBillitem  extends CBOViewModel<IFBillNewOrder> {
    private mBillItem item = new mBillItem();
    private BillBatchDB billBatchDB;

    @Override
    public void onUpdateView(Activity context, IFBillNewOrder view) {

        billBatchDB = new BillBatchDB(context);

        view.setDetaileLayoutEnabled(false);
        view.getReferencesById();
        view.setItem(new mBillItem().setName(""));
    }



    public mBillItem getItem(){
        return item;
    }



    public void setItem(mBillItem item){



        view.setDetaileLayoutEnabled(!item.getId().equalsIgnoreCase("0"));

        this.item = item;
        if ( item.getQty() == 0D) {
            //updateDiscount();
            view.setAddText("ADD");
        }else {
            view.setAddText("Update");
        }


        view.setItemName(item.getName());
        view.setQty(item.getQty());
        view.updateRate(Double.valueOf(item.getSALE_RATE()));
        view.setFocusQty(!item.getId().equalsIgnoreCase("0"));
    }


    public void showBatchForSelection(Context context, mBillItem item){
        new Batch_Dialog(context,billBatchDB.batches(item) , new Batch_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(mBillBatch item) {

            }
        });
    }





}

