package com.ssi.ms.common.database.dao;


import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author Praveenraja Paramsivam
 *The persistent class for the USERS_USR database table.
 */
@Entity
@Table(name = "USERS_USR")
@Data
public class UserDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USR_ID", nullable = false)
    private Long userId;

    @Column(name = "USR_FIRST_NAME", nullable = false)
    private String userFirstName;

    @Column(name = "USR_LAST_NAME", nullable = false)
    private String userLastName;

    @Column(name = "USR_STATUS_CD", nullable = false)
    private Long usrStatusCd;

    @Column(name = "USR_TYPE_CD", nullable = false)
    private Long usrTypeCd;

    @Formula("USR_FIRST_NAME || ' ' || USR_LAST_NAME")
    private String fullName;
}
