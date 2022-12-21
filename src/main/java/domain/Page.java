package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhw
 * @version 1.0
 **/
public class Page <T>{
    //每页的大小
    public static final int PAGE_SIZE = 5;

    //存放每页的对象
    List<T> objs = new ArrayList<>();

    //当前页码
    private int pageNo;

    //数据库总共有几页
    private int pageTotal;

    //数据库总共有几个对象
    private Long itemsTotal;

    private String url;

    private String selectType;
    private String keyboard;
    public Page() {
    }


    public List<T> getObjs() {
        return objs;
    }

    public void setObjs(List<T> objs) {
        this.objs = objs;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo<1){
          pageNo=1;
        }
        if (pageNo>pageTotal&&pageTotal>0) {
            pageNo=this.pageTotal;
        }
        this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Long getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(Long itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(String keyboard) {
        this.keyboard = keyboard;
    }
}
