// DefaultTransactionListener
package org.didnelpsun.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;

public class DefaultTransactionListener implements TransactionListener {
    // 回调操作
    // 消息预提交成功会触发该方法，用于完成本地事务
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("预提交消息成功:" + new String(message.getBody(), StandardCharsets.UTF_8));
        // 根据传入消息的tag对事务进行处理
        // 成功commit，失败rollback，消息回查unknow
        if (StringUtils.equals("commit", message.getTags())) return LocalTransactionState.COMMIT_MESSAGE;
        else if (StringUtils.equals("rollback", message.getTags())) return LocalTransactionState.ROLLBACK_MESSAGE;
        else if (StringUtils.equals("unknow", message.getTags())) return LocalTransactionState.UNKNOW;
        return LocalTransactionState.UNKNOW;
    }

    // 当没有收到发送半消息的响应，broker将会通过该方法回查本地事务的状态，从而决定半消息是提交还是回滚
    // 引发原因
    // 1.回调操作返回UNKNOW
    // 2.TC没有接收到TM的最终全局事务确认指令
    // 因回查被取消因此checkLocalTransaction(MessageExt msg)没有作用了，所以如果LocalTransactionState.UNKNOW将无法处理，会使得topic一直处于不显示状态
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("消息回查:" + messageExt.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
