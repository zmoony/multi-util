package org.scaffold.difyapi.exception;

import lombok.Data;
import org.scaffold.difyapi.bean.StatusEnum;

/**
 * ServiceException
 *
 * @author yuez
 * @since 2024/2/6
 */
@Data
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -3303518302920463234L;

    private final StatusEnum status;

    public ServiceException(StatusEnum status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(StatusEnum status) {
        this(status, status.message);
    }
}

