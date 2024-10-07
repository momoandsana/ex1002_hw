package web.mvc.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = "/front") // 모든 "/front" 경로에 대해 필터를 적용
public class UserLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        // 로그인 기능 자체는 필터링 당하면 안 된다
        String uri = request.getRequestURI();
        String methodName = request.getParameter("methodName");

        if ("/front".equals(uri) && "login".equals(methodName)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 로그인된 사용자인지 확인 (세션에 loginUser라는 속성이 있는지 확인)
        if (session.getAttribute("loginUser") == null) {
            request.setAttribute("errorMsg", "로그인하고 이용해주세요");
            System.out.println("필터: 로그인되지 않음. 에러 메시지 설정 완료");
            request.getRequestDispatcher("/error/error.jsp").forward(servletRequest, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
