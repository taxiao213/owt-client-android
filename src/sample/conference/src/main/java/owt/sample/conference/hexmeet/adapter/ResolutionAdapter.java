package owt.sample.conference.hexmeet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import owt.sample.conference.R;
import owt.sample.conference.hexmeet.utils.UIUtils;

/**
 * Created by hanqq on 2020/12/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class ResolutionAdapter implements ListAdapter {
    private Context mContext;
    private ArrayList<Point> mList;

    public ResolutionAdapter(Context context, ArrayList<Point> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Point getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResolutionViewHolder holder = null;
        if (convertView == null) {
            holder = new ResolutionViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_resolution, null, false);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.tv_resolution = (TextView) convertView.findViewById(R.id.tv_resolution);
            convertView.setTag(holder);
        } else {
            holder = (ResolutionViewHolder) convertView.getTag();
        }
        Point point = mList.get(position);
        Point resolution = UIUtils.getInstance().getResolution();
        if (point == resolution) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.tv_resolution.setText(point.x + " x " + point.y);
        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    //ViewHolder静态类
    public final class ResolutionViewHolder {
        public CheckBox checkBox;
        public TextView tv_resolution;
    }
}
