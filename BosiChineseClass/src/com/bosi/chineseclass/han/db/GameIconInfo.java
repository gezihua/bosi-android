package com.bosi.chineseclass.han.db;


public class GameIconInfo extends EntityBase {

    private int type;
    private String icon_path;

    public GameIconInfo() {
        super();
    }

    public GameIconInfo(int type, String iconPath) {
        super();
        this.type = type;
        this.icon_path = iconPath;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getIconPath() {
        return icon_path;
    }
    public void setIconPath(String iconPath) {
        this.icon_path = iconPath;
    }

    public static final String TYPE = "type";
    public static final String ICON_PATH= "icon_path";

}
