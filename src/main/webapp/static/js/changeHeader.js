$(function () {
    let $tabHeadSpans = $(".tabHead").find("span");
    let $login1 = $("#login1");
    let $login2 = $("#login2");
    let $codeImg1 = $login1.find(".codeImg");
    let $codeImg2 = $login2.find(".codeImg");
    //选择账号密码登录还是手机验证登录窗口
    $tabHeadSpans.each(function (index) {
        $(this).click(function (){
            $tabHeadSpans.removeClass("specialSp");
            $tabHeadSpans.eq(index).addClass("specialSp");
            if(index==0){
                //一次只能有一个验证码发送请求
                $codeImg2.attr("src","");
                $codeImg1.attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());

                //显示哪个窗口
                $login1[0].style.display="block";
                $login2[0].style.display="none";
            }
            else if (index==1){
                $codeImg1.attr("src","");
                $codeImg2.attr("src","/WebFinalProject/kaptcha.jpg?d="+Math.random());
                $login1[0].style.display="none";
                $login2[0].style.display="block";
            }
        });
    })
})