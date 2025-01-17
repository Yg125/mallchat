package org.yg.mallchat.common.common.exception;

import lombok.Data;

import static org.yg.mallchat.common.common.exception.CommonErrorEnum.BUSINESS_ERROR;

/**
 * @author yangang
 * @create 2025-01-16-下午4:55
 */
@Data
public class BusinessException extends RuntimeException {
    protected Integer errorCode;
    protected String errorMsg;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = BUSINESS_ERROR.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMsg());
        this.errorCode = errorEnum.getErrorCode();
        this.errorMsg = errorEnum.getErrorMsg();
    }
}
