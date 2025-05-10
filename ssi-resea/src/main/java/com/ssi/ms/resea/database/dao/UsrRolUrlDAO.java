package com.ssi.ms.resea.database.dao;

import com.ssi.ms.common.database.dao.UserDAO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "USR_ROL_URL")
@Data
public class UsrRolUrlDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "URL_ID", unique = true, nullable = false)
    private Long urlId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_USR_ID")
    private UserDAO userDAO;

    @Column(name = "FK_ROL_ID", length = 4)
    private Long fkRolId;
}
