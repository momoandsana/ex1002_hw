package controller;

/**
 * 요청을 처리한 후 최종적으로 이동할 뷰 정보와 이동 방식에 대한 속성을 관리한다
 */
public class ModelAndView {
    private String viewName; // 이동할 뷰 이름
    private boolean redirect; // 이동한 방식 결정(ture 이면 redirect 방식, false 이면 forward)

    public ModelAndView() {}

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, boolean redirect) {
        this(viewName);// 생성자 오버로딩
        this.redirect = redirect;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
}