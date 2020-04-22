package com.lixiang.service;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.lixiang.error.BussinessException;
import com.lixiang.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BussinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);
}
