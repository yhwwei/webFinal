window.onload = function () {


    /*轮播js开始处*/
    var index=1
    var img = document.getElementById("bImg");
    var btns = document.getElementsByClassName("bannerBtn");
    var time1 = setInterval(lunbo,3000);


    btns[0].style.background="yellow";
    for(var i=0;i<btns.length;i++){
        btns[i].index2 = i+1;
        btns[i].onclick = function (event){
            clearInterval(time1);
            index = this.index2;
            img.src = "../static/imgs/banner/bannerImg"+index+".png";

            for(var i=0;i<btns.length;i++){
                if(i+1==index){
                    btns[i].style.background = "yellow";
                }
                else{
                    btns[i].style.background = "white";
                }
            }

            time1 = setInterval(lunbo,3000);
            event.stopPropagation();
        };
    }

    //鼠标悬浮，退出时的js
    img.onmouseover = function () {
        clearInterval(time1);
    }
    img.onmouseout = function (event){
        time1 = setInterval(lunbo,3000);
        event.stopPropagation();
    }



    function lunbo(){

        if(index>3){
            index=1;
        }
        img.src = "../static/imgs/banner/bannerImg"+index+".png";
        for(var i=0;i<btns.length;i++){
            if(i+1==index){
                btns[i].style.background = "yellow";
            }
            else{
                btns[i].style.background = "white";
            }
        }
        index++;

    }//轮播结束




    //导航栏搜索点击js

    //获取搜索图标元素
    var obj = document.getElementById("soId");
    //搜索框元素
    var searchObj = document.getElementsByClassName("searchBox");

    //文案链接的div
    var hBottomR = document.getElementsByClassName("hBottomR");
    var hTopR = document.getElementsByClassName("hTopR");

    //绑定点击事件
    obj.onclick = function (event){

        //判断条件是判断当前是显示什么内容，然后做出修改
        if(this.className=="so-btn"){
            this.className ="x-btn";
            //显示图标
            searchObj[0].style.display="block";

            //隐藏
            hBottomR[0].style.display="none";
            hTopR[0].style.display="none";
        }
        else if(this.className=="x-btn"){
            this.className ="so-btn";
            searchObj[0].style.display="none";
            hBottomR[0].style.display="block";
            hTopR[0].style.display="block";
        }
        // event.preventDefault();
    }


    //二级导航栏显示
    let navDlObjs = document.getElementsByClassName("navDl");
    let navDtObjs = document.getElementsByClassName("hBottom")[0].getElementsByTagName("dt");
    let navDdObjs = document.getElementsByClassName("secondHeader")
    for(let i=0;i<navDtObjs.length;i++) {
        navDtObjs[i].onmouseenter = function () {
            navDdObjs[i].style.display = "block";
        }
        navDlObjs[i].onmouseleave = function () {
            navDdObjs[i].style.display = "none";

        }
    }

    //main中点击平滑移动
    let imgWidth = document.getElementsByClassName("pic")[0].getElementsByTagName("img")[0].offsetWidth;
    let  main1_body_left = document.getElementsByClassName("main1_body_left")[0];
    let mainUlLiObjs = document.getElementsByClassName("main1Ul")[0].getElementsByTagName("li");
    mainUlLiObjs[0].style.backgroundImage="url(../static/imgs/littleCSUIcon2.png)";
    let smoothIndexNow=0;
    main1_body_left.scrollLeft = smoothIndexNow*imgWidth;
    let dsq1;
    for(let i =0; i<mainUlLiObjs.length;i++){
        mainUlLiObjs[i].onclick = function () {
            let indexGo = this.value;
            mainUlLiObjs[smoothIndexNow].style.backgroundImage="url(../static/imgs/littleCSUIcon.png)";
            mainUlLiObjs[indexGo].style.backgroundImage="url(../static/imgs/littleCSUIcon2.png)";
            smoothChange(indexGo*imgWidth);
            smoothIndexNow = indexGo;
        }
    }

    function smoothChange(end){
        let start = main1_body_left.scrollLeft;
        let speed = (end-start)/20;
        let step = 0;
        //如果有轮播定时器要先终结   这里我没写轮播

        clearInterval(dsq1);
        dsq1 = setInterval(function () {
            step++;
            if (step>=20){
                clearInterval(dsq1);
            }
            start+=speed;
            main1_body_left.scrollLeft=start;
        },30);
    }









    //底部js

    //底部隐藏链接div显示和箭头的变化
    let dtObjs = document.getElementsByClassName("ft_dt");
    let ddObjs = document.getElementsByClassName("ft1")[0].getElementsByTagName("dd");
    let dlObjs = document.getElementsByClassName("ft1")[0].getElementsByTagName("dl");

    for(let i=0;i<dtObjs.length;i++){
        let objSp = document.getElementById("ftSp"+(i+1));

        dtObjs[i].onmouseenter = function () {
            objSp.className = "arrow arrowDown"
            ddObjs[i].style.display = "block";
        }
        dlObjs[i].onmouseleave = function () {
            objSp.className = "arrow";
            ddObjs[i].style.display = "none";

        }
    }


    //显示二维码
    let tuBiaoObjs = document.getElementsByClassName("tuBiaoImg");
    let ImgObj = document.getElementsByClassName("QRCode");
    for(let i=0;i<tuBiaoObjs.length;i++){
        tuBiaoObjs[i].onmouseenter = function (){
            ImgObj[i].style.display = "block";
        }
        tuBiaoObjs[i].onmouseout = function (){
            ImgObj[i].style.display = "none";
        }
    }


    //右侧导航栏js
    let hArrowObj = document.getElementById("hArrow");
    let ulObj = document.getElementById("frightUl");
    hArrowObj.onclick = function () {
        if(this.className =="arrowDown"){
            this.className="";
            ulObj.style.width="50px";
        }
        else{
            this.className ="arrowDown";
            ulObj.style.width="130px";

        }
    }


    let returnTopBtn = document.getElementById("returnTopDiv");
    //返回顶部按钮显示js
    window.onscroll = function () {
        var scroll = document.documentElement.scrollTop||document.body.scrollTop;
        if(scroll>100){
            returnTopBtn.style.display = "block";
        }
        else {
            returnTopBtn.style.display = "none";
        }
    }
}