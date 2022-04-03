// IConfirm.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

public interface IConfirm {
    public boolean confirm(Channel channel);
}
