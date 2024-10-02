package web.mvc.service;

import web.mvc.dao.UserDAO;
import web.mvc.dao.UserDAOImpl;
import web.mvc.dto.UserDTO;
import web.mvc.exception.AuthenticationException;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public UserDTO loginCheck(UserDTO userDTO) throws SQLException, AuthenticationException {

        UserDTO dbUser = userDAO.loginCheck(userDTO);

        if (dbUser == null) {
            // 사용자 정보가 없으면 로그인 실패
            throw new AuthenticationException("이름이나 비밀번호가 틀렸습니다");
        }

        return dbUser;
    }
}
