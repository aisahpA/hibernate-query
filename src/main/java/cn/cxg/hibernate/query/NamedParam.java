package cn.cxg.hibernate.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 命名查询参数和值
 *
 * @author Chenxg on 2016/7/25.
 */
public class NamedParam {

    private final List<String> paramNames = new ArrayList<>(8);
    private final List<Object> values = new ArrayList<>(8);

    public NamedParam() {
    }

    public NamedParam(Object... params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                add(String.valueOf(i + 1), params[i]);
            }
        }
    }

    public NamedParam(Map<String, Object> paramsMap) {
        if (paramsMap != null && !paramsMap.isEmpty()) {
            paramsMap.entrySet().forEach(param -> add(param.getKey(), param.getValue()));
        }
    }

    public NamedParam add(String paramName, Object value) {
        if (paramName != null && value != null) {
            paramNames.add(paramName);
            values.add(value);
        }
        return this;
    }


    public String[] getParamNamesArr() {
        return paramNames.isEmpty() ? null : paramNames.toArray(new String[paramNames.size()]);
    }

    public Object[] getValuesArr() {
        return values.isEmpty() ? null : values.toArray(new Object[values.size()]);
    }

}
