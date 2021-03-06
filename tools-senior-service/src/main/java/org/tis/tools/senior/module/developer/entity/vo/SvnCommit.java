package org.tis.tools.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class SvnCommit {

    /**
     * 版本号revision
     */
    private int revision;

    /**
     * 提交人author
     */
    private String author;

    /**
     * 日期date
     */
    private Date commitDate;

    /**
     * 信息log message
     */
    private String message;

    /**
     * 路径
     */
    private List<SvnPath> changePaths;

}
