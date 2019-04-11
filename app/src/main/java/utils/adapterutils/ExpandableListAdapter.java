package utils.adapterutils;



import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils_new.Custom_Variables_And_Method;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cboDbHelper;
    Boolean clicked=false;
    Float net_value=0f;
    ExpandableListView listView;
    ImageView attach_img;
    //private List<Integer> visible_status;
    private HashMap<String, HashMap<String, ArrayList<String>>> _listDataChild;

    private ExpandableListAdapter.Summary_interface summary_interface;

    public interface Summary_interface {
        public void Edit_Call( String Dr_id,String Dr_name);
        public void delete_Call(String Dr_id,String Dr_name);
    }

    public ExpandableListAdapter(ExpandableListView listView, Context context, ArrayList<String> listDataHeader,
                                 HashMap<String, HashMap<String, ArrayList<String>>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.listView=listView;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);
        cboDbHelper=customVariablesAndMethod.get_cbo_db_instance();

        //this.visible_status = visible_status;

    }
    private String TeniviaMenuName(){
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context,"Tenivia_NOT_REQUIRED").equals("N")) {
            return cboDbHelper.getMenu("DCR", "D_DR_RX").get("D_DR_RX");
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context,"Rx_NOT_REQUIRED").equals("N")) {
            return cboDbHelper.getMenu("DCR", "D_RX_GEN").get("D_RX_GEN");
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context,"Rx_NA_NOT_REQUIRED").equals("N")) {
            return cboDbHelper.getMenu("DCR", "D_RX_GEN_NA").get("D_RX_GEN_NA");
        }
        return "";
    }

    @Override
    public  HashMap<String, ArrayList<String>> getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HashMap<String, ArrayList<String>> childText = (HashMap<String, ArrayList<String>>) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.summary_child_item_view, null);
        }

        TextView txtList_name = (TextView) convertView.findViewById(R.id.name);
        TextView txtList_time = (TextView) convertView.findViewById(R.id.time);
        ImageView call_time_img = (ImageView) convertView.findViewById(R.id.call_time_img);
        ImageView edit=(ImageView) convertView.findViewById(R.id.call_edit);
        ImageView delete=(ImageView) convertView.findViewById(R.id.call_delete);

        LinearLayout remark_layout=(LinearLayout) convertView.findViewById(R.id.remark_layout);
        LinearLayout attach=(LinearLayout) convertView.findViewById(R.id.attach);
        TextView remark_text = (TextView) convertView.findViewById(R.id.remark_text);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + childText.get("attach").get(childPosition));
                if(file2.exists() && !childText.get("attach").get(childPosition).equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);

                   // builder.setPositiveButton("OK", null);
                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater =(LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogLayout = inflater.inflate(R.layout.attachment_pop_up, null);
                    attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
                    ImageView close= (ImageView) dialogLayout.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                    previewCapturedImage(file2.getPath());
                    dialog.setView(dialogLayout);
                    //dialog.setTitle("Attachment");
                    dialog.show();
                }
            }
        });

        if ((childText.get("name").size()>0 && !childText.get("name").get(0).equals("Yet to make your first Call"))) {
            txtList_name.setText(childText.get("name").get(childPosition));
            txtList_time.setText(childText.get("time").get(childPosition));
            txtList_name.setTypeface(null, Typeface.BOLD);
            call_time_img.setVisibility(View.VISIBLE);
            int fontColor = 0xff125688;
            try {
                if(childText.get("color").get(childPosition).equalsIgnoreCase("Y")){
                   String DR_COLOR = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context,"DR_COLOR","");
                    if(!DR_COLOR.trim().equals(""))
                        fontColor = Color.parseColor(DR_COLOR);
                }

            }catch ( Exception e){
                fontColor = 0xff125688;
            }
            txtList_name.setTextColor(fontColor);

            if (childText.get("show_edit_delete")!=null && childText.get("show_edit_delete").get(childPosition).equals("1")){
                summary_interface= (Summary_interface) _context ;
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }

        }else{
            txtList_name.setText(childText.get("name").get(childPosition));
            txtList_time.setText(childText.get("time").get(childPosition));
            call_time_img.setVisibility(View.GONE);
            txtList_name.setTypeface(null, Typeface.NORMAL);
            txtList_name.setTextColor(Color.RED);
        }

       // if(_listDataHeader.get(groupPosition).equals("Expenses")){
        if(_listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP"))){
            txtList_time.setText("\u20B9 "+childText.get("time").get(childPosition));
            call_time_img.setVisibility(View.GONE);
        }

        TableLayout stk = (TableLayout) convertView.findViewById(R.id.promotion);
        TableLayout gift_layout = (TableLayout) convertView.findViewById(R.id.gift);
        TableLayout work_with_layout = (TableLayout) convertView.findViewById(R.id.work_with);
        TableLayout dr_detail_layout = (TableLayout) convertView.findViewById(R.id.dr_detail);
        TextView expend = (TextView) convertView.findViewById(R.id.expend);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_interface.delete_Call(childText.get("id").get(childPosition),childText.get("name").get(childPosition));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_interface.Edit_Call(childText.get("id").get(childPosition),childText.get("name").get(childPosition));
            }
        });

        //if( _listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && !_listDataHeader.get(groupPosition).equals("Non-Listed Call")&& !_listDataHeader.get(groupPosition).equals("Expenses")&& !_listDataHeader.get(groupPosition).equals("Appraisal")) {
        if( _listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && !_listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_NLC_CALL").get("D_NLC_CALL"))&& !_listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP"))&& !_listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_AP").get("D_AP"))) {
            if (!childText.get("sample_name").get(childPosition).equals("") ) {
                String[] sample_name= childText.get("sample_name").get(childPosition).split(",");
                String[] sample_qty= childText.get("sample_qty").get(childPosition).split(",");
                String[] sample_pob= childText.get("sample_pob").get(childPosition).split(",");
                String[] sample_noc= childText.get("sample_noc").get(childPosition).split(",");
                init(stk,sample_name,sample_qty,sample_pob,sample_noc,_listDataHeader.get(groupPosition));
            }
            if (!childText.get("gift_name").get(childPosition).equals("")) {
                String[] gift_name= childText.get("gift_name").get(childPosition).split(",");
                String[] gift_qty= childText.get("gift_qty").get(childPosition).split(",");
                if (!_listDataHeader.get(groupPosition).equals(TeniviaMenuName())) {
                    init_gift(gift_layout, gift_name, gift_qty, "Gift", " Qty. ");
                }else{
                    init_gift(gift_layout, gift_name, gift_qty, "", "");
                }
            }

            String sample_remark= childText.get("remark").get(childPosition);
            remark_text.setText(sample_remark);
            if(sample_remark.equals("")){
                remark_layout.setVisibility(View.GONE);
            }else{
                remark_layout.setVisibility(View.VISIBLE);
            }

            if (childText.get("workwith")!=null && !childText.get("workwith").get(childPosition).equals("")){
                String Title = "Work-with";
                if(_listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_DAIRY").get("D_DAIRY")) ||
                        _listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_POULTRY").get("D_POULTRY"))){
                    Title = "Contact person";
                }
                init_work_with(work_with_layout,childText.get("workwith").get(childPosition).split(","),Title);
            }else {
                work_with_layout.removeAllViews();
            }
            if (childText.get("class")!=null && childText.get("potential")!=null && childText.get("area")!=null ) {
                Doc_Detail(dr_detail_layout, childText.get("class").get(childPosition), childText.get("potential").get(childPosition),"", childText.get("area").get(childPosition),childText.get("dr_crm").get(childPosition),childText.get("dr_camp_group").get(childPosition),"Class","Potential","Area","Dr CRM","Campaign Group",1,1);
            }

            if (childText.get("attach") != null) {
                if (childText.get("attach").get(childPosition).equals("")) {
                    attach.setVisibility(View.GONE);
                } else {
                    attach.setVisibility(View.VISIBLE);
                }
            }

            expend.setText("-");
            expend.setVisibility(View.VISIBLE);

        //}else if (_listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && _listDataHeader.get(groupPosition).equals("Appraisal")){
        }else if (_listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && _listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_AP").get("D_AP"))){
            stk.removeAllViews();
            gift_layout.removeAllViews();
            expend.setText("-");
            expend.setVisibility(View.VISIBLE);

            String sample_remark= childText.get("remark").get(childPosition);
            remark_text.setText(sample_remark);
            if(sample_remark.equals("")){
                remark_layout.setVisibility(View.GONE);
            }else{
                remark_layout.setVisibility(View.GONE);

                TableRow tbrow00= new TableRow(_context);
                tbrow00.setPadding(35,2,35,2);
                tbrow00.setBackgroundColor(0xff125688);
                stk.addView(tbrow00);

                TableRow tbrow2 = new TableRow(_context);
                //tbrow0.setBackgroundColor(0xff125688);
                TextView tv02 = new TextView(_context);
                tv02.setText(childText.get("remark").get(childPosition));
                tv02.setPadding(5, 5, 5, 0);
                tv02.setTextColor(Color.BLACK);
                tv02.setTypeface(null, Typeface.NORMAL);
                tbrow2.addView(tv02);
                stk.addView(tbrow2);

            }

            if (!childText.get("gift_name").get(childPosition).equals("")) {
                String[] gift_name= childText.get("gift_name").get(childPosition).replace("^",",").split(",");
                String[] gift_qty= childText.get("gift_qty").get(childPosition).replace("^",",").split(",");
                init_gift(dr_detail_layout,gift_name,gift_qty," Particulars"," Grade ");
            }


        //}else if (_listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && _listDataHeader.get(groupPosition).equals("Non-Listed Call") ){
        }else if (_listDataChild.get(_listDataHeader.get(groupPosition)).get("visible_status").get(childPosition).equals("1") && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") && _listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_NLC_CALL").get("D_NLC_CALL")) ){
            stk.removeAllViews();
            gift_layout.removeAllViews();

            TableRow tbrow00= new TableRow(_context);
            tbrow00.setPadding(35,2,35,2);
            tbrow00.setBackgroundColor(0xff125688);
            //stk.addView(tbrow00);

           if (childText.get("sample_name").get(childPosition).contains("(c&f)")) {
                TableRow tbrow1 = new TableRow(_context);
                //tbrow0.setBackgroundColor(0xff125688);
                TextView tv01 = new TextView(_context);
                tv01.setText("Contact Person :- "+childText.get("sample_name").get(childPosition).substring(0,childText.get("sample_name").get(childPosition).lastIndexOf("(")));
                tv01.setPadding(5, 5, 5, 0);
                tv01.setTextSize(10);
                tv01.setTextColor(Color.BLACK);
                tv01.setTypeface(null, Typeface.NORMAL);
                tbrow1.addView(tv01);
                stk.addView(tbrow1);
            }else   if (!childText.get("sample_name").get(childPosition).contains("()")) {
               TableRow tbrow1 = new TableRow(_context);
               //tbrow0.setBackgroundColor(0xff125688);
               TextView tv01 = new TextView(_context);
               tv01.setText("Qlf. (Spl.) :- "+childText.get("sample_name").get(childPosition));
               tv01.setPadding(5, 5, 5, 0);
               tv01.setTextSize(10);
               tv01.setTextColor(Color.BLACK);
               tv01.setTypeface(null, Typeface.NORMAL);
               tbrow1.addView(tv01);
               stk.addView(tbrow1);
           }
            TableRow tbrow2 = new TableRow(_context);
            //tbrow0.setBackgroundColor(0xff125688);
            TextView tv02 = new TextView(_context);
            tv02.setText("Address :- "+childText.get("sample_qty").get(childPosition));
            tv02.setTextSize(10);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.NORMAL);
            tbrow2.addView(tv02);
            stk.addView(tbrow2);


            TableRow tbrow0 = new TableRow(_context);
            TextView tv1 = new TextView(_context);
            tv1.setText("Mob. :-"+childText.get("sample_pob").get(childPosition));
            tv1.setTextSize(10);
            tv1.setPadding(5, 5, 5, 0);
            tv1.setTextColor(Color.BLACK);
            tv1.setTypeface(null, Typeface.NORMAL);
            tbrow0.addView(tv1);
            stk.addView(tbrow0);

            expend.setText("-");
            expend.setVisibility(View.VISIBLE);

            String sample_remark= childText.get("remark").get(childPosition);
            remark_text.setText(sample_remark);
            if(sample_remark.equals("")){
                remark_layout.setVisibility(View.GONE);
            }else{
                remark_layout.setVisibility(View.VISIBLE);
            }

            if (childText.get("class")!=null && childText.get("potential")!=null && childText.get("area")!=null && !childText.get("sample_name").get(childPosition).contains("(c&f)")) {
                Doc_Detail(dr_detail_layout, childText.get("class").get(childPosition), childText.get("potential").get(childPosition),"", childText.get("area").get(childPosition),"","","Class","Potential","Area","","",1,1);
            }else{
                stk.removeAllViews();
                Doc_Detail(stk, childText.get("sample_qty").get(childPosition), childText.get("sample_pob").get(childPosition),"", childText.get("sample_name").get(childPosition).substring(0,childText.get("sample_name").get(childPosition).lastIndexOf("(")),"","","Address","Mob.","Contact Person","","",1,0);
                Doc_Detail(gift_layout, childText.get("class").get(childPosition), childText.get("potential").get(childPosition),"", childText.get("area").get(childPosition),"","","DL No.","TIN No.","Business Start Date","","",0,1);
                if( childText.get("attach").get(childPosition).equals("")){
                    attach.setVisibility(View.GONE);
                }else{
                    attach.setVisibility(View.VISIBLE);
                }
            }

        //}else if (getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") || _listDataHeader.get(groupPosition).equals("Expenses")) {
        }else if (getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call") || _listDataHeader.get(groupPosition).equals(cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP"))) {
            expend.setVisibility(View.GONE);

            String sample_remark= childText.get("remark").get(childPosition);
            remark_text.setText(sample_remark);
            if(sample_remark.equals("")){
                remark_layout.setVisibility(View.GONE);
            }else{
                remark_layout.setVisibility(View.VISIBLE);
            }

            if( childText.get("attach").get(childPosition).equals("")){
                attach.setVisibility(View.GONE);
            }else{
                attach.setVisibility(View.VISIBLE);
            }

            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);

            stk.removeAllViews();
            gift_layout.removeAllViews();
            work_with_layout.removeAllViews();
            dr_detail_layout.removeAllViews();
        }else{
            remark_layout.setVisibility(View.GONE);
            stk.removeAllViews();
            gift_layout.removeAllViews();
            work_with_layout.removeAllViews();
            dr_detail_layout.removeAllViews();
            expend.setText("+");
            expend.setVisibility(View.VISIBLE);
            attach.setVisibility(View.GONE);
        }

        if (isLastChild) {
            convertView.setPadding(0, 0, 0, 35);
        }else {
            convertView.setPadding(0, 0, 0, 0);
        }
        return convertView;
    }


    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //options.inSampleSize = 10;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            attach_img.setVisibility(View.VISIBLE);
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get("name").size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.header);
        TextView count = (TextView) convertView.findViewById(R.id.call_count);
        TextView expend = (TextView) convertView.findViewById(R.id.expend);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        int a= getChildrenCount(groupPosition);
        count.setText(""+a);
        if (isExpanded){
            expend.setText("-");
        }else{
            expend.setText("+");
        }

        final TableLayout DA_layout = (TableLayout) convertView.findViewById(R.id.DA_layout);
        //if(!headerTitle.equals("Expenses")) {
        if(!headerTitle.equals(cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP"))) {
            if (a > 0 && !getChild(groupPosition, 0).get("name").get(0).equals("Yet to make your first Call")) {
                count.setVisibility(View.VISIBLE);
            } else {
                count.setVisibility(View.GONE);
            }
           DA_layout.removeAllViews();
           DA_layout.setBackgroundColor(Color.TRANSPARENT);
        }else{
            if (a > 0 && !getChild(groupPosition, 0).get("name").get(0).equals("No extra expenses")) {
                count.setVisibility(View.VISIBLE);
                if (listView.isGroupExpanded(groupPosition))
                    count.setText(String.format("%d", a - 1));

            } else {
                count.setVisibility(View.GONE);
            }

           /* if (clicked){
                expence(groupPosition,false);
                init_DA_type(DA_layout,groupPosition);
            }else{
                init_DA_type(DA_layout,groupPosition);
                clicked=true;
            }*/
            init_DA_type(DA_layout,groupPosition);

            DA_layout.setBackgroundResource(R.drawable.custom_square_transparent_bg);
            DA_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expence(groupPosition,true);
                    init_DA_type(DA_layout,groupPosition);
                }
            });
        }
        return convertView;
    }

    private void expence(int groupPosition,boolean expend){
        String exp=cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP");
        if (!listView.isGroupExpanded(groupPosition)){
            int length=getChildrenCount(groupPosition);
            _listDataChild.get(exp).get("name").add(length,"Total Expenses");
            _listDataChild.get(exp).get("time").add(""+net_value);
            _listDataChild.get(exp).get("sample_name").add(length,"");
            _listDataChild.get(exp).get("sample_qty").add(length,"");
            _listDataChild.get(exp).get("gift_name").add(length,"");
            _listDataChild.get(exp).get("gift_qty").add(length,"");
            _listDataChild.get(exp).get("sample_pob").add(length,"");
            _listDataChild.get(exp).get("visible_status").add(length,"0");
            _listDataChild.get(exp).get("remark").add(length,"");
            _listDataChild.get(exp).get("show_edit_delete").add(length,"0");

            _listDataChild.get(exp).get("class").add(length,"");
            _listDataChild.get(exp).get("potential").add(length,"");
            _listDataChild.get(exp).get("area").add(length,"");
            _listDataChild.get(exp).get("workwith").add(length,"");
            _listDataChild.get(exp).get("attach").add(length,"");

            if (expend) {
                listView.expandGroup(groupPosition);
            }
        }else{
            int length=getChildrenCount(groupPosition)-1;
            _listDataChild.get(exp).get("name").remove(length);
            _listDataChild.get(exp).get("time").remove(length);
            _listDataChild.get(exp).get("sample_name").remove(length);
            _listDataChild.get(exp).get("sample_qty").remove(length);
            _listDataChild.get(exp).get("gift_name").remove(length);
            _listDataChild.get(exp).get("gift_qty").remove(length);
            _listDataChild.get(exp).get("sample_pob").remove(length);
            _listDataChild.get(exp).get("visible_status").remove(length);
            _listDataChild.get(exp).get("remark").remove(length);
            _listDataChild.get(exp).get("show_edit_delete").remove(length);
            _listDataChild.get(exp).get("class").remove(length);
            _listDataChild.get(exp).get("potential").remove(length);
            _listDataChild.get(exp).get("area").remove(length);
            _listDataChild.get(exp).get("workwith").remove(length);
            _listDataChild.get(exp).get("attach").remove(length);

            if (expend) {
                listView.collapseGroup(groupPosition);
            }
        }


    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void init(TableLayout stk1, String[] sample_name, String[] sample_qty, String[] sample_pob,String[] sample_noc,String headType) {

        Boolean showNOC=true;
        if (!headType.equals(cboDbHelper.getMenu("DCR", "D_DRCALL").get("D_DRCALL")) || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context,"NOC_HEAD","").isEmpty()){
            showNOC=false;
        }

        TableLayout stk=stk1;
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(_context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(_context);
        tv0.setText("Product");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(_context);
        tv1.setText(" Sample ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(_context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setText(" POB ");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        if (showNOC) {
            TextView tv3 = new TextView(_context);
            tv3.setPadding(5, 5, 5, 0);
            tv3.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "NOC_HEAD"));
            tv3.setTextColor(Color.WHITE);
            tv3.setTypeface(null, Typeface.BOLD);
            tbrow0.addView(tv3);
        }
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < sample_name.length; i++) {
            TableRow tbrow = new TableRow(_context);
            TextView t1v = new TextView(_context);
            t1v.setText(sample_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(_context);
            t2v.setText(sample_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(_context);
            t3v.setText(sample_pob[i]);
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            if (showNOC) {
                TextView t4v = new TextView(_context);
                t4v.setText(sample_noc[i]);
                t4v.setPadding(5, 5, 5, 0);
                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);
            }
            stk.addView(tbrow);
        }
        if(sample_name.length==1 && sample_name[0].equals("0")){
            stk.removeAllViews();
        }

    }

    private void init_gift(TableLayout stk1, String[] gift_name, String[] gift_qty,String Left_Head, String Right_Head) {
        TableLayout stk=stk1;
        TableRow tbrow0 = new TableRow(_context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        if (Left_Head.equals("") && Right_Head.equals("")){
            tbrow0.setBackgroundColor(0xff125688);
            TextView tv0 = new TextView(_context);
            tv0.setLayoutParams(params);
            tv0.setHeight(2);
            tbrow0.addView(tv0);
        }else {

            tbrow0.setBackgroundColor(0xff125688);
            TextView tv0 = new TextView(_context);
            tv0.setText(Left_Head);
            tv0.setPadding(5, 5, 5, 0);
            tv0.setTextColor(Color.WHITE);
            tv0.setTypeface(null, Typeface.BOLD);
            tv0.setLayoutParams(params);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(_context);
            tv1.setText(Right_Head);
            tv1.setPadding(5, 5, 5, 0);
            tv1.setTextColor(Color.WHITE);
            tv1.setTypeface(null, Typeface.BOLD);
            tbrow0.addView(tv1);
        }
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < gift_name.length; i++) {
            TableRow tbrow = new TableRow(_context);
            TextView t1v = new TextView(_context);
            t1v.setText(gift_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(_context);
            t2v.setText(gift_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            stk.addView(tbrow);
        }

        if(gift_name.length==1 && gift_name[0].equals("0")){
            stk.removeAllViews();
        }

    }

    private void init_DA_type(TableLayout stk,int groupPosition) {

        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(_context);
        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(_context);
        tv0.setText("DA. Type");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(_context);
        tv1.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "DA_TYPE"));
        tv1.setGravity(Gravity.RIGHT);
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(_context);
        //tbrow1.setBackgroundColor(0xff125688);
        TextView tv10 = new TextView(_context);
        tv10.setText("DA. Value");
        tv10.setPadding(5, 5, 5, 0);
        tv10.setTextColor(Color.BLACK);
        tv10.setTypeface(null, Typeface.BOLD);
        tv10.setLayoutParams(params);
        tbrow1.addView(tv10);
        TextView tv11 = new TextView(_context);
        tv11.setText(_context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "da_val","0"));
        tv11.setPadding(5, 5, 5, 0);
        tv11.setTextColor(Color.BLACK);
        tv11.setGravity(Gravity.RIGHT);
        tv11.setTypeface(null, Typeface.BOLD);
        tbrow1.addView(tv11);
        stk.addView(tbrow1);

        Float Dis_val =0f;
        if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "ACTUALFAREYN","").equalsIgnoreCase("Y")) {
            TableRow tbrow2 = new TableRow(_context);
            //tbrow2.setBackgroundColor(0xff125688);
            TextView tv21 = new TextView(_context);
            tv21.setText("TA. Value");
            tv21.setPadding(5, 5, 5, 0);
            tv21.setTextColor(Color.BLACK);
            tv21.setTypeface(null, Typeface.BOLD);
            tv21.setLayoutParams(params);
            tbrow2.addView(tv21);
            TextView tv22 = new TextView(_context);
            tv22.setText(_context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "distance_val", "0"));
            tv22.setGravity(Gravity.RIGHT);
            tv22.setPadding(5, 5, 5, 0);
            tv22.setTextColor(Color.BLACK);
            tv22.setTypeface(null, Typeface.BOLD);
            tbrow2.addView(tv22);
            stk.addView(tbrow2);
            Dis_val = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "distance_val","0"));
        }

        TableRow tbrow4 = new TableRow(_context);
        //tbrow4.setBackgroundColor(0xff125688);
        TextView tv40 = new TextView(_context);
        tv40.setText("Other Value");
        tv40.setPadding(5, 5, 5, 0);
        tv40.setTextColor(0xff125688);
        tv40.setTypeface(null, Typeface.BOLD);
        tv40.setLayoutParams(params);
        tbrow4.addView(tv40);
        TextView tv41 = new TextView(_context);


        Float other = 0f;
        int length=getChildrenCount(groupPosition);

        if (listView.isGroupExpanded(groupPosition)){
            length=getChildrenCount(groupPosition)-1;
        }
        for (int i = 0; i <length;i++){
            other+=Float.parseFloat(getChild(groupPosition, i).get("time").get(i));
        }

        tv41.setText(_context.getResources().getString(R.string.rs)+" "+other);
        tv41.setPadding(5, 5, 5, 0);
        tv41.setTextColor(Color.BLACK);
        tv41.setGravity(Gravity.RIGHT);
        tv41.setTypeface(null, Typeface.BOLD);
        tbrow4.addView(tv41);
        stk.addView(tbrow4);

        net_value = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(_context, "da_val","0"))
                + Dis_val
                + other;

        if (!listView.isGroupExpanded(groupPosition)) {

            TableRow tbrow5 = new TableRow(_context);
            tbrow5.setPadding(2,2,2,2);
            tbrow5.setBackgroundColor(0xff125688);
            stk.addView(tbrow5);

            TableRow tbrow3 = new TableRow(_context);
            //tbrow3.setBackgroundColor(0xff125688);
            TextView tv31 = new TextView(_context);
            tv31.setText("Total Expenses");
            tv31.setPadding(5, 5, 5, 0);
            tv31.setTextColor(Color.BLACK);
            tv31.setTypeface(null, Typeface.BOLD);
            tv31.setLayoutParams(params);
            tbrow3.addView(tv31);
            TextView tv32 = new TextView(_context);

            tv32.setText(_context.getResources().getString(R.string.rs) + " " + net_value);

            tv32.setGravity(Gravity.RIGHT);
            tv32.setPadding(5, 5, 5, 0);
            tv32.setTextColor(Color.BLACK);
            tv32.setTypeface(null, Typeface.BOLD);
            tbrow3.addView(tv32);
            stk.addView(tbrow3);
        }


    }

    private void init_work_with(TableLayout stk, String[] work_with,String Title) {

        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(_context);
        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(_context);
        tv0.setText(Title);
        tv0.setTextSize(10);
        tv0.setPadding(35, 5, 5, 0);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);

        for (int i = 0; i < work_with.length; i++) {
            TableRow tbrow = new TableRow(_context);
            TextView t1v = new TextView(_context);
            t1v.setText(work_with[i]);
            t1v.setPadding(55, 5, 5, 0);
            t1v.setTextSize(10);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            stk.addView(tbrow);
        }
    }

    private void Doc_Detail(TableLayout stk,String doc_class, String doc_potential, String doc_last_visited,String area, String crm_count,String camp_group,String class_title,String potential_title,String area_title, String crm_count_title,String camp_group_title,int top_line,int bottom_line) {
        stk.removeAllViews();

        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        if (top_line==1) {
            TableRow tbrow5 = new TableRow(_context);
            tbrow5.setPadding(2, 2, 2, 2);
            tbrow5.setBackgroundColor(0xff125688);
            stk.addView(tbrow5);
        }

        if (!area.equals("")) {
            TableRow tbrow00 = new TableRow(_context);
            TextView tv00 = new TextView(_context);
            tv00.setText(area_title);
            tv00.setTextSize(11);
            tv00.setPadding(5, 5, 5, 0);
            tv00.setTextColor(Color.BLACK);
            tv00.setTypeface(null, Typeface.BOLD);
            tv00.setLayoutParams(params);
            tbrow00.addView(tv00);

            TextView tv01 = new TextView(_context);
            tv01.setText(area);
            tv01.setTextSize(11);
            tv01.setPadding(5, 5, 5, 0);
            tv01.setTextColor(Color.BLACK);
            tv01.setGravity(Gravity.RIGHT);
            tv01.setTypeface(null, Typeface.NORMAL);
            tv01.setLayoutParams(params);
            tbrow00.addView(tv01);
            stk.addView(tbrow00);
        }

        if (!doc_class.equals("") ) {
            TableRow tbrow0 = new TableRow(_context);
            TextView tv0 = new TextView(_context);
            tv0.setText(class_title);
            tv0.setTextSize(11);
            tv0.setPadding(5, 5, 5, 0);
            tv0.setTextColor(Color.BLACK);
            tv0.setTypeface(null, Typeface.BOLD);
            tv0.setLayoutParams(params);
            tbrow0.addView(tv0);

            TextView tv1 = new TextView(_context);
            tv1.setText(doc_class);
            tv1.setTextSize(11);
            tv1.setPadding(5, 5, 5, 0);
            tv1.setTextColor(Color.BLACK);
            tv1.setGravity(Gravity.RIGHT);
            tv1.setTypeface(null, Typeface.NORMAL);
            tv1.setLayoutParams(params);
            tbrow0.addView(tv1);

            stk.addView(tbrow0);
        }

        if (!doc_potential.equals("")) {
            TableRow tbrow01 = new TableRow(_context);
            TextView tv01 = new TextView(_context);
            tv01.setText(potential_title);
            tv01.setTextSize(11);
            tv01.setPadding(5, 5, 5, 0);
            tv01.setTextColor(Color.BLACK);
            tv01.setTypeface(null, Typeface.BOLD);
            tv01.setLayoutParams(params);
            tbrow01.addView(tv01);
            TextView tv11 = new TextView(_context);
            tv11.setText(doc_potential);
            tv11.setTextSize(11);
            tv11.setPadding(5, 5, 5, 0);
            tv11.setTextColor(Color.BLACK);
            tv11.setGravity(Gravity.RIGHT);
            tv11.setTypeface(null, Typeface.NORMAL);
            tbrow01.addView(tv11);
            stk.addView(tbrow01);
        }

        if (!doc_last_visited.equals("")) {

            TableRow tbrow02 = new TableRow(_context);
            TextView tv02 = new TextView(_context);
            tv02.setText("Last Visited");
            tv02.setTextSize(11);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.BOLD);
            tv02.setLayoutParams(params);
            tbrow02.addView(tv02);

            TextView tv12 = new TextView(_context);
            tv12.setText(doc_last_visited);
            tv12.setPadding(5, 5, 5, 0);
            tv12.setTextSize(11);
            tv12.setTextColor(Color.BLACK);
            tv12.setGravity(Gravity.RIGHT);
            tv12.setTypeface(null, Typeface.NORMAL);
            tv12.setLayoutParams(params);
            tbrow02.addView(tv12);

            stk.addView(tbrow02);
        }

        if (!crm_count.equals("")) {

            TableRow tbrow02 = new TableRow(_context);
            TextView tv02 = new TextView(_context);
            tv02.setText(crm_count_title);
            tv02.setTextSize(11);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.BOLD);
            tv02.setLayoutParams(params);
            tbrow02.addView(tv02);

            TextView tv12 = new TextView(_context);
            tv12.setText(crm_count);
            tv12.setPadding(5, 5, 5, 0);
            tv12.setTextSize(11);
            tv12.setTextColor(Color.BLACK);
            tv12.setGravity(Gravity.RIGHT);
            tv12.setTypeface(null, Typeface.NORMAL);
            tv12.setLayoutParams(params);
            tbrow02.addView(tv12);

            stk.addView(tbrow02);
        }

        if (!camp_group.equals("")) {
            TableRow tbrow01 = new TableRow(_context);
            TextView tv01 = new TextView(_context);
            tv01.setText(camp_group_title);
            tv01.setTextSize(11);
            tv01.setPadding(5, 5, 5, 0);
            tv01.setTextColor(Color.BLACK);
            tv01.setTypeface(null, Typeface.BOLD);
            tv01.setLayoutParams(params);
            tbrow01.addView(tv01);
            TextView tv11 = new TextView(_context);
            tv11.setText(camp_group);
            tv11.setTextSize(11);
            tv11.setPadding(5, 5, 5, 0);
            tv11.setTextColor(Color.BLACK);
            tv11.setGravity(Gravity.RIGHT);
            tv11.setTypeface(null, Typeface.NORMAL);
            tbrow01.addView(tv11);
            stk.addView(tbrow01);
        }



        if (bottom_line==1) {
            TableRow tbrow6 = new TableRow(_context);
            tbrow6.setPadding(2, 2, 2, 2);
            tbrow6.setBackgroundColor(0xff125688);
            stk.addView(tbrow6);
        }



    }

}
