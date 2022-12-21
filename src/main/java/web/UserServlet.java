package web;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import domain.Page;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;
import service.UserService;
import utils.JedisPoolUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * @author yhw
 * @version 1.0
 **/
public class UserServlet extends ViewBaseServlet {

    private UserService userService = new UserService();
    private String token = "";
    private String code = "";
    private String phoneNum = "";
    private static Properties properties = null;

    static {
        properties = new Properties();
        try {
            InputStream is = UserServlet.class.getClassLoader().getResourceAsStream("DeveloperInfo.properties");
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //跳转到注册页面（因为得手动通过thymeleaf解析）
    public void toRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.processTemplate("user/register", request, response);
    }


    //跳转到忘记密码页面
    public void toChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.processTemplate("user/changePassword", request, response);
    }







    //通过用户名登录
    public void loginById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        System.out.println("接收到 通过学号登录  请求");


        getCodeAndTokenAndPhoneNum(request);

        //判断验证码正不正确
        if (code == "" || !code.equalsIgnoreCase(token)) {
            System.out.println("验证码错误");
            System.out.println(code + "    " + token);
            request.setAttribute("errMsg", "验证码错误请重新输入");
            //验证码验证错误则返回登录页面
            request.getRequestDispatcher("/welcome").forward(request, response);
            return;
        }

        //生成user对象
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));

        //查询数据库中是否存在这个user对象
        user = userService.loginService(user);
        if (user == null) {
            request.setAttribute("errMsg", "账号或者密码错误");
            super.processTemplate("user/login",request,response);
//            request.getRequestDispatcher("/welcome").forward(request, response);
            return;
        }

        //登录成功后将user放入session中
        request.getSession().setAttribute("user", user);

        System.out.println("登录成功");
        System.out.println(request.getSession().getAttribute("user"));
        //重定向到管理首页  经过thymeleaf
        response.sendRedirect(request.getContextPath() + "/manager?action=toShowUsers");

    }

    //通过手机号码登录
    public void loginByPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //对于前台验证码的验证在点击发送短信时会判断


        // 短信验证码是否正确
        int flag = checkDynamicCode(request);
        response.getWriter().write(setData(flag));

        //正确的话  session保存
        if (flag==0){
            User user = userService.loginByPhoneService(phoneNum);
            request.getSession().setAttribute("user", user);
        }
    }


    //手机号登录时判断 满不满足发送短信验证码条件

    /**
     * 若手机号还未注册则失败  不然判断输入的验证码正确与否
     * 满足条件生成动态验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void figureLoginSend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("figureLoginSend");
        getCodeAndTokenAndPhoneNum(request);
        User user = new User();
        user.setPhoneNum(phoneNum);

        //如果这个手机号未注册则提醒用户
        if (userService.isExistUser(user) != -2) {
            response.getWriter().write("手机号未注册");
            System.out.println("手机号还没注册");
            return;
        }

        if (code == "" || !code.equalsIgnoreCase(token)) {
            response.getWriter().write("验证码错误");
            System.out.println("验证码错误");
            return;
        }
        //发送短信验证码
        String dynamicCode = sendDynamicCode(phoneNum);

        //生成的短信验证码为-1说明服务器有问题  可能redis忘记开启了
        if (dynamicCode.equals("-1")) {
            response.getWriter().write("服务器异常，稍后重试");
        } else {
            response.getWriter().write("pass");
        }
    }

    //注册时 满不满足发送短信验证码条件

    /**
     * 注册时先判断用户名以及手机号是否被注册过，验证码是否错误
     * 满足条件则发送短信验证码
     *
     * @param request
     * @param response
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void figureRegisterSend(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        System.out.println("figureRegisterSend");
        getCodeAndTokenAndPhoneNum(request);

        User user = new User();
        //通过BeanUtils工具帮助我们从请求中
        BeanUtils.populate(user, request.getParameterMap());
        int flag = userService.isExistUser(user);
        if (flag == -1) {
            response.getWriter().write("用户名账号已存在");
            return;
        } else if (flag == -2) {
            response.getWriter().write("手机号码已被注册");
            return;
        }
        if (code == "" || !code.equalsIgnoreCase(token)) {
            System.out.println("验证码错误");
            System.out.println(code + "    " + token);
            response.getWriter().write("验证码错误");
            return;
            //验证码验证错误则返回登录页面
        }
        String dynamicCode = sendDynamicCode(phoneNum);
        if (dynamicCode.equals("-1")) {
            response.getWriter().write("服务器异常，稍后重试");
        } else {
            response.getWriter().write("pass");
        }
    }

    public void figureByPhoneSend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        getCodeAndTokenAndPhoneNum(request);
        user.setUsername(request.getParameter("username"));
        user.setPhoneNum(phoneNum);
        System.out.println(user.getUsername() + "   " + user.getPhoneNum());
        if (userService.queryByUsernameAndPhoneNum(user) == null) {
            response.getWriter().write("不是绑定该手机号");
            return;
        }
        sendDynamicCode(phoneNum);
        response.getWriter().write("pass");
    }

    /**
     * 注册
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        int flag = checkDynamicCode(request);

        if (flag == -1) {
            response.getWriter().write("动态验证码错误");
        } else if (flag == -2) {
            response.getWriter().write("服务器繁忙");
        } else {
            User user = new User();
            BeanUtils.populate(user, request.getParameterMap());
            userService.registerService(user);
            response.getWriter().write("pass");
        }

    }

    //修改密码的四步骤

    public void stepOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getCodeAndTokenAndPhoneNum(request);
        User user = new User();
        user.setUsername(request.getParameter("username"));
        int flag = userService.isExistUser(user);
        if (flag == 0) {
            response.getWriter().write("用户不存在");
            return;
        }
        if (!code.equalsIgnoreCase(token)) {
            response.getWriter().write("验证码错误");
            return;
        }
        response.getWriter().write("pass");
    }

    public void stepTwo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int flag = checkDynamicCode(request);

        response.getWriter().write(setData(flag));
    }

    public void stepThree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        if (userService.resetPassword(user) == 0) {
            response.getWriter().write("服务器异常");
            return;
        }
        response.getWriter().write("pass");
    }

    public void stepFour(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }


    //随机生成六位动态码通过  容联云平台  发送
    //   然后将动态码放在redis缓存 过期时间设置为1分钟
    private static String sendDynamicCode(String phone) throws IOException {


        //随机生成六位数
        String dynamicCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        System.out.println(dynamicCode);


        //通过第三方发送短信  Ip  +  端口
        String serverIp = properties.getProperty("serverIp");
        String serverPort = properties.getProperty("serverPort");
        //主账号,登陆容联云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSID = properties.getProperty("accountSID");
        //账户授权令牌
        String accountToken = properties.getProperty("accountToken");

        String appId = properties.getProperty("appId");

        CCPRestSmsSDK sdk = new CCPRestSmsSDK();

        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSID, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);

        //模板ID
        String templateId = properties.getProperty("templateId");
        //短信格式 ：  你的验证码是{dynamicCode}，请于1分钟内正确输入。
        String[] datas = {dynamicCode, "1"};

        //将动态验证码放入redis缓存
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.ping().equalsIgnoreCase("PONG")) {
                return "-1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }

        HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas);

        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }

            //jedis  修改了  变成用SetParams的方法来实现了
            jedis.set(phone, dynamicCode, SetParams.setParams().ex(60).nx());
            jedis.close();
            return dynamicCode;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            return "-1";
        }
    }


    /**
     * 判断手机号对应的短信动态验证码正不正确
     *
     * @param request
     * @return -1代表验证码错误   -2代表 服务器有问题  0表示正常
     */
    private int checkDynamicCode(HttpServletRequest request) {
        String inputDynamicCode = request.getParameter("dynamicalCode");
        getCodeAndTokenAndPhoneNum(request);
        //判断手机号对应的短信动态验证码正不正确
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = null;
        try {
            System.out.println(phoneNum);
            jedis = jedisPool.getResource();
            String dynamicCode = jedis.get(phoneNum);
            if (dynamicCode == null || !dynamicCode.equalsIgnoreCase(inputDynamicCode)) {
                System.out.println("输入的动态验证码为:" + inputDynamicCode + "  redis中的为：" + dynamicCode);
                return -1;
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            jedis.close();
        }
    }
    private String setData(int i){
        if(i==-1){
            return "验证码错误";
        } else if (i==-2) {
            return "服务器异常";
        }
        else {
            return "pass";
        }
    }

    //获取请求中的  验证码  token 以及电话号
    private void getCodeAndTokenAndPhoneNum(HttpServletRequest request) {
        token = "";
        code = "";
        phoneNum = "";
        //获取动态生成的验证码
        token = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);

        //处理完这个就得删除  因为占的是session
        request.getSession().removeAttribute(KAPTCHA_SESSION_KEY);

        code = request.getParameter("code");
        //手机号码
        phoneNum = request.getParameter("phoneNum");
    }


}
