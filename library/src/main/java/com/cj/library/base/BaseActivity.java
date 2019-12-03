//package com.cj.library.base;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.cj.library.model.DataObserver;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.Observer;
//
///**
// Create by chenjiao at 2019/12/3 0003
// 描述：
// */
//public abstract class BaseActivity extends AppCompatActivity implements IBaseFramework,IActivityData,IActivityOtherFragmentData {
//    @Override
//    public void onConfig(Bundle arguments) {
//
//    }
//
//    @Override
//    public void onPrepared() {
//
//    }
//
//    @Override
//    public View doInflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return null;
//    }
//
//    @Override
//    public void onRefresh() {
//
//    }
//
//    @Override
//    public boolean isActivity() {
//        return false;
//    }
//
//    @Override
//    public FragmentActivity getActivity() {
//        return null;
//    }
//
//    @Override
//    public FragmentManager getFragmentManagerEx() {
//        return null;
//    }
//
//    @Override
//    public void setRefreshThreshold(Long refreshThreshold) {
//
//    }
//
//    @Override
//    public void setToolbar(Toolbar toolbar) {
//
//    }
//
//    @Override
//    public Toolbar getToolbar() {
//        return null;
//    }
//
//    @Override
//    public void onToolbarPrepare(Toolbar toolbar, Menu menu) {
//
//    }
//
//    @Override
//    public void onToolbarNavigationClick() {
//
//    }
//
//    @Override
//    public boolean onToolbarMenuItemClick(MenuItem menuItem) {
//        return false;
//    }
//
//    @Override
//    public void observeData(String key, Observer<Object> observer) {
//
//    }
//
//    @Override
//    public void observeData(DataObserver observer) {
//
//    }
//
//    @Override
//    public void dispatchData(String key, Object object) {
//
//    }
//
//    @Override
//    public Object queryData(String key) {
//        return null;
//    }
//
//    @Override
//    public void observeCommand(String name, Observer observer) {
//
//    }
//
//    @Override
//    public void observeCommand(DataObserver observer) {
//
//    }
//
//    @Override
//    public void dispatchCommand(String name, Object data) {
//
//    }
//
//    @Override
//    public void onReceiveDataFromFragment(String tag, String name, Object data) {
//
//    }
//
//    @Override
//    public void onReceiveOtherFragmentData(String senderTag, String receiverTag, String name, Object data) {
//
//    }
//
//    @Override
//    public Object onQueryOtherFragmentData(String senderTag, String receiverTag, String name, Object data) {
//        return null;
//    }
//
//    @Override
//    public void sendDataToFragment(String tag, String name, Object data) {
//
//    }
//
//    @Override
//    public Object queryFragmentData(String tag, String name, Object data) {
//        return null;
//    }
//
//    @Override
//    public Object onQueryDataFromFragment(String tag, String name, Object data) {
//        return null;
//    }
//}
