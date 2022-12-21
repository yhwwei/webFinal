$(function () {
    let $tipInfo = $("#login2").find(".tipInfo");


    //点击发送短信 要先判断满不满足条件  ，满足条件则通过第三方发送短信，同时用redis设置过期时间
    $("#sendMsg").click(function () {
        let $phoneNum = $.trim($("#phoneNum").val());
        let $code = $.trim($("#login2").find(".code1").val());

        if(confirmInput($phoneNum,$code)==false){
            return false;
        }
        $tipInfo.text("");
        //输入符合格式后 发送ajax请求   让后端服务器判断 对应的手机号是否注册了以及验证码是否正确
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=figureLoginSend&phoneNum="+$phoneNum+"&code="+$code,
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        $tipInfo.text(data);
                        //出错了更新验证码
                        $("#login2").find(".codeImg").attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                        return false;
                    }
                    let num=60;
                    $tipInfo.html("发送短信成功").css('color',"green");
                    //判断通过，说明已经发短信了  设置按钮不可点以及定时器
                    $("#sendMsg").prop('disabled',true);
                    $("#sendMsg").text(num+"秒");
                    //设置定时器倒计时1分钟
                    let timer = setInterval(()=>{
                        $("#sendMsg").text(num+"秒");
                        num-=1;
                        //当超时了  设置按钮可用  清除定时器
                        if(num==0){
                            $tipInfo.html("验证超时，请重新验证!").css('color',"red");
                            $("#sendMsg").prop('disabled',false);
                            clearInterval(timer);
                            $("#sendMsg").text("发送");
                        }
                    },1000);
                },
                dataType:"text"
            });
    });
    $("#AjaxLogin").click(function () {
        let $phoneNum = $.trim($("#phoneNum").val());
        let $code = $.trim($("#login2").find(".code1").val());
        if(confirmInput($phoneNum,$code)==false){
            return false;
        }
        let $dynamicCode = $.trim($("#dynamicalCode").val());
        if ($dynamicCode==null||$dynamicCode==""){
            $tipInfo.text("请输入动态验证码!").css("color","red");
            return false;
        }
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=loginByPhone&phoneNum="+$phoneNum+"&dynamicalCode="+$dynamicCode,
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        $tipInfo.text(data).css("color","red");;
                        //出错了更新验证码
                        $("#login2").find(".codeImg").attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                        return false;
                    }
                    let rootPath = getRootPath();
                    $(location).attr("href",rootPath+"/manager?action=toShowUsers");
                },
                dataType:"text"
            });
    });


    //检查输入
    function confirmInput($phoneNum,$code) {

        if($phoneNum==""){
            $tipInfo.text("请输入手机号码！");
            return false;
        }
        // 判断满不满足正则条件
        if(!(/^1[3|4|5|7|8]\d{9}$/.test($phoneNum))){
            $tipInfo.text("请正确输入手机号码格式!").css("color","red");
            return false;
        }
        if($code==null||$code==""){
            $tipInfo.text("请输入验证码!").css("color","red");
            return false;
        }
        return true;
    }
    function getRootPath() {
        // 1、获取当前全路径，如： http://localhost:8080/springmvc/page/frame/test.html
        let curWwwPath = window.location.href;
        // 获取当前相对路径： /springmvc/page/frame/test.html
        let pathName = window.location.pathname;    // 获取主机地址,如： http://localhost:8080
        let local = curWwwPath.substring(0,curWwwPath.indexOf(pathName));
        // 获取带"/"的项目名，如：/springmvc
        let projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        let rootPath = local + projectName;
        return rootPath;
    }
});