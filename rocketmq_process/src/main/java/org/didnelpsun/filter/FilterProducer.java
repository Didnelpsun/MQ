// FilterProducer.java
package org.didnelpsun.filter;

import org.apache.rocketmq.common.message.Message;
import org.didnelpsun.Producer;

import java.util.Map;

// 参数生产者
public class FilterProducer extends Producer {

    // Map类型参数
    protected Map<String, Object> properties;

    public FilterProducer() {
        super("filter");
    }

    public FilterProducer(Map<String, Object> properties) {
        super("filter");
        this.properties = properties;
    }

    public FilterProducer(String group) {
        super(group);
    }

    public FilterProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    // 添加参数
    public Object put(String key, Object value) {
        return this.properties.put(key, value);
    }

    // 移除参数
    public Object remove(String key) {
        return this.properties.remove(key);
    }

    // 对参数赋值
    public Message putUserProperty(Message message){
        for (String key : this.properties.keySet()) {
            message.putUserProperty(key,  String.valueOf(this.properties.get(key)));
        }
        return message;
    }
}
