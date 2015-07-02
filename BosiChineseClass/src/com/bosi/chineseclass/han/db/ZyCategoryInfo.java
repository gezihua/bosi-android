package com.bosi.chineseclass.han.db;


public class ZyCategoryInfo extends EntityBase {

    private int type;
    private String icon_path;
    private String web_path_id;
    private String title;
    private String title_background_path;

    public ZyCategoryInfo() {
        super();
    }

    public ZyCategoryInfo(int type, String iconPath, String title, String title_background_path) {
        super();
        this.type = type;
        this.icon_path = iconPath;
        this.title = title;
        this.title_background_path = title_background_path;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIconPath() {
        return icon_path;
    }
    public void setIconPath(String iconPath) {
        this.icon_path = iconPath;
    }

    public String getWeb_path_id() {
        return web_path_id;
    }

    public void setWeb_path_id(String web_path_id) {
        this.web_path_id = web_path_id;
    }

    public String getTitle_background_path() {
        return title_background_path;
    }

    public void setTitle_background_path(String title_background_path) {
        this.title_background_path = title_background_path;
    }


    public static final String TYPE = "type";
    public static final String ICON_PATH= "icon_path";
    public static final String WEB_PATH_ID = "web_path_id";
    public static final String TITLE = "title";
    public static final String TITLE_BACKGROUND_PATH = "title_background_path";

}
