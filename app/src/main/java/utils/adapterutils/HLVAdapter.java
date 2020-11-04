package utils.adapterutils;

import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cbo.cbomobilereporting.R;

import java.io.File;
import java.util.ArrayList;

import interfaces.ItemClickListener;

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

    ArrayList<File> alImage;
    Context context;

    public HLVAdapter(Context context, ArrayList<File> alImage) {
        super();
        this.context = context;
        this.alImage = alImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.attachment_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        //viewHolder.tvSpecies.setText(alName.get(i));
       // viewHolder.imgThumbnail.setImageResource(alImage.get(i));
       // Bitmap b = BitmapFactory.decodeFile(alImage.get(i).getPath().toString());
        //viewHolder.imgThumbnail.setImageBitmap(b);

        Glide.with(context)
                .load(alImage.get(i).getPath().toString())
                .error(R.drawable.no_image)
                .into(viewHolder.imgThumbnail);

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    switch (view.getId()){
                        case R.id.tv_species:
                            alImage.remove(i);
                            notifyItemRemoved(i);
                            break;
                         default:
                             Toast.makeText(context, "#" + i + " - " + alImage.get(i) + " (Long click)", Toast.LENGTH_SHORT).show();

                    }
                    //context.startActivity(new Intent(context, MainActivity.class));
                } else {
                    switch (view.getId()){
                        case R.id.tv_species:
                            alImage.remove(position);
                            notifyItemRemoved(position);
                            break;
                        default:
                            PreviewProfileImage(i);
                    }
                }
            }
        });


    }


    private void PreviewProfileImage(int Position){
        //Bitmap b = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setPositiveButton("OK", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.attachment_pop_up, null);
        ImageView attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
        ImageView close= (ImageView) dialogLayout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        //attach_img.setImageBitmap(b);
        Glide.with(context)
                .load(alImage.get(Position).getPath().toString())
                .error(R.drawable.no_image)
                .into(attach_img);
        dialog.setView(dialogLayout);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return alImage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView tvSpecies;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            tvSpecies.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getLayoutPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getLayoutPosition(), true);
            return true;
        }
    }

}