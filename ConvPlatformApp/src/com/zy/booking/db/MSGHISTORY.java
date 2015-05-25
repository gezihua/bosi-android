
package com.zy.booking.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

//emsg 消息数据库操作
public class MSGHISTORY extends AbsDbOperation {

    private final String DBNAME = "chathistory";


    // 根据sql 查询相关数据集合
    @Override
    public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
        List<DbModel> dbModels = null;
        ArrayList<ChatMsgBean> mData = new ArrayList<ChatMsgBean>();
        try {

            dbModels = mDbManager.getContentDb().findDbModelAll(sql);
            for (DbModel mDbModle : dbModels) {
                ChatMsgBean mChatMsgBean = new ChatMsgBean();
                mChatMsgBean.msgContent = mDbModle
                        .getString(ChatMsgBean.MSGCONTENT);
                mChatMsgBean.isRead = mDbModle.getString(ChatMsgBean.ISREAD);
                mChatMsgBean.contentType = mDbModle
                        .getString(ChatMsgBean.CONTENTTYPE);
                mChatMsgBean.msgTime = mDbModle.getLong(ChatMsgBean.MSGTIME);
                mChatMsgBean.msgFrom = mDbModle.getString(ChatMsgBean.MSGFROM);
                mChatMsgBean.name = mDbModle.getString(ChatMsgBean.NAME);
                mChatMsgBean.type = mDbModle.getString(ChatMsgBean.TYPE);
                mChatMsgBean.msgTo = mDbModle.getString(ChatMsgBean.MSGTO);
                mChatMsgBean.column0 = mDbModle.getString(ChatMsgBean.COLUMN0);
                mChatMsgBean.column1 = mDbModle.getString(ChatMsgBean.COLUMN1);
                mChatMsgBean.column2 = mDbModle.getString(ChatMsgBean.COLUMN2);
                mChatMsgBean.gid = mDbModle.getString(ChatMsgBean.GID);
                mChatMsgBean.voicelength = mDbModle
                        .getInt(ChatMsgBean.VOICELENGTH);
                mData.add(mChatMsgBean);
            }
        } catch (DbException e) {
            System.out.println(e);
        } finally {
            if (dbModels != null) {
                dbModels.clear();
                dbModels = null;
            }

        }
        return (T) mData;

    }

    // 根据sql 查询 单独的一条数据
    public EntityBase getEntityBaseFromSql(String sql) {
        List<EntityBase> mList = selectDataFromDb(sql);
        if (mList != null && mList.size() > 0)
            return mList.get(0);
        return null;
    }

    /**
     * 获取所有的未读信息数量
     */
    public String getUnReadMsgCount(String selectSql) {
        String unReadMsgCount = "0";
        Cursor mCursor = null;
        try {
            mCursor = mDbManager
                    .getContentDb()
                    .execQuery(
                            selectSql);
            mCursor.moveToPosition(-1);
            while (mCursor.moveToNext()) {
                unReadMsgCount = mCursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null)
                mCursor.close();
        }

        return unReadMsgCount;
    }

    public boolean updateAllUnReadStateByMsgFrom(String msgFrom, List<ChatMsgBean> mDatas) {
        boolean isUpDateAllSuccessFul = false;
        try {
            for (ChatMsgBean mChatBean : mDatas) {
                mChatBean.isRead = "1";
            }
            mDbManager
                    .getContentDb().updateAll(mDatas,
                            WhereBuilder.b(ChatMsgBean.MSGFROM, "=", msgFrom), ChatMsgBean.ISREAD);
            isUpDateAllSuccessFul = true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return isUpDateAllSuccessFul;
    }

    public boolean updateAllNotifyPanelMsg(List<ChatMsgBean> mDatas) {
        boolean isUpDateAllSuccessFul = false;
        try {
            mDbManager
                    .getContentDb().updateAll(mDatas);
            isUpDateAllSuccessFul = true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return isUpDateAllSuccessFul;
    }

    @Override
    public String getDbName() {
        // TODO Auto-generated method stub
        return ChatMsgBean.DBNAME;
    }
}
