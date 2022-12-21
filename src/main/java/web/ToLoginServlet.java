package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yhw
 * @version 1.0
 *
 * 修改默认首页  通过thymeleaf解析我们自己写的登录页面
 **/
public class ToLoginServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


    //需要经历thymeleaf模板引擎加载一下，不然一些显示不出来
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.processTemplate("user/login",request,response);

    }
}
