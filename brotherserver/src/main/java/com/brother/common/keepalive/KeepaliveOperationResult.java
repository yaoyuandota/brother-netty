package com.brother.common.keepalive;

import com.brother.common.OperationResult;
import lombok.Data;


@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
