package com.accuvally.hdtui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.utils.Trace;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewBaseActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Liu on 2016/11/24.
 */
public class ImageAdapter3 extends BaseAdapter {

    ArrayList<String> imageUrls;
    private LayoutInflater mInflater;
    private Activity mContext;

    public ImageAdapter3(Activity mContext, ArrayList<String> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ArrayList<ImageItem> getImages(ArrayList<String> imageUrls){
        ArrayList<ImageItem> imageItems=new ArrayList<>();
        for (String str:imageUrls){
            ImageItem imageItem=new ImageItem();
            imageItem.path=str;
            Trace.e(" getImages  imageItem.path", str);
            imageItems.add(imageItem);
        }
        return imageItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) mInflater.inflate(R.layout.item_grid_image3, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }
        ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent imageBrowserActivity=new Intent(mContext,ImageBrowserActivity2.class);
                imageBrowserActivity.putExtra(ImageBrowserActivity2.IMGURL,imageUrls.get(position));
                mContext.startActivity(imageBrowserActivity);*/


                //打开预览
                Intent intentPreview = new Intent(mContext, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) getImages(imageUrls));
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePreviewBaseActivity.customImagePreview, true);
                mContext.startActivity(intentPreview);
            }
        });

        return imageView;
    }
}
