/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 产品融资信息
 * 
 * <pre>
 *     id                   bigint not null auto_increment,
 *     create_date         timestamp not null default 0,
 *     update_date         timestamp not null default 0,
 *     
 *     item_id             bigint  not null        COMMENT '众筹产品项目id',
 *     amount              int(6)                  COMMENT '融资金额(单位是万元)',
 *     percent             float                   COMMENT '出让股份百分比(单位%)',
 *     stock               int(6)                  COMMENT '出让多少股份数,投资人个数限制',
 *     capital_uses        varchar(2048)           COMMENT '资金用途',
 *     pdf_url             varchar(512)            COMMENT '融资计划书url',
 *     ex_finance          text                    COMMENT '融资资料',
 *     per_stock           float                   COMMENT '每份多少钱(单位是万元)',
 *     per_percent         float                   COMMENT '每份占股多少百分比(单位%)',
 *     real_sub            float   default 0       COMMENT '投资人实际已认购金额(仅支付保证金)',
 *     real_pay            float   default 0       COMMENT '投资人实际已支付额(全额支付)',
 *     suber_num           int     default 0       COMMENT '认购的人数(已交保证金的用户)',统计认购表状态值为2的
 * </pre>
 * 
 * @author zxc Jun 10, 2015 3:19:05 PM
 */
public class ItemFinance implements Serializable {

	private static final long serialVersionUID = -9034360867298295376L;

	private Long id;
	private Date createDate;
	private Date updateDate;

	private Long itemId;

	private Integer amount;
	private Float percent;
	private Integer stock;
	private String capitalUses;
	private String pdfUrl;
	private String exFinance;
	private Float perStock;
	private Float perPercent;

	private Float realSub;
	private Float realPay;
	private Integer suberNum;

	private Float realPercent; // 投资人实际已经认购股份

	public ItemFinance() {

	}

	public Float getPerStock() {
		Float realPerStock = 0.00f;
		if (stock == null || stock <= 0) {
			return realPerStock;
		}
		if (amount == null || amount <= 0) {
			return realPerStock;
		}
		realPerStock = (float) (amount / stock);
		perStock = realPerStock;
		return perStock;
	}

	public void setPerStock(Float perStock) {
		this.perStock = perStock;
	}

	public Float getPerPercent() {
		Float realPerPercent = 0.00f;
		if (stock == null || stock <= 0) {
			return realPerPercent;
		}
		if (percent == null || percent <= 0) {
			return realPerPercent;
		}
		realPerPercent = (float) (percent / stock);
		perPercent = realPerPercent;
		return perPercent;
	}

	public void setPerPercent(Float perPercent) {
		this.perPercent = perPercent;
	}

	public Float getRealPercent() {
		return realPercent == null ? 0.0f : realPercent;
	}

	public void setRealPercent(Float realPercent) {
		this.realPercent = realPercent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Float getRealSub() {
		return realSub == null ? 0.0f : realSub;
	}

	public void setRealSub(Float realSub) {
		this.realSub = realSub;
	}

	public Float getRealPay() {
		return realPay == null ? 0.0f : realPay;
	}

	public void setRealPay(Float realPay) {
		this.realPay = realPay;
	}

	public Integer getSuberNum() {
		return suberNum == null ? 0 : suberNum;
	}

	public void setSuberNum(Integer suberNum) {
		this.suberNum = suberNum;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getAmount() {
		return amount == null ? 0 : amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Float getPercent() {
		return percent == null ? 0.0f : percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}

	public String getCapitalUses() {
		return capitalUses;
	}

	public void setCapitalUses(String capitalUses) {
		this.capitalUses = capitalUses;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getExFinance() {
		return exFinance;
	}

	public void setExFinance(String exFinance) {
		this.exFinance = exFinance;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/*
	 * 是否融资额已达到目标融资额
	 */
	public boolean isReachAmount() {
		try {
			float amountFloat = Float.parseFloat(amount + "");
			return amountFloat <= realPay.floatValue();
		} catch (Exception e) {
			return false;
		}
	}
}
