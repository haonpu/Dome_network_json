package Utils;

/**
 * Created by User on 2015/8/11.
 * 这是一个实体类，用于处理json数据，将解析的json格式字符串通过GSON自动映射成此类
 * Author 郝硕
 * date 2015-8-11 16:16:23
 */


public class App {
    private String id;
    private String name;
    private String version;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }


    // auto generate

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }
}
