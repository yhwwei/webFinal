$(function () {

    let spanObjs = $("span");
    $("#username").blur(function () {
        let username = this.value;
        if(username.length>=3){
            $(spanObjs[0]).text("用户名").css("color","green");
        }
        else {
            $(spanObjs[0]).text("*用户名长度不少于三位").css("color","red");
        }
    });
    $("#phoneNum").blur(function () {
        let phone = this.value;
        if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
            $(spanObjs[1]).text("*请输入11位手机号码").css("color","red");
        }
        else {
            $(spanObjs[1]).text("手机号").css("color","green");
        }
    });
    $("#password").blur(function () {
        let password = this.value;
        if(password.length>=7 && password.length<=15){
            $(spanObjs[2]).text("*密码").css("color","green");
        }
        else {
            $(spanObjs[2]).text("*密码长度7~15位").css("color","red");
        }
    });
    $("#confirmPwd").blur(function () {
        let password = $("#password").val();
        let confirmPwd = this.value;
        if($.trim(password)!=$.trim(confirmPwd)){
            $(spanObjs[3]).text("*再次确认密码").css("color","red");
        }
        else{
            $(spanObjs[3]).text("*").css("color","green");
        }
    });
    $("#code").blur(function () {
        let code = this.value;
        if($.trim(code)==""){
            $(spanObjs[4]).text("*输入验证码").css("color","red");
        }
        else {
            $(spanObjs[4]).text("*验证码").css("color","green");
        }
    });
    $("#dynamicalCode").blur(function () {
        let dynamicalCode = this.value;
        if($.trim(dynamicalCode)==""){
            $(spanObjs[5]).text("*输入短信验证码").css("color","red");
        }
        else {
            $(spanObjs[5]).text("*短信验证码").css("color","green");
        }
    });

    //点击注册按钮时判断短信验证码正确吗
    $("#registerButton").click(function () {
        let dynamicCode  = $("#dynamicalCode").val();
        if($.trim(dynamicCode)==""){
            $(spanObjs[6]).text("*短信动态码不能为空").css("color","red");
            return false;
        }
        if (checkFirstStep()==false){
            return false;
        }
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=register&"+$("#register_form").serialize(),
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        $(spanObjs[6]).text(data).css("color","red");;
                        //出错了更新验证码
                        $(".code").attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                        return false;
                    }
                    alert("注册成功！！！")
                },
                dataType:"text"
            });
    });


    //点击发送短信验证码按钮 在这之前，会判断许多条件 如用户名和手机号是否被使用了
    $("#sendMsg").click(function () {
        if (checkFirstStep()==false){
            return false;
        }
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=figureRegisterSend&"+$("#register_form").serialize(),
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        $(spanObjs[6]).text(data).css("color","red");
                        //出错了更新验证码
                        $(".code").attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                        return false;
                    }
                    let num=60;
                    $(spanObjs[6]).text("发送短信成功").css('color',"green");
                    //判断通过，说明已经发短信了  设置按钮不可点以及定时器
                    $("#sendMsg").prop('disabled',true);
                    $("#sendMsg").text(num+"秒");
                    //设置定时器倒计时1分钟
                    let timer = setInterval(()=>{
                        $("#sendMsg").text(num+"秒");
                        num-=1;
                        //当超时了  设置按钮可用  清除定时器
                        if(num==0){
                            $(spanObjs[6]).text("验证超时，请重新验证!").css('color',"red");
                            $("#sendMsg").prop('disabled',false);
                            clearInterval(timer);
                            $("#sendMsg").text("发送");
                        }
                    },1000);
                },
                dataType:"text"
            });
    });


    //按钮
    function checkFirstStep(){
        let phone = $("#phoneNum").val();
        let username = $("#username").val();
        let password = $("#password").val();
        let confirmPwd = $("#confirmPwd").val();
        let code = $("#code").val();
        if(username.length<3){
            $(spanObjs[6]).text("*用户名长度不少于三位").css("color","red");
            return false;
        }
        else if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
            $(spanObjs[6]).text("*请输入11位手机号码").css("color","red");
            return false;
        }
        else if(password!=confirmPwd){
            $(spanObjs[6]).text("*密码前后不一致").css("color","red");
            return false;
        }
        else if($.trim(code)==""){
            $(spanObjs[6]).text("*验证码不能为空").css("color","red");
            return false;
        }
        $(spanObjs[6]).text("");
        return true;
    }
});