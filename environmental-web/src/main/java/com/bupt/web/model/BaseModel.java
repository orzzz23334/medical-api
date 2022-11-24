package com.bupt.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.Date;


/**
 * 基本model
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseModel<T extends Model<?>> extends Model<T> {

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("id")
    protected Long id;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    protected Timestamp createdAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    protected Timestamp updatedAt;

    @ApiModelProperty(hidden = true)
    private String createdBy;

    @ApiModelProperty(hidden = true)
    private String updatedBy;

}
