package com.ssi.ms.database.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "ALLOW_VAL_ALV")
public class AllowValAlv implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6545275126077485860L;
    private Long alvId;
    // private Long fkAlcId;
    private AllowCatAlc allowCatAlc;
    // private Long fkAlvId;
    private String alvActiveInd;
    private Long alvRelatedCd;
    private String alvShortDescTxt;
    private String alvDecipherCode;
    private Long alvSortOrderNbr;
    private String alvLongDescTxt;
    private String alvSpShortDescTxt;

    @Id
    @Column(name = "ALV_ID", unique = true, nullable = false, precision = 4, scale = 0)
    public Long getAlvId() {
        return this.alvId;
    }

    public void setAlvId(final Long alvId) {
        this.alvId = alvId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ALC_ID", nullable = false)
    public AllowCatAlc getAllowCatAlc() {
        return this.allowCatAlc;
    }

    public void setAllowCatAlc(final AllowCatAlc allowCatAlc) {
        this.allowCatAlc = allowCatAlc;
    }

    // @Column(name = "FK_ALC_ID")
    // public Long getFkAlcId() {
    // return fkAlcId;
    // }
    //
    //
    // public void setFkAlcId(Long fkAlcId) {
    // this.fkAlcId = fkAlcId;
    // }

    // @Column(name = "FK_ALV_ID")
    // public Long getFkAlvId() {
    // return fkAlvId;
    // }
    //
    // public void setFkAlvId(Long fkAlvId) {
    // this.fkAlvId = fkAlvId;
    // }

    @Column(name = "ALV_ACTIVE_IND")
    public String getAlvActiveInd() {
        return this.alvActiveInd;
    }

    public void setAlvActiveInd(final String alvActiveInd) {
        this.alvActiveInd = alvActiveInd;
    }

    @Column(name = "ALV_RELATED_CD")
    public Long getAlvRelatedCd() {
        return this.alvRelatedCd;
    }

    public void setAlvRelatedCd(final Long alvRelatedCd) {
        this.alvRelatedCd = alvRelatedCd;
    }

    @Column(name = "ALV_SHORT_DESC_TXT")
    public String getAlvShortDescTxt() {
        return this.alvShortDescTxt;
    }

    public void setAlvShortDescTxt(final String alvShortDescTxt) {
        this.alvShortDescTxt = alvShortDescTxt;
    }

    @Column(name = "ALV_DECIPHER_CODE")
    public String getAlvDecipherCode() {
        return this.alvDecipherCode;
    }

    public void setAlvDecipherCode(final String alvDecipherCode) {
        this.alvDecipherCode = alvDecipherCode;
    }

    @Column(name = "ALV_SORT_ORDER_NBR")
    public Long getAlvSortOrderNbr() {
        return this.alvSortOrderNbr;
    }

    public void setAlvSortOrderNbr(final Long alvSortOrderNbr) {
        this.alvSortOrderNbr = alvSortOrderNbr;
    }

    @Column(name = "ALV_LONG_DESC_TXT")
    public String getAlvLongDescTxt() {
        return this.alvLongDescTxt;
    }

    public void setAlvLongDescTxt(final String alvLongDescTxt) {
        this.alvLongDescTxt = alvLongDescTxt;
    }

    @Column(name = "ALV_SP_SHORT_DESC_TXT")
    public String getAlvSpShortDescTxt() {
        return this.alvSpShortDescTxt;
    }

    public void setAlvSpShortDescTxt(final String alvSpShortDescTxt) {
        this.alvSpShortDescTxt = alvSpShortDescTxt;
    }

}
