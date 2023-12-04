package com.cs407.icebox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ColorAdaptor extends BaseAdapter {

    private Context context;
    private List<Item> itemList;
    private List<Integer> colorList;

    public ColorAdaptor(Context context, List<Item> itemList, List<Integer> colorList) {
        this.context = context;
        this.itemList = itemList;
        this.colorList = colorList;
    }



    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_layout, null);
        }

        int color = colorList.get(position);
        view.setBackgroundColor(color);

        TextView itemName = (TextView) view.findViewById(R.id.itemNameText);
        itemName.setText(itemList.get(position).getItemName());

        return view;
    }
}