package cn.ityun.com.carservice.bean.gsonbean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class IllegaInfo {
    public info result;
    public String msg;

    public class info {
        public String carorg;
        public String lsnum;
        public String lsprefix;
        public ArrayList<jutiInfo> list;

        public class jutiInfo {
            public String address;
            public String content;
            public String time;
            public int  price;
            public int  score;
        }
    }
}
