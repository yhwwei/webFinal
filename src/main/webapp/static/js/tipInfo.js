$(function () {
    $("#login1").find(":submit").click(
        function () {
            let $tipInfo = $("#login1").find(".tipInfo");
            let $loginId = $("#loginId");
            let $password = $("#login1").find(":password");
            let $code = $("#login1").find(".code1");

            /*
            * 判断输入不能为空
            * */
            if($.trim($loginId.val())==""){
                $tipInfo.text("学号不能为空!");
                return false;
            }
            else if($password.val()==""){
                $tipInfo.text("密码不能为空！");
                return  false;
            }
            else if($.trim($code.val())==""){
                $tipInfo.text("请输入验证码!");
                return false
            }
            //都正确之后消除提示信息  自动提交到表单对应的url处理
            $tipInfo.text("");
        }
    );
});