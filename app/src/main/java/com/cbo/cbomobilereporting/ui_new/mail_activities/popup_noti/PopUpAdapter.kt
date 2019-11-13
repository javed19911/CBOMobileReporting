package com.cbo.cbomobilereporting.ui_new.popup_noti

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.cbo.cbomobilereporting.R
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper
import com.cbo.cbomobilereporting.ui.TotalChemistRpt
import com.cbo.cbomobilereporting.ui.TotalDrRpt
import com.cbo.cbomobilereporting.ui.TotalStockistRpt
import com.cbo.cbomobilereporting.ui_new.mail_activities.popup_noti.PopUpModel
import com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport.DcrReportsNew

import java.util.ArrayList

import utils_new.Custom_Variables_And_Method

class PopUpAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    internal var customVariablesAndMethod: Custom_Variables_And_Method
    private val VIEW_HEADER = 2
    private val VIEW_BODY = 1
    internal var cbohelp: CBO_DB_Helper
    private var date = ""
    private var context: Context? = null
    private var arrayList: ArrayList<PopUpModel>? = null


    constructor(context: Context, arrayList: ArrayList<PopUpModel>) {
        this.context = context
        this.arrayList = arrayList
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance(context)
        cbohelp = customVariablesAndMethod._cbo_db_instance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vHolder: RecyclerView.ViewHolder? = null

        if (viewType == VIEW_BODY) {

            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.mylayout_text, parent, false)
            vHolder = MySingleViewHolder(view)

        } else if (viewType == VIEW_HEADER) {

            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_header, parent, false)
            vHolder = MyHeaderViewHolder(v)

        }
        return vHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MySingleViewHolder) {
            holder.onBindData(position)
        } else if (holder is MyHeaderViewHolder) {
            holder.onBindData(position)
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun getItemViewType(position: Int): Int {

        return if (arrayList!![position].tvalue == "") {
            VIEW_HEADER
        } else {
            VIEW_BODY
        }
    }


    inner class MySingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView
        private val tvValue: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvValue = itemView.findViewById(R.id.tvValue)
        }

        fun onBindData(position: Int) {

            tvTitle.text = arrayList!![position].tname
            tvValue.text = arrayList!![position].tvalue
            if (!arrayList!![position].getPAGE_CODE().equals("")) {
                tvValue.setTextColor(Color.BLUE);
            } else {
                tvValue.setTextColor(Color.BLACK);
            }
            tvValue.setOnClickListener {
                if (arrayList!![position].pagE_CODE.equals("DR", ignoreCase = true)) {
                    val intent = Intent(context, TotalDrRpt::class.java)
                    intent.putExtra("PAID", "")
                    intent.putExtra("DCR_ID", arrayList!![position].dcR_CODE)
                    intent.putExtra("date", "")
                    intent.putExtra("call_type", "D")
                    intent.putExtra("Title", cbohelp.getMenu("DCR", "D_DRCALL")["D_DRCALL"])
                    context!!.startActivity(intent)
                } else if (arrayList!![position].pagE_CODE.equals("CHEM", ignoreCase = true)) {
                    val ttlche = Intent(context, TotalChemistRpt::class.java)
                    ttlche.putExtra("PAID", "")
                    ttlche.putExtra("DCR_ID", arrayList!![position].dcR_CODE)
                    ttlche.putExtra("date", "")
                    ttlche.putExtra("Title", "Chemist")
                    context!!.startActivity(ttlche)
                } else if (arrayList!![position].pagE_CODE.equals("STK", ignoreCase = true)) {
                    val ttlStk = Intent(context, TotalStockistRpt::class.java)
                    ttlStk.putExtra("DCR_ID", arrayList!![position].dcR_CODE)
                    ttlStk.putExtra("PAID", "")
                    ttlStk.putExtra("date", "")
                    context!!.startActivity(ttlStk)

                }else if(arrayList!![position].pagE_CODE.equals("DCRVIEW", ignoreCase = true)){
                    val i = Intent(context, DcrReportsNew::class.java)
                    i.putExtra("DCR_ID", arrayList!![position].dcR_CODE)
                    context!!.startActivity(i)
                }
            }
        }
    }

    inner class MyHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
        }
        fun onBindData(position: Int) {
            tvTitle.text = arrayList!![position].tname.replace("\n", "")
        }
    }
}
