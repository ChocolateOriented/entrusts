package com.entrusts.interceptor;

/**
 * 应用上下文对象
 */
public class CommonRequestContext {

    private static final ThreadLocal<CommonRequestContext> APPLICATION_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 当前用户id
     */
    private Long currentUserId;

    /**
     * token
     */
    private String accessToken;

    /**
     * 客户端版本
     */
    private String clientVersion;

    /**
     * 设备号
     */
    private String deviceId;
    
    /**
     * 国家
     */
    private String country;

    public static CommonRequestContext getInstance() {
        return APPLICATION_CONTEXT_THREAD_LOCAL.get();
    }

    private CommonRequestContext(CommonRequestContextBuilder commonRequestContextBuilder) {
        this.clientId = commonRequestContextBuilder.clientId;
        this.accessToken = commonRequestContextBuilder.accessToken;
        this.currentUserId = commonRequestContextBuilder.currentUserId;
        this.clientVersion = commonRequestContextBuilder.clientVersion;
        this.deviceId = commonRequestContextBuilder.deviceId;
        this.country = commonRequestContextBuilder.country;
    }

    private static CommonRequestContext newInstance(CommonRequestContextBuilder commonRequestContextBuilder) {
        CommonRequestContext commonRequestContext = new CommonRequestContext(commonRequestContextBuilder);
        setContext(commonRequestContext);
        return commonRequestContext;
    }

    public static void clean() {
        APPLICATION_CONTEXT_THREAD_LOCAL.remove();
    }

    private static void setContext(CommonRequestContext commonRequestContext) {
        APPLICATION_CONTEXT_THREAD_LOCAL.set(commonRequestContext);
    }

    public String getClientId() {
        return clientId;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }
    
    public String getCountry() {
		return country;
	}

    public static class CommonRequestContextBuilder {
        private String clientId;
        private Long currentUserId;
        private String accessToken;
        private String clientVersion;
        private String deviceId;
        private String country;

        public CommonRequestContextBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public CommonRequestContextBuilder currentUserId(Long currentUserId) {
            this.currentUserId = currentUserId;
            return this;
        }

        public CommonRequestContextBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public CommonRequestContextBuilder clientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
            return this;
        }

        public CommonRequestContextBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }
        
        public CommonRequestContextBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CommonRequestContext build() {
            return CommonRequestContext.newInstance(this);
        }
    }
}