package cn.mengxiaozhu.jsonrpc.example;

import cn.mengxiaozhu.jsonrpc.Config;
import cn.mengxiaozhu.jsonrpc.StreamServer;

import java.util.HashMap;
import java.util.Map;

public class App {
    class Example {

        private Integer code;
        private Data data;
        private String msg;
        private Integer version;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    class Data {
        private Boolean later;
        private Boolean need;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Boolean getLater() {
            return later;
        }

        public void setLater(Boolean later) {
            this.later = later;
        }

        public Boolean getNeed() {
            return need;
        }

        public void setNeed(Boolean need) {
            this.need = need;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    public static class Fun {
        public Example echo(Example a) {
            a.code = 101;
            return a;
        }
    }

    public static void main(String[] args) {
        StreamServer server = new StreamServer(8099, 100);
        Config config = server.getConfig();
        config.registerService("fun", new Fun());
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
