# Android WebViewJavascriptBridge

#### gradle script for jsbridge.jar

```bash
./gradlew :jsbridge:publish
```

> 生成的jar包和js文件位于jsbridge/dist/目录下

### how to use
1. 复制jsbridge/dist/WebViewJavascriptBridge.js到应用目录的assets文件夹下，复制jsbridge/dist/js-bridge.jar到应用目录的libs目录下；
2. 在应用的build.gradle文件中添加依赖

```
dependencies {
    ...
    compile files('libs/jsbridge-1.0.0.jar')
}
```

3. Sample Code

```java
// Code In Activity: 
import com.wangdahoo.jsbridge.JSBridgeWebView;
import com.wangdahoo.jsbridge.JSBridgeWebViewClient;
import com.wangdahoo.jsbridge.MessageDispatcher;

...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 1. 创建JSBridgeWebView实例
        webView = new JSBridgeWebView(this);
        // 2. 创建MessageDispather：消息派发组件, 拦截js端发送过来的消息，然后派发给消息中指定的消息处理组件（Handler）进行处理
        MessageDispatcher messageDispatcher = new MessageDispatcher();
        // 3. 给MessageDispather添加Hanlder
        messageDispatcher.registerHandler("DialogAlert", new DialogAlertHandler(this));
        // 4. 设置WebViewClient
        webView.setWebViewClient(new JSBridgeWebViewClient(webView, messageDispatcher));
        
        ...
    }
    
...
    
```

```java
// 实现MessageHanlder

public class DialogAlertHandler extends BaseMessageHandler implements MessageHandler {

    String title = "提示";
    String content = "";
    String btnOkText = "确定";

    Context context;

    public DialogAlertHandler(Context context) {
        this.context = context;
    }
    
    // 实现handle方法即可
    @Override
    public void handle(JSONObject message, final Callback callback) {

        JSONObject data = getData(message);
        final String callbackId = getStringValueByField(message, "callbackId");

        if (data.has("title"))
            title = getStringValueByField(data, "title");
        if (data.has("content"))
            content = getStringValueByField(data, "content");
        if (data.has("btnOkText"))
            btnOkText = getStringValueByField(data, "btnOkText");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.onCallback(makeResponse(callbackId));
                    }
                });

        builder.create();
        builder.show();
    }

}

```

That's all and have fun.
