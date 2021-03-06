package com.qiyei.mall.goodsmanager.mvp.view

import com.qiyei.framework.mvp.view.IBaseView
import com.qiyei.mall.goodsmanager.data.bean.Category

/**
 * @author Created by qiyei2015 on 2018/10/5.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
interface ICategoryManagerView:IBaseView {

    /**
     * 获取到分类的回调
     */
    fun onCategoryResult(result:MutableList<Category>?)

}