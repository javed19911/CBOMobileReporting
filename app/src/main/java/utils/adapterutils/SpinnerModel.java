package utils.adapterutils;

import utils.model.DropDownModel;

public class SpinnerModel extends DropDownModel{
	
	/*private  String name="";

	private  String id="";*/
	private  String LAST_VISIT_DATE="";

	private  String CLASS="";
	private  String POTENCY_AMT="";
	private  String ITEM_NAME="";
	private  String ITEM_POB="";
	private  String ITEM_SALE="";
	private  String AREA="";
	private  String FREQ="";
	private  String DR_LAT_LONG="";
	private  String DR_LAT_LONG2="";
	private  String DR_LAT_LONG3="";
	private  String PANE_TYPE="";
	private  String NO_VISITED="";
	private  String COLORYN="0";
	private  String CALLYN="0";
	private  String REF_LAT_LONG="";

	private  String CRM_COUNT="";
	private  String DRCAPM_GROUP="";
	/*********** Set Methods ******************/
	public SpinnerModel(String name,String id){
		super(name,id);
	}
	public SpinnerModel(String name,String id,String PLANE_TYPE){
		/*this.name=name;
		this.id=id;*/
		super(name,id);
		this.PANE_TYPE= PLANE_TYPE;
	}

	public SpinnerModel(String name,String id,String last_visited, String DR_LAT_LONG,String DR_LAT_LONG2, String DR_LAT_LONG3,String CALLYN){
		/*this.name=name;
		this.id=id;*/
		super(name,id);
		LAST_VISIT_DATE=last_visited;
		this.DR_LAT_LONG=DR_LAT_LONG;
		this.DR_LAT_LONG2=DR_LAT_LONG2;
		this.DR_LAT_LONG3=DR_LAT_LONG3;
		this.PANE_TYPE="1";
		this.CALLYN=CALLYN;
	}

	public SpinnerModel(String name,String id,String last_visited,String CLASS,String POTENCY_AMT,String ITEM_NAME
			,String ITEM_POB,String ITEM_SALE,String AREA,String PANE_TYPE, String DR_LAT_LONG,String FREQ, String NO_VISITED
			,String DR_LAT_LONG2, String DR_LAT_LONG3,String COLORYN,String CALLYN,String CRM_COUNT,String DRCAPM_GROUP){
		/*this.name=name;
		this.id=id;*/
		super(name,id);
		LAST_VISIT_DATE=last_visited;
		this.CLASS=CLASS;
		this.POTENCY_AMT=POTENCY_AMT;
		this.ITEM_NAME=ITEM_NAME;
		this.ITEM_POB=ITEM_POB;
		this.ITEM_SALE=ITEM_SALE;
		this.AREA=AREA;
		this.PANE_TYPE=PANE_TYPE;
		this.DR_LAT_LONG=DR_LAT_LONG;
		this.DR_LAT_LONG2=DR_LAT_LONG2;
		this.DR_LAT_LONG3=DR_LAT_LONG3;
		this.FREQ=FREQ;
		this.NO_VISITED=NO_VISITED;
		this.COLORYN=COLORYN;
		this.CALLYN=CALLYN;
		this.CRM_COUNT = CRM_COUNT;
		this.DRCAPM_GROUP = DRCAPM_GROUP;
	}

	public SpinnerModel(String name){
		/*this.name=name;*/
		super(name,"0");
	}
	/*public void setName(String name)
	{
		this.name = name;
	}*/

	public String getLastVisited()
	{
		return LAST_VISIT_DATE;
	}
	public String getCLASS()
	{
		return CLASS;
	}
	public String getPOTENCY_AMT()
	{
		return POTENCY_AMT;
	}
	public String getITEM_NAME()
	{
		return ITEM_NAME;
	}
	public String getITEM_POB()
	{
		return ITEM_POB;
	}
	public String getITEM_SALE()
	{
		return ITEM_SALE;
	}
	public String getAREA()
	{
		return AREA;
	}
	public String getPANE_TYPE()
	{
		return PANE_TYPE;
	}


	
	
/*	public void setId(String id)
	{
		this.id = id;
	}
	
	*//*********** Get Methods ****************//*
	public String getName()
	{
		return this.name;
	}
	public String getId()
	{
		return this.id;
	}*/

	public String getLoc()
	{
		return this.DR_LAT_LONG.trim();
	}
	public void setLoc(String DR_LAT_LONG) {
		this.DR_LAT_LONG=DR_LAT_LONG;
	}

	public String getLoc2()
	{
		if	(DR_LAT_LONG2 == null) {
			return "";
		}
		return DR_LAT_LONG2.trim();
	}
	public void setLoc2(String DR_LAT_LONG2) {
		this.DR_LAT_LONG2=DR_LAT_LONG2;
	}

	public String getLoc3()
	{
		if	(DR_LAT_LONG3 == null) {
			return "";
		}

		return DR_LAT_LONG3.trim();
	}
	public void setLoc3(String DR_LAT_LONG3) {
		this.DR_LAT_LONG3=DR_LAT_LONG3;
	}

	public String getFREQ()
	{
		return this.FREQ;
	}
	public void setFREQ(String FREQ) {
		this.FREQ=FREQ;
	}

	public String getNO_VISITED()
	{
		return this.NO_VISITED;
	}
	public void setNO_VISITED(String NO_VISITED) {
		this.NO_VISITED=NO_VISITED;
	}

	public String getCOLORYN()
	{
		return this.COLORYN;
	}
	public void setCOLORYN(String COLORYN) {
		this.COLORYN=COLORYN;
	}

	public String getCALLYN()
	{
		return this.CALLYN;
	}
	public void setCALLYN(String CALLYN) {
		this.CALLYN=CALLYN;
	}

	public String getCRM_COUNT()
	{
		return this.CRM_COUNT;
	}
	public void setCRM_COUNT(String CRM_COUNT) {
		this.CRM_COUNT=CRM_COUNT;
	}

	public String getDRCAPM_GROUP()
	{
		return this.DRCAPM_GROUP;
	}
	public void setDRCAPM_GROUP(String DRCAPM_GROUP) {
		this.DRCAPM_GROUP=DRCAPM_GROUP;
	}

	public String getREF_LAT_LONG() {
		return REF_LAT_LONG;
	}

	public void setREF_LAT_LONG(String REF_LAT_LONG) {
		this.REF_LAT_LONG = REF_LAT_LONG;
	}
}
