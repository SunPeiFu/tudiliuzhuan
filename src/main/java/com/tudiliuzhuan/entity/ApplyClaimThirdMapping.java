package com.tudiliuzhuan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 申请理赔与第三方关联表 
 * </p>
 *
 * @author mybatis-plus
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("claim_c_apply_claim_third_mapping")
@ApiModel(value="ApplyClaimThirdMapping对象", description="申请理赔与第三方关联表 ")
public class ApplyClaimThirdMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "申请理赔表id")
    @TableField("claim_record_id")
    private String claimRecordId;

    @ApiModelProperty(value = "esign流程Id")
    @TableField("sign_flow_id")
    private String signFlowId;

    @ApiModelProperty(value = "签署结果 2:签署完成 3:失败 4:拒签")
    @TableField("sign_result")
    private Integer signResult;

    @ApiModelProperty(value = "拒签或失败时，附加的原因描述")
    @TableField("result_desc")
    private String resultDesc;

    @ApiModelProperty(value = "esign签名地址")
    @TableField("sign_url")
    private String signUrl;

    @ApiModelProperty(value = "esign签名pdf文件Id")
    @TableField("sign_file_id")
    private String signFileId;

    @ApiModelProperty(value = "上传到我方Oss的esign签名文档地址")
    @TableField("sign_oss_url")
    private String signOssUrl;

    @ApiModelProperty(value = "签署时间或拒签时间")
    @TableField("signTime")
    private LocalDateTime signTime;

    @ApiModelProperty(value = "签署文件主题描述")
    @TableField("business_scence")
    private String businessScence;

    @ApiModelProperty(value = "任务状态 2-已完成: 所有签署人完成签署；\n\n3-已撤销: 发起方撤销签署任务；\n\n\n5-已过期: 签署截止日到期后触发；\n\n7-已拒签")
    @TableField("flow_status")
    private Integer flowStatus;

    @ApiModelProperty(value = "当流程异常结束时，附加终止原因描述")
    @TableField("status_desc")
    private String statusDesc;

    @ApiModelProperty(value = "签署任务发起时间")
    @TableField("sign_create_time")
    private LocalDateTime signCreateTime;

    @ApiModelProperty(value = "签署任务结束时间")
    @TableField("sign_end_time")
    private LocalDateTime signEndTime;

    @ApiModelProperty(value = "签署完成回调json")
    @TableField("sign_flow_update_json")
    private String signFlowUpdateJson;

    @ApiModelProperty(value = "流程结束回调json")
    @TableField("sign_flow_finish_json")
    private String signFlowFinishJson;

    @ApiModelProperty(value = "e签宝回调时 是否真实请求保险公司发起申请理赔 true 是 false 否  默认true")
    @TableField("is_real_apply_claim")
    private Integer realApplyClaim;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    private Integer deleted;


}
