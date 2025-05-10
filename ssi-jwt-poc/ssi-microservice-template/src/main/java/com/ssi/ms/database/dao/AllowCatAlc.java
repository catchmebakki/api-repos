package com.ssi.ms.database.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "ALLOW_CAT_ALC")
public class AllowCatAlc implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2018285990870085455L;
    private Long alcId;
    private String alcName;
    private String alcDescTxt;
    private Set<AllowValAlv> allowValAlvs = new HashSet<AllowValAlv>();

    @Id
    @Column(name = "ALC_ID", unique = true, nullable = false, precision = 4, scale = 0)
    public Long getAlcId() {
        return this.alcId;
    }

    public void setAlcId(final Long alcId) {
        this.alcId = alcId;
    }

    @Column(name = "ALC_NAME")
    public String getAlcName() {
        return this.alcName;
    }

    public void setAlcName(final String alcName) {
        this.alcName = alcName;
    }

    @Column(name = "ALC_DESC_TXT")
    public String getAlcDescTxt() {
        return this.alcDescTxt;
    }

    public void setAlcDescTxt(final String alcDescTxt) {
        this.alcDescTxt = alcDescTxt;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "allowCatAlc")
    public Set<AllowValAlv> getAllowValAlvs() {
        return this.allowValAlvs;
    }

    public void setAllowValAlvs(final Set<AllowValAlv> allowValAlvs) {
        this.allowValAlvs = allowValAlvs;
    }

    @Override
    public boolean equals(final Object obj1) {
        boolean result = true;
        if (obj1 == null || this != null && this.getClass() != obj1.getClass()) {
            result = false;
        } else {
            final AllowCatAlc obj2 = (AllowCatAlc) obj1;
            if (this.alcDescTxt == null ? obj2.alcDescTxt != null : !this.alcDescTxt.equals(obj2.alcDescTxt)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 33;
        hash = 77 * hash + (this.alcDescTxt != null ? this.alcDescTxt.hashCode() : 0);
        return hash;
    }

}
