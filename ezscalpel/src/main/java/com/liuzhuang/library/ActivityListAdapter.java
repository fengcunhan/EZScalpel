package com.liuzhuang.library;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhuang.library.utils.EZSharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by liuzhuang on 11/4/15.
 */
class ActivityListAdapter extends BaseAdapter {
    private ArrayList<String> data;
    private Context context;

    public ActivityListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_name_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String item = getItem(position);
        if (! TextUtils.isEmpty(item)) {
            viewHolder.name.setText(item);
            viewHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EZSharedPreferenceUtil.deleteActivityName(context, data.get(position));
                    refresh();
                }
            });
        }
        return convertView;
    }

    public void refresh() {
        if (data == null) {
            data = new ArrayList<String>();
        }
        data.clear();
        Set<String> dataSet = EZSharedPreferenceUtil.getActivityNameSet(context);
        if (dataSet != null) {
            for (String name :
                    dataSet) {
                data.add(name);
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView name;
        public ImageView del;

        public ViewHolder(View parent) {
            if (parent == null) {
                return;
            }
            name = (TextView) parent.findViewById(R.id.activity_name_item_tv);
            del = (ImageView) parent.findViewById(R.id.activity_name_item_del);
        }
    }
}
