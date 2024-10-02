 package web.mvc.service;

 import web.mvc.dto.UserDTO;
 import web.mvc.exception.AuthenticationException;

 import java.sql.SQLException;



public interface UserService {
	/**
	 * 로그인 체크
	 * */
   UserDTO loginCheck(UserDTO userDTO)throws SQLException , AuthenticationException;
}
