package net.archeryc.radarview;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/6/22 10:25
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class RadarUserEntity {
    private int type_sex;
    private String img_url;
    private String name;
    private int uid;
    private int lv;

    public int getType_sex() {
        return type_sex;
    }

    public void setType_sex(int type_sex) {
        this.type_sex = type_sex;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }
}
