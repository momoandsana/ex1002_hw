package web.mvc.dao;

import web.mvc.dto.UserDTO;

import java.sql.SQLException;



public interface UserDAO {
  /**
   * 로그인 기능
   * select user_id , pwd, name from users where user_id=? and pwd=?
   * */
	UserDTO loginCheck(UserDTO userDTO)throws SQLException;
}
