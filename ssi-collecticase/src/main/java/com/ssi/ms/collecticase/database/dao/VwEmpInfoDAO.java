package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;


/**
 * The persistent class for the VW_EMP_INFO database table.
 */
@Entity
@Table(name = "VW_EMP_INFO")
@Data
public class VwEmpInfoDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column
    private String addr1;

    @Column
    private String addr2;

    @Column
    private String addr3;

    @Column
    private String altPhone;

    @Column
    private String city;

    @Column
    private Long country;

    @Column
    private String email;

    @Column
    private String empDbaName;

    @Id
    @Column
    private Long empId;

    @Column
    private String empName;

    @Column
    private String ext;

    @Column
    private String fax;

    @Column
    private Long fein;

    @Column
    private String mainPhone;

    @Column
    private String state;

    @Column
    private String uiAcct;

    @Column
    private String zip;

}