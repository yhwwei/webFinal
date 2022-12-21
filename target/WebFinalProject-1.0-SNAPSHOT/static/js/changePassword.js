$(function () {
    //下一步按钮
    let btn = $(".nextBtn");
    let stepItems = $(".step-item");
    let spans = $(".circle");
    let username;
    let inputs = $(".form").find("input")
    let timer;
    //点击下一步  不同步骤调用方法不一样
    btn.click(function () {
        let id = btn.attr('stepBy');
        switch (id){
            case '1':
                stepOne()
                break
            case '2':
                stepTwo()
                break
            case '3':
                stepThree()
                break
            case '4':
                $(location).attr("href",'http://localhost:8080/WebFinalProject/');
                break;
        }
    });
    function stepOne(){
        username = $("#username").val();
        let code = $("#code").val();
        if(username==""||code==""){
            alert("输入不能为空")
            return false;
        }
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=stepOne&username="+username+"&code="+code,
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        alert(data)
                        $(".code").attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                        return false;
                    }
                    $(stepItems[1]).css("color","#2d8cf0");;
                    $(spans[1]).addClass("circlePass");
                    btn.attr('stepBy','2')
                    //生成下一个  输入框显示
                    $(inputs[0]).attr("placeholder","请输入绑定的手机号");
                    $(inputs[0]).attr("id","phoneNum")
                    $(inputs[0]).val("")
                    $(inputs[1]).attr("placeholder","请输入短信验证码");
                    $(inputs[1]).attr("id","dynamicalCode")
                    $(inputs[1]).val("")
                    $(".form").find("img").remove()
                    $("form").append(' <button type="button" class="send">获取验证码</button>');

                    //绑定发送短信事件
                    $(".send").click(function () {
                        let phoneNum = $("#phoneNum").val();
                        if(phoneNum==""||!(/^1[3|4|5|7|8]\d{9}$/.test(phoneNum))){
                            alert("请检查手机号码输入");
                            return false;
                        }
                        $.ajax(
                            {
                                url:"http://localhost:8080/WebFinalProject/user",
                                data:"action=figureByPhoneSend&phoneNum="+phoneNum+"&username="+username,
                                type:"get",
                                success:function (data) {
                                    if(data!="pass"){
                                        alert(data)
                                        return false;
                                    }
                                    let num=60;
                                    alert("短信发送成功，注意查收")
                                    //判断通过，说明已经发短信了  设置按钮不可点以及定时器
                                    $(".send").prop('disabled',true);
                                    $(".send").text(num+"秒");
                                    //设置定时器倒计时1分钟
                                    timer = setInterval(()=>{
                                        $(".send").text(num+"秒");
                                        num-=1;
                                        //当超时了  设置按钮可用  清除定时器
                                        if(num==0){
                                            alert("验证超时，请重新验证");
                                            $(".send").prop('disabled',false);
                                            clearInterval(timer);
                                            $(".send").text("获取验证码");
                                        }
                                    },1000);
                                },
                                dataType:"text"
                            });
                    })
                },
                dataType:"text"
            });
    }
    function stepTwo(){
        timer=null;
        let phoneNum = $("#phoneNum").val();
        let dynamicalCode = $("#dynamicalCode").val();
        if(phoneNum==""||!(/^1[3|4|5|7|8]\d{9}$/.test(phoneNum))){
            alert("请检查手机号码输入");
            return false;
        }
        if(dynamicalCode==""){
            alert("短信验证码不能为空")
            return false;
        }
        $.ajax(
            {
                url:"http://localhost:8080/WebFinalProject/user",
                data:"action=stepTwo&phoneNum="+phoneNum+"&dynamicalCode="+dynamicalCode,
                type:"get",
                success:function (data) {
                    if(data!="pass"){
                        alert(data)
                        return false;
                    }
                    btn.attr('stepBy','3')
                    $(stepItems[2]).css("color","#2d8cf0");
                    $(spans[2]).addClass("circlePass");
                    $(inputs[0]).attr("placeholder","请输入新密码");
                    $(inputs[0]).attr('type','password')
                    $(inputs[0]).attr("id","password")
                    $(inputs[0]).val("")
                    $(inputs[1]).attr("placeholder","请确认新密码");
                    $(inputs[1]).attr('type','password')
                    $(inputs[1]).attr("id","confirmPassword")
                    $(inputs[1]).val("")
                    $(inputs[1]).css('width','80%');
                    $(".send").remove()
                },
                dataType:"text"
            });
    }
    function stepThree(){
        let password = $("#password").val();
        let confirmPassword = $("#confirmPassword").val();
        if(password==""||password.length<7){
            alert("失败：密码为空或长度者少于7位");
            return false;
        }
        if(confirmPassword!=password){
            alert("前后密码不一致")
            return false;
        }
        $.ajax({
            url:"http://localhost:8080/WebFinalProject/user",
            data:"action=stepThree&password="+password+"&username="+username,
            type:"get",
            success:function (data) {
                if(data!="pass"){
                    alert(data)
                    return false;
                }
                btn.attr('stepBy','4')
                $(stepItems[3]).css("color","#2d8cf0");
                $(spans[3]).addClass("circlePass");
                $("form").remove()
                $(".form").prepend(' <h1>密码重置成功</h1>');
                btn.text("返回首页");
            },
            dataType:"text"

        });
    }

});