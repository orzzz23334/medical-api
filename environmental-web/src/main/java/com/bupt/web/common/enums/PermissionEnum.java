package com.bupt.web.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum PermissionEnum implements IEnum<Integer> {
    /**
     * 游览
     * 新增
     * 修改
     * 删除
     */
    BROWSE(1),
    CREATE(2),
    UPDATE(3),
    DELETE(4),
    OTHER(5);


    private Integer code;

    PermissionEnum(Integer code) {
        this.code = code;
    }


    @Override
    public Integer getValue() {
        return this.code;
    }
}
