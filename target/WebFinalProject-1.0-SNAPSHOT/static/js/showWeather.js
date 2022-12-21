$(function () {



    //获取ip地址
    function dynamicIp() {
        $.get(
                'https://bird.ioliu.cn/ip',
                "",
                function (data) {
                    let cityIp = data.data.ip;
                    dynamicGetWeather(cityIp)
                },
                'jsonp'
        );
    }


    //显示星期几的对象数组
    let $showMiddleSpans = $("#showMiddle>span");

    //显示天气的图片数组对象
    let $showBottomImgs = $("#showBottom>img");

    //获取当前温度显示p对象
    let $currentTem = $("#showTopL").find("p");

    //获取城市名span对象
    let $cityName = $("#showTopL").find("span")


    //显示空气质量
    let $airQualityI = $("#showTopL").find("i")

    let $showTopBSpans = $("#showTopB").find("span");

    //右上角的天气图标
    let $weatherIcon = $("#showTopR").find("img")

    let $weaType = $(".weaType");

    //当前日+星期几
    let $date1 = $(".date");
    /*
    * 传入所在城市的ip地址，到天气网站去请求数据
    * */
    function dynamicGetWeather(cityIp){
        $.get(

            //获取天气消息的网站  有API文档查看
            'https://v0.yiketianqi.com/api',

            //这个要去API文档去看  会更新
            "unescape=1&version=v91&appid=35937933&appsecret=x6aRcDPb&ip="+cityIp,

            //对返回的jsonp数据进行处理
            function (data) {

                //设置当前温度
                $currentTem.text(data.data[0].tem+"℃");

                //设置城市名字
                $cityName.text(data.city)

                $weatherIcon.attr('src','/WebFinalProject/static/imgs/weather/'+chooseImg(data.data[0].wea_img));
                //设置 日+星期几
                $date1.text(data.data[0].day)

                //天气是哪种类型
                $weaType.text(data.data[0].wea_day);

                $airQualityI.text(data.data[0].air_level);
                setAirQuality(data.data[0].air_level)

                $($showTopBSpans[0]).text(data.data[0].tem1)
                $($showTopBSpans[1]).text(data.data[0].tem2)
                $($showTopBSpans[2]).text(data.data[0].win+" 风速:"+data.data[0].win_speed)
                $($showTopBSpans[3]).text(data.data[0].humidity)

                for(let i=0;i<4;i++){
                    //获取 是 星期几 的数据
                    $($showMiddleSpans[i]).text(data.data[i].week);
                    let wea_img = data.data[i].wea_img;

                    //调用下面的chooseImg设置对应天气的图片
                    $($showBottomImgs[i]).attr('src','/WebFinalProject/static/imgs/weather/'+chooseImg(wea_img));

                }
                //额外标注一下今天
                $($showMiddleSpans[0]).text("(今天)"+data.data[0].week);
            },
            "jsonp"
        )
    }

    /*
    *  根据ajax请求获得的jsonp中取出来的数据 来判断是什么天气，然后选择对应的图片
    * */
    function chooseImg(wea_img) {
        if (wea_img=="xue"){
            return "snowy.png";
        }
        else if (wea_img=="lei"){
            return "thunder.png";
        }
        else if (wea_img=="shachen"){
            return "sandstorm.png";
        }
        else if (wea_img=="wu"){
            return "frog.png";
        }
        else if (wea_img=="bingbao"){
            return "hailstone.png";
        }
        else if (wea_img=="yun"){
            return "cloudy.png";
        }
        else if (wea_img=="yu"){
            return "rainy.png";
        }
        else if (wea_img=="yin"){
            return "overcast.png";
        }
        else {
            return "sunny.png"
        }
    }


    //对应不同空气质量  设置不同的背景色
    function setAirQuality(air_level){


        if(air_level=="优"){
            $airQualityI[0].style.backgroundColor="#73d538";
        }
        else if(air_level=="良"){
            $airQualityI[0].style.backgroundColor="#FFF000";

        }
        else {
            $airQualityI[0].style.backgroundColor="#f76707";

        }

    }
    dynamicIp();
});

