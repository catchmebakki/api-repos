package com.ssi.ms.collecticase.database.repository.custom;

import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomOracleDialect extends Oracle12cDialect {

    public CustomOracleDialect()
    {
        super();
        registerFunction("fnInvGetAlvDescription",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "fn_inv_get_alv_description(?1)"));
        registerFunction("fnGetUserName",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "fn_get_user_name(?1)"));
    }
}
