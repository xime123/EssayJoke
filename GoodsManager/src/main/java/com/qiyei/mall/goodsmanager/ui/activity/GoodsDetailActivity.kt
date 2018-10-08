package com.qiyei.mall.goodsmanager.ui.activity

import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import android.view.View
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.qiyei.framework.titlebar.TabLayoutTitleBar
import com.qiyei.framework.ui.activity.BaseActivity
import com.qiyei.mall.goodsmanager.R
import com.qiyei.mall.goodsmanager.common.GoodsConstant
import com.qiyei.mall.goodsmanager.event.AddCartEvent
import com.qiyei.mall.goodsmanager.event.UpdateCartCountEvent
import com.qiyei.mall.goodsmanager.ui.adapter.GoodsDetailViewPagerAdapter
import com.qiyei.sdk.dc.DataManager
import kotlinx.android.synthetic.main.activity_goods_detail.*
import org.jetbrains.anko.toast
import q.rorbin.badgeview.QBadgeView

class GoodsDetailActivity : BaseActivity() {

    private lateinit var mTabLayoutTitleBar: TabLayoutTitleBar
    /**
     * 购物车角标
     */
    private lateinit var mCartBadge:QBadgeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_detail)
        initView()
        initObserver()
        loadCartCount()
    }

    override fun getTAG(): String {
        return GoodsDetailActivity::class.java.simpleName
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when(view.id){
            R.id.mShareTextView -> {
                toast("分享")
            }
            R.id.mCartTextView -> {
                toast("购物车")
            }
            R.id.mAddToCartButton -> {
                Bus.send(AddCartEvent())
                toast("添加到购物车")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }

    private fun initView(){
        mGoodsViewPager.adapter = GoodsDetailViewPagerAdapter(supportFragmentManager,this)
        mTabLayoutTitleBar = TabLayoutTitleBar.Builder(this)
                .setupWithViewPager(mGoodsViewPager)
                .build()

        mShareTextView.setOnClickListener(this)
        mCartTextView.setOnClickListener(this)
        mAddToCartButton.setOnClickListener(this)
        mCartBadge = QBadgeView(this)

    }

    private fun initObserver(){
        Bus.observe<UpdateCartCountEvent>()
                .subscribe {
                    updateCartBadge()
                }.registerInBus(this)
    }

    private fun loadCartCount(){
        updateCartBadge()
    }

    /**
     * 更新购物车
     */
    private fun updateCartBadge() {
        mCartBadge.badgeGravity = Gravity.END or Gravity.TOP
        mCartBadge.setGravityOffset(22f,-2f,true)
        mCartBadge.setBadgeTextSize(6f,true)
        val cartUri = DataManager.getInstance().getUri(GoodsConstant.javaClass,GoodsConstant.SP_CART_SIZE)
        mCartBadge.bindTarget(mCartTextView).badgeNumber = DataManager.getInstance().getInt(cartUri,0)
        toast(mCartBadge.bindTarget(mCartTextView).badgeNumber.toString())
    }

}