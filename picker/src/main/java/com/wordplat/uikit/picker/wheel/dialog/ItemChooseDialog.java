package com.wordplat.uikit.picker.wheel.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wordplat.uikit.picker.R;
import com.wordplat.uikit.picker.wheel.adapter.ArrayWheelAdapter;
import com.wordplat.uikit.picker.wheel.lib.WheelView;

import java.util.ArrayList;

/**
 * <p>简单项目选择 对话框</p>
 * <p>Date: 2017/5/19</p>
 *
 * @author afon
 */

public class ItemChooseDialog extends BaseBottomDialog {

    private WheelView goodsView; // 商品滚轮选择器
    private ArrayList<String> goodsList; // 商品列表
    private ArrayWheelAdapter<String> adapter; // 适配器
    private int currentItem = 0; // 当前选择的商品位置

    public ItemChooseDialog(Context context) {
        super(context);

        goodsList = new ArrayList<>();
    }

    public static ItemChooseDialog from(Context context) {
        return new ItemChooseDialog(context);
    }

    public ItemChooseDialog withItemList(ArrayList<String> goodsList, int currentItem) {
        this.goodsList.addAll(goodsList);
        this.currentItem = currentItem;

        return this;
    }

    /** 返回选择的商品位置 */
    public int getSelectGoodsPosition() {
        return goodsView.getCurrentItem();
    }

    public ItemChooseDialog onActionClick(final OnGoodsListener goodsListener) {
        myActionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsListener.onSelected(goodsView.getCurrentItem());
            }
        };
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_choose_item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGoods();
    }

    private void initGoods() {
        goodsView = (WheelView) findViewById(R.id.Item_Info);
        goodsView.setCyclic(false);

        adapter = new ArrayWheelAdapter<>(goodsList);
        goodsView.setAdapter(adapter);
        goodsView.setCurrentItem(currentItem);
    }

    public interface OnGoodsListener {
        void onSelected(int currentItem);
    }
}