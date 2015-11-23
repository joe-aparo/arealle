package net.jsa.arealle.task.pojo;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import net.jsa.crib.ds.utils.type.ToBgdUtils;
import net.jsa.crib.ds.utils.type.ToFltUtils;
import net.jsa.crib.ds.utils.type.ToLngUtils;

public class ListingInfo {
	private static final char DELIMITER = '|';
	
	private String sourceId;
	private String listingId;
	private String detailUrl;
	private String imageUrl;
	private String address;
	private Long price;
	private String type;
	private String status;
	private Long bedCt;
	private BigDecimal bathCt;
	private Long bayCt;
	private String broker;

	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String mlsId) {
		this.listingId = mlsId;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getBedCt() {
		return bedCt;
	}
	public void setBedCt(Long bedCt) {
		this.bedCt = bedCt;
	}
	public BigDecimal getBathCt() {
		return bathCt;
	}
	public void setBathCt(BigDecimal bathCt) {
		this.bathCt = bathCt;
	}
	public Long getBayCt() {
		return bayCt;
	}
	public void setBayCt(Long bayCt) {
		this.bayCt = bayCt;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(nullToStr(sourceId));
		buf.append(DELIMITER);
		buf.append(nullToStr(listingId));
		buf.append(DELIMITER);
		buf.append(nullToStr(detailUrl));
		buf.append(DELIMITER);
		buf.append(nullToStr(imageUrl));
		buf.append(DELIMITER);
		buf.append(nullToStr(address));
		buf.append(DELIMITER);
		buf.append(nullToStr(price));
		buf.append(DELIMITER);
		buf.append(nullToStr(type));
		buf.append(DELIMITER);
		buf.append(nullToStr(status));
		buf.append(DELIMITER);
		buf.append(nullToStr(bedCt));
		buf.append(DELIMITER);
		buf.append(nullToStr(bathCt));
		buf.append(DELIMITER);
		buf.append(nullToStr(bayCt));
		buf.append(DELIMITER);
		buf.append(nullToStr(broker));
		
		return buf.toString();
	}
	
	public void fromString(String str) {
		String[] parts = StringUtils.splitPreserveAllTokens(str, DELIMITER);
		
		sourceId = mkStr(parts[0]);
		listingId = mkStr(parts[1]);
		detailUrl = mkStr(parts[2]);
		imageUrl = mkStr(parts[3]);
		address = mkStr(parts[4]);
		price = mkLong(parts[5]);
		type = mkStr(parts[6]);
		status = mkStr(parts[7]);
		bedCt = mkLong(parts[8]);
		bathCt = mkBgd(parts[9]);
		bayCt = mkLong(parts[10]);
		broker = mkStr(parts[11]);
	}
	
	private String mkStr(String str) {
		return (!StringUtils.isEmpty(str) ? str : null);
	}
	
	private Long mkLong(String str) {
		return (!StringUtils.isEmpty(str) ? ToLngUtils.str2Lng(str) : null);
	}
	
	private Float mkFlt(String str) {
		return (!StringUtils.isEmpty(str) ? ToFltUtils.str2Flt(str) : null);
	}
	
	private BigDecimal mkBgd(String str) {
		return (!StringUtils.isEmpty(str) ? ToBgdUtils.str2Bgd(str) : null);
	}
	
	private String nullToStr(Object val) {
		return val != null ? val.toString() : "";
	}
	
}
