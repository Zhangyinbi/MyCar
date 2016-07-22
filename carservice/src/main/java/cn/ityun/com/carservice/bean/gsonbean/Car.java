package cn.ityun.com.carservice.bean.gsonbean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class Car {
    public Type result;
    public class Type{
            public ArrayList<Info> branditems;
        public class Info{
            public String name;
        }
    }
}
