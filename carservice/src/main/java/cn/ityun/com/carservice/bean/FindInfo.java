package cn.ityun.com.carservice.bean;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class FindInfo {
    private int imageId;
    private String name;
    private String detail;

    public FindInfo(int imageId, String name, String detail) {
        this.imageId = imageId;
        this.name = name;
        this.detail = detail;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public FindInfo() {


    }
}
