package com.dk.edu.core.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 作者：janabo on 2017/1/4 15:52
 */
public abstract class BaseLazyFragment extends Fragment {

    protected Context mContext = null;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    @Override
    public void onActivityCreated ( Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );
        initPrepare ( );
    }

    @Override
    public void onAttach ( Context context ) {
        super.onAttach ( context );
        mContext=context;
    }

    @Deprecated
    @Override
    public void onResume ( ) {
        super.onResume ( );
        if ( isFirstResume ) {
            isFirstResume = false;
            return;
        }
        if ( getUserVisibleHint ( ) ) {
            onUserVisible ( );
        }
    }
    @Deprecated
    @Override
    public void onPause ( ) {
        super.onPause ( );
        if ( getUserVisibleHint ( ) ) {
            onUserInvisible ( );
        }
    }

    @Override
    public void setUserVisibleHint ( boolean isVisibleToUser ) {
        super.setUserVisibleHint ( isVisibleToUser );
        if ( isVisibleToUser ) {
            if ( isFirstVisible ) {
                isFirstVisible = false;
                initPrepare ( );
            } else {
                onUserVisible ( );
            }
        } else {
            if ( isFirstInvisible ) {
                isFirstInvisible = false;
                onFirstUserInvisible ( );
            } else {
                onUserInvisible ( );
            }
        }
    }

    public synchronized void initPrepare ( ) {
        if ( isPrepared ) {
            onFirstUserVisible ( );
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible ( ) {

    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible ( ) {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible ( ) {

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible ( ) {

    }
}
