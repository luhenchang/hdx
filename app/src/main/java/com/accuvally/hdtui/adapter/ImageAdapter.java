package com.accuvally.hdtui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.accuvally.hdtui.R;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.Utils;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewBaseActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;

/**
 * Created by Andy Liu on 2017/2/7.
 */
public class ImageAdapter extends BaseAdapter {

    ArrayList<String> thumbimgs;
    ArrayList<String> imgs;
    private LayoutInflater mInflater;
    private Activity mContext;


    ImageSize mImageSize;
    private ImagePicker imagePicker;

    public ImageAdapter(Activity mContext, ArrayList<String> thumbimgs,ArrayList<String> imgs) {
        this.mContext = mContext;
        this.thumbimgs = thumbimgs;
        this.imgs = imgs;
        mInflater = LayoutInflater.from(mContext);
        int size = Utils.getImageItemWidth(mContext);
        mImageSize= new ImageSize(size, size);
        imagePicker = ImagePicker.getInstance();
    }


    @Override
    public int getCount() {
        return thumbimgs.size();
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
            imageItems.add(imageItem);
        }
        return imageItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) mInflater.inflate(R.layout.item_grid_image, parent, false);
            int size = Utils.getImageItemWidth(mContext);
            imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size-40)); //让图片是个正方形
        } else {
            imageView = (ImageView) convertView;
        }


        ImageLoader.getInstance().displayImage(thumbimgs.get(position), imageView,mImageSize);

//        imagePicker.getImageLoader().displayImage(mContext, imageUrls.get(position), imageView, mImageSize, mImageSize); //显示图片

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent imageBrowserActivity=new Intent(mContext,ImageBrowserActivity2.class);
                imageBrowserActivity.putExtra(ImageBrowserActivity2.IMGURL,imageUrls.get(position));
                mContext.startActivity(imageBrowserActivity);*/


                Intent intentPreview = new Intent(mContext, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) getImages(imgs));
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePreviewBaseActivity.customImagePreview, true);
                mContext.startActivity(intentPreview);
            }
        });

        return imageView;
    }
}
