package org.fish.appium.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Result extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    //    @ApiModelProperty(value = "version")
    private final String version = "1.0.2";

    //    @ApiModelProperty(value = "code")
    private Integer code;

    //    @ApiModelProperty(value = "message")
    private String message;

    //    @ApiModelProperty(value = "data")
    private Map<String, Object> result = new LinkedHashMap<>();

    public Result() {
        put("version", version);
        put("code", 0);
        put("messages", "Success!");
    }

    public static Result ok(String message) {
        Result r = new Result();
        r.put("code", HttpStatus.SC_OK);
        r.put("messages", message);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.put("code", HttpStatus.SC_OK);
        r.put("result", map);
        return r;
    }

    public static Result ok(String message, Map<String, Object> map) {
        Result r = new Result();
        r.put("code", HttpStatus.SC_OK);
        r.put("messages", message);
        r.put("result", map);
        return r;
    }

    public static Result ok(List<Object> list) {
        Result r = new Result();
        r.put("code", HttpStatus.SC_OK);
        r.put("result", list);
        return r;
    }

    public static Result error() {
        Result r = new Result();
        r.put("code", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        r.put("messages", "未知异常");
        return r;
    }

    public static Result error(String message) {
        Result r = new Result();
        r.put("code", HttpStatus.SC_BAD_REQUEST);
        r.put("messages", message);
        return r;
    }

    public static Result error(Integer code, String message) {
        Result r = new Result();
        r.put("code", code);
        r.put("messages", message);
        return r;
    }

    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result result(Map<String, Object> map) {
        this.setResult(map);
        return this;
    }

    public Result result(String key, Object value) {
        this.result.put(key, value);
        return this;
    }
}


