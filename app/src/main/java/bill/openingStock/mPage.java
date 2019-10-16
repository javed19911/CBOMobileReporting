package bill.openingStock;

import java.io.Serializable;

public class mPage implements Serializable {
    private String Title="";
    private String Code ="";
    private String OnLoadApi;
    private String OnDetailApi;
    private String OnFinalizeApi;
    private String onDeleteApi;

    public mPage(String title, String code) {
        Title = title;
        Code = code;
    }

    //geter

    public String getTitle() {
        return Title;
    }

    public String getCode() {
        return Code;
    }

    public String getOnLoadApi() {
        return OnLoadApi;
    }

    public String getOnDetailApi() {
        return OnDetailApi;
    }

    public String getOnFinalizeApi() {
        return OnFinalizeApi;
    }

    public String getOnDeleteApi() {
        return onDeleteApi;
    }


    //setter

    public mPage setOnLoadApi(String onLoadApi) {
        OnLoadApi = onLoadApi;
        return this;
    }

    public mPage setOnDetailApi(String onDetailApi) {
        OnDetailApi = onDetailApi;
        return this;
    }

    public mPage setOnFinalizeApi(String onFinalizeApi) {
        OnFinalizeApi = onFinalizeApi;
        return this;
    }

    public mPage setOnDeleteApi(String onDeleteApi) {
        this.onDeleteApi = onDeleteApi;
        return this;
    }

}
