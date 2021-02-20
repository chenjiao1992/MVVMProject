package com.cj.skin.activity

import android.accounts.Account
import android.content.Intent
import android.view.View
import com.cj.mvvmproject.AccountManager
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivitySkinBinding
import skin.support.SkinCompatManager

/**
 *  Create by chenjiao at 2021/2/19 0019
 *  描述：换肤页面
 */
class SkinActivity : BaseBindingActivity<ActivitySkinBinding>() {
    override fun getLayoutId() = R.layout.activity_skin

    override fun onConfig(arguments: Intent?) {
        binding.onClick = View.OnClickListener {
            if (it.id == R.id.changeThemeGreen) {
             var   themeName=  AccountManager.themeName
                if(themeName=="green"){
                    SkinCompatManager.getInstance().restoreDefaultTheme()
                    AccountManager.themeName=""
                    return@OnClickListener
                }
                SkinCompatManager.getInstance().loadSkin("green", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
                AccountManager.themeName="green"
            }
        }
    }

}