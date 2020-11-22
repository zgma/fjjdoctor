package com.qingeng.fjjdoctor.user;

import com.contrarywind.interfaces.IPickerViewData;
import com.qingeng.apilibrary.bean.BaseBean;

import java.util.List;

public class CityBean extends BaseBean implements IPickerViewData {

    /**
     * name : 城市
     * area : ["东城区","西城区","崇文区","昌平区"]
     */

    private String name;
    private List<String> area;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
