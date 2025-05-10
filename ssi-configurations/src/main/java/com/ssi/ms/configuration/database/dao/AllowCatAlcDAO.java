package com.ssi.ms.configuration.database.dao;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "ALLOW_CAT_ALC")
@Data
public class AllowCatAlcDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALC_ID", unique = true, nullable = false)
    private Long alcId;
    @Column(name = "ALC_NAME", length = 30, nullable = false)
    private String alcName;
    @Column(name = "ALC_DESC_TXT", length = 200)
    private String alcDescTxt;
    @Column(name = "ALC_DECIPHER_LABEL", length = 200)
    private String alcDecipherLabel;
    @Column(name = "ALC_CATEGORY_CD", length = 4)
    private Long alcCategoryCd;

    public String getAlcName() {
        return StringUtils.trimToNull(this.alcName);
    }
    public String getAlcDescTxt() {
        return StringUtils.trimToNull(this.alcDescTxt);
    }
    public String getAlcDecipherLabel() {
        return StringUtils.trimToNull(this.alcDecipherLabel);
    }
}
