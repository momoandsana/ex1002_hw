package web.mvc.service;

import web.mvc.dao.ElectronicsDAO;
import web.mvc.dao.ElectronicsDAOImpl;
import web.mvc.dto.Electronics;

import java.sql.SQLException;
import java.util.List;

public class ElectronicsServiceImpl implements ElectronicsService {

    private ElectronicsDAO electronicsDAO = new ElectronicsDAOImpl();

    @Override
    public List<Electronics> selectAll() throws SQLException {
        // 모든 레코드 검색
        return electronicsDAO.selectAll();
    }

    // 페이징은 나중에?
    @Override
    public List<Electronics> selectAll(int pageNo) throws SQLException {
        // 페이징 처리를 통해 특정 페이지의 레코드 검색
        return electronicsDAO.getBoardList(pageNo);
    }

    @Override
    public void insert(Electronics electronics) throws SQLException {
        // 레코드 삽입
        electronicsDAO.insert(electronics);
    }

    @Override
    public Electronics selectByModelnum(String modelNum, boolean flag) throws SQLException {
        Electronics electronics = electronicsDAO.selectByModelNum(modelNum);

        // flag가 true인 경우 조회수 증가
        if (flag) {
            electronicsDAO.increamentByReadnum(modelNum);
        }

        return electronics;
    }

    @Override
    public void delete(String modelNum, String password, String saveDir) throws SQLException {
        int result = electronicsDAO.delete(modelNum, password);
        // 파일 시스템에서 파일 삭제
    }

    @Override
    public void update(Electronics electronics) throws SQLException {
        int result = electronicsDAO.update(electronics);
    }
}
