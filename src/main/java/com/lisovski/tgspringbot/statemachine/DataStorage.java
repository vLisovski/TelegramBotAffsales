package com.lisovski.tgspringbot.statemachine;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataStorage {

    private final Map<Long, Object> data;

    public DataStorage() {
        data = new HashMap<>();
    }

    public void add(Long key, Object value) {
        data.put(key, value);
    }

    public Object get(Long key) {
        return data.get(key);
    }

    public void delete(Long key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }
}
