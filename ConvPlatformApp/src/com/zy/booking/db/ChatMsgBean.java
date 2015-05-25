
package com.zy.booking.db;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.zy.booking.R;

import java.util.List;

@Table(name = "chathistory", execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON chathistory(msgid)")
public class ChatMsgBean extends EntityBase {

    public static final String DBNAME = "chathistory";
    public static final String NAME = "name";
    public static final String MSGCONTENT = "msgcontent";
    public static final String MSGTIME = "msgtime";
    public static final String TYPE = "type"; // 单聊还是群聊
    public static final String ISREAD = "isread";
    public static final String MSGID = "msgid";
    public static final String CONTENTTYPE = "contenttype";
    public static final String COLUMN0 = "column0";
    public static final String COLUMN1 = "column1";
    public static final String COLUMN2 = "column2";
    public static final String MSGFROM = "msgfrom";
    public static final String MSGTO = "msgto";
    public static final String VOICELENGTH = "voicelength";
    public static final String GID = "gid";

    @Column(column = "name", defaultValue = "")
    public String name;

    @Column(column = "msgcontent", defaultValue = "")
    public String msgContent;

    @Column(column = "msgtime")
    public long msgTime;

    @Column(column = "type")
    public String type; // 单聊还是群聊 单聊是0 群聊是1

    @Column(column = "msgfrom")
    public String msgFrom;

    @Column(column = "isread", defaultValue = "0")
    public String isRead; // 0 为未读

    @Column(column = "msgid", defaultValue = "0")
    public String msgId = System.currentTimeMillis() + "";

    @Column(column = "contenttype")
    public String contentType = "text";

    @Column(column = "column0", defaultValue = "")
    public String column0;

    @Column(column = "column1",defaultValue = "")
    public String column1;  //for messagetype

    @Column(column = "column2")
    public String column2;
    // select * from chathistory where isread = '0' group by msgId

    @Column(column = "msgto")
    public String msgTo;

    @Column(column = "voicelength")
    public int voicelength;

    @Column(column = "gid")
    public String gid;

    // public String iconUrl ;

    DBUSER mdbDbuser = new DBUSER();

    public void updateChatMsgBean(String mId, Context mContext) {
        // 从user 表中更新信息到 数据中来

        if(TextUtils.isEmpty(mId)){
            return ;
        }
        
        mId =mId.split("@")[0];
        String mUpdateSql = mContext.getString(R.string.select_userinfo_baseuid);

        List<User> mUsers = mdbDbuser.selectDataFromDb(String.format(mUpdateSql, mId));
        if (mUsers != null && mUsers.size() > 0) {
            User mUser = mUsers.get(0);
            this.column0 = mUser.photo;
            this.name = mUser.nickname;
        }

    }

}
