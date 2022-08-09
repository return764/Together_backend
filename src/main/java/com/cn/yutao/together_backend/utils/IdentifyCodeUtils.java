package com.cn.yutao.together_backend.utils;

import cn.hutool.core.util.IdUtil;

public class IdentifyCodeUtils {
    private IdentifyCodeUtils() {
    }

    public static String genIdCode() {
        return IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
    }
}
