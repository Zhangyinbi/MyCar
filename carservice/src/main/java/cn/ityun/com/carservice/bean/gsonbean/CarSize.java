package cn.ityun.com.carservice.bean.gsonbean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class CarSize {
    public String msg;
    public ArrayList<Size> result;
    public class Size {
        public String name;
        public String code;
    }
}
