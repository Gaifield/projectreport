package cn.damai.boss.projectreport.commons.utils;

/**
 * Created by 炜 on 14-2-21.
 */
public class KeyValueUtils {


        /**
         * key
         */
        private String key;
        /**
         * value
         */
        private String value;

        public KeyValueUtils(){

        }

        public KeyValueUtils(String key, String value){
            this.key 	= key;
            this.value 	= value;
        }
        
        public KeyValueUtils(long key, String value){
            this.key 	= String.valueOf(key);
            this.value 	= value;
        }
        
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

}
