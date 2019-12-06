package com.cj.library.helper;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：记录生命周期相关的变量
 */
public class RefreshHelper {
    private long mRefreshThreshold = 0L;
    private boolean mFirstEnter = true;
    private boolean mResume = false;
    private long mPauseTime = 0L;


    public void setRefreshThreshold(long refreshThreshold) {
        if (refreshThreshold >= 0L) {
            mRefreshThreshold = refreshThreshold;
        } else {
            mRefreshThreshold = 0L;
        }
    }

    public void pause() {
        mResume = false;
        if (mRefreshThreshold >= 0L) {
            mPauseTime = System.currentTimeMillis();
        }

    }

    public boolean isResume() {
        return mResume;
    }

    public void resume() {
        mResume = true;
    }

    boolean isResume = mResume;

    public void destory() {
        mResume = false;
        mFirstEnter = true;
    }

    public boolean firstEnter() {
        if (mFirstEnter) {
            mFirstEnter = false;
            return true;
        }
        return false;
    }

    public boolean shouldRefresh() {
        if (mRefreshThreshold >= 0L) {
            return System.currentTimeMillis() - mPauseTime > mRefreshThreshold;
        }
        return false;
    }
}
