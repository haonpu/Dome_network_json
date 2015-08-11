package Utils;

/**
 * Created by User on 2015/8/11.
 * 耗时逻辑在子线程里进行
 * Java的回调机制返回数据
 */
public interface HttpCallbackListener {
    void onFinish(String response);  //服务器成功响应请求时调用
    void onError(Exception e);  //错误发生时调用

}


