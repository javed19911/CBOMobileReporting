package saleOrder.Views;

import java.util.ArrayList;

import saleOrder.Model.mParty;

/**
 * Created by cboios on 06/03/19.
 */

public interface iClient {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();
    void setTile();
    void openOrder(mParty party);
    void openNewOrder(mParty party);
    void onPartyListUpdated(ArrayList<mParty> parties);
}
