package com.cj.library.helper;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class RefreshHelper {
    private long mRefreshThreshold = 0L;
    private boolean mFirstEnter = true;
    private boolean mResume = false;
    private long mPauseTime = 0L;


    void setRefreshThreshold(long refreshThreshold) {
        if (refreshThreshold >= 0L) {
            mRefreshThreshold = refreshThreshold;
        } else {
            mRefreshThreshold = 0L;
        }
    }

    void pause() {
        mResume = false;
        if (mRefreshThreshold >= 0L) {
            mPauseTime = System.currentTimeMillis();
        }

    }

    void resume() {
        mResume = true;
    }

    boolean isResume = mResume;

    void destory() {
        mResume = false;
        mFirstEnter = true;
    }

    boolean firstEnter() {
        if (mFirstEnter) {
            mFirstEnter = false;
            return true;
        }
        return false;
    }

    boolean shouldRefresh() {
        if (mRefreshThreshold >= 0L) {
            return System.currentTimeMillis() - mPauseTime > mRefreshThreshold;
        }
        return false;
    }
}
