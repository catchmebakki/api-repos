package com.ssi.ms.resea.database.dao;

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
}
