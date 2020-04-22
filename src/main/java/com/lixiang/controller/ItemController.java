package com.lixiang.controller;

import com.alibaba.druid.util.StringUtils;
import com.lixiang.controller.viewobject.ItemVO;
import com.lixiang.error.BussinessException;
import com.lixiang.response.CommonReturnType;
import com.lixiang.service.ItemService;
import com.lixiang.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value="/create",method={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType creatItem(@RequestParam(name="title")String title,
                                      @RequestParam(name="description")String description,
                                      @RequestParam(name="price")BigDecimal price,
                                      @RequestParam(name="stock")Integer stock,
                                      @RequestParam(name="imgUrl")String imgUrl) throws BussinessException {
        //封装service请求用来创建商品
        ItemModel itemModel=new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO=convertVOFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVO);
    }
    //商品列表页面浏览
    @RequestMapping(value="/list",method={RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList=itemService.listItem();
        List<ItemVO> itemVOList=itemModelList.stream().map(itemModel -> {
            ItemVO itemVO=this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);

    }
    //商品详情页浏览
    @RequestMapping(value="/get",method={RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name="id")Integer id){
        ItemModel itemModel=itemService.getItemById(id);
        ItemVO itemVO=convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private  ItemVO convertVOFromModel(ItemModel itemModel){
        ItemVO itemVO=new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }
}
