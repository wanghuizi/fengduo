package com.fengduo.bee.service.interfaces;

import java.util.List;

import com.fengduo.bee.model.entity.ConsigneeAddr;

/**
 * 收货人地址service
 * 
 * @author jie.xu
 * @date 2015年6月23日 上午10:18:35
 */
public interface ConsigneeAddrService {

    /**
     * 查询用户下的地址信息
     */
    List<ConsigneeAddr> listAddrByUserId(Long userId);

    /**
     * 添加收货人地址
     */
    ConsigneeAddr insert(ConsigneeAddr consigneeAddr);

    /**
     * 设地址为默认
     */
    boolean setAddrDefault(Long id);

    /**
     * 删除地址
     */
    boolean delAddr(Long id);
}
