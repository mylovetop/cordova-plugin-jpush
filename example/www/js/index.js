var app = {
    initialize: function () {
        this.bindEvents();
    },
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    onDeviceReady: function () {

        var $noticeArea, $callBackArea, $setAliasInput, $setTagsInput;


        $setAliasInput = document.getElementById('setAliasInput');
        $setTagsInput = document.getElementById('setTagsInput');
        $callBackArea = document.getElementById('callBackArea');
        $noticeArea = document.getElementById('noticeArea');


        function setTags() {
            plugins.jPush.setTags($setTagsInput.value.split(','), msgCallBack);
        }

        function setAlias() {
            plugins.jPush.setAlias($setAliasInput.value, msgCallBack);
        }

        function stopPush() {
            plugins.jPush.stopPush(msgCallBack);
        }

        function resumePush() {
            plugins.jPush.resumePush(msgCallBack);
        }


        function msgCallBack(msg) {
            console.log("jpush", JSON.stringify(msg));
            $callBackArea.innerHTML = JSON.stringify(msg);
        }

        document.getElementById('setTags').addEventListener('click', setTags, false);
        document.getElementById('setAlias').addEventListener('click', setAlias, false);
        document.getElementById('stopPush').addEventListener('click', stopPush, false);
        document.getElementById('resumePush').addEventListener('click', resumePush, false);


        function noticeCallBack(data) {
            console.log("jpush", JSON.stringify(data));
            $noticeArea.innerHTML = JSON.stringify(data);
        }


        plugins.jPush.setNoticeCallBack(noticeCallBack).init(true, msgCallBack);


    }
};
