package com.metro.nccc.migration.model;


import com.metro.nccc.migration.utils.response.ResponseStatus;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-16 上午9:41
 */
public class MigrationException extends RuntimeException {
    private static final long serialVersionUID = -4742832112872227456L;
    /**
     * 系统错误码
     */
    private ResponseStatus status;

    private String details;

    public MigrationException() {
        super();
    }

    public MigrationException(Throwable t) {
        super(t);
    }

    public MigrationException(ResponseStatus status, String details) {
        super();
        this.status = status;
        this.details = details;
    }

    public MigrationException(ResponseStatus status) {
        super();
        this.status = status;
        this.details = status.getDesc();
    }

    public MigrationException(String details) {
        super();
        this.status = ResponseStatus.FAILURE;
        this.details = details;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}
