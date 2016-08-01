;(function () {

  if (window.WebViewJavascriptBridge) {
    return;
  }

  window.WebViewJavascriptBridge = {
    registerHandler: registerHandler,
    callHandler: callHandler,
    _fetchQueue: _fetchQueue,
    // _handleMessageFromJava: _handleMessageFromJava
  };

  var CUSTOM_PROTOCOL_SCHEME = 'wvjbscheme';
  var QUEUE_HAS_MESSAGE = '__WVJB_QUEUE_MESSAGE__';

  /**
   * 用于发送消息的iFrame
   * @type {Element}
   */
  var messagingIframe;

  var sendMessageQueue = [];
  var responseCallbacks = {};

  var messageHandlers = {};

  var uniqueId = 1;

  /**
   * 注册JS Handler
   * @param handlerName
   * @param handler
   */
  function registerHandler(handlerName, handler) {
    messageHandlers[handlerName] = handler;
  }

  /**
   * 调用原生Handler
   * @param handlerName
   * @param data
   * @param responseCallback
   */
  function callHandler(handlerName, data, responseCallback) {
    if (arguments.length == 2 && typeof data == 'function') {
      responseCallback = data;
      data = null;
    }
    // 消息中注明调用的Handler名称
    _doSend({ handlerName:handlerName, data:data }, responseCallback);
  }

  /**
   * 发消息给webview
   * @param message           发送给原生代码的消息
   * @param responseCallback  处理原生返回数据的回调函数
   * @private
   */
  function _doSend(message, responseCallback) {
    if (responseCallback) {
      // 生成唯一的callbackId作为message的唯一标识符及回调函数的键
      var callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
      responseCallbacks[callbackId] = responseCallback;
      message['callbackId'] = callbackId;
    }
    // 将消息放入消息队列中, 等待原生代码读取
    sendMessageQueue.push(message);
    // 通知原生代码消息队列中有消息
    //messagingIframe.src = CUSTOM_PROTOCOL_SCHEME + '://' + QUEUE_HAS_MESSAGE;

    messagingIframe = document.createElement('iframe');
    messagingIframe.style.display = 'none';
    messagingIframe.src = CUSTOM_PROTOCOL_SCHEME + '://' + QUEUE_HAS_MESSAGE;
    document.documentElement.appendChild(messagingIframe);

    setTimeout(function () { document.documentElement.removeChild(messagingIframe) });
  }

  /**
   * 原生代码将调用这个方法来读取消息队列中的消息
   * @private
   */
  function _fetchQueue() {
    var messageQueueString = JSON.stringify(sendMessageQueue);
    sendMessageQueue = [];
    return messageQueueString;
  }



})();