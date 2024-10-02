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
        return electronicsDAO.selectAll();
    }

    // 페이징은 나중에?
    @Override
    public List<Electronics> selectAll(int pageNo) throws SQLException {
        return electronicsDAO.getBoardList(pageNo);
    }

    @Override
    public void insert(Electronics electronics) throws SQLException {
        electronicsDAO.insert(electronics);
    }

    @Override
    public Electronics selectByModelnum(String modelNum, boolean flag) throws SQLException {
        Electronics electronics = electronicsDAO.selectByModelNum(modelNum);
        System.out.println("ElectronicsServiceImpl.selectByModelnum");
        // flag가 true인 경우 조회수 증가

        System.out.println(flag);
        if (flag)
        {
            electronicsDAO.increamentByReadnum(modelNum);
        }

        return electronics;
    }

    @Override
    public void delete(String modelNum, String password, String saveDir) throws SQLException {
        int result = electronicsDAO.delete(modelNum, password);
    }

    @Override
    public void update(Electronics electronics) throws SQLException {
        int result = electronicsDAO.update(electronics);
    }
}
