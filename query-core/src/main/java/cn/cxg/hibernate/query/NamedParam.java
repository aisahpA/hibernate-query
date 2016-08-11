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

    /**
     * 构建命名参数
     * <br/>将参数顺序值作为参数命名值
     *
     * @param params 参数值
     */
    public NamedParam(Object... params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                add(String.valueOf(i + 1), params[i]);
            }
        }
    }

    /**
     * 构建命名参数
     * <br/>将Map转换为命名参数对象
     *
     * @param paramsMap <参数命名，参数值>
     */
    public NamedParam(Map<String, Object> paramsMap) {
        if (paramsMap != null && !paramsMap.isEmpty()) {
            paramsMap.entrySet().forEach(param -> add(param.getKey(), param.getValue()));
        }
    }

    /**
     * 添加一个参数以及对应的值
     * <br/>只有当参数名称和值都不为空的时候才添加
     *
     * @param paramName 参数名称，jpa格式的可以使用数字的字符串形式，如?1，这里可填写"1"
     * @param value 参数值
     * @return 原对象，以便链式添加
     */
    public NamedParam add(String paramName, Object value) {
        if (paramName != null && value != null) {
            paramNames.add(paramName);
            values.add(value);
        }
        return this;
    }


    public String[] getParamNamesArr() {
        return paramNames.isEmpty() ? new String[0] : paramNames.toArray(new String[paramNames.size()]);
    }

    public Object[] getValuesArr() {
        return values.isEmpty() ? new Object[0] : values.toArray(new Object[values.size()]);
    }

}
