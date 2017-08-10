package com.dk.mp.csyxy.ui.hy.db;

import android.content.Context;

import com.dk.mp.core.entity.Bm;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.core.entity.Ks;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by cobb on 2017/8/10.
 */

public class YellowRealmHelper {

    private Realm mRealm;

    public YellowRealmHelper(Context context){
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * 批量新增部门
     * @param d
     */
    public void addDepartment(final List<Bm> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * 查找部门
     * @return
     */
    public List<Bm> queryAllDepartment(){
        RealmResults<Bm> jbxxs = mRealm.where(Bm.class).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * delete部门 （删）
     */
    public void deleteDepartment() {
        RealmResults<Bm> d = mRealm.where(Bm.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * 批量新增部门
     * @param d
     */
    public void addPersons(final List<Ks> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * delete指定部门下的人员（删）
     */
    public void deletePersonsByDepartmentid(String departmentid) {
        RealmResults<Ks> d = mRealm.where(Ks.class).equalTo("departmentid",departmentid).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * 查询指定部门下人员
     * @return
     */
    public List<Ks> queryPersonsByDepartmentid(String departmentid){
        RealmResults<Ks> jbxxs = mRealm.where(Ks.class).equalTo("departmentid",departmentid).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }
}
