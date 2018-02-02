package com.headlth.management.entity;

import java.io.Serializable;

/**
 * Created by abc on 2017/5/17.
 *  //阿里云视频标识类
 */
public class OSSinfo implements Serializable {
        private String AccessKeyId;//key
        private String AccessKeySecret;//
        private String SecurityToken;
        private String Expiration;
        private String BucketName;
        private String Endpoint;
        private String Filedir;

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            AccessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            AccessKeySecret = accessKeySecret;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String securityToken) {
            SecurityToken = securityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String expiration) {
            Expiration = expiration;
        }

        public String getBucketName() {
            return BucketName;
        }

        public void setBucketName(String bucketName) {
            BucketName = bucketName;
        }

        public String getEndpoint() {
            return Endpoint;
        }

        public void setEndpoint(String endpoint) {
            Endpoint = endpoint;
        }

        public String getFiledir() {
            return Filedir;
        }

        public void setFiledir(String filedir) {
            Filedir = filedir;
        }

}
