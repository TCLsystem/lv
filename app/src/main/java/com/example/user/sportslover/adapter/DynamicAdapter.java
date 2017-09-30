package com.example.user.sportslover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.sportslover.R;
import com.example.user.sportslover.bean.DynamicItem;
import com.example.user.sportslover.bean.User;
import com.example.user.sportslover.customview.FixedGridView;
import com.example.user.sportslover.model.SportModelInter;
import com.example.user.sportslover.model.UserModelImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by user on 17-9-19.
 */
public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<DynamicItem> mDatas;
    private int mLayoutRes;
    private Context mContext;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public DynamicAdapter(Context context, int layoutRes, List<DynamicItem> datas) {
        this.mContext=context;
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }


    public List<DynamicItem> returnmDatas() {
        return this.mDatas;
    }

    public void addAll(List<DynamicItem> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<DynamicItem> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            holder = new ViewHolder();
           // holder.write_photo = (RoundImageView) convertView.findViewById(R.id.write_photo);
            holder.write_name = (TextView) convertView.findViewById(R.id.write_name);
            holder.write_date = (TextView) convertView.findViewById(R.id.write_date);
            holder.dynamic_text = (TextView) convertView.findViewById(R.id.dynamic_text);
            holder.dynamic_photo = (FixedGridView) convertView.findViewById(R.id.dynamic_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicItem dynamicItem = mDatas.get(position);
        final ViewHolder viewHolder = holder;
        new UserModelImpl().getUser(dynamicItem.getWriter().getObjectId(), new SportModelInter.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                User user = (User) o;
              //  imageLoader.displayImage(user.getPhoto().getUrl(), viewHolder.write_photo, options);
                viewHolder.write_name.setText(user.getName());
            }

            @Override
            public void getFailure() {

            }
        });
        viewHolder.write_date.setText(dynamicItem.getCreatedAt());
        holder.dynamic_text.setText(dynamicItem.getDetail());
        holder.dynamic_photo.setAdapter(new DynamicPhotoAdapter(mContext,R.layout.item_dynamic_gridview,dynamicItem.getPhotoList()));
        return convertView;
    }

    private final class ViewHolder {
      //  RoundImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
        FixedGridView dynamic_photo;
    }
}

