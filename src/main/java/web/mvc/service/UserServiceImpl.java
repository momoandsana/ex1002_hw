package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.UserDTO;
import exception.AuthenticationException;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public UserDTO loginCheck(UserDTO userDTO) throws SQLException, AuthenticationException {
        // DAO를 통해 데이터베이스에서 사용자 정보 확인
        UserDTO dbUser = userDAO.loginCheck(userDTO);

        if (dbUser == null) {
            // 사용자 정보가 없으면 로그인 실패
            throw new AuthenticationException("Invalid username or password.");
        }

        // 사용자 정보가 있으면 로그인 성공
        return dbUser;
    }
}
