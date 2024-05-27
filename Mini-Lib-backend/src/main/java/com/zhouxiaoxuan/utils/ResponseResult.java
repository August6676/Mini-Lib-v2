package com.zhouxiaoxuan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 响应数据封装对象
 */
@Data
public class ResponseResult<T> {

    /**
     * 状态信息
     */
    private Status status;

    /**
     * 数据
     */
    private T data;

    public ResponseResult(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    /**
     * 返回成功对象
     * @param data
     * @return
     */
    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(Status.OK, data);
    }

    /**
     * 返回错误对象
     * @param status
     * @return
     */
    public static ResponseResult<String> error(Status status) {
        return new ResponseResult<>(status, status.getMessage());
    }

    /**
     * 返回错误对象
     * @param status
     * @param msg
     * @return
     */
    public static ResponseResult<String> error(Status status, String msg) {
        return new ResponseResult<>(status, msg);
    }

    /**
     * 向流中输出结果
     * @param resp
     * @param result
     * @throws IOException
     */
    public static void write(HttpServletResponse resp, ResponseResult<?> result) throws IOException {
        // 设置返回数据的格式
        resp.setContentType("application/json;charset=UTF-8");
        // Jackson是JSON解析包，ObjectMapper用于解析 writeValueAsString 将Java对象转换为JSON字符串
        String msg = new ObjectMapper().writeValueAsString(result);
        // 用流发送给前端
        resp.getWriter().print(msg);
        resp.getWriter().close();
    }

    /**
     * 状态枚举类
     */
    public enum Status {
        OK("OK", "Success"),
        ERROR("ERROR", "Error");

        private final String code;
        private final String message;

        Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
