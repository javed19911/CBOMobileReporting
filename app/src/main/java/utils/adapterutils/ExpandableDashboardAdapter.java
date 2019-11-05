package utils.adapterutils;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils_new.Custom_Variables_And_Method;

public class ExpandableDashboardAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    Custom_Variables_And_Method customVariablesAndMethod;
    Boolean clicked=false;
    Float net_value=0f;
    ExpandableListView listView;
    String  month;
    private  HashMap<String, ArrayList<Map<String, String>>>  _listDataChild;


    public ExpandableDashboardAdapter(ExpandableListView listView, Context context, List<String> listDataHeader,
                                      HashMap<String, ArrayList<Map<String, String>>> listChildData,String month) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.listView=listView;
        this.month=month;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


    }

    @Override
    public  ArrayList<Map<String, String>> getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ArrayList<Map<String, String>> childText = (ArrayList<Map<String, String>>) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.marketing_view, null);
        }

        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.row);
        TextView remark = (TextView) convertView.findViewById(R.id.remark_marketing);
        TextView amount = (TextView) convertView.findViewById(R.id.amount_marketing);
        TextView amount_cumm = (TextView) convertView.findViewById(R.id.amount_cumm_marketing);

        if (childText.get(childPosition).get("URL").equals("")){
            remark.setText(childText.get(childPosition).get("REMARK"));
        }else {
            remark.setText(Html.fromHtml("<u>"+childText.get(childPosition).get("REMARK")+"</u>"));
        }
        amount.setText(childText.get(childPosition).get("AMOUNT"));
        amount_cumm.setText(childText.get(childPosition).get("AMOUNT_CUMM"));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (childText.get(childPosition).get("AMOUNT").equals("") && childText.get(childPosition).get("AMOUNT_CUMM").equals("")){
            row.setBackgroundColor(0xFFf4f5fa);
            layoutParams.setMargins(40, 0, 5, 0);
            row.setLayoutParams(layoutParams);
            remark.setTextColor(ContextCompat.getColor(_context, R.color.colorPrimaryDark));
            remark.setTypeface(null, Typeface.BOLD);
        }else{
            row.setBackgroundResource(R.drawable.custom_square_transparent_bg);
            layoutParams.setMargins(40, 0, 5, 0);
            row.setLayoutParams(layoutParams);
            remark.setTextColor(ContextCompat.getColor(_context, R.color.colorPrimary));
            remark.setTypeface(null, Typeface.NORMAL);
        }

        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!childText.get(childPosition).get("URL").equals("")){
                    MyCustumApplication.getInstance().LoadURL(childText.get(childPosition).get("REMARK"),childText.get(childPosition).get("URL"));
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
            convertView = infalInflater.inflate(R.layout.dashboard_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.header);
        TextView from = (TextView) convertView.findViewById(R.id.from);
        TextView upto = (TextView) convertView.findViewById(R.id.upto);
        TextView expend = (TextView) convertView.findViewById(R.id.expend);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        from.setText(month);
        upto.setText("Upto "+month);
        if (isExpanded){
            expend.setText("-");
        }else{
            expend.setText("+");
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
