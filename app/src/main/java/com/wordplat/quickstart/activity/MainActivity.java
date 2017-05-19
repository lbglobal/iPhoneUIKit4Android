package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.picker.wheel.dialog.CityChooseDialog;
import com.wordplat.uikit.picker.wheel.dialog.CityChooseDialog.ProvinceParseBean;
import com.wordplat.uikit.picker.wheel.dialog.DateChooseDialog;
import com.wordplat.uikit.picker.wheel.dialog.ItemChooseDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private CityChooseDialog cityChooseDialog;
    private String selectProvince;
    private String selectCity;

    private int selectItem = 0;

    private DateChooseDialog.DateInfoBean selectStartDate;
    private DateChooseDialog.DateInfoBean selectEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTranslucentStatus(true);
    }

    @Event(value = {R.id.item1, R.id.item2, R.id.item3, R.id.item6,
            R.id.item4, R.id.item5, R.id.item7, R.id.item8}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.item1:
                startActivity(LoadingAnimActivity.createIntent(mActivity));
                break;

            case R.id.item2:
                startActivity(DialogExampleActivity.createIntent(mActivity));
                break;

            case R.id.item3:
                startActivity(WebViewActivity.createIntent(mActivity,
                        WebViewActivity.class,
                        "https://m.baidu.com/",
                        "百度"));
                break;

            case R.id.item4:
                startActivity(NetworkEmptyLayoutExampleActivity.createIntent(mActivity));
                break;

            case R.id.item5:
                startActivity(PickerNumberListActivity.createIntent(mActivity));
                break;

            case R.id.item6:
                ArrayList<String> items = new ArrayList<>();
                items.add("商品一");
                items.add("商品二");
                items.add("商品三");
                items.add("商品四");
                items.add("商品五");
                items.add("商品六");
                items.add("商品七");
                items.add("商品八");
                items.add("商品九");

                ItemChooseDialog.from(mActivity)
                        .withItemList(items, selectItem)
                        .onActionClick(new ItemChooseDialog.OnGoodsListener() {
                            @Override
                            public void onSelected(int currentItem) {
                                selectItem = currentItem;
                            }
                        })
                        .show();
                break;

            case R.id.item7:
                if (cityChooseDialog == null) {
                    ProvinceParseBean parseBean = JSON.parseObject(CityChooseDialog.CITY_DATA, ProvinceParseBean.class);
                    cityChooseDialog = CityChooseDialog.from(mActivity)
                            .withProvincesList(parseBean.getProvincesList());
                }

                cityChooseDialog
                        .withSelect(selectProvince, selectCity)
                        .onActionClick(new CityChooseDialog.OnCityListener() {
                            @Override
                            public void onSelected(String province, String city) {
                                selectProvince = province;
                                selectCity = city;
                            }
                        })
                        .show();

                break;

            case R.id.item8:
                DateChooseDialog.from(mActivity)
                        .withSelectedStartDateInfo(selectStartDate)
                        .withSelectedEndDateInfo(selectEndDate)
                        .onActionClick(new DateChooseDialog.OnDateListener() {
                            @Override
                            public void onSelected(DateChooseDialog.DateInfoBean startDateInfo, DateChooseDialog.DateInfoBean endDateInfo) {
                                selectStartDate = startDateInfo;
                                selectEndDate = endDateInfo;
                            }
                        })
                        .show();

                break;
        }
    }

    /**
     * 点击两次退出
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
