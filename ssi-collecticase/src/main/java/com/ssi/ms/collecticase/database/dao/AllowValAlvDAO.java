package com.ssi.ms.collecticase.database.dao;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "ALLOW_VAL_ALV")
@Data
public class AllowValAlvDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALV_ID", unique = true, nullable = false)
    private Long alvId;
    @Column(name = "ALV_ACTIVE_IND", length = 1, nullable = false)
    private String alvActiveInd;
    @Column(name = "ALV_RELATED_CD", precision = 4)
    private Long alvRelatedCd;
    @Column(name = "ALV_SHORT_DESC_TXT", length = 50)
    private String alvShortDecTxt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ALC_ID")
    private AllowCatAlcDAO allowCatAlcDAO;

    @Column(name = "FK_ALV_ID", precision = 4)
    private Long fkAlvId;
    @Column(name = "ALV_DECIPHER_CODE", length = 20)
    private String alvDecipherCode;
    @Column(name = "ALV_SORT_ORDER_NBR", precision = 4)
    private Long alvSortOrderNbr;
    @Column(name = "ALV_LONG_DESC_TXT", length = 1000)
    private String alvLongDescTxt;
    @Column(name = "ALV_SP_SHORT_DESC_TXT", length = 200)
    private String alvSpShortDescTxt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALV_DISPLAY_ON")
    private AllowValAlvDAO alvDisplayOnAlvDao;

    @Column(name = "ALV_CHANGE_LOG", length = 200)
    private String alvChangeLog;

    public String getAlvShortDecTxt() {
        return StringUtils.trimToEmpty(alvShortDecTxt);
    }

    public String getAlvLongDescTxt() {
        return StringUtils.trimToNull(alvLongDescTxt);
    }

    public String getAlvSpShortDescTxt() {
        return StringUtils.trimToNull(alvSpShortDescTxt);
    }

    public String getAlvChangeLog() {
        return StringUtils.trimToNull(alvChangeLog);
    }

    public Long getAlvId() {
        return alvId;
    }

    public void setAlvId(Long alvId) {
        this.alvId = alvId;
    }

    public String getAlvActiveInd() {
        return alvActiveInd;
    }

    public void setAlvActiveInd(String alvActiveInd) {
        this.alvActiveInd = alvActiveInd;
    }

    public Long getAlvRelatedCd() {
        return alvRelatedCd;
    }

    public void setAlvRelatedCd(Long alvRelatedCd) {
        this.alvRelatedCd = alvRelatedCd;
    }

    public void setAlvShortDecTxt(String alvShortDecTxt) {
        this.alvShortDecTxt = alvShortDecTxt;
    }

    public String getAlvDecipherCode() {
        return alvDecipherCode;
    }

    public void setAlvDecipherCode(String alvDecipherCode) {
        this.alvDecipherCode = alvDecipherCode;
    }

    public Long getAlvSortOrderNbr() {
        return alvSortOrderNbr;
    }

    public void setAlvSortOrderNbr(Long alvSortOrderNbr) {
        this.alvSortOrderNbr = alvSortOrderNbr;
    }

    public void setAlvLongDescTxt(String alvLongDescTxt) {
        this.alvLongDescTxt = alvLongDescTxt;
    }

    public void setAlvSpShortDescTxt(String alvSpShortDescTxt) {
        this.alvSpShortDescTxt = alvSpShortDescTxt;
    }

    public AllowValAlvDAO getAlvDisplayOnAlvDao() {
        return alvDisplayOnAlvDao;
    }

    public void setAlvDisplayOnAlvDao(AllowValAlvDAO alvDisplayOnAlvDao) {
        this.alvDisplayOnAlvDao = alvDisplayOnAlvDao;
    }

    public void setAlvChangeLog(String alvChangeLog) {
        this.alvChangeLog = alvChangeLog;
    }

    public AllowCatAlcDAO getAllowCatAlcDAO() {
        return allowCatAlcDAO;
    }

    public void setAllowCatAlcDAO(AllowCatAlcDAO allowCatAlcDAO) {
        this.allowCatAlcDAO = allowCatAlcDAO;
    }

    public Long getFkAlvId() {
        return fkAlvId;
    }

    public void setFkAlvId(Long fkAlvId) {
        this.fkAlvId = fkAlvId;
    }
}
