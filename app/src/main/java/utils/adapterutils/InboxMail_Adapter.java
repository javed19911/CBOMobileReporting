package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import services.ServiceHandler;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

public class InboxMail_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<InboxMail_Model> dataList= new ArrayList<InboxMail_Model>();
    Custom_Variables_And_Method customVariablesAndMethod;
    String aT1;







    public InboxMail_Adapter(Context context,ArrayList<InboxMail_Model> data){

      this.context = context;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      dataList = data;
      customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.inbox_row,null);

            holder.date=(TextView) convertView.findViewById(R.id.mail_date);
            holder.from=(TextView) convertView.findViewById(R.id.mail_from);
            holder.subject=(TextView) convertView.findViewById(R.id.mail_sub);
            holder.attachText1=(TextView) convertView.findViewById(R.id.tv_att1);
            holder.attachtext2=(TextView) convertView.findViewById(R.id.tv_att2);
            holder.attachtext3=(TextView) convertView.findViewById(R.id.tv_att3);
            holder.msg=(TextView) convertView.findViewById(R.id.msg);
            holder.filehead1 =(Button) convertView.findViewById(R.id.bt_att1);
            holder.filehead2 =(Button) convertView.findViewById(R.id.bt_att2);
            holder.filehead3 =(Button) convertView.findViewById(R.id.bt_att3);
            holder.attachmentLayout =(LinearLayout) convertView.findViewById(R.id.attachment_Layout);
            holder.attach=(ImageView) convertView.findViewById(R.id.attach);
            convertView.setTag(holder);

        }else {
     holder = (ViewHolder) convertView.getTag();
        }
        holder.date.setText(dataList.get(position).getDate());
        holder.from.setText(dataList.get(position).getFrom());
        holder.subject.setText(dataList.get(position).getSubject());
        holder.attachText1.setText(dataList.get(position).getFilename());
        holder.attachtext2.setText(dataList.get(position).getFilename2());
        holder.attachtext3.setText(dataList.get(position).getFileneme3());
        holder.msg.setText(dataList.get(position).getMail());
        holder.filehead1.setText(dataList.get(position).getFileheading());
        holder.filehead2.setText(dataList.get(position).getFileheading2());
        holder.filehead3.setText(dataList.get(position).getFileheading3());

        holder.subject.setTag(position);
        holder.attachText1.setTag(position);
        holder.attachtext2.setTag(position);
        holder.attachtext3.setTag(position);
        holder.filehead1.setTag(position);
        holder.filehead2.setTag(position);
        holder.filehead3.setTag(position);

       final String subject=  dataList.get(position).getSubject();
        aT1=  dataList.get(position).getFilename();
        String aT2=  dataList.get(position).getFilename2();
        String atT3=  dataList.get(position).getFileneme3();
        String fh1=  dataList.get(position).getFileheading();
        String fh2=  dataList.get(position).getFileheading2();
        String fh3=  dataList.get(position).getFileheading3();


        holder.linearLayoutSubject = (LinearLayout) convertView.findViewById(R.id.text_content);
        holder.linearLayoutSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customVariablesAndMethod.msgBox(context,subject);
            }
        });

        if (aT1.equals("")){
            holder.attach.setVisibility(View.GONE);
        }else {
            holder.attach.setVisibility(View.VISIBLE);
            holder.filehead2.setVisibility(View.GONE);
            holder.filehead3.setVisibility(View.GONE);
            holder.attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!aT1.contains("http://")){
                        aT1="http://"+aT1;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aT1));
                    context.startActivity(browserIntent);
                }
            });

        }







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

        TextView from,subject,date,attachText1,attachtext2,attachtext3,msg;
        Button filehead1,filehead2,filehead3;
        LinearLayout attachmentLayout,linearLayoutSubject;
        ImageView attach;


    }
}
