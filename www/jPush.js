var JPush = function () {
    this.noticeData = null;
    this.noticeCallBack = function (noticeData) {
        console.log(noticeData);
    };
};

JPush.prototype._failureCallback = function (msg) {
    console.log("JPush Javascript Callback Error: " + msg)
};


JPush.prototype._callNative = function (action, args, successCallback) {
    if (arguments.length == 2) {
        args = []
    }
    return cordova.exec(
        successCallback, // called when signature capture is successful
        this._failureCallback, // called when signature capture encounters an error
        'JPush', // Tell cordova that we want to run "JPush"
        action, // Tell the plugin the action we want to perform
        args); // List of arguments to the plugin
};


/**
 * 初始化，设置是否 debug
 * @param isDebug
 * @param callback
 * @returns {*}
 */
JPush.prototype.init = function (isDebug, callback) {
    var self = this;
    this._callNative("init", [isDebug], callback);
    document.addEventListener("resume", function () {
        self.getNoticeData(self.noticeCallBack);
    });
    // 从应用关闭时候打开需要触发
    this.getNoticeData(this.noticeCallBack);
    return this;
};


/**
 * 主动获取通知
 * @returns {*}
 */
JPush.prototype.getNoticeData = function (callback) {
    this._callNative("getNoticeData", [], callback);
    return this;
};

/**
 * 暂停推送
 * @param callback
 * @returns {*}
 */
JPush.prototype.stopPush = function (callback) {
    this._callNative("stopPush", [], callback);
    return this;
};


/**
 * 恢复推送
 * @param callback
 * @returns {*}
 */
JPush.prototype.resumePush = function (callback) {
    this._callNative("resumePush", [], callback);
    return this;
};


/**
 * 设置标签数组
 * @param tagsArr
 * @param callback
 * @returns {*}
 */
JPush.prototype.setTags = function (tagsArr, callback) {
    this._callNative("setTags", [tagsArr], callback);
    return this;
};

/**
 * 设置别名字符串
 * @param aliasString
 * @param callback
 * @returns {*}
 */
JPush.prototype.setAlias = function (aliasString, callback) {
    this._callNative("setAlias", [aliasString], callback);
    return this;
};


/**
 * 设置标签数组和别名字符串
 * @param tagsArr
 * @param aliasString
 * @param callback
 * @returns {*}
 */
JPush.prototype.setTagAlias = function (tagsArr, aliasString, callback) {
    this._callNative("setTagAlias", [tagsArr, aliasString], callback);
    return this;
};

/**
 * 给 java 用的设置通知内容
 * @param noticeData
 * @param isCallBack
 * @returns {*}
 */
JPush.prototype.setNoticeData = function (noticeData, isCallBack) {
    this.noticeData = noticeData;
    if (isCallBack) {
        this.noticeCallBack(noticeData);
    }
    return this;
};


/**
 * 给 js 调用指向对应的回调方法
 * @param noticeCallBack
 * @returns {*}
 */
JPush.prototype.setNoticeCallBack = function (noticeCallBack) {
    this.noticeCallBack = noticeCallBack;
    return this;
};

module.exports = (new JPush());
