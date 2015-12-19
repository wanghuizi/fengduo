/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.item;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.lang.ArrayUtils;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.data.dao.ItemCommentDao;
import com.fengduo.bee.data.dao.ItemDao;
import com.fengduo.bee.data.dao.ItemFinanceDao;
import com.fengduo.bee.data.dao.ItemMemberDao;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemComment;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemFull;
import com.fengduo.bee.model.entity.ItemMember;
import com.fengduo.bee.service.interfaces.ItemService;

/**
 * 产品服务接口实现
 * 
 * @author zxc Jun 9, 2015 1:25:24 PM
 */
@Service
public class ItemServiceImpl extends BaseService implements ItemService {

    @Autowired
    private ItemDao        itemDao;
    @Autowired
    private ItemMemberDao  itemMemberDao;
    @Autowired
    private ItemFinanceDao itemFinanceDao;
    @Autowired
    private ItemCommentDao itemCommentDao;

    @Override
    public ItemFull findItemFull(Parameter q) {
        if (q == null) {
            return null;
        }
        return itemDao.findFull(q);
    }

    @Override
    public List<ItemFull> listItemFull(Parameter q) {
        if (q == null) {
            return Collections.<ItemFull> emptyList();
        }
        return itemDao.listFull(q);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<ItemFull> listPaginationItemFull(Parameter q, IPageUrl... ipageUrls) {
        return pagination(q, itemDao, "countFull", "listPaginationFull", ipageUrls);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 产品信息表(Item)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Item findItem(Parameter q) {
        if (q == null) {
            return null;
        }
        return itemDao.find(q);
    }

    @Override
    public List<Item> listItem(Parameter q) {
        if (q == null) {
            return Collections.<Item> emptyList();
        }
        return itemDao.list(q);
    }

    /**
     * int pageNumber, int pageSize, String sortType
     */
    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<Item> listPaginationItem(Parameter q, IPageUrl... ipageUrls) {
        return pagination(q, itemDao, ipageUrls);
    }

    @Override
    public Item getItemById(Long id) {
        if (Argument.isNotPositive(id)) {
            return null;
        }
        return itemDao.getById(id);
    }

    @Override
    public Long add(Item... t) {
        t = ArrayUtils.removeNullElement(t);
        if (Argument.isEmptyArray(t)) {
            return 0l;
        }
        Integer count = 0;
        for (Item item : t) {
            count = itemDao.insert(item);
            if (t.length == 1) {
                return 1l;
            }
        }
        return count == 0 ? 0l : 1l;
    }

    @Override
    public boolean update(Item t) {
        if (t == null) {
            return false;
        }
        if (Argument.isNotPositive(t.getId())) {
            return false;
        }
        Integer count = itemDao.updateById(t);
        return count == 0 ? false : true;
    }

    @Override
    public boolean deleteItem(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        boolean isSuccess = update(new Item(id, DelFlagEnum.DELETE));
        return isSuccess;
    }

    @Override
    public boolean realDeleteItem(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        Integer count = itemDao.deleteById(id);
        return count == 0 ? false : true;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 产品团队信息表(ItemMember)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemMember findItemMember(Parameter q) {
        if (q == null) {
            return null;
        }
        return itemMemberDao.find(q);
    }

    @Override
    public List<ItemMember> listItemMember(Parameter q) {
        if (q == null) {
            return Collections.<ItemMember> emptyList();
        }
        return itemMemberDao.list(q);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<ItemMember> listPaginationItemMember(Parameter q, IPageUrl... ipageUrls) {
        return pagination(q, itemMemberDao, ipageUrls);
    }

    @Override
    public ItemMember getItemMemberById(Long id) {
        if (Argument.isNotPositive(id)) {
            return null;
        }
        return itemMemberDao.getById(id);
    }

    @Override
    public Long add(ItemMember... t) {
        t = ArrayUtils.removeNullElement(t);
        if (Argument.isEmptyArray(t)) {
            return 0l;
        }
        Integer count = 0;
        for (ItemMember itemMember : t) {
            count = itemMemberDao.insert(itemMember);
            if (t.length == 1) {
                return 1l;
            }
        }
        return count == 0 ? 0l : 1l;
    }

    @Override
    public boolean update(ItemMember t) {
        if (t == null) {
            return false;
        }
        if (Argument.isNotPositive(t.getId())) {
            return false;
        }
        Integer count = itemMemberDao.updateById(t);
        return count == 0 ? false : true;
    }

    @Override
    public boolean deleteItemMember(Long id) {
        return true;
    }

    @Override
    public boolean realDeleteItemMember(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        Integer count = itemMemberDao.deleteById(id);
        return count == 0 ? false : true;
    }

    @Override
    public List<ItemMember> getItemMemberByItemId(Long itemId) {
        Parameter query = new Parameter();
        query.put("itemId", itemId);
        return listItemMember(query);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 产品融资信息表(ItemFinance)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemFinance findItemFinance(Parameter q) {
        if (q == null) {
            return null;
        }
        return itemFinanceDao.find(q);
    }

    @Override
    public List<ItemFinance> listItemFinance(Parameter q) {
        if (q == null) {
            return Collections.<ItemFinance> emptyList();
        }
        return itemFinanceDao.list(q);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<ItemFinance> listPaginationItemFinance(Parameter q, IPageUrl... ipageUrls) {
        return pagination(q, itemFinanceDao, ipageUrls);
    }

    @Override
    public ItemFinance getItemFinanceById(Long id) {
        if (Argument.isNotPositive(id)) {
            return null;
        }
        return itemFinanceDao.getById(id);
    }

    @Override
    public Long add(ItemFinance... t) {
        t = ArrayUtils.removeNullElement(t);
        if (Argument.isEmptyArray(t)) {
            return 0l;
        }
        Integer count = 0;
        for (ItemFinance itemFinance : t) {
            count = itemFinanceDao.insert(itemFinance);
            if (t.length == 1) {
                return 1l;
            }
        }
        return count == 0 ? 0l : 1l;
    }

    @Override
    public boolean update(ItemFinance t) {
        if (t == null) {
            return false;
        }
        if (Argument.isNotPositive(t.getId())) {
            return false;
        }
        Integer count = itemFinanceDao.updateById(t);
        return count == 0 ? false : true;
    }

    @Override
    public boolean deleteItemFinance(Long id) {
        return true;
    }

    @Override
    public boolean realDeleteItemFinance(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        Integer count = itemFinanceDao.deleteById(id);
        return count == 0 ? false : true;
    }

    @Override
    public ItemFinance getItemFinanceByItemId(Long itemId) {
        Parameter query = new Parameter();
        query.put("itemId", itemId);
        return findItemFinance(query);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 产品讨论信息表(ItemComment)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemComment findItemComment(Parameter q) {
        if (q == null) {
            return null;
        }
        return itemCommentDao.find(q);
    }

    @Override
    public List<ItemComment> listItemComment(Parameter q) {
        if (q == null) {
            return Collections.<ItemComment> emptyList();
        }
        return itemCommentDao.list(q);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<ItemComment> listPaginationItemComment(Parameter q, IPageUrl... i) {
        return pagination(q, itemCommentDao, i);
    }

    @Override
    public ItemComment getItemCommentById(Long id) {
        if (Argument.isNotPositive(id)) {
            return null;
        }
        return itemCommentDao.getById(id);
    }

    @Override
    public Long add(ItemComment... t) {
        t = ArrayUtils.removeNullElement(t);
        if (Argument.isEmptyArray(t)) {
            return 0l;
        }
        Integer count = 0;
        for (ItemComment itemComment : t) {
            count = itemCommentDao.insert(itemComment);
            if (t.length == 1) {
                return 1l;
            }
        }
        return count == 0 ? 0l : 1l;
    }

    @Override
    public boolean update(ItemComment t) {
        if (t == null) {
            return false;
        }
        if (Argument.isNotPositive(t.getId())) {
            return false;
        }
        Integer count = itemCommentDao.updateById(t);
        return count == 0 ? false : true;
    }

    @Override
    public boolean deleteItemComment(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        boolean isSuccess = update(new ItemComment(id, DelFlagEnum.DELETE));
        return isSuccess;
    }

    @Override
    public boolean realDeleteItemComment(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        Integer count = itemCommentDao.deleteById(id);
        return count == 0 ? false : true;
    }

    @Override
    public List<ItemComment> getItemCommentByItemId(Long itemId) {
        Parameter query = new Parameter();
        query.put("itemId", itemId);
        return listItemComment(query);
    }
}
