package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.picker.wheel.lib.NumberListWheelView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>PickerNumberListActivity</p>
 * <p>Date: 2017/5/19</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_picker_number_list)
public class PickerNumberListActivity extends BaseActivity {

    @ViewInject(R.id.titleView) private SuperTextView titleView = null;
    @ViewInject(R.id.backBut) private TextView backBut = null;

    @ViewInject(R.id.Number_List_Wheel) private NumberListWheelView Number_List_Wheel = null;

    private final List<String> wheelItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        titleView.setCenterString("Number list picker");
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setNumber(8, 28, 7);
    }

    private void setNumber(int min, int max, int selectIndex) {
        wheelItems.clear();
        for(int i = min, j = 0 ; i <= max ; i++, j++) {
            wheelItems.add(String.valueOf(i));
        }
        Number_List_Wheel.setItems(wheelItems);
        Number_List_Wheel.selectIndex(selectIndex);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, PickerNumberListActivity.class);
        return intent;
    }
}
