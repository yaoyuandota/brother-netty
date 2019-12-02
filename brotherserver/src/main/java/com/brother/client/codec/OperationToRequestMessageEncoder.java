package com.brother.client.codec;

import com.brother.common.Operation;
import com.brother.common.RequestMessage;
import com.brother.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class OperationToRequestMessageEncoder extends MessageToMessageEncoder<Operation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {
          RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), operation);

          out.add(requestMessage);
     }
}
