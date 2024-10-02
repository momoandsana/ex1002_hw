package controller;

import dto.UserDTO;
import exception.AuthenticationException;
import service.UserService;
import service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class UserController implements Controller {

    private UserService userService = new UserServiceImpl();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String pwd = request.getParameter("pwd");

        // DTO 객체 생성
        UserDTO userDTO = new UserDTO(userId, pwd, null);

        ModelAndView mv = new ModelAndView();

        try {
            // 로그인 확인
            UserDTO dbUser = userService.loginCheck(userDTO);

            if (dbUser != null) {
                // 로그인 성공
                request.getSession().setAttribute("user", dbUser);
                mv.setViewName("loginSuccess.jsp");
                mv.setRedirect(true); // 리다이렉트 방식으로 이동
            }
        }
        catch (AuthenticationException e)
        {
            // 로그인 실패
            request.setAttribute("error", e.getMessage());
            mv.setViewName("/error/error.jsp"); // 에러 페이지로 이동
            mv.setRedirect(false); // 포워드 방식으로 이동

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred");
            mv.setViewName("/error/error.jsp"); // 에러 페이지로 이동
            mv.setRedirect(false); // 포워드 방식으로 이동
        }

        return mv; // ModelAndView 반환
    }
}
