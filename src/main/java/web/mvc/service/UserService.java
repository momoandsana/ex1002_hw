 package service;

 import dto.UserDTO;
 import exception.AuthenticationException;

 import java.sql.SQLException;



public interface UserService {
	/**
	 * 로그인 체크
	 * */
   UserDTO loginCheck(UserDTO userDTO)throws SQLException , AuthenticationException;
}
