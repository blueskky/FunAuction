package com.fun.auction.ui.adapter;

import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fun.auction.R;


public class PoiListCheckAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> {


    public PoiListCheckAdapter() {
        super(R.layout.poi_result_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiInfo item) {
        helper.setText(R.id.tv_name, item.getName());

        String city = item.city;
        String address = item.address;
        if (!address.contains(city)) {
            address = city + address;
        }
        helper.setText(R.id.tv_adress, address);
    }

}