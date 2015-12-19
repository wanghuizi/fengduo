/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.interfaces;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemComment;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemFull;
import com.fengduo.bee.model.entity.ItemMember;

/**
 * 产品服务接口
 * 
 * @author zxc Jun 9, 2015 1:22:54 PM
 */
public interface ItemService {

    // 产品基础信息 产品融资信息连表查询
    ItemFull findItemFull(Parameter q);

    List<ItemFull> listItemFull(Parameter q);

    PaginationList<ItemFull> listPaginationItemFull(Parameter q, IPageUrl... i);

    // ////////////////////////////////////////////////////////////////////////////////////////
    // //////////产品信息表(Item)
    // ////////////////////////////////////////////////////////////////////////////////////////

    Item findItem(Parameter q);

    List<Item> listItem(Parameter q);

    PaginationList<Item> listPaginationItem(Parameter q, IPageUrl... i);

    Item getItemById(Long id);

    Long add(Item... t);

    boolean update(Item t);

    boolean deleteItem(Long id);

    boolean realDeleteItem(Long id);

    // ////////////////////////////////////////////////////////////////////////////////////////
    // //////////产品融资信息表(ItemFinance)
    // ////////////////////////////////////////////////////////////////////////////////////////

    ItemFinance findItemFinance(Parameter q);

    List<ItemFinance> listItemFinance(Parameter q);

    PaginationList<ItemFinance> listPaginationItemFinance(Parameter q, IPageUrl... i);

    ItemFinance getItemFinanceById(Long id);

    ItemFinance getItemFinanceByItemId(Long itemId);

    Long add(ItemFinance... t);

    boolean update(ItemFinance t);

    boolean deleteItemFinance(Long id);

    boolean realDeleteItemFinance(Long id);

    // ////////////////////////////////////////////////////////////////////////////////////////
    // //////////产品团队信息表(ItemMember)
    // ////////////////////////////////////////////////////////////////////////////////////////

    ItemMember findItemMember(Parameter q);

    List<ItemMember> listItemMember(Parameter q);

    PaginationList<ItemMember> listPaginationItemMember(Parameter q, IPageUrl... i);

    ItemMember getItemMemberById(Long id);

    List<ItemMember> getItemMemberByItemId(Long itemId);

    Long add(ItemMember... t);

    boolean update(ItemMember t);

    boolean deleteItemMember(Long id);

    boolean realDeleteItemMember(Long id);

    // ////////////////////////////////////////////////////////////////////////////////////////
    // //////////产品讨论信息表(ItemComment)
    // ////////////////////////////////////////////////////////////////////////////////////////

    ItemComment findItemComment(Parameter q);

    List<ItemComment> listItemComment(Parameter q);

    PaginationList<ItemComment> listPaginationItemComment(Parameter q, IPageUrl... i);

    ItemComment getItemCommentById(Long id);

    List<ItemComment> getItemCommentByItemId(Long itemId);

    Long add(ItemComment... t);

    boolean update(ItemComment t);

    boolean deleteItemComment(Long id);

    boolean realDeleteItemComment(Long id);
}
