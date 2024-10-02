package web.mvc.controller;

import web.mvc.dto.UserDTO;
import web.mvc.exception.AuthenticationException;
import web.mvc.service.UserService;
import web.mvc.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

public class UserController implements Controller {

    private UserService userService = new UserServiceImpl();

    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String pwd = request.getParameter("pwd");
        // login.jsp 에서 받는 값. name="userId", name="pwd"

        UserDTO userDTO = new UserDTO(userId, pwd, null);
        ModelAndView mv = new ModelAndView();

        try
        {
            UserDTO dbUser = userService.loginCheck(userDTO);

            if (dbUser != null) {
                // 로그인 성공(값을 받아 왔으니까)
                System.out.println("로그인 완료");
                request.getSession().setAttribute("loginUser", dbUser); // 세션에 로그인 정보 저장
                mv.setViewName("user/login.jsp"); // 로그인 성공하면 메인화면? 필터에서 로그인 성공했는지 판단
                mv.setRedirect(true);
            }
        }
        catch (AuthenticationException e)
        {
            // 로그인 실패
            request.setAttribute("error", e.getMessage());
            mv.setViewName("/error/error.jsp"); // 에러 페이지로 이동
            mv.setRedirect(false);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류");
            mv.setViewName("/error/error.jsp"); // 에러 페이지로 이동
            mv.setRedirect(false);
        }

        return mv;
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션에서 사용자 정보 제거
        request.getSession().invalidate();


        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.jsp");
        mv.setRedirect(true);

        return mv;
    }
}
