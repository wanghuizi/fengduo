/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.commons.pagination;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fengduo.bee.commons.cons.SearchCons;

/**
 * 提供一些分页的方法,主要是针对使用缓存时的分页
 * 
 * @author zxc May 28, 2015 4:47:53 PM
 */
public class PaginationParser {

    /**
     * 分页的页面URL自定义接口
     * 
     * @author zxc Jul 6, 2015 4:03:23 PM
     */
    public interface IPageUrl {

        /**
         * 不同实例的分页页面需要重写此方法
         * 
         * @param clazz
         * @return
         */
        public String parsePageUrl(Object... objs);
    }

    /**
     * 默认URL链接为空的形式
     * 
     * @author zxc Jul 6, 2015 4:02:57 PM
     */
    public static class DefaultIpageUrl implements IPageUrl {

        @Override
        public String parsePageUrl(Object... objs) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 默认以分页页码结尾的URL链接形式
     * 
     * <pre>
     * 例如:
     *  /my/xxx/2 
     *  /item/3
     * 其中:
     *  url 为传入的自定义URL链接前缀
     * </pre>
     * 
     * @author zxc Jul 6, 2015 4:02:26 PM
     */
    public static class UrlDefaultIpageUrl implements IPageUrl {

        private String url;

        public UrlDefaultIpageUrl(String url) {
            this.url = url;
        }

        @Override
        public String parsePageUrl(Object... objs) {
            Integer page = (Integer) objs[1];
            if (StringUtils.lastIndexOf(url, "/") > 0) {
                return url + "/" + (page == null ? 1 : page);
            } else {
                return url + (page == null ? 1 : page);
            }
        }
    }

    /**
     * @param page
     * @param pageSize
     * @param totalNum
     * @param iPageUrl URL策略
     * @return
     */
    public static PagesPagination getPaginationList(Integer page, int pageSize, int totalNum, IPageUrl iPageUrl) {
        PagesPagination pagesPagination = getPagination(page, pageSize, totalNum);
        initPages(pagesPagination, iPageUrl);
        return pagesPagination;
    }

    /**
     * @param page 当前页码
     * @param pageSize 每页大小
     * @param totalNum 总记录数
     * @return
     */
    public static PagesPagination getPagination(Integer page, int pageSize, int totalNum) {
        return new PagesPagination(pageSize, page, totalNum);
    }

    // /**
    // * 初始化分页的页码URL
    // *
    // * @param pagination
    // * @param url
    // */
    // public static void initPages(PagesPagination pagination, IPageUrl url) {
    // initPages(-1, pagination, url);
    // }
    //
    // /**
    // * 初始化分页的页码URL
    // *
    // * @param pagination
    // * @param url
    // * @param data
    // */
    // public static void initPages(PagesPagination pagination, IPageUrl url, Object... data) {
    // initPages(-1, pagination, url, data);
    // }

    /**
     * 生成分页信息,implements IPageUrl
     * 
     * @param pagination
     */
    public static void initPages(PagesPagination pagination, IPageUrl iPageUrl, Object... objs) {

        int index = pagination.getFirstPageIndex();
        PageInfo firstpage = new PageInfo(SearchCons.FIRSTPAGE, index + 1, pagination.isFirstPage(),
                                          iPageUrl.parsePageUrl(objs, index + 1));
        pagination.setFirstPage(firstpage);
        // 上一页
        index = pagination.getPrevPageIndex();
        PageInfo prevpage = new PageInfo(SearchCons.PREPAGE, index + 1, false, iPageUrl.parsePageUrl(objs, index + 1));
        pagination.setPrevPage(prevpage);
        // 下一页
        index = pagination.getNextPageIndex();
        PageInfo nextpage = new PageInfo(SearchCons.NEXTPAGE, index + 1, false, iPageUrl.parsePageUrl(objs, index + 1));
        pagination.setNextPage(nextpage);
        // 尾页
        index = pagination.getLastPageIndex();
        PageInfo lastpage = new PageInfo(SearchCons.LASTPAGE, index + 1, pagination.isLastPage(),
                                         iPageUrl.parsePageUrl(objs, index + 1));
        pagination.setLastPage(lastpage);
        //
        int nowPageIndex = pagination.getNowPageIndex();
        List<PageInfo> pages = new ArrayList<PageInfo>();
        List<Integer> skipPageIndexs = pagination.getSkipPageIndex();
        for (Integer integer : skipPageIndexs) {
            pages.add(new PageInfo(StringUtils.EMPTY + (integer + 1), integer + 1, nowPageIndex == integer,
                                   iPageUrl.parsePageUrl(objs, integer + 1)));
        }
        pagination.setPages(pages);
    }

    /**
     * 对所有数据进行分页,每页默认10个
     * 
     * @param <T>
     * @param all 所有数据
     * @param nowPageIndex 第几页(1,2,3,4......)
     * @return
     */
    public static <T> PaginationList<T> getPaginationList(List<T> all, int nowPageIndex) {
        return getPaginationList(all, nowPageIndex, Pagination.DEFAULT_PAGESIZE);
    }

    /**
     * 对所有数据进行分页
     * 
     * @param <T>
     * @param all 所有数据
     * @param nowPageIndex 第几页(1,2,3,4......)
     * @param pageSize 每页数量
     * @return
     */
    public static <T> PaginationList<T> getPaginationList(List<T> all, int nowPageIndex, int pageSize) {
        return getPaginationList(all, new Pagination(), nowPageIndex, pageSize);
    }

    /**
     * 对所有数据进行分页
     * 
     * @param <T>
     * @param all 所有数据
     * @param nowPageIndex 第几页(1,2,3,4......)
     * @param pageSize 每页数量
     * @return
     */
    public static <T> PaginationList<T> getPaginationList(List<T> all, Pagination pagination, int nowPageIndex,
                                                          int pageSize) {
        if (all == null || pagination == null) {
            return null;
        }
        int size = all.size();
        nowPageIndex = nowPageIndex >= 1 ? nowPageIndex - 1 : 0;
        pageSize = pageSize > 0 ? pageSize : Pagination.DEFAULT_PAGESIZE;
        pagination.setNowPageIndex(nowPageIndex);
        pagination.setPageSize(pageSize);
        pagination.init(size);
        int start = pagination.getStartRecordIndex();
        int end = pagination.getEndRecordIndex();
        end = end > size ? size : end;
        start = start - 1 >= 0 ? start - 1 : 0;
        end = end - 1;
        PaginationList<T> page = new PaginationList<T>(pagination);
        for (int i = start; i <= end; i++) {
            page.add(all.get(i));
        }
        return page;
    }
}
