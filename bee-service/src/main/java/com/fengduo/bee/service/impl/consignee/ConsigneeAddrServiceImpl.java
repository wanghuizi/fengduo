package com.fengduo.bee.service.impl.consignee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.data.dao.ConsigneeAddrDao;
import com.fengduo.bee.model.cons.AddrDefaultFlagEnum;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.entity.ConsigneeAddr;
import com.fengduo.bee.service.interfaces.ConsigneeAddrService;

/**
 * 收货人地址service
 * 
 * @author jie.xu
 * @date 2015年6月22日 下午10:24:49
 */
@Service("consigneeAddrService")
public class ConsigneeAddrServiceImpl extends BaseService implements ConsigneeAddrService {

    @Autowired
    private ConsigneeAddrDao consigneeAddrDao;

    @Override
    public ConsigneeAddr insert(ConsigneeAddr consigneeAddr) {
        // 构造查询条件
        Parameter query = Parameter.newParameter()//
        .pu("userId", consigneeAddr.getUserId())//
        .pu("defaultFlag", AddrDefaultFlagEnum.YES.getValue())//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
        ConsigneeAddr defaultAddr = consigneeAddrDao.find(query);
        if (defaultAddr != null) {
            consigneeAddr.setDefaultFlag(AddrDefaultFlagEnum.NO.getValue());
        } else {
            consigneeAddr.setDefaultFlag(AddrDefaultFlagEnum.YES.getValue());
        }
        consigneeAddrDao.insert(consigneeAddr);
        return consigneeAddr;
    }

    @Override
    public boolean setAddrDefault(Long id) {
        ConsigneeAddr addr = consigneeAddrDao.getById(id);
        if (addr.getDefaultFlag() == AddrDefaultFlagEnum.YES.getValue()) {
            return false;
        }
        // 构造查询条件
        Parameter query = Parameter.newParameter()//
        .pu("userId", addr.getUserId())//
        .pu("defaultFlag", AddrDefaultFlagEnum.YES.getValue())//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
        ConsigneeAddr defaultAddr = consigneeAddrDao.find(query);
        // 将原有的默认地址设为非默认
        if (defaultAddr != null) {
            ConsigneeAddr updateAddr = new ConsigneeAddr();
            updateAddr.setId(defaultAddr.getId());
            updateAddr.setDefaultFlag(AddrDefaultFlagEnum.NO.getValue());
            int i = consigneeAddrDao.updateById(updateAddr);
            if (i <= 0) {
                return false;
            }
        }
        addr.setDefaultFlag(AddrDefaultFlagEnum.YES.getValue());
        return consigneeAddrDao.updateById(addr) > 0;
    }

    @Override
    public boolean delAddr(Long id) {
        ConsigneeAddr updateAddr = new ConsigneeAddr();
        updateAddr.setId(id);
        updateAddr.setDelFlag(DelFlagEnum.DELETE.getValue());
        return consigneeAddrDao.updateById(updateAddr) > 0;
    }

    @Override
    public List<ConsigneeAddr> listAddrByUserId(Long userId) {
        Parameter query = Parameter.newParameter()//
        .pu("userId", userId)//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
        return consigneeAddrDao.list(query);
    }
}
