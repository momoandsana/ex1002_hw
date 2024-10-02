package web.mvc.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/front")
public class AutheticationCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpSession session=request.getSession();

        if(session.getAttribute("userId")==null)
        {
            request.setAttribute("errorMsg","로그인하고 이용해주세요");
            System.out.println("에러메시지를 담았음");
            request.getRequestDispatcher("/error/error.jsp").forward(request,servletResponse);
        }

    }
}
