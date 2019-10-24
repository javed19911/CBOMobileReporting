package com.cbo.cbomobilereporting.databaseHelper.DCR;

import com.cbo.cbomobilereporting.MyCustumApplication;

/**
 * Created by cboios on 12/02/19.
 */

public class mDCR {
    private String Id; //DCR_ID
    private String Date; //DCR_DATE
    private String DisplayDate; //DATE_NAME
    private String Planed_Date; //Dcr_Planed_Date  i.e Date
    private String DateOnMobile; //dcr_date_real
    private String WorkTypeId; //working_code
    private String WorkType; //working_head


    private String WorkWithTitle; //WW_TITLE
    private String ShowWorkWithAsPerTP; //DCR_LOCKWW
    private String DiverWorkWith; //DIVERTWWYN
    private String WorkWithArr; //route_Ww_Name
    private String WorkWithIdArr; //route_Ww_ID
    private String WorkWithIndividualId;
    private String WorkWithIndividual; //work_with_individual_name

    private Boolean DivertRoute; //ROUTEDIVERTYN_Checked
    private String DivertRemark; //sDivert_Remark
    private String RouteTitle; //ROUTE_TITLE
    private String ShowRouteAsPerTP; //DCR_TP_AREA_AUTOYN
    private String RouteArr; //route_Route_Name
    private String RouteIdArr;

    private String AreaReq; //DCR_ADDAREANA
    private String NoOfAddAreaAlowed ; //DCR_NOADDAREA
    private String AreaTitle; //AREA_TITLE
    private String AreaArr; //area_name
    private String AreaIdArr;

    private String ApprovalMsg; //APPROVAL_MSG
    private String PlanTimeDiffererence_Server; //DcrPlanTime_server

    private String Backdate;  //IsBackDate
    private String BackDateReason; //BackDateReason

    private String AdditionalAreaApprovalReqd; //ADDAREA_APPYN
    private String AdditionalAreaValidationReqd ; //ADDITIONALAREA_MENDETYN

    private String AttachmentTilte; //SELFIE_TITLE
    private Boolean AttachmentMandatory ; //SELFIE_MANDATORYYN
    private String Attachment;


    ///getter
    public String getId() {
        //if (Id == null){
            Id = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_ID","0");
        //}
        return Id;
    }

    public String getDate() {
        if (Date == null){
            Date = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_DATE","0");
        }
        return Date;
    }

    public String getDisplayDate() {
        if (DisplayDate == null){
            DisplayDate = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DATE_NAME","0");
        }
        return DisplayDate;
    }

    public String getWorkTypeId() {
        if (WorkTypeId == null){
            WorkTypeId = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("working_code","W");
        }
        return WorkTypeId;
    }

    public String getWorkType() {
        if (WorkType == null){
            WorkType = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("working_head","Working");
        }
        return WorkType;
    }

    public String getDiverWorkWith() {
        return DiverWorkWith;
    }

    public String getWorkWithTitle() {
        if (WorkWithTitle == null){
            WorkWithTitle = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("WW_TITLE","Work-With");
        }
        return WorkWithTitle;
    }

    public String getWorkWithArr() {
        return WorkWithArr;
    }

    public String getWorkWithIdArr() {
        return WorkWithIdArr;
    }

    public Boolean IsRouteDiverted() {
        if (DivertRoute == null) {
            DivertRoute = MyCustumApplication.getInstance()
                    .getDataFrom_FMCG_PREFRENCE("ROUTEDIVERTYN_Checked", "N")
                    .equalsIgnoreCase("Y");
        }
        return DivertRoute;
    }

    public String getRouteTitle() {
        if (RouteTitle == null){
            RouteTitle = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ROUTE_TITLE","Route List");
        }
        return RouteTitle;
    }

    public String getRouteArr() {
        return RouteArr;
    }

    public String getRouteIdArr() {
        return RouteIdArr;
    }

    public String getAreaReq() {
        return AreaReq;
    }

    public String getAreaTitle() {
        if (AreaTitle == null){
            AreaTitle = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("AREA_TITLE","Area List");
        }
        return AreaTitle;
    }

    public String getAreaArr() {
        return AreaArr;
    }

    public String getAreaIdArr() {
        return AreaIdArr;
    }

    public String getPlaned_Date() {
        return Planed_Date;
    }

    public String getDateOnMobile() {
        return DateOnMobile;
    }

    public String getShowWorkWithAsPerTP() {
        if (ShowWorkWithAsPerTP == null){
            ShowWorkWithAsPerTP = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_LOCKWW","");
        }
        return ShowWorkWithAsPerTP;
    }

    public String getShowRouteAsPerTP() {
        if (ShowRouteAsPerTP == null){
            ShowRouteAsPerTP = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_TP_AREA_AUTOYN","");
        }
        return ShowRouteAsPerTP;
    }

    public String getNoOfAddAreaAlowed() {
        if (NoOfAddAreaAlowed == null){
            NoOfAddAreaAlowed = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_NOADDAREA","");
        }
        return NoOfAddAreaAlowed;
    }

    public String getAdditionalAreaApprovalReqd() {
        if (AdditionalAreaApprovalReqd == null){
            AdditionalAreaApprovalReqd = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ADDAREA_APPYN","");
        }
        return AdditionalAreaApprovalReqd;
    }

    public String getAdditionalAreaValidationReqd() {
        if (AdditionalAreaValidationReqd == null){
            AdditionalAreaValidationReqd = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ADDITIONALAREA_MENDETYN","");
        }
        return AdditionalAreaValidationReqd;
    }

    public String getAttachmentTilte() {
        if (AttachmentTilte == null){
            AttachmentTilte = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SELFIE_TITLE","");
        }
        return AttachmentTilte;
    }

    public Boolean getAttachmentMandatory(String doc_type) {
//        if (AttachmentMandatory == null){
            AttachmentMandatory = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SELFIE_MANDATORYYN","").equalsIgnoreCase("y")||
                    MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SELFIE_MANDATORYYN","").contains(doc_type);
//        }
        return AttachmentMandatory;
    }

    public String getAttachment() {
        if (Attachment == null){
            Attachment = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DAY_ATTACHMENT","");
        }
        return Attachment;
    }



    ///setter


    public mDCR setId(String id) {
        Id = id.trim().equalsIgnoreCase("")?"0":id.trim();
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DCR_ID",Id);
        return this;
    }

    public mDCR setDate(String date) {
        Date = date;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DCR_DATE",date);
        return this;
    }

    public mDCR setDisplayDate(String displayDate) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DATE_NAME",displayDate);
        DisplayDate = displayDate;
        return this;
    }

    public mDCR setWorkTypeId(String workTypeId) {
        WorkTypeId = workTypeId;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("working_code",workTypeId);
        return this;
    }

    public mDCR setWorkType(String workType) {
        WorkType = workType;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("working_head",workType);
        return this;
    }

    public mDCR setDiverWorkWith(String diverWorkWith) {
        DiverWorkWith = diverWorkWith;
        return this;
    }

    public mDCR setWorkWithTitle(String workWithTitle) {
        WorkWithTitle = workWithTitle;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("WW_TITLE",ShowWorkWithAsPerTP);
        return this;
    }

    public mDCR setWorkWithArr(String workWithArr) {
        WorkWithArr = workWithArr;
        return this;
    }

    public mDCR setWorkWithIdArr(String workWithIdArr) {
        WorkWithIdArr = workWithIdArr;
        return this;
    }

    public mDCR setDivertRoute(Boolean divertRoute) {
        DivertRoute = divertRoute;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ROUTEDIVERTYN_Checked",divertRoute?"Y":"N");
        return this;
    }

    public mDCR setRouteTitle(String routeTitle) {
        RouteTitle = routeTitle;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ROUTE_TITLE",RouteTitle);
        return this;
    }

    public mDCR setRouteArr(String routeArr) {
        RouteArr = routeArr;
        return this;
    }

    public mDCR setRouteIdArr(String routeIdArr) {
        RouteIdArr = routeIdArr;
        return this;
    }

    public mDCR setAreaReq(String areaReq) {
        AreaReq = areaReq;
        return this;
    }

    public mDCR setAreaTitle(String areaTitle) {
        AreaTitle = areaTitle;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("AREA_TITLE",AreaTitle);
        return this;
    }

    public mDCR setAreaArr(String areaArr) {
        AreaArr = areaArr;
        return this;
    }

    public mDCR setAreaIdArr(String areaIdArr) {
        AreaIdArr = areaIdArr;
        return this;
    }

    public mDCR setPlaned_Date(String planed_Date) {
        Planed_Date = planed_Date;
        return this;
    }

    public mDCR setDateOnMobile(String dateOnMobile) {
        DateOnMobile = dateOnMobile;
        return this;
    }

    public mDCR setShowWorkWithAsPerTP(String showWorkWithAsPerTP) {
        ShowWorkWithAsPerTP = showWorkWithAsPerTP;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DCR_LOCKWW",ShowWorkWithAsPerTP);
        return this;
    }

    public mDCR setShowRouteAsPerTP(String showRouteAsPerTP) {
        ShowRouteAsPerTP = showRouteAsPerTP;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DCR_TP_AREA_AUTOYN",ShowRouteAsPerTP);
        return this;
    }

    public mDCR setNoOfAddAreaAlowed(String noOfAddAreaAlowed) {
        NoOfAddAreaAlowed = noOfAddAreaAlowed.trim().equalsIgnoreCase("")?"0":noOfAddAreaAlowed.trim();
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DCR_NOADDAREA",NoOfAddAreaAlowed);
        return this;
    }

    public mDCR setAdditionalAreaApprovalReqd(String additionalAreaApprovalReqd) {
        AdditionalAreaApprovalReqd = additionalAreaApprovalReqd;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ADDAREA_APPYN",additionalAreaApprovalReqd);
        return this;
    }

    public mDCR setAdditionalAreaValidationReqd(String additionalAreaValidationReqd) {
        AdditionalAreaValidationReqd = additionalAreaValidationReqd;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ADDITIONALAREA_MENDETYN",additionalAreaValidationReqd);
        return this;
    }

    public mDCR setAttachmentTilte(String attachmentTilte) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("SELFIE_TITLE",attachmentTilte);
        AttachmentTilte = attachmentTilte;
        return this;
    }

    public mDCR setAttachmentMandatory(String attachmentMandatory) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("SELFIE_MANDATORYYN",attachmentMandatory);
        AttachmentMandatory = null;
        return this;
    }

    public void setAttachment(String attachment) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DAY_ATTACHMENT",attachment);
        Attachment = attachment;
    }

}
