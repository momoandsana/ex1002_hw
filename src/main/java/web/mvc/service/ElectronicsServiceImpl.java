package web.mvc.service;

import web.mvc.dao.ElectronicsDAO;
import web.mvc.dao.ElectronicsDAOImpl;
import web.mvc.dto.Electronics;
import web.mvc.exception.ElectronicsException;

import java.sql.SQLException;
import java.util.List;

public class ElectronicsImpl implements ElectronicsService {

    private ElectronicsDAO electronicsDAO = new ElectronicsDAOImpl();

    @Override
    public List<Electronics> selectAll() throws SQLException {
        return electronicsDAO.selectAll();
    }

    @Override
    public List<Electronics> getBoardList(int pageNo) throws SQLException {
        return electronicsDAO.getBoardList(pageNo);
    }

    @Override
    public Electronics selectByModelNum(String modelNum) throws SQLException, ElectronicsException {
        Electronics electronics = electronicsDAO.selectByModelNum(modelNum);
        if (electronics == null) {
            throw new ElectronicsException("No electronics found with model number: " + modelNum);
        }
        return electronics;
    }

    @Override
    public int increamentByReadnum(String modelNum) throws SQLException {
        return electronicsDAO.increamentByReadnum(modelNum);
    }

    @Override
    public int insert(Electronics electronics) throws SQLException {
        return electronicsDAO.insert(electronics);
    }

    @Override
    public int delete(String modelNum, String password) throws SQLException, ElectronicsException {
        int result = electronicsDAO.delete(modelNum, password);
        if (result == 0) {
            throw new ElectronicsException("Deletion failed. Invalid model number or password.");
        }
        return result;
    }

    @Override
    public int update(Electronics electronics) throws SQLException, ElectronicsException {
        int result = electronicsDAO.update(electronics);
        if (result == 0) {
            throw new ElectronicsException("Update failed. Invalid model number or password.");
        }
        return result;
    }
}
