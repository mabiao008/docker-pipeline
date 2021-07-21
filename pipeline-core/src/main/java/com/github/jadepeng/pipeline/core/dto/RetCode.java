package com.github.jadepeng.pipeline.core.dto;

public enum RetCode {

    /**
     * 成功
     */
    SUCCESS("000000", "成功"),

    /**
     * 开始时间不能大于结束时间
     */
    TIMESTAMP_INVALID("000001", "开始时间不能大于结束时间"),
    UNAUTHORIZED_OPERATION("400102", "非法操作"),

    /**
     * 未找到相关记录
     */
    NOT_FOUND("404", "未找到相关记录"),
    /**
     * 身份不合法
     */
    UNAUTHORIZED("401", "身份不合法"),

    /**
     * 请求参数不合法
     */
    INVALID_PARAM("100001", "请求参数不合法"),

    /**
     * 词典类型不存在
     */
    INVALID_DICT_TYPE_ID("300101", "词典类型不存在"),

    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD_ERROR("200001", "用户名或密码错误"),

    /**
     * 登录名已经被占用
     */
    USERNAME_REPETITION("200002", "用户名已存在"),

    /**
     * 注册时，appId不存在
     */
    APPID_NOT_INEXISTENCE("200003", " APPID不存在"),

    /**
     * 用户长时间未操作，请重新登录
     */
    SESSION_TIMEOUT("200004", "Session过期，请重新登录"),

    /**
     * 用户未激活，请联系管理员激活
     */
    USER_NONACTIVATED("200005", "用户未激活，请联系管理员激活"),

    /**
     * 注册时，邮箱已经被占用
     */
    EMAIL_OCCUPIED("200006", "邮箱已存在"),

    /**
     * 用户未登录，请登录
     */
    USER_NOT_LOGIN("200007", "用户未登录，请登录"),

    /**
     * TOKEN不合法
     */
    TOKEN_VALID("200008", "TOKEN不合法"),

    /**
     * 手机号被占用
     */
    PHONE_OCCUPIED("200009", "手机号已存在"),


    /**
     * 手机号被占用
     */
    ACCESS_DENIED("200010", "无权限访问"),

    /**
     * 账号异常
     * 待明确异常类型 todo
     */
    ACCOUNT_EXCEPTION("200011", "账号状态异常"),


    /**
     * 任务流节点分类无法删除，请先删除概念下的所有子任务流节点分类
     */
    CATEGORY_CANNOT_BE_DELETED("300024", "任务流节点分类无法删除，请先删除概念下的所有子任务流节点分类"),

    /**
     * 任务流节点分类无法删除，被工作任务节点引用
     */
    CATEGORY_CANNOT_BE_DELETED_BY_TASKNODE("300025", "任务流节点分类无法删除，被工作任务节点引用"),

    /**
     * 任务流节点分类无法删除，被工作任务节点引用
     */
    TASK_TYPE_CANNOT_BE_DELETED_BY_TASKNODE("300026", "任务流节点类型无法删除，被工作任务节点引用"),

    /**
     * 任务流节点无法删除，被工作任务节点引用
     */
    TASK_NODE_CANNOT_BE_DELETED_BY_PIPELINETASKNODE("300027", "任务流节点无法删除，被pipelineTasj节点引用"),

    NOT_OPERATION("400101", "没有权限"),

    /**
     * JSON解析异常
     */
    JSON_PARSE_EXCEPTION("400005", "json解析异常"),

    /**
     * Drone api 异常
     */
    DRONE_API_EXCEPTION("500000", "drone api 异常"),

    /**
     * 不合法的工作流Id
     */
    INVALID_PIPLINE_ID("600001", "不合法的工作流Id"),

    /**
     * 不合法的工作流作业Id
     */
    INVALID_PIPELINE_JOB_ID("600002", "不合法的工作流作业Id"),

    /**
     * 不合法的工作流任务Id
     */
    INVALID_PIPELINE_TASK_ID("600003", "不合法的工作流任务Id"),

    /**
     * 不合法的工作流任务分类Id
     */
    INVALID_TASK_CATEGORY_ID("600004", "不合法的工作流任务分类Id"),

    /**
     * 不合法的工作流任务类型Id
     */
    INVALID_TASK_TYPE_ID("600005", "不合法的工作流任务类型Id"),

    /**
     * 不合法的工作流任务节点Id
     */
    INVALID_TASK_NODE_ID("600006", "不合法的工作流任务节点Id"),

    /**
     * 不合法的工作流Id
     */
    INVALID_ID("600007", "无效的Id"),

    /**
     * 同一图谱实例下的工作流名称不可以重复
     */
    PIPELINE_CANT_DUPLICATED("600007", "同一图谱实例下的工作流名称不可以重复"),

    /**
     * pipelineTask已经被复用过，不能被重复复用
     */
    PIPELINETASK_TO_TASKNODE_DUPLICATED("600008", "pipelineTask已经被复用过，不能被重复复用"),

    /**
     * 同一图谱实例下的工作流任务节点名称不可以重复
     */
    PIPELINE_TASK_CANT_DUPLICATED("600009", "同一用户下的工作流任务节点名称不可以重复"),


    PIPELINE_JOB_NOT_EXIST("600010", "工作流任务不存在"),

    PIPELINE_NOT_EXIST("600011", "工作流不存在"),

    PIPELINE_DELETEED("600012", "工作流已删除"),

    DEMAND_NO("601001", "需求编号不存在"),
    DEMAND_ILLEGAL("601002", "数据需求缺失数据类型"),

    /**
     * 文件未找到
     */
    DV_NO_SUCH_FILE("700001", "文件未找到"),

    DV_FILE_USED_ERROR("700002", "操作失败,文件正在被使用!"),

    DV_NOT_STANDARD_ZIP_FILE("700003", "请确保解压文件为标准zip格式"),

    DV_FILE_RENAME_FAIL("700004", "文件重命名失败"),
    DV_FILE_NOT_DIR("700005", "该文件不是目录"),

    DV_FILE_TYPE_ERR("700006", "文件类型错误"),

    /**
     * git异常
     */
    GIT_API_EXCEPTION("800001", "GIT 操作失败"),


    /**
     * 标注众包任务名称重复
     */
    PUBLIC_TASK_NAME_DUPLICATED("900001", "任务名称重复"),


    /**
     * 数据库连接管理相关错误
     */
    /**
     * 连接名已经被占用
     */
    CONNNAME_REPETITION("200002", "连接名已存在"),


    /**
     * 常用异常
     * */
    DUPLICATE("1700001", "名称重复"),

    /**
     * 常用异常
     * */
    NOT_AVAILABLE("1700002", "服务不可用"),

    /**
     * 常用异常
     * */
    BUTTON_ENAME_DUPLICATE("1700003", "操作英文名称已存在"),
    BUTTON_FUNCTION_DUPLICATE("1700004", "操作功能描述在当前菜单已存在"),
    NO_PERMISSION("1700005", "无权限"),

    /**
     * 异常
     */
    UNKNOWN_ERROR("999999", "出现未知异常");


    private String code;

    private String msg;

    RetCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
