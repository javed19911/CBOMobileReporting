package com.cbo.cbomobilereporting.ui_new.dcr_activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import com.cbo.cbomobilereporting.R
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.DairyCallDB
import com.cbo.cbomobilereporting.databaseHelper.Call.mDairyCall
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS
import com.uenics.javed.CBOLibrary.Response
import locationpkg.Const
import services.CboServices
import services.Sync_service
import utils.adapterutils.ExpandableListAdapter
import utils.adapterutils.SpinAdapter
import utils.adapterutils.SpinAdapter_new
import utils.adapterutils.SpinnerModel
import utils.clearAppData.MyCustumApplication
import utils.networkUtil.NetworkUtil
import utils_new.*
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class DairyCall : AppCompatActivity() , ExpandableListAdapter.Summary_interface{

    lateinit var context: Context
    lateinit var loc: EditText
    lateinit var workwithdr:EditText
    lateinit var drname: Button
    lateinit var back:Button
    lateinit var btn_remark:Button
    lateinit var btn_intrested:Button
    lateinit var submit: Button
    lateinit var getdr:Button
    lateinit var customVariablesAndMethod: Custom_Variables_And_Method
    var PA_ID: Int = 0
    var adapter: SpinAdapter? = null
    lateinit var locLayout: RelativeLayout
    lateinit var cbohelp: CBO_DB_Helper
    var dr_name :String = ""
    var dr_name_reg : String  = ""
    var dr_id : String = ""
    var dr_id_reg = ""
    var dr_id_index = ""
    var doc_name = ""
    var part1 = ""
    var part2 = ""
    var part3 = ""
    var workwith1 = ""
    var workwith2 = ""
    var workwith34 = ""
    var work_with_name: String = ""
    var work_with_id:String = ""
    lateinit var docList: ArrayList<SpinnerModel>
    var data1: List<String> = ArrayList()
    lateinit var network_status: String
    lateinit var TitleName: Array<SpinnerModel>
    lateinit var array_sort: ArrayList<SpinnerModel>
    private var myalertDialog: AlertDialog? = null
    internal var textlength = 0
    internal var drInTime: String? = null
    lateinit var myBatteryLevel: String
    lateinit var dr_remark: EditText
    lateinit var spinImg: ImageView
    lateinit var remark_img:ImageView
    lateinit var interseted_img:ImageView

    internal var updated = "0"
    internal var drKm = "0"
    lateinit var networkUtil: NetworkUtil
    lateinit var myCustomMethod: MyCustomMethod
    lateinit var live_km: String
    lateinit var remark: TextView

    lateinit var POB_layout: LinearLayout
    lateinit private var product_layout:LinearLayout
    lateinit var call_layout: LinearLayout
    lateinit private var detail_layout:LinearLayout
    lateinit var Tab:LinearLayout
    lateinit var dr_remarkLayout: LinearLayout
    lateinit var dr_remark_dp: LinearLayout
    lateinit var summary_layout: ExpandableListView
    lateinit var tab_call: Button
    lateinit var tab_summary:Button
    internal var summary_list = HashMap<String, HashMap<String, ArrayList<String>>>()
    internal var doctor_list_summary = HashMap<String, ArrayList<String>>()
    internal var doctor_list = HashMap<String, ArrayList<String>>()
    lateinit var listAdapter: ExpandableListAdapter
    lateinit var stk: TableLayout
    lateinit var doc_detail:TableLayout
    internal var plan_type = "1"
    var AREA = ""
    var call_type = "0"     //plan_type=1 for planed, 0 for unplaned
    lateinit var IsInterested: CheckBox
    internal var currentBestLocation: Location? = null
     var locExtra = ""
    lateinit var remark_list: ArrayList<String>
    lateinit var intersed_list: ArrayList<String>
    private var showRegistrtion = 1

    lateinit var products:Button
    lateinit var gift:Button
    lateinit var pob:EditText

    internal var sample_name = ""
    internal var sample_pob = ""
    internal var sample_sample = ""
    internal var gift_name: String = ""
    internal var gift_qty:String = ""

    internal var sample_name_previous = ""
    internal var sample_pob_previous = ""
    internal var sample_sample_previous = ""
    internal var gift_name_previous: String = ""
    internal var gift_qty_previous:String = ""

    lateinit var alertdFragment: Report_Registration
    private val GPS_TIMMER = 4
    private val PRODUCT_DILOG = 5
    private val GIFT_DILOG = 6
    private val WORK_WITH_DIALOG = 3
    private val MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL = 1
    var progress1: ProgressDialog? = null
    var DOC_TYPE : String = "D"
    var head = "Dairy" // "Poultry"
    var latLong: String? = ""
    var ref_latLong = "";

    internal var result = 0.0
    internal var sample: String = "0.0"
    internal var name: String = ""
    internal var resultList:String = ""
    internal var name2: String = ""
    internal var name3:String = ""
    internal var name4:String = ""
    internal var IsRefreshedClicked = true
    lateinit var service: Service_Call_From_Multiple_Classes

    ///firebase DB
    internal var mdairyCall: mDairyCall? = null
    lateinit var dairyCallDB: DairyCallDB
    lateinit var locationDB: LocationDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dairy_call)


        val toolbar : android.support.v7.widget.Toolbar= findViewById(R.id.toolbar_hadder)
        val hader_text : TextView = findViewById(R.id.hadder_text_1)


        DOC_TYPE = if (intent.getStringExtra("DOC_TYPE") != null) intent.getStringExtra("DOC_TYPE") else "D"
        if(!DOC_TYPE.equals("D")){
            head = "Poultry";
        }
        hader_text.text = "$head Call"
        setSupportActionBar(toolbar)

        locationDB = LocationDB();
        dairyCallDB = DairyCallDB(head);

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_hadder_2016)
        }

        progress1 = ProgressDialog(this)

        context = this
        service = Service_Call_From_Multiple_Classes()
        loc= findViewById(R.id.loc)
        workwithdr = findViewById(R.id.get_workwith)
        drname = findViewById(R.id.drname)
        back = findViewById(R.id.bkfinal_button)
        btn_remark = findViewById(R.id.remark)
        submit  = findViewById(R.id.add)
        getdr= findViewById(R.id.getdcal)
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance()
        networkUtil = NetworkUtil(context)
        myCustomMethod= MyCustomMethod(context)

        locLayout  = findViewById(R.id.layout2)
        cbohelp = CBO_DB_Helper(applicationContext)

        POB_layout  = findViewById(R.id.pob_layout)
        product_layout  = findViewById(R.id.product_layout)

        products = findViewById(R.id.dairy_product)
        gift = findViewById(R.id.dairy_gift)
        pob = findViewById(R.id.dairy_pob)

        dr_remark = findViewById(R.id.dr_remark_edit)
        spinImg = findViewById(R.id.spinner_img_drCall)
        remark_img = findViewById(R.id.remark_img)

        interseted_img = findViewById(R.id.intrested_img)
        btn_intrested = findViewById(R.id.btn_intrested)

        remark = findViewById(R.id.last_visited)
        call_layout = findViewById(R.id.call_layout)
        detail_layout = findViewById(R.id.detail_layout)
        Tab = findViewById(R.id.tab)
        dr_remarkLayout = findViewById(R.id.dr_remark_layout)
        dr_remark_dp = findViewById(R.id.dr_remark_Dp)
        summary_layout = findViewById(R.id.summary_layout)
        tab_call = findViewById(R.id.call)
        tab_summary = findViewById(R.id.summary)
        stk = findViewById(R.id.last_pob)
        doc_detail = findViewById(R.id.doc_detail)
        IsInterested = findViewById(R.id.call_missed)



        if (Custom_Variables_And_Method.location_required == "N") {
            locLayout.visibility = View.GONE
        } else if (Custom_Variables_And_Method.location_required == "Y") {
            locLayout.visibility = View.VISIBLE
        } else {
            locLayout.visibility = View.GONE
        }
        //latLong = "" + Custom_Variables_And_Method.GLOBAL_LATLON
        //dr_remarkLayout.visibility = View.GONE
        dr_remark.visibility = View.GONE
        live_km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "live_km")
        detail_layout.setBackgroundColor(Color.TRANSPARENT)


        submit.setText("ADD $head")
        drname.text = "---Select---"
        btn_remark.text = "---Select Remark---"

        network_status = NetworkUtil.getConnectivityStatusString(applicationContext)

        customVariablesAndMethod.getbattrypercentage(context)
        remark_list = cbohelp._phdairy_reason

        intersed_list = ArrayList()
        intersed_list.add("--Select--")
        intersed_list.add("Interserted")
        intersed_list.add("Not Interserted")

        btn_intrested.text = intersed_list[0]
        SetInterested()


        docList = ArrayList()

        showRegistrtion = Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "IsBackDate", "1"))


       /* IsInterested.setOnCheckedChangeListener { compoundButton, ischecked ->
            if (ischecked ) {
                dr_remarkLayout.visibility = View.GONE
                dr_remark.visibility = View.GONE

                POB_layout.visibility = View.GONE
                product_layout.visibility = View.VISIBLE

                dr_remark.setText("")
            } else {
                dr_remarkLayout.visibility = View.VISIBLE
                dr_remark.visibility = View.GONE

                POB_layout.visibility = View.GONE
                product_layout.visibility = View.GONE
                sample_name = ""
                sample_sample = ""
                sample_pob = ""
                gift_name = ""
                gift_qty = ""
                stk.removeAllViews()
                doc_detail.removeAllViews()
                IsInterested.setChecked(false)
            }
        }*/

        spinImg.setOnClickListener { OnClickDrLoad() }
        remark_img.setOnClickListener { onClickDrCallRemark() }

        btn_intrested.setOnClickListener { onClickDrCallIntersed() }
        interseted_img.setOnClickListener { onClickDrCallIntersed() }

        drname.setOnClickListener { OnClickDrLoad() }
        btn_remark.setOnClickListener { onClickDrCallRemark() }

        getdr.setOnClickListener {
            if (!dr_id.equals("")) {
                /*val i = Intent(this@DairyCall, Dr_Workwith::class.java)
                i.putExtra("DAIRY_ID", dr_id)
                startActivityForResult(i, 2)*/
                val b = Bundle()
                b.putString("DAIRY_ID", dr_id)
                Dr_Workwith_Dialog(context, mHandler, b, WORK_WITH_DIALOG).show()
            }else{
                customVariablesAndMethod.getAlert(context,"Select !!!","Please select a $head....")
            }
        }

        val ProductCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "SAMPLE_BTN_CAPTION", "")
        if (!ProductCaption.isEmpty())
            products.text = ProductCaption

        products.setOnClickListener(View.OnClickListener {
            val chemName = drname.getText().toString()
            if (chemName.equals("---Select---", ignoreCase = true)) {

                customVariablesAndMethod.msgBox(context, "Please Select $head First..")
            } else {
               /* val i = Intent(applicationContext, Chm_Sample::class.java)
                i.putExtra("intent_fromRcpaCAll", head)
                i.putExtra("sample_name", sample_name)
                i.putExtra("sample_pob", sample_pob)
                i.putExtra("sample_sample", sample_sample)
                startActivityForResult(i, 0)*/

                val b = Bundle()
                b.putString("intent_fromRcpaCAll", head)
                b.putString("sample_name", sample_name)
                b.putString("sample_pob", sample_pob)
                b.putString("sample_sample", sample_sample)

                b.putString("sample_name_previous", sample_name_previous)
                b.putString("sample_pob_previous", sample_pob_previous)
                b.putString("sample_sample_previous", sample_sample_previous)

                Chm_Sample_Dialog(context, mHandler, b, PRODUCT_DILOG).Show()
            }
        })

        val GiftCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "GIFT_BTN_CAPTION", "")
        if (!GiftCaption.isEmpty())
            gift.text = GiftCaption

        gift.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub

            val chemName = drname.getText().toString()
            if (chemName.equals("---Select---", ignoreCase = true)) {

                customVariablesAndMethod.msgBox(context, "Please Select $head First..")
            } else {
//                val i = Intent(applicationContext, Chemist_Gift::class.java)
//                i.putExtra("intent_fromRcpaCAll", "dr")
//                i.putExtra("gift_name", gift_name)
//                i.putExtra("gift_qty", gift_qty)
//                startActivityForResult(i, 1)

                val b = Bundle()
                b.putString("intent_fromRcpaCAll", "dr")
                b.putString("gift_name", gift_name)
                b.putString("gift_qty", gift_qty)
                b.putString("gift_name_previous", gift_name_previous)
                b.putString("gift_qty_previous", gift_qty_previous)
                b.putString("ID", dr_id)
                Chemist_Gift_Dialog(context, mHandler, b, GIFT_DILOG).Show()
            }
        })



        submit.setOnClickListener {
            // TODO Auto-generated method stub

            //drInTime = getTime();

            myBatteryLevel = Custom_Variables_And_Method.BATTERYLEVEL
            setAddressToUI()
            if (loc.text.toString() == "" || loc.text.toString() == null) {
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON)
            }

            if (latLong == null || latLong == "0.0,0.0" || latLong == "") {
                latLong = Custom_Variables_And_Method.GLOBAL_LATLON
            }

            if (dr_id == "") {
                customVariablesAndMethod.getAlert(context,"Select !!!","Please select a $head....")
            } else if (docList.isEmpty() && dr_id == "") {
                customVariablesAndMethod.msgBox(context, "No Data in List")


            }else if (btn_intrested.getText().toString().equals(intersed_list[0],true)) {
                customVariablesAndMethod.msgBox(context, "Please Select a Interested/Not Intersted...")
            } else if (/*customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "REMARK_WW_MANDATORY").contains("P") && */(work_with_id == null || work_with_name == "")) {
                customVariablesAndMethod.msgBox(context, "Please select person...")


            } else if (/*customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "REMARK_WW_MANDATORY").contains("P") && */dr_remark.text.toString() == "" /*&& btn_intrested.getText().toString().equals(intersed_list[2],true)*/) {
                customVariablesAndMethod.msgBox(context, "Please enter remark...")

            } else if (btn_intrested.getText().toString().equals(intersed_list[1],true) && sample_name.equals("")) {
                customVariablesAndMethod.msgBox(context, "Please Select a product...")
            } else {

                val getCurrentTime = customVariablesAndMethod._currentTimeStamp
                val planTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DcrPlantimestamp", customVariablesAndMethod._currentTimeStamp)
                val t1 = java.lang.Float.parseFloat(getCurrentTime)
                val t2 = java.lang.Float.parseFloat(planTime)

                val t3 = t1 - t2
                if (t3 >= 0 || t3 >= -0.9) {
                    submitDoctor(false)
                } else {
                    customVariablesAndMethod.msgBox(context, "Your Plan Time can not be \n Higher Than Current time...")
                }

            }
        }

        back.setOnClickListener { finish() }



        val header_title = ArrayList<String>()
        try {
            doctor_list_summary = cbohelp.getCallDetail(head, "", "1")
            //expense_list=new CBO_DB_Helper(context).getCallDetail("tempdr");
            summary_list = LinkedHashMap()
            if(DOC_TYPE.equals("D")) {
                summary_list[cbohelp.getMenu("DCR", "D_DAIRY")["D_DAIRY"]!!] = doctor_list_summary
            }else{
                summary_list[cbohelp.getMenu("DCR", "D_POULTRY")["D_POULTRY"]!!] = doctor_list_summary
            }


            //final List<Integer> visible_status=new ArrayList<>();
            for (main_menu in summary_list.keys) {
                header_title.add(main_menu)
                //visible_status.add(0);
            }


            listAdapter = ExpandableListAdapter(summary_layout, this, header_title, summary_list)

            // setting list adapter
            summary_layout.setAdapter(listAdapter)
            summary_layout.setGroupIndicator(null)
            for (i in 0 until listAdapter.groupCount)
                summary_layout.expandGroup(i)
            //doctor.expandGroup(1);
        } catch (e: Exception) {
            val toEmailList = Arrays.asList(*"mobilereporting@cboinfotech.com".split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            SendMailTask().execute("mobilereporting@cboinfotech.com",
                    "mreporting", toEmailList, Custom_Variables_And_Method.COMPANY_CODE + ": " + "Error in DR_Call_sample", context.resources.getString(R.string.app_name) + "\n Company Code :" + Custom_Variables_And_Method.COMPANY_CODE + "\n DCR ID :" + Custom_Variables_And_Method.DCR_ID + "\n PA ID : " + Custom_Variables_And_Method.PA_ID + "\n App version : " + Custom_Variables_And_Method.VERSION + "\n Error Alert :" + "Error in DR_sample" + "\n" + e.toString())

            CboServices.getAlert(context, "Error!!!", e.toString())
            e.printStackTrace()
        }

        summary_layout.setOnChildClickListener { expandableListView, view, groupPosition, childPosition, l ->

            if ( summary_list[header_title[groupPosition]]!!["visible_status"]!!.get(childPosition) == "1") {
                summary_list[header_title[groupPosition]]!!["visible_status"]!!.set(childPosition, "0")
            } else {
                summary_list[header_title[groupPosition]]!!["visible_status"]!!.set(childPosition, "1")
            }
            listAdapter.notifyDataSetChanged()
            false
        }

        tab_call.setOnClickListener {

            call_layout.visibility = View.VISIBLE
            summary_layout.visibility = View.GONE

            tab_call.setBackgroundResource(R.drawable.tab_selected)
            tab_summary.setBackgroundResource(R.drawable.tab_deselected)

            // new DoctorData().execute();

            drname.text = "---Select---"
            remark.visibility = View.GONE
            workwithdr.setText("")
            dr_remark.setText("")
            //IsInterested.setChecked(false)
            stk.removeAllViews()
            doc_detail.removeAllViews()
            detail_layout.setBackgroundColor(Color.TRANSPARENT)
            drname.setOnClickListener { OnClickDrLoad() }
        }


        tab_summary.setOnClickListener {
            summary_layout.visibility = View.VISIBLE
            call_layout.visibility = View.GONE
            tab_call.setBackgroundResource(R.drawable.tab_deselected)
            tab_summary.setBackgroundResource(R.drawable.tab_selected)
        }


        val intent = intent
        remark.text = ""
        if (intent.getStringExtra("id") != null) {
            OnClickDrLoad()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            startActivity(Intent(this@DairyCall, ViewPager_2016::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    fun remAdded(): ArrayList<String> {
        /*val drlist = ArrayList<String>()
        drlist.clear()
        val c = cbohelp.doctorName1
        if (c.moveToFirst()) {
            do {
                drlist.add(c.getString(c.getColumnIndex("dr_id")))
            } while (c.moveToNext())
        }*/
        doctor_list = cbohelp.getCallDetail(head, "", "0")
        return doctor_list["id"]!!
    }


    fun setLocationToUI() {
        if (network_status == "Not connected to Internet") {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON)
        } else {

            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON)
        }


    }

    fun setAddressToUI() {
        if (network_status == "Not connected to Internet") {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON)
        } else {
            //loc.setText(MyConnection.GLOBAL_LATLON);
            if (Custom_Variables_And_Method.global_address != null || Custom_Variables_And_Method.global_address !== "") {
                loc.setText(Custom_Variables_And_Method.global_address)
            } else {
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON)
            }

        }
    }

    fun submitDoctor( Skip_Verification : Boolean) {


        var mydr : Long = 0
        var msg = "$head Added successfully"
        val PobAmt = pob.text.toString()
        var AllItemId = name
        var AllItemQty = name2
        var AllGiftId = name3
        val AllSampleQty = sample
        var AllGiftQty = name4


        mdairyCall?.setRemark(remark.text.toString())
        mdairyCall?.setWorkwith(work_with_name)
        mdairyCall?.setInterested(btn_intrested.getText().toString())

        if (remAdded().contains(dr_id)) {

            //customVariablesAndMethod.msgBox(context, "$dr_name Allready Added...")
           mydr =  cbohelp.update_phdairy_dcr(dr_id, doc_name,DOC_TYPE,dr_remark.text.toString(),work_with_name,work_with_id,PobAmt,AllItemId,AllItemQty,AllSampleQty,AllGiftId,AllGiftQty,"",if (btn_intrested.getText().toString().equals(intersed_list[1],true) ) "1" else "0")
            msg = "$head Updated successfully"
            customVariablesAndMethod.msgBox(context, msg)

            dairyCallDB.insert(mdairyCall)
            locationDB.insert(mdairyCall)
            finish()


        } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
             IsRefreshedClicked = false
            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                    IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE))
        }else {

            customVariablesAndMethod.SetLastCallLocation(context)

            Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON)

            currentBestLocation = customVariablesAndMethod.getObject(context, "currentBestLocation", Location::class.java)

            if (currentBestLocation != null) {
                locExtra = "Lat_Long " + currentBestLocation!!.getLatitude() + "," + currentBestLocation!!.getLongitude() + ", Accuracy " + currentBestLocation!!.getAccuracy() + ", Time " + currentBestLocation!!.getTime() + ", Speed " + currentBestLocation!!.getSpeed() + ", Provider " + currentBestLocation!!.getProvider()
            }


            mdairyCall!!.setSrno(customVariablesAndMethod.srno(context))
                    .setLOC_EXTRA(locExtra)
                    .setTime(customVariablesAndMethod.currentTime(context))

           mydr = cbohelp.insert_phdairy_dcr(dr_id, doc_name,DOC_TYPE, customVariablesAndMethod.currentTime(context),latLong,
                   myBatteryLevel, Custom_Variables_And_Method.global_address,dr_remark.text.toString(),
                   drKm,mdairyCall!!.srno,work_with_name,work_with_id,PobAmt,
                   AllItemId,AllItemQty,AllSampleQty,AllGiftId,AllGiftQty,"",locExtra,
                   if (btn_intrested.getText().toString().equals(intersed_list[1],true) ) "1" else "0",ref_latLong)

            if (mydr > 0) {

                customVariablesAndMethod.msgBox(context, msg)
                if (networkUtil.internetConneted(context)!!) {
                    if (live_km.equals("Y", ignoreCase = true) || live_km.equals("Y5", ignoreCase = true)) {
                        val myCustomMethod = MyCustomMethod(context)
                        myCustomMethod.stopAlarm10Minute()
                        myCustomMethod.startAlarmIn10Minute()
                    } else {
                        startService(Intent(context, Sync_service::class.java))
                    }
                }

                dairyCallDB.insert(mdairyCall)
                locationDB.insert(mdairyCall)
                finish()

            } else {
                customVariablesAndMethod.msgBox(context, "Insertion fail")
            }
        }



    }

    override fun Edit_Call(Dr_id: String, Dr_name: String) {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null)
        val Alert_title = dialogLayout.findViewById(R.id.title) as TextView
        val Alert_message = dialogLayout.findViewById(R.id.message) as TextView
        val Alert_Positive = dialogLayout.findViewById(R.id.positive) as Button
        val Alert_Nagative = dialogLayout.findViewById(R.id.nagative) as Button
        Alert_Nagative.text = "Cancel"
        Alert_Positive.text = "Edit"
        Alert_title.text = "Edit!!!"
        Alert_message.text = "Do you want to edit \n$Dr_name ?"

        val builder1 = AlertDialog.Builder(context)
        val dialog = builder1.create()
        dialog.setView(dialogLayout)
        Alert_Positive.setOnClickListener {
            call_layout.visibility = View.VISIBLE
            summary_layout.visibility = View.GONE
            tab_call.setBackgroundResource(R.drawable.tab_selected)
            tab_summary.setBackgroundResource(R.drawable.tab_deselected)


            dr_id = Dr_id
            dr_name = Dr_name
            drname.setText(dr_name)

            /*val last_visited = findViewById(R.id.last_visited) as TextView
            for (i in chemist.indices) {
                if (chemist.get(i).getId() == Dr_id) {
                    last_visited.visibility = View.VISIBLE
                    last_visited.text = "Last visited on : " + chemist.get(i).getLastVisited()
                    break
                }
            }*/


            mdairyCall = mDairyCall(head)
                    .setId(Dr_id)
                    .setName(Dr_name)
                    .setDcr_id(MyCustumApplication.getInstance().user.dcrId)
                    .setDcr_date(MyCustumApplication.getInstance().user.dcrDate) as mDairyCall

            UpadteUI_If_Called()
            dialog.dismiss()
        }
        Alert_Nagative.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(false)
        dialog.show()


    }

    override fun delete_Call(Dr_id: String, Dr_name: String) {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null)
        val Alert_title = dialogLayout.findViewById(R.id.title) as TextView
        val Alert_message = dialogLayout.findViewById(R.id.message) as TextView
        val Alert_Positive = dialogLayout.findViewById(R.id.positive) as Button
        val Alert_Nagative = dialogLayout.findViewById(R.id.nagative) as Button
        Alert_title.text = "Delete!!!"
        Alert_message.text = "Do you Really want to delete $Dr_name ?"
        Alert_Nagative.text = "Cancel"
        Alert_Positive.text = "Delete"

        val builder1 = AlertDialog.Builder(context)


        val dialog = builder1.create()

        dialog.setView(dialogLayout)
        Alert_Positive.setOnClickListener {
            dialog.dismiss()

            mdairyCall = mDairyCall(head).setId(Dr_id) as mDairyCall
            dairyCallDB.delete(mdairyCall)

            cbohelp.delete_phdairy_dcr(Dr_id)
            customVariablesAndMethod.msgBox(context, "$Dr_name sucessfully Deleted.")
            finish()
        }
        Alert_Nagative.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(false)
        dialog.show()

    }


    private fun OnClickDrLoad() {
        spinImg.isEnabled = false
        drname.isEnabled = false
        docList = ArrayList()
        GPS_Timmer_Dialog(context, mHandler, "Scanning Doctors...", GPS_TIMMER).show()
    }


    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

                PRODUCT_DILOG ->{

                    val b1 = msg.data
                    name = b1!!.getString("val")//id
                    name2 = b1.getString("val2")//score or pob
                    result = b1.getDouble("resultpob")
                    sample = b1.getString("sampleQty")// sample
                    val f = DecimalFormat("#.00")
                    resultList = b1.getString("resultList")
                    val result3 = f.format(result)
                    pob.setText(result3)

                    sample_name = resultList
                    sample_pob = name2
                    sample_sample = sample

                    mdairyCall!!.setSample_name_Arr(sample_name)
                            .setSample_pob_Arr(sample_pob)
                            .setSample_qty_Arr(sample_sample)

                    val sample_name1 = resultList.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val sample_qty1 = sample.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val sample_pob1 = name2.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    init(sample_name1, sample_qty1, sample_pob1)
                }
                GIFT_DILOG ->{

                    val b1 = msg.data
                    name3 = b1!!.getString("giftid")
                    name4 = b1.getString("giftqan")
                    if (b1.getString("giftname") != "") {
                        val gift_name1 = b1.getString("giftname")!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val gift_qty1 = b1.getString("giftqan")!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        gift_name = b1.getString("giftname")
                        gift_qty = b1.getString("giftqan")

                        init_gift(doc_detail, gift_name1, gift_qty1)
                    }


                    mdairyCall!!.setGift_name_Arr(gift_name)
                            .setGift_qty_Arr(gift_qty)
                }
                WORK_WITH_DIALOG -> {
                    val b1 = msg.data
                    work_with_name = b1!!.getString("workwith_name")
                    work_with_id = b1.getString("workwith_id")
                    workwithdr.setText("" + work_with_name)
                }
                GPS_TIMMER -> {
                    spinImg.isEnabled = true
                    drname.isEnabled = true
                    DoctorData().execute()
                }
                MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL -> {
                    onDownloadAllResponse();
                }
                99 -> if (null != msg.data) {
                    customVariablesAndMethod.msgBox(context, msg.data.getString("Error"))
                    //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun onDownloadAllResponse() {
        Custom_Variables_And_Method.GPS_STATE_CHANGED = true
        Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME = customVariablesAndMethod._currentTimeStamp
        GPS_Timmer_Dialog(context, mHandler, "Scanning $head...", GPS_TIMMER).show()
    }
    internal inner class DoctorData : AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>>() {
        var pd: ProgressDialog = ProgressDialog(this@DairyCall)
        //GPS_Timmer_Dialog dilog=new GPS_Timmer_Dialog(context);
        @SafeVarargs
        override fun doInBackground(vararg params: ArrayList<SpinnerModel>): ArrayList<SpinnerModel> {
            //getWorkWithDetails();

            try {
                docList.clear()

                val c = cbohelp.getphdairy(DOC_TYPE)
                // chemist.add(new SpinnerModel("--Select--",""));
                if (c.moveToFirst()) {
                    do {
                        docList.add(SpinnerModel(c.getString(c.getColumnIndex("DAIRY_NAME")), c.getString(c.getColumnIndex("DAIRY_ID")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")), c.getString(c.getColumnIndex("DR_LAT_LONG")), c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))))
                    } while (c.moveToNext())

                }


            } catch (e: Exception) {
                //customVariablesAndMethod.msgBox(context,e.toString());
                e.printStackTrace()

            }

            return docList
        }

        override fun onPreExecute() {
            super.onPreExecute()

            spinImg.isClickable = false
            drname.isClickable = false

            pd.setTitle("CBO")
            pd.setMessage("Processing......." + "\n" + "please wait")
            pd.setCancelable(false)
            pd.setCanceledOnTouchOutside(false)
            pd.show()

        }

        override fun onPostExecute(s: ArrayList<SpinnerModel>) {
            super.onPostExecute(s)
            pd.dismiss()
            setLocationToUI()
            try {
                pd.dismiss()
                if (!s.isEmpty() || s.size < 0) {
                    TitleName = Array<SpinnerModel>(s.size,{i -> s[i]})
                    array_sort = ArrayList(Arrays.asList(*TitleName))

                    val intent = intent
                    if (intent.getStringExtra("id") != null) {

                        for (i in docList.indices) {

                            if (docList[i].id == intent.getStringExtra("id").trim { it <= ' ' }) {
                                dr_id = docList[i].id
                                doc_name = docList[i].name.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                                drname.text = doc_name
                                if (intent.getStringExtra("remark") != null) {
                                    remark.visibility = View.VISIBLE
                                    remark.text = intent.getStringExtra("remark")
                                }
                            }
                        }
                    } else {
                        onClickDrName()
                    }
                } else {
                    customVariablesAndMethod.getAlert(context, "Data not found", "No $head in Planned Dcr...")


                    //  spinImg.setClickable(false);
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            spinImg.isClickable = true
            drname.isClickable = true
        }
    }


    private fun onClickDrName() {
        IsRefreshedClicked = true
        val myDialog = AlertDialog.Builder(this@DairyCall)
        val editText = EditText(this@DairyCall)
        val listview = ListView(this@DairyCall)
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, R.mipmap.ref3, 0)
        array_sort = ArrayList(Arrays.asList(*TitleName))
        val layout = LinearLayout(this@DairyCall)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(editText)
        layout.addView(listview)
        myDialog.setView(layout)
        val arrayAdapter = SpinAdapter_new(this@DairyCall, R.layout.spin_row, array_sort, showRegistrtion)
        listview.adapter = arrayAdapter
        listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            myalertDialog?.dismiss()
            dr_id = (view.findViewById(R.id.spin_id) as TextView).text.toString()
            doc_name = (view.findViewById(R.id.spin_name) as TextView).text.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            drname.text = doc_name
            latLong = ""
            ref_latLong = ""

            mdairyCall = null
            val model = array_sort[position]

            if ((view.findViewById(R.id.distance) as TextView).text.toString() == "Registration pending...") {
                if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                    customVariablesAndMethod.Connect_to_Internet_Msg(context)
                    drname.text = "---Select---"
                    dr_id = ""
                    doc_name = ""
                } else {
                    val intent = Intent(context, Doctor_registration_GPS::class.java)
                    intent.putExtra("id", dr_id)
                    intent.putExtra("name", doc_name)
                    intent.putExtra("type", DOC_TYPE +"A")
                    startActivity(intent)
                    finish()
                }
            } else if ((view.findViewById(R.id.distance) as TextView).text.toString().contains("Km Away")) {
                //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+doc_name,true);

                val fm = supportFragmentManager
                alertdFragment = Report_Registration()
                var km = (view.findViewById(R.id.distance) as TextView).text.toString()
                alertdFragment.setAlertLocation(array_sort[position].loc, array_sort[position].loc2, array_sort[position].loc3)
                alertdFragment.setAlertData("Not In Range", "You are $km from $doc_name")
                alertdFragment.show(fm, "Alert Dialog Fragment")
                km = km.replace("Km Away", "").trim { it <= ' ' }

                dr_id_reg = dr_id
                dr_id_index = ""
                dr_name_reg = doc_name
                if (array_sort[position].loc2 == "" && java.lang.Float.parseFloat(km) < java.lang.Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "RE_REG_KM", "5"))) {
                    dr_id_index = "2"
                } else if (array_sort[position].loc3 == "" && java.lang.Float.parseFloat(km) < java.lang.Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "RE_REG_KM", "5"))) {
                    dr_id_index = "3"
                }

                drname.text = "---Select---"
                dr_id = ""
                doc_name = ""

            }  else {


                mdairyCall = mDairyCall(head)
                        .setId(model.id)
                        .setName(model.name)
                        .setArea(model.area)
                        .setDcr_id(MyCustumApplication.getInstance().user.dcrId)
                        .setDcr_date(MyCustumApplication.getInstance().user.dcrDate)
                        .setRef_latlong(model.reF_LAT_LONG)
                        .setLatLong(arrayAdapter.latLong)
                        .setBattery(MyCustumApplication.getInstance().user.battery) as mDairyCall

                latLong = arrayAdapter.latLong
                ref_latLong = array_sort[position].reF_LAT_LONG
                UpadteUI_If_Called()
            }
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence,
                                           start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textlength = editText.text.length
                array_sort.clear()
                for (i in TitleName.indices) {
                    if (textlength <= TitleName[i].name.length) {

                        if (TitleName[i].name.toLowerCase().contains(editText.text.toString().toLowerCase().trim { it <= ' ' })) {
                            array_sort.add(TitleName[i])
                        }
                    }
                }
                try {
                    listview.adapter = SpinAdapter_new(this@DairyCall, R.layout.spin_row, array_sort, showRegistrtion)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })


        editText.setOnTouchListener(
        object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                        // your action here
                        myalertDialog!!.dismiss()
                        if ((!customVariablesAndMethod.checkIfCallLocationValid(context, true, false))!!) {
                            customVariablesAndMethod.msgBox(context, "Verifing Your Location")
                            IsRefreshedClicked = true
                            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                                    IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE))
                        } else {
                            //Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL)
                            service.DownloadAll(context, object : Response {
                                override fun onSuccess(bundle: Bundle) {
                                    onDownloadAllResponse()
                                }

                                override fun onError(s: String, s1: String) {
                                    AppAlert.getInstance().getAlert(context, s, s1)
                                }
                            })
                        }

                        val vbr = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                        vbr!!.vibrate(100)
                        return true
                    }
                }
                return false
            }
        })
        myalertDialog = myDialog.show()
    }

    private val mLocationUpdated = object : BroadcastReceiver() {
        override fun onReceive(contex: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(Const.LBM_EVENT_LOCATION_UPDATE)

            if (IsRefreshedClicked) {
                //Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL)
                service.DownloadAll(context, object : Response {
                    override fun onSuccess(bundle: Bundle) {
                        onDownloadAllResponse()
                    }

                    override fun onError(s: String, s1: String) {
                        AppAlert.getInstance().getAlert(context, s, s1)
                    }
                })
            } else {
                submitDoctor(true)
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)

        }
    }

    private fun UpadteUI_If_Called(){

        doctor_list = cbohelp.getCallDetail(head, dr_id, "0")
        if (doctor_list["id"]!!.contains(dr_id)) {



            if (doctor_list["time"]!!.get(0) != "") {
                remark.visibility = View.VISIBLE
                remark.text = "You have visited today"
            } else {
                remark.visibility = View.GONE
            }

            if (doctor_list["workwith"]!!.get(0) != "") {
                work_with_name = doctor_list["workwith"]!!.get(0)
                workwithdr.setText(work_with_name)

            } else {
                work_with_name = ""
                workwithdr.setText(work_with_name)
            }



            if (doctor_list.get("sample_name")!!.get(0) != "") {
//                val sample_name1 = doctor_list.get("sample_name")!!.get(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//                val sample_qty1 = doctor_list.get("sample_qty")!!.get(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//                val sample_pob1 = doctor_list.get("sample_pob")!!.get(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                sample_name = doctor_list.get("sample_name")!!.get(0)
                sample_sample = doctor_list.get("sample_qty")!!.get(0)
                sample_pob = doctor_list.get("sample_pob")!!.get(0)


            }else{
                sample_name = ""
                sample_sample = ""
                sample_pob = ""


            }

            sample_name_previous = sample_name
            sample_sample_previous = sample_sample
            sample_pob_previous = sample_pob


            init(sample_name.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), sample_sample.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), sample_pob.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())

            if (doctor_list.get("gift_name")!!.get(0) != "") {
//                val gift_name1 = doctor_list.get("gift_name")!!.get(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//                val gift_qty1 = doctor_list.get("gift_qty")!!.get(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                gift_name = doctor_list.get("gift_name")!!.get(0)
                gift_qty = doctor_list.get("gift_qty")!!.get(0)


            }else{
                gift_name = ""
                gift_qty = ""
            }

            gift_name_previous = gift_name
            gift_qty_previous = gift_name




            init_gift(doc_detail, gift_name.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), gift_qty.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())

            if (doctor_list["remark"]!!.get(0) != "") {
                var remark = doctor_list["remark"]?.get(0)
                if (remark!!.contains("\u20B9"))
                    mdairyCall?.setPOBAmt(remark.split("\\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])
                    remark = remark.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

                if (remark_list.contains(remark)) {
                    btn_remark.text = remark
                    dr_remark.visibility = View.GONE
                } else {
                    btn_remark.text = remark_list[remark_list.size - 1]
                    dr_remark.visibility = View.VISIBLE
                }



                /*dr_remarkLayout.visibility = View.VISIBLE
                dr_remark.visibility = View.GONE

                POB_layout.visibility = View.GONE
                product_layout.visibility = View.GONE*/

                dr_remark.setText(remark)


                btn_intrested.text = if(gift_name.isEmpty() && sample_name.isEmpty()) intersed_list[2] else intersed_list[1]

              /*  sample_name = ""
                sample_sample = ""
                sample_pob = ""
                gift_name = ""
                gift_qty = ""
                stk.removeAllViews()
                doc_detail.removeAllViews()
                IsInterested.setChecked(false)*/
            } else {
                /*dr_remarkLayout.visibility = View.GONE
                dr_remark.visibility = View.GONE

                POB_layout.visibility = View.GONE
                product_layout.visibility = View.VISIBLE*/
                dr_remark.setText("")
                btn_remark.text = "---Select Remark---"
                btn_intrested.text = intersed_list[1]

                /*dr_remark.visibility = View.GONE
                IsInterested.setChecked(true)*/
            }

            SetInterested()
            submit.setText("Update $head")
            //save.setText("Update Dairy")
        }else{
            work_with_name = ""
            workwithdr.setText(work_with_name)

            sample_name = ""
            sample_sample = ""
            sample_pob = ""

            init(sample_name.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), sample_sample.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), sample_pob.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())


            gift_name = ""
            gift_qty = ""
            init_gift(doc_detail, gift_name.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray(), gift_qty.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())

            dr_remark.setText("")
            btn_remark.text = "---Select Remark---"
            btn_intrested.text = intersed_list[1]
            SetInterested()

            submit.setText("ADD $head")
        }


        mdairyCall?.setInterested(btn_intrested.text .toString());
        mdairyCall?.setRemark(dr_remark.text.toString());
        mdairyCall?.setWorkwith(work_with_name)
        mdairyCall!!.setGift_name_Arr(gift_name)
                .setGift_qty_Arr(gift_qty)
                .setSample_name_Arr(sample_name)
                .setSample_pob_Arr(sample_pob)
                .setSample_qty_Arr(sample_sample)

        //Doc_Detail(array_sort[position].`class`, array_sort[position].potencY_AMT, array_sort[position].lastVisited, AREA)
    }


    private fun init(sample_name: Array<String>, sample_qty: Array<String>, sample_pob: Array<String>) {
        //TableLayout stk= (TableLayout) findViewById(R.id.promotion);
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        val tbrow0 = TableRow(context)
        tbrow0.setBackgroundColor(-0xeda978)
        val params = TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f)
        val tv0 = TextView(context)
        tv0.text = "Product"
        tv0.setPadding(5, 5, 5, 0)
        tv0.setTextColor(Color.WHITE)
        tv0.setTypeface(null, Typeface.BOLD)
        tv0.layoutParams = params
        tbrow0.addView(tv0)
        val tv1 = TextView(context)
        tv1.text = " Sample "
        tv1.setPadding(5, 5, 5, 0)
        tv1.setTextColor(Color.WHITE)
        tv1.setTypeface(null, Typeface.BOLD)
        tbrow0.addView(tv1)
        val tv2 = TextView(context)
        tv2.setPadding(5, 5, 5, 0)
        tv2.text = " POB "
        tv2.setTextColor(Color.WHITE)
        tv2.setTypeface(null, Typeface.BOLD)
        tbrow0.addView(tv2)
        stk.removeAllViews()
        stk.addView(tbrow0)
        for (i in sample_name.indices) {
            val tbrow = TableRow(context)
            val t1v = TextView(context)
            t1v.text = sample_name[i]
            t1v.setPadding(5, 5, 5, 0)
            t1v.setTextColor(Color.BLACK)
            t1v.layoutParams = params
            tbrow.addView(t1v)
            val t2v = TextView(context)
            t2v.text = sample_qty[i]
            t2v.setPadding(5, 5, 5, 0)
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.addView(t2v)
            val t3v = TextView(context)
            t3v.text = sample_pob[i]
            t3v.setPadding(5, 5, 5, 0)
            t3v.setTextColor(Color.BLACK)
            t3v.gravity = Gravity.CENTER
            tbrow.addView(t3v)
            stk.addView(tbrow)
        }


        if (sample_name.size == 0 ) {
            stk.removeAllViews()
        }

    }


    private fun init_gift(stk1: TableLayout, gift_name: Array<String>, gift_qty: Array<String>) {
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        val tbrow0 = TableRow(context)
        tbrow0.setBackgroundColor(-0xeda978)
        val params = TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f)
        val tv0 = TextView(context)
        tv0.text = "Gift"
        tv0.setPadding(5, 5, 5, 0)
        tv0.setTextColor(Color.WHITE)
        tv0.setTypeface(null, Typeface.BOLD)
        tv0.layoutParams = params
        tbrow0.addView(tv0)
        val tv1 = TextView(context)
        tv1.text = " Qty. "
        tv1.setPadding(5, 5, 5, 0)
        tv1.setTextColor(Color.WHITE)
        tv1.setTypeface(null, Typeface.BOLD)
        tbrow0.addView(tv1)
        stk1.removeAllViews()
        stk1.addView(tbrow0)
        for (i in gift_name.indices) {
            val tbrow = TableRow(context)
            val t1v = TextView(context)
            t1v.text = gift_name[i]
            t1v.setPadding(5, 5, 5, 0)
            t1v.setTextColor(Color.BLACK)
            t1v.layoutParams = params
            tbrow.addView(t1v)
            val t2v = TextView(context)
            t2v.text = gift_qty[i]
            t2v.setPadding(5, 5, 5, 0)
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.addView(t2v)
            stk1.addView(tbrow)
        }
        if (gift_name.size == 0) {
            stk1.removeAllViews()
        }

    }

    private fun onClickDrCallRemark() {
        if (!dr_id.equals("")) {
            val myDialog = AlertDialog.Builder(this@DairyCall)
            val listview = ListView(this@DairyCall)
            val layout = LinearLayout(this@DairyCall)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(listview)
            myDialog.setView(layout)
            //ArrayAdapter arrayAdapter = new ArrayAdapter(mDrCall.this, R.layout.spin_row, cbohelp.get_Doctor_Call_Remark());
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, remark_list)
            listview.adapter = arrayAdapter
            listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                myalertDialog?.dismiss()
                btn_remark.text = remark_list[position]
                if (remark_list[position].equals("other", ignoreCase = true)) {
                    dr_remark.setText("")
                    dr_remark.visibility = View.VISIBLE
                } else {
                    dr_remark.setText(remark_list[position])
                    dr_remark.visibility = View.GONE
                }
            }

            myalertDialog = myDialog.show()
        }else{
            customVariablesAndMethod.getAlert(context,"Select !!!","Please select a Dairy....")
        }

    }


    private fun onClickDrCallIntersed() {
        if (!dr_id.equals("")) {
            val myDialog = AlertDialog.Builder(this@DairyCall)
            val listview = ListView(this@DairyCall)
            val layout = LinearLayout(this@DairyCall)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(listview)
            myDialog.setView(layout)

            //ArrayAdapter arrayAdapter = new ArrayAdapter(mDrCall.this, R.layout.spin_row, cbohelp.get_Doctor_Call_Remark());
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, intersed_list)
            listview.adapter = arrayAdapter
            listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                myalertDialog?.dismiss()
                btn_intrested.text = intersed_list[position]
                SetInterested()
            }

            myalertDialog = myDialog.show()
        }else{
            customVariablesAndMethod.getAlert(context,"Select !!!","Please select a Dairy....")
        }

    }


    private fun SetInterested(){
        if (btn_intrested.getText().toString().equals(intersed_list[1],true)) {
            dr_remarkLayout.visibility = View.VISIBLE
            dr_remark_dp.visibility = View.GONE
            dr_remark.visibility = View.VISIBLE

            POB_layout.visibility = View.GONE
            product_layout.visibility = View.VISIBLE

            //dr_remark.setText("")
        } else if(btn_intrested.getText().toString().equals(intersed_list[2],true)) {
            dr_remarkLayout.visibility = View.VISIBLE
            dr_remark_dp.visibility = View.VISIBLE
            dr_remark.visibility = View.GONE

            POB_layout.visibility = View.GONE
            product_layout.visibility = View.GONE
            sample_name = ""
            sample_sample = ""
            sample_pob = ""
            gift_name = ""
            gift_qty = ""
            stk.removeAllViews()
            doc_detail.removeAllViews()
            //IsInterested.setChecked(false)
        }else{
            dr_remark.setText("")
            dr_remarkLayout.visibility = View.GONE
            dr_remark_dp.visibility = View.GONE
            dr_remark.visibility = View.GONE

            POB_layout.visibility = View.GONE
            product_layout.visibility = View.GONE

            sample_name = ""
            sample_sample = ""
            sample_pob = ""
            gift_name = ""
            gift_qty = ""
            stk.removeAllViews()
            doc_detail.removeAllViews()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            0 ->{
                if (resultCode == Activity.RESULT_OK){
                    val b1 = data!!.extras
                    name = b1!!.getString("val")//id
                    name2 = b1.getString("val2")//score or pob
                    result = b1.getDouble("resultpob")
                    sample = b1.getString("sampleQty")// sample
                    val f = DecimalFormat("#.00")
                    resultList = b1.getString("resultList")
                    val result3 = f.format(result)
                    pob.setText(result3)

                    sample_name = resultList
                    sample_pob = name2
                    sample_sample = sample

                    val sample_name1 = resultList.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val sample_qty1 = sample.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val sample_pob1 = name2.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    init(sample_name1, sample_qty1, sample_pob1)
                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val b1 = data!!.extras
                    name3 = b1!!.getString("giftid")
                    name4 = b1.getString("giftqan")
                    //if (b1.getString("giftname") != "") {
                        val gift_name1 = b1.getString("giftname")!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val gift_qty1 = b1.getString("giftqan")!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        gift_name = b1.getString("giftname")
                        gift_qty = b1.getString("giftqan")

                        init_gift(doc_detail, gift_name1, gift_qty1)
                   // }
                }
            }

            WORK_WITH_DIALOG -> {
                val b1 = data!!.extras
                work_with_name = b1!!.getString("workwith_name")
                work_with_id = b1.getString("workwith_id")
                workwithdr.setText("" + work_with_name)
            }
            Report_Registration.REQUEST_CAMERA -> if (resultCode == Activity.RESULT_OK) {


                val file1 = File(Environment.getExternalStorageDirectory().toString() + File.separator + "CBO" + File.separator + alertdFragment.filename)





                if (dr_id_index != "" && cbohelp.getCallDetail(head, dr_id_reg, "0")["time"]!!.get(0) == "") {
                    cbohelp.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON), dr_id_reg, "D", dr_id_index)
                    dr_id = dr_id_reg
                    doc_name = dr_name_reg
                    drname.text = doc_name

                    if (Custom_Variables_And_Method.internetConneted(context)) {
                        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, IntentFilter("SyncComplete"))
                        Sync_service.ReplyYN = "Y"
                        progress1?.setMessage("Please Wait..\n" +
                                dr_name_reg + " is being Registered")
                        progress1?.setCancelable(false)
                        progress1?.show()
                        startService(Intent(context, Sync_service::class.java))
                    } else {
                        customVariablesAndMethod.getAlert(context, "Registered", "$dr_name_reg Successfully Re-Registered($dr_id_index)")

                    }
                } else if (file1.exists() && Custom_Variables_And_Method.internetConneted(context)) {
                    val currentBestLocation = customVariablesAndMethod.getObject(context, "currentBestLocation", Location::class.java)
                    SendAttachment(context as Activity).execute(Custom_Variables_And_Method.COMPANY_CODE + ": Out of Range Error report", context.resources.getString(R.string.app_name) + "\n Company Code :" + Custom_Variables_And_Method.COMPANY_CODE + "\n DCR ID :" + Custom_Variables_And_Method.DCR_ID + "\n PA ID : " + Custom_Variables_And_Method.PA_ID + "\n App version : " + Custom_Variables_And_Method.VERSION + "\n massege : " + alertdFragment.Alertmassege +
                            "\nLocation-timestamp : " + currentBestLocation!!.time + "\nLocation-Lat : " + currentBestLocation.latitude +
                            "\nLocation-long : " + currentBestLocation.longitude + "\n time : " + customVariablesAndMethod.currentTime(context) + "\nlatlong : " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON), alertdFragment.compressImage(file1))

                } else {
                    customVariablesAndMethod.Connect_to_Internet_Msg(context)
                }


                /**/


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(context,
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show()
            } else {
                // failed to capture image
                Toast.makeText(context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show()
            }
        }


    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(contex: Context, intent: Intent) {
            if (progress1 != null) {
                progress1?.dismiss()
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            if (intent.getStringExtra("message") == "Y") {
                customVariablesAndMethod.getAlert(context, "Registered", "$dr_name_reg Successfully Re-Registered($dr_id_index)")
            }

        }
    }



}
