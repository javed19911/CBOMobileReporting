package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui.Inbox_Msg;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * Created by Akshit on 11/28/2015.
 */
public class OutboxMail_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String,String>> dataList= new ArrayList<Map<String,String>>();

    public OutboxMail_Adapter(Context context, ArrayList<Map<String,String>> data){

      this.context = context;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      dataList = data;

  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.outbox_row,null);

            holder.date=(TextView) convertView.findViewById(R.id.mail_dateto);
            holder.from=(TextView) convertView.findViewById(R.id.mail_to);
            holder.subject=(TextView) convertView.findViewById(R.id.mail_subto);
            holder.attach=(ImageView) convertView.findViewById(R.id.attach);
            holder.cir=(TextView) convertView.findViewById(R.id.inbox_idto);
            holder.des=(TextView) convertView.findViewById(R.id.describtion);

            convertView.setTag(holder);

        }else {
             holder = (ViewHolder) convertView.getTag();
        }
        if (getDate("dd/MM/yyyy").equals(dataList.get(position).get("date"))){
            holder.date.setText(dataList.get(position).get("time"));
        }else {
            holder.date.setText(dataList.get(position).get("date"));
        }
        holder.from.setText(dataList.get(position).get("from"));
        holder.subject.setText(dataList.get(position).get("sub"));
        holder.des.setText(dataList.get(position).get("REMARK"));
        holder.cir.setText(dataList.get(position).get("from").substring(0,1).toUpperCase());

        final Drawable drawable = holder.cir.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

        if (dataList.get(position).get("IS_READ").equals("0")){
            holder.from.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            color[0] = Color.argb(255, 192, 192, 192);
            holder.from.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        }


        holder.date.setTag(position);
        holder.from.setTag(position);
        holder.subject.setTag(position);
        String file_name=dataList.get(position).get("FILE_NAME");
        if(file_name.equals("")){
            holder.attach.setVisibility(View.GONE);
        }else{
            holder.attach.setVisibility(View.VISIBLE);
            final String[] aT1 = {file_name};
            holder.attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!aT1[0].contains("http://")){
                        aT1[0] ="http://"+ aT1[0];
                    }
                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aT1[0]));
                    context.startActivity(browserIntent);*/
                    Intent i = new Intent(context, CustomWebView.class);
                    i.putExtra("A_TP1", aT1[0]);
                    i.putExtra("Menu_code", "");
                    i.putExtra("Title", "Attachment");
                    context.startActivity(i);
                }
            });
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color[0] = Color.argb(255, 192, 192, 192);
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
                holder.from.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                Intent intent=new Intent(context, Inbox_Msg.class);
                intent.putExtra("mail_id",dataList.get(position).get("mail_id"));
                intent.putExtra("mail_type","");
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder{
        ImageView attach;
        TextView from,subject,date,cir,des;
    }

    private String getDate(String date_format){
        SimpleDateFormat format = new SimpleDateFormat(date_format);//"yyyy.MM.dd HH:mm");
        Calendar cal = Calendar.getInstance();
        System.out.println(format.format(cal.getTime()));
        return format.format(cal.getTime());
    }

}
