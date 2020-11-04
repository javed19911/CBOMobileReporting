package utils.adapterutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui.Show_Sample;
import com.imagezoom.ImageAttacher;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mResources;

    public ViewPagerAdapter(Context mContext, ArrayList<String> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        if (!mResources.get(position).equals("no_image")) {
            Bitmap b = BitmapFactory.decodeFile(mResources.get(position));
            imageView.setImageBitmap(b);
            usingSimpleImage(imageView);
        }else{
            imageView.setImageResource(R.drawable.no_image);
            usingSimpleImage(imageView);
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (Show_Sample) mContext).makeFullScreen();
            }
        });



        container.addView(itemView);

        return itemView;
    }

    public void usingSimpleImage(ImageView imageView) {
		ImageAttacher mAttacher = new ImageAttacher(imageView);
		ImageAttacher.MAX_ZOOM = 10.0f; // triple the current Size
		ImageAttacher.MIN_ZOOM = 0.3f; // same as the  current Size
        mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
//		MatrixChangeListener mMaListener = new MatrixChangeListener();
//		mAttacher.setOnMatrixChangeListener((ImageAttacher.OnMatrixChangedListener) mMaListener);
		PhotoTapListener mPhotoTap = new PhotoTapListener();
		mAttacher.setOnPhotoTapListener((ImageAttacher.OnPhotoTapListener) mPhotoTap);
	}

	private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {

	        @Override
	        public void onPhotoTap(View view, float x, float y) {
                ( (Show_Sample) mContext).makeFullScreen();
	        }
	    }

	    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {

	        @Override
	        public void onMatrixChanged(RectF rect) {

	        }
	    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
