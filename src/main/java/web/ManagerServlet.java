package web;

import domain.Page;
import domain.User;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yhw
 * @version 1.0
 **/
public class ManagerServlet extends ViewBaseServlet{
    private UserService userService = new UserService();
    //跳转到用户显示页面
    public void toShowUsers(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String pageNoStr = request.getParameter("pageNo");
        //判断请求第几页数据
        int pageNo ;
        String selectType =  request.getParameter("selectType")!=null?request.getParameter("selectType"):"username";
        String keyboard =  request.getParameter("keyboard")!=null?request.getParameter("keyboard"):"";

        //传进来的pageNo
        if(pageNoStr==null||pageNoStr==""){
            pageNo=1;
        }
        else {
            pageNo = Integer.parseInt(pageNoStr);
        }
        System.out.println("selectType:  "+selectType +"   keyboard:"+keyboard);

        //在后面数据库交互时，我们还会对pageNo进行范围限定
        Page<User> page =userService.page(pageNo,Page.PAGE_SIZE,selectType,keyboard);
        page.setSelectType(selectType);
        page.setKeyboard(keyboard);
        page.setUrl("/manager?action=toShowUsers&selectType="+selectType+"&keyboard="+keyboard);
        request.setAttribute("page",page);
        request.setAttribute("keyboard",keyboard);

        //获取经过 范围判断的  当前页码
        pageNo = page.getPageNo();


        //设置分页的起始、结束页
        if(pageNo>1&&pageNo<page.getPageTotal()){
            request.setAttribute("startPage",pageNo-1);
            request.setAttribute("endPage",pageNo+1);
        }
        else if (pageNo==1){
            request.setAttribute("startPage",1);
            int endPage = Math.min(page.getPageTotal(),3);
            if (endPage==0){
                endPage=1;
            }
            request.setAttribute("endPage",endPage);

        } else if (pageNo==page.getPageTotal()) {
            request.setAttribute("endPage",pageNo);
            int startPage = Math.max(pageNo-2,1);
            request.setAttribute("startPage",startPage);
        }

        super.processTemplate("manager/showUsers", request, response);

    }

    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String pageNo = request.getParameter("pageNo");
        String selectType = request.getParameter("selectType");
        String keyboard = request.getParameter("keyboard");

        userService.deleteByUsername(username);

        response.sendRedirect(request.getContextPath()+"/manager?action=toShowUsers&pageNo="+pageNo+"&selectType="+selectType+"&keyboard="+keyboard);

    }
}
