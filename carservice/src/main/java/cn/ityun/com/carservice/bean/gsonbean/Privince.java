package cn.ityun.com.carservice.bean.gsonbean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class Privince {
    public String msg;
    public type result;
    public class type{
        public ArrayList<Sheng> data;
        public class  Sheng{
            public String carorg;
            public String engineno;
            public String frameno;
            public String lsnum;
            public String lsprefix;
            public String province;
        }
    }

}
