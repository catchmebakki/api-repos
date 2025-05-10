package com.ssi.ms.resea.database.dao;

import com.ssi.ms.common.database.dao.UserDAO;
import lombok.Data;
import org.hibernate.annotations.Formula;

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
@Table(name = "STAFF_STF")
@Data
public class StaffStfDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STF_ID", unique = true, nullable = false)
    private Long stfId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_USR_ID")
    private UserDAO userDAO;

    @Column(name = "STF_FIRST_NAME", length = 25)
    private String stfFirstName;

    @Column(name = "STF_MIDDLE_INITIAL", length = 1)
    private String stfMiddleInitial;

    @Column(name = "STF_LAST_NAME", length = 25)
    private String stfLastName;

    @Formula("STF_FIRST_NAME || ' ' || STF_LAST_NAME")
    private String staffName;
}
