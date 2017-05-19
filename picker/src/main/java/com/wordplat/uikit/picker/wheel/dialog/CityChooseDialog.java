package com.wordplat.uikit.picker.wheel.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wordplat.uikit.picker.R;
import com.wordplat.uikit.picker.wheel.adapter.WheelAdapter;
import com.wordplat.uikit.picker.wheel.lib.WheelView;
import com.wordplat.uikit.picker.wheel.listener.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>省份、城市联动选择 对话框</p>
 * <p>Date: 2017/5/19</p>
 *
 * @author afon
 */

public class CityChooseDialog extends BaseBottomDialog {

    private WheelView Province; // 省份
    private WheelView City; // 城市

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private static List<ProvinceBean> provincesList; // 省份、城市数据
    private int selectedProvince = 0;
    private int selectedCity = 0;

    public CityChooseDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_choose_city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCityAdapter();
    }

    public CityChooseDialog withProvincesList(List<ProvinceBean> provincesList) {
        this.provincesList = provincesList;

        return this;
    }

    public CityChooseDialog withSelect(String province, String city) {
        if(provincesList == null || provincesList.size() == 0) {
            return this;
        }
        for(int i = 0 ; i < provincesList.size() ; i++) {
            ProvinceBean provinceBean = provincesList.get(i);
            if(provinceBean != null && !TextUtils.isEmpty(provinceBean.getName()) && provinceBean.getName().equals(province)) {
                selectedProvince = i;

                List<CityBean> cityList = provinceBean.getCitys();
                if(cityList == null || cityList.size() == 0) {
                    break;
                }
                for(int j = 0 ; j < cityList.size() ; j++) {
                    CityBean cityBean = cityList.get(j);

                    if(cityBean != null && !TextUtils.isEmpty(cityBean.getName()) && cityBean.getName().equals(city)) {
                        selectedCity = j;
                        break;
                    }
                }
                break;
            }
        }
        return this;
    }

    public CityChooseDialog onActionClick(final OnCityListener onCityListener) {
        myActionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedProvince < provinceAdapter.getItemsCount() && selectedCity < cityAdapter.getItemsCount()) {
                    String province = (String) provinceAdapter.getItem(selectedProvince);
                    String city = (String) cityAdapter.getItem(selectedCity);
                    onCityListener.onSelected(province, city);
                }
            }
        };
        return this;
    }

    private void initCityAdapter() {
        Province = (WheelView) findViewById(R.id.Province);
        City = (WheelView) findViewById(R.id.City);

        if(provincesList == null || provincesList.size() == 0) {
            Toast.makeText(context, "加载城市数据出错，请重试", Toast.LENGTH_SHORT).show();
            return ;
        }

        provinceAdapter = new ProvinceAdapter();
        cityAdapter = new CityAdapter(selectedProvince);

        Province.setAdapter(provinceAdapter);
        City.setAdapter(cityAdapter);

        Province.setCurrentItem(selectedProvince);
        Province.setCyclic(false);
        City.setCurrentItem(selectedCity);
        City.setCyclic(false);

        Province.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int count = cityAdapter.setProvinceIndex(index);
                City.setAdapter(cityAdapter);

                if(City.getCurrentItem() > count - 1) {
                    selectedCity = count - 1;
                    City.setCurrentItem(count - 1);
                }

                selectedProvince = index;
            }
        });

        City.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectedCity = index;
            }
        });
    }

    public static CityChooseDialog from(Context context) {
        CityChooseDialog dialog = new CityChooseDialog(context);
        return dialog;
    }

    public interface OnCityListener {
        void onSelected(String province, String city);
    }

    private static class CityAdapter implements WheelAdapter {

        private List<String> itemString;

        public CityAdapter(int provinceIndex) {
            setProvinceIndex(provinceIndex);
        }

        public int setProvinceIndex(int provinceIndex) {
            itemString = new ArrayList<>();
            for(CityBean cityBean : provincesList.get(provinceIndex).getCitys()) {
                String city = cityBean.getName();
//                int pos = city.indexOf("市");
//                if(pos != -1) {
//                    city = city.substring(0, pos);
//                }
                itemString.add(city);
            }
            return itemString.size();
        }

        @Override
        public int getItemsCount() {
            return itemString.size();
        }

        @Override
        public Object getItem(int index) {
            return itemString.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return itemString.indexOf(o);
        }
    }

    private static class ProvinceAdapter implements WheelAdapter {

        private List<String> itemString;

        public ProvinceAdapter() {
            itemString = new ArrayList<>();
            for(ProvinceBean provinceBean : provincesList) {
                String province = provinceBean.getName();
//                province = province.substring(0, 2);
                itemString.add(province);
            }
        }

        @Override
        public int getItemsCount() {
            return itemString.size();
        }

        @Override
        public Object getItem(int index) {
            return itemString.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return itemString.indexOf(o);
        }
    }

    // 城市 Bean
    public static class CityBean {

        private String Name;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

    // 省份 Bean
    public static class ProvinceBean {

        private String Name;

        private List<CityBean> Citys;

        public List<CityBean> getCitys() {
            return Citys;
        }

        public void setCitys(List<CityBean> citys) {
            Citys = citys;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

    // 省份、城市数据 Json解析类
    public static class ProvinceParseBean {

        private List<ProvinceBean> provincesList;

        public List<ProvinceBean> getProvincesList() {
            return provincesList;
        }

        public void setProvincesList(List<ProvinceBean> provincesList) {
            this.provincesList = provincesList;
        }
    }

    public static String CITY_DATA = "{\"provincesList\": [\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"北京\"}],\n" +
            "        \"Name\": \"北京\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"上海\"}],\n" +
            "        \"Name\": \"上海\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"天津\"}],\n" +
            "        \"Name\": \"天津\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"重庆\"}],\n" +
            "        \"Name\": \"重庆\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"银川\"},\n" +
            "            {\"Name\": \"石嘴山\"},\n" +
            "            {\"Name\": \"吴忠\"},\n" +
            "            {\"Name\": \"固原\"},\n" +
            "            {\"Name\": \"中卫\"}\n" +
            "        ],\n" +
            "        \"Name\": \"宁夏\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"南昌\"},\n" +
            "            {\"Name\": \"九江\"},\n" +
            "            {\"Name\": \"上饶\"},\n" +
            "            {\"Name\": \"抚州\"},\n" +
            "            {\"Name\": \"宜春\"},\n" +
            "            {\"Name\": \"吉安\"},\n" +
            "            {\"Name\": \"赣州\"},\n" +
            "            {\"Name\": \"景德镇\"},\n" +
            "            {\"Name\": \"萍乡\"},\n" +
            "            {\"Name\": \"新余\"},\n" +
            "            {\"Name\": \"鹰潭\"}\n" +
            "        ],\n" +
            "        \"Name\": \"江西\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"昆明\"},\n" +
            "            {\"Name\": \"大理\"},\n" +
            "            {\"Name\": \"红河\"},\n" +
            "            {\"Name\": \"曲靖\"},\n" +
            "            {\"Name\": \"保山\"},\n" +
            "            {\"Name\": \"文山\"},\n" +
            "            {\"Name\": \"玉溪\"},\n" +
            "            {\"Name\": \"楚雄\"},\n" +
            "            {\"Name\": \"普洱\"},\n" +
            "            {\"Name\": \"昭通\"},\n" +
            "            {\"Name\": \"临沧\"},\n" +
            "            {\"Name\": \"怒江\"},\n" +
            "            {\"Name\": \"迪庆\"},\n" +
            "            {\"Name\": \"丽江\"},\n" +
            "            {\"Name\": \"德宏\"},\n" +
            "            {\"Name\": \"西双版纳\"}\n" +
            "        ],\n" +
            "        \"Name\": \"云南\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"广州\"},\n" +
            "            {\"Name\": \"韶关\"},\n" +
            "            {\"Name\": \"惠州\"},\n" +
            "            {\"Name\": \"梅州\"},\n" +
            "            {\"Name\": \"汕头\"},\n" +
            "            {\"Name\": \"深圳\"},\n" +
            "            {\"Name\": \"珠海\"},\n" +
            "            {\"Name\": \"佛山\"},\n" +
            "            {\"Name\": \"肇庆\"},\n" +
            "            {\"Name\": \"湛江\"},\n" +
            "            {\"Name\": \"江门\"},\n" +
            "            {\"Name\": \"河源\"},\n" +
            "            {\"Name\": \"清远\"},\n" +
            "            {\"Name\": \"云浮\"},\n" +
            "            {\"Name\": \"潮州\"},\n" +
            "            {\"Name\": \"东莞\"},\n" +
            "            {\"Name\": \"中山\"},\n" +
            "            {\"Name\": \"阳江\"},\n" +
            "            {\"Name\": \"揭阳\"},\n" +
            "            {\"Name\": \"茂名\"},\n" +
            "            {\"Name\": \"汕尾\"}\n" +
            "        ],\n" +
            "        \"Name\": \"广东\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"南京\"},\n" +
            "            {\"Name\": \"无锡\"},\n" +
            "            {\"Name\": \"镇江\"},\n" +
            "            {\"Name\": \"苏州\"},\n" +
            "            {\"Name\": \"南通\"},\n" +
            "            {\"Name\": \"扬州\"},\n" +
            "            {\"Name\": \"盐城\"},\n" +
            "            {\"Name\": \"徐州\"},\n" +
            "            {\"Name\": \"淮安\"},\n" +
            "            {\"Name\": \"连云港\"},\n" +
            "            {\"Name\": \"常州\"},\n" +
            "            {\"Name\": \"泰州\"},\n" +
            "            {\"Name\": \"宿迁\"}\n" +
            "        ],\n" +
            "        \"Name\": \"江苏\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"兰州\"},\n" +
            "            {\"Name\": \"定西\"},\n" +
            "            {\"Name\": \"平凉\"},\n" +
            "            {\"Name\": \"庆阳\"},\n" +
            "            {\"Name\": \"武威\"},\n" +
            "            {\"Name\": \"金昌\"},\n" +
            "            {\"Name\": \"张掖\"},\n" +
            "            {\"Name\": \"酒泉\"},\n" +
            "            {\"Name\": \"天水\"},\n" +
            "            {\"Name\": \"陇南\"},\n" +
            "            {\"Name\": \"临夏\"},\n" +
            "            {\"Name\": \"甘南\"},\n" +
            "            {\"Name\": \"白银\"},\n" +
            "            {\"Name\": \"嘉峪关\"}\n" +
            "        ],\n" +
            "        \"Name\": \"甘肃\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"郑州\"},\n" +
            "            {\"Name\": \"安阳\"},\n" +
            "            {\"Name\": \"新乡\"},\n" +
            "            {\"Name\": \"许昌\"},\n" +
            "            {\"Name\": \"平顶山\"},\n" +
            "            {\"Name\": \"信阳\"},\n" +
            "            {\"Name\": \"南阳\"},\n" +
            "            {\"Name\": \"开封\"},\n" +
            "            {\"Name\": \"洛阳\"},\n" +
            "            {\"Name\": \"商丘\"},\n" +
            "            {\"Name\": \"焦作\"},\n" +
            "            {\"Name\": \"鹤壁\"},\n" +
            "            {\"Name\": \"濮阳\"},\n" +
            "            {\"Name\": \"周口\"},\n" +
            "            {\"Name\": \"漯河\"},\n" +
            "            {\"Name\": \"驻马店\"},\n" +
            "            {\"Name\": \"三门峡\"},\n" +
            "            {\"Name\": \"济源\"}\n" +
            "        ],\n" +
            "        \"Name\": \"河南\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"海口\"},\n" +
            "            {\"Name\": \"三亚\"},\n" +
            "            {\"Name\": \"东方\"},\n" +
            "            {\"Name\": \"临高\"},\n" +
            "            {\"Name\": \"澄迈\"},\n" +
            "            {\"Name\": \"儋州\"},\n" +
            "            {\"Name\": \"昌江\"},\n" +
            "            {\"Name\": \"白沙\"},\n" +
            "            {\"Name\": \"琼中\"},\n" +
            "            {\"Name\": \"定安\"},\n" +
            "            {\"Name\": \"屯昌\"},\n" +
            "            {\"Name\": \"琼海\"},\n" +
            "            {\"Name\": \"文昌\"},\n" +
            "            {\"Name\": \"保亭\"},\n" +
            "            {\"Name\": \"万宁\"},\n" +
            "            {\"Name\": \"陵水\"},\n" +
            "            {\"Name\": \"西沙\"},\n" +
            "            {\"Name\": \"南沙\"},\n" +
            "            {\"Name\": \"乐东\"},\n" +
            "            {\"Name\": \"五指山\"}\n" +
            "        ],\n" +
            "        \"Name\": \"海南\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"南宁\"},\n" +
            "            {\"Name\": \"崇左\"},\n" +
            "            {\"Name\": \"柳州\"},\n" +
            "            {\"Name\": \"来宾\"},\n" +
            "            {\"Name\": \"桂林\"},\n" +
            "            {\"Name\": \"梧州\"},\n" +
            "            {\"Name\": \"贺州\"},\n" +
            "            {\"Name\": \"贵港\"},\n" +
            "            {\"Name\": \"玉林\"},\n" +
            "            {\"Name\": \"百色\"},\n" +
            "            {\"Name\": \"钦州\"},\n" +
            "            {\"Name\": \"河池\"},\n" +
            "            {\"Name\": \"北海\"},\n" +
            "            {\"Name\": \"防城港\"}\n" +
            "        ],\n" +
            "        \"Name\": \"广西\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"沈阳\"},\n" +
            "            {\"Name\": \"大连\"},\n" +
            "            {\"Name\": \"鞍山\"},\n" +
            "            {\"Name\": \"抚顺\"},\n" +
            "            {\"Name\": \"本溪\"},\n" +
            "            {\"Name\": \"丹东\"},\n" +
            "            {\"Name\": \"锦州\"},\n" +
            "            {\"Name\": \"营口\"},\n" +
            "            {\"Name\": \"阜新\"},\n" +
            "            {\"Name\": \"辽阳\"},\n" +
            "            {\"Name\": \"铁岭\"},\n" +
            "            {\"Name\": \"朝阳\"},\n" +
            "            {\"Name\": \"盘锦\"},\n" +
            "            {\"Name\": \"葫芦岛\"}\n" +
            "        ],\n" +
            "        \"Name\": \"辽宁\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"长沙\"},\n" +
            "            {\"Name\": \"湘潭\"},\n" +
            "            {\"Name\": \"株洲\"},\n" +
            "            {\"Name\": \"衡阳\"},\n" +
            "            {\"Name\": \"郴州\"},\n" +
            "            {\"Name\": \"常德\"},\n" +
            "            {\"Name\": \"益阳\"},\n" +
            "            {\"Name\": \"娄底\"},\n" +
            "            {\"Name\": \"邵阳\"},\n" +
            "            {\"Name\": \"岳阳\"},\n" +
            "            {\"Name\": \"张家界\"},\n" +
            "            {\"Name\": \"怀化\"},\n" +
            "            {\"Name\": \"永州\"},\n" +
            "            {\"Name\": \"湘西\"}\n" +
            "        ],\n" +
            "        \"Name\": \"湖南\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"拉萨\"},\n" +
            "            {\"Name\": \"日喀则\"},\n" +
            "            {\"Name\": \"山南\"},\n" +
            "            {\"Name\": \"林芝\"},\n" +
            "            {\"Name\": \"昌都\"},\n" +
            "            {\"Name\": \"那曲\"},\n" +
            "            {\"Name\": \"阿里\"}\n" +
            "        ],\n" +
            "        \"Name\": \"西藏\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"哈尔滨\"},\n" +
            "            {\"Name\": \"齐齐哈尔\"},\n" +
            "            {\"Name\": \"牡丹江\"},\n" +
            "            {\"Name\": \"佳木斯\"},\n" +
            "            {\"Name\": \"绥化\"},\n" +
            "            {\"Name\": \"黑河\"},\n" +
            "            {\"Name\": \"大兴安岭\"},\n" +
            "            {\"Name\": \"伊春\"},\n" +
            "            {\"Name\": \"大庆\"},\n" +
            "            {\"Name\": \"七台河\"},\n" +
            "            {\"Name\": \"鸡西\"},\n" +
            "            {\"Name\": \"鹤岗\"},\n" +
            "            {\"Name\": \"双鸭山\"}\n" +
            "        ],\n" +
            "        \"Name\": \"黑龙江\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"贵阳\"},\n" +
            "            {\"Name\": \"遵义\"},\n" +
            "            {\"Name\": \"安顺\"},\n" +
            "            {\"Name\": \"黔南\"},\n" +
            "            {\"Name\": \"黔东南\"},\n" +
            "            {\"Name\": \"铜仁\"},\n" +
            "            {\"Name\": \"毕节\"},\n" +
            "            {\"Name\": \"六盘水\"},\n" +
            "            {\"Name\": \"黔西南\"}\n" +
            "        ],\n" +
            "        \"Name\": \"贵州\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"武汉\"},\n" +
            "            {\"Name\": \"襄阳\"},\n" +
            "            {\"Name\": \"鄂州\"},\n" +
            "            {\"Name\": \"孝感\"},\n" +
            "            {\"Name\": \"黄冈\"},\n" +
            "            {\"Name\": \"黄石\"},\n" +
            "            {\"Name\": \"咸宁\"},\n" +
            "            {\"Name\": \"荆州\"},\n" +
            "            {\"Name\": \"宜昌\"},\n" +
            "            {\"Name\": \"恩施\"},\n" +
            "            {\"Name\": \"十堰\"},\n" +
            "            {\"Name\": \"神农架\"},\n" +
            "            {\"Name\": \"随州\"},\n" +
            "            {\"Name\": \"荆门\"},\n" +
            "            {\"Name\": \"天门\"},\n" +
            "            {\"Name\": \"仙桃\"},\n" +
            "            {\"Name\": \"潜江\"}\n" +
            "        ],\n" +
            "        \"Name\": \"湖北\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"福州\"},\n" +
            "            {\"Name\": \"厦门\"},\n" +
            "            {\"Name\": \"宁德\"},\n" +
            "            {\"Name\": \"莆田\"},\n" +
            "            {\"Name\": \"泉州\"},\n" +
            "            {\"Name\": \"漳州\"},\n" +
            "            {\"Name\": \"龙岩\"},\n" +
            "            {\"Name\": \"三明\"},\n" +
            "            {\"Name\": \"南平\"},\n" +
            "            {\"Name\": \"钓鱼岛\"}\n" +
            "        ],\n" +
            "        \"Name\": \"福建\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"成都\"},\n" +
            "            {\"Name\": \"攀枝花\"},\n" +
            "            {\"Name\": \"自贡\"},\n" +
            "            {\"Name\": \"绵阳\"},\n" +
            "            {\"Name\": \"南充\"},\n" +
            "            {\"Name\": \"达州\"},\n" +
            "            {\"Name\": \"遂宁\"},\n" +
            "            {\"Name\": \"广安\"},\n" +
            "            {\"Name\": \"巴中\"},\n" +
            "            {\"Name\": \"泸州\"},\n" +
            "            {\"Name\": \"宜宾\"},\n" +
            "            {\"Name\": \"内江\"},\n" +
            "            {\"Name\": \"资阳\"},\n" +
            "            {\"Name\": \"乐山\"},\n" +
            "            {\"Name\": \"眉山\"},\n" +
            "            {\"Name\": \"凉山\"},\n" +
            "            {\"Name\": \"雅安\"},\n" +
            "            {\"Name\": \"甘孜\"},\n" +
            "            {\"Name\": \"阿坝\"},\n" +
            "            {\"Name\": \"德阳\"},\n" +
            "            {\"Name\": \"广元\"}\n" +
            "        ],\n" +
            "        \"Name\": \"四川\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"呼和浩特\"},\n" +
            "            {\"Name\": \"包头\"},\n" +
            "            {\"Name\": \"乌海\"},\n" +
            "            {\"Name\": \"乌兰察布\"},\n" +
            "            {\"Name\": \"通辽\"},\n" +
            "            {\"Name\": \"兴安盟\"},\n" +
            "            {\"Name\": \"赤峰\"},\n" +
            "            {\"Name\": \"鄂尔多斯\"},\n" +
            "            {\"Name\": \"巴彦淖尔\"},\n" +
            "            {\"Name\": \"锡林郭勒\"},\n" +
            "            {\"Name\": \"呼伦贝尔\"},\n" +
            "            {\"Name\": \"阿拉善盟\"}\n" +
            "        ],\n" +
            "        \"Name\": \"内蒙古\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"西安\"},\n" +
            "            {\"Name\": \"咸阳\"},\n" +
            "            {\"Name\": \"延安\"},\n" +
            "            {\"Name\": \"榆林\"},\n" +
            "            {\"Name\": \"渭南\"},\n" +
            "            {\"Name\": \"商洛\"},\n" +
            "            {\"Name\": \"安康\"},\n" +
            "            {\"Name\": \"汉中\"},\n" +
            "            {\"Name\": \"宝鸡\"},\n" +
            "            {\"Name\": \"铜川\"},\n" +
            "            {\"Name\": \"杨凌\"}\n" +
            "        ],\n" +
            "        \"Name\": \"陕西\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"长春\"},\n" +
            "            {\"Name\": \"吉林\"},\n" +
            "            {\"Name\": \"延边\"},\n" +
            "            {\"Name\": \"四平\"},\n" +
            "            {\"Name\": \"通化\"},\n" +
            "            {\"Name\": \"白城\"},\n" +
            "            {\"Name\": \"辽源\"},\n" +
            "            {\"Name\": \"松原\"},\n" +
            "            {\"Name\": \"白山\"}\n" +
            "        ],\n" +
            "        \"Name\": \"吉林\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"济南\"},\n" +
            "            {\"Name\": \"青岛\"},\n" +
            "            {\"Name\": \"淄博\"},\n" +
            "            {\"Name\": \"德州\"},\n" +
            "            {\"Name\": \"烟台\"},\n" +
            "            {\"Name\": \"潍坊\"},\n" +
            "            {\"Name\": \"济宁\"},\n" +
            "            {\"Name\": \"泰安\"},\n" +
            "            {\"Name\": \"临沂\"},\n" +
            "            {\"Name\": \"菏泽\"},\n" +
            "            {\"Name\": \"滨州\"},\n" +
            "            {\"Name\": \"东营\"},\n" +
            "            {\"Name\": \"威海\"},\n" +
            "            {\"Name\": \"枣庄\"},\n" +
            "            {\"Name\": \"日照\"},\n" +
            "            {\"Name\": \"莱芜\"},\n" +
            "            {\"Name\": \"聊城\"}\n" +
            "        ],\n" +
            "        \"Name\": \"山东\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"乌鲁木齐\"},\n" +
            "            {\"Name\": \"克拉玛依\"},\n" +
            "            {\"Name\": \"石河子\"},\n" +
            "            {\"Name\": \"昌吉\"},\n" +
            "            {\"Name\": \"吐鲁番\"},\n" +
            "            {\"Name\": \"巴音郭楞\"},\n" +
            "            {\"Name\": \"阿拉尔\"},\n" +
            "            {\"Name\": \"阿克苏\"},\n" +
            "            {\"Name\": \"喀什\"},\n" +
            "            {\"Name\": \"伊犁\"},\n" +
            "            {\"Name\": \"塔城\"},\n" +
            "            {\"Name\": \"哈密\"},\n" +
            "            {\"Name\": \"和田\"},\n" +
            "            {\"Name\": \"阿勒泰\"},\n" +
            "            {\"Name\": \"克州\"},\n" +
            "            {\"Name\": \"博尔塔拉\"}\n" +
            "        ],\n" +
            "        \"Name\": \"新疆\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"合肥\"},\n" +
            "            {\"Name\": \"蚌埠\"},\n" +
            "            {\"Name\": \"芜湖\"},\n" +
            "            {\"Name\": \"淮南\"},\n" +
            "            {\"Name\": \"马鞍山\"},\n" +
            "            {\"Name\": \"安庆\"},\n" +
            "            {\"Name\": \"宿州\"},\n" +
            "            {\"Name\": \"阜阳\"},\n" +
            "            {\"Name\": \"亳州\"},\n" +
            "            {\"Name\": \"黄山\"},\n" +
            "            {\"Name\": \"滁州\"},\n" +
            "            {\"Name\": \"淮北\"},\n" +
            "            {\"Name\": \"铜陵\"},\n" +
            "            {\"Name\": \"宣城\"},\n" +
            "            {\"Name\": \"六安\"},\n" +
            "            {\"Name\": \"巢湖\"},\n" +
            "            {\"Name\": \"池州\"}\n" +
            "        ],\n" +
            "        \"Name\": \"安徽\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"杭州\"},\n" +
            "            {\"Name\": \"湖州\"},\n" +
            "            {\"Name\": \"嘉兴\"},\n" +
            "            {\"Name\": \"宁波\"},\n" +
            "            {\"Name\": \"绍兴\"},\n" +
            "            {\"Name\": \"台州\"},\n" +
            "            {\"Name\": \"温州\"},\n" +
            "            {\"Name\": \"丽水\"},\n" +
            "            {\"Name\": \"金华\"},\n" +
            "            {\"Name\": \"衢州\"},\n" +
            "            {\"Name\": \"舟山\"}\n" +
            "        ],\n" +
            "        \"Name\": \"浙江\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"西宁\"},\n" +
            "            {\"Name\": \"海东\"},\n" +
            "            {\"Name\": \"黄南\"},\n" +
            "            {\"Name\": \"海南\"},\n" +
            "            {\"Name\": \"果洛\"},\n" +
            "            {\"Name\": \"玉树\"},\n" +
            "            {\"Name\": \"海西\"},\n" +
            "            {\"Name\": \"海北\"},\n" +
            "            {\"Name\": \"格尔木\"}\n" +
            "        ],\n" +
            "        \"Name\": \"青海\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"太原\"},\n" +
            "            {\"Name\": \"大同\"},\n" +
            "            {\"Name\": \"阳泉\"},\n" +
            "            {\"Name\": \"晋中\"},\n" +
            "            {\"Name\": \"长治\"},\n" +
            "            {\"Name\": \"晋城\"},\n" +
            "            {\"Name\": \"临汾\"},\n" +
            "            {\"Name\": \"运城\"},\n" +
            "            {\"Name\": \"朔州\"},\n" +
            "            {\"Name\": \"忻州\"},\n" +
            "            {\"Name\": \"吕梁\"}\n" +
            "        ],\n" +
            "        \"Name\": \"山西\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"石家庄\"},\n" +
            "            {\"Name\": \"保定\"},\n" +
            "            {\"Name\": \"张家口\"},\n" +
            "            {\"Name\": \"承德\"},\n" +
            "            {\"Name\": \"唐山\"},\n" +
            "            {\"Name\": \"廊坊\"},\n" +
            "            {\"Name\": \"沧州\"},\n" +
            "            {\"Name\": \"衡水\"},\n" +
            "            {\"Name\": \"邢台\"},\n" +
            "            {\"Name\": \"邯郸\"},\n" +
            "            {\"Name\": \"秦皇岛\"}\n" +
            "        ],\n" +
            "        \"Name\": \"河北\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"香港\"}],\n" +
            "        \"Name\": \"香港\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [{\"Name\": \"澳门\"}],\n" +
            "        \"Name\": \"澳门\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"Citys\": [\n" +
            "            {\"Name\": \"台北\"},\n" +
            "            {\"Name\": \"高雄\"},\n" +
            "            {\"Name\": \"台中\"}\n" +
            "        ],\n" +
            "        \"Name\": \"台湾\"\n" +
            "    },\n" +
            "]}";
}
