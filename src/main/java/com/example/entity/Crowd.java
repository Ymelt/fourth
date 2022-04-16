package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Crowd {

    @TableField("隔离场所")
    private String place;
    @TableField("房间号（床号）")
    private String roomNumber;
    @TableField("姓名")
    private String name;
    @TableField("性别")
    private String sex;
    @TableField("人员类型")
    private String personnelType;
    @TableField("健康状态")
    private String healthCodeStatus;
    @TableField("联系方式")
    private String contact;
    @TableField("基础疾病")
    private String diseaseStatus;
    @TableField("核算检测结果")
    private String nucleicAcidTestResults;
    @TableField("年龄")
    private String age;
//    @TableId(type = IdType.AUTO)
//    private Integer id;
}
