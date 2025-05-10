package com.ssi.ms.configuration.constant;


import com.ssi.ms.constant.AlvCodeConstantParent;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AlvConfigEnumConstant {

    @Getter
    @AllArgsConstructor
    public enum ParCategoryCd implements AlvCodeConstantParent {
        WORK_SEARCH(4949);
        private Integer code;
    }
}
