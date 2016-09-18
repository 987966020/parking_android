package hbie.vip.parking.bean;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class Verson_bean {

    /**
     * status : success
     * data : {"id":"1","description":"修复了相关Bug","version":"1.1.0","versionid":"11","force":"1","url":"http://parking.xiaocool.net/parking3.apk","type":"android","create_time":"0"}
     */

    private String status;
    /**
     * id : 1
     * description : 修复了相关Bug
     * version : 1.1.0
     * versionid : 11
     * force : 1
     * url : http://parking.xiaocool.net/parking3.apk
     * type : android
     * create_time : 0
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String description;
        private String version;
        private String versionid;
        private String force;
        private String url;
        private String type;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersionid() {
            return versionid;
        }

        public void setVersionid(String versionid) {
            this.versionid = versionid;
        }

        public String getForce() {
            return force;
        }

        public void setForce(String force) {
            this.force = force;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
