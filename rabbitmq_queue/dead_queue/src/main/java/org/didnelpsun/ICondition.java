// ICondition.java
package org.didnelpsun;

import com.rabbitmq.client.Delivery;

// 拒绝条件接口
public interface ICondition {
    boolean condition(String consumerTag, Delivery message);
}
