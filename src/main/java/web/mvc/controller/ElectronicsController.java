package web.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.mvc.dto.Electronics;
import web.mvc.service.ElectronicsService;
import web.mvc.service.ElectronicsServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ElectronicsController implements Controller {

    private ElectronicsService electronicsService = new ElectronicsServiceImpl();

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        List<Electronics> electronicsList = electronicsService.selectAll();
        request.setAttribute("list", electronicsList);
        // list.jsp 에서도 RequestScope.list 로 받아야 함

        for (Electronics electronics : electronicsList) {
            System.out.println(electronics);
        }

        ModelAndView mv = new ModelAndView("elec/list.jsp", false);
        return mv;
    }

    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String modelNum = request.getParameter("modelNum");
        // 'incrementRead' 파라미터를 확인하여 boolean 값으로 변환
        boolean incrementRead = Boolean.parseBoolean(request.getParameter("incrementRead"));
        Electronics electronics = electronicsService.selectByModelnum(modelNum, incrementRead);
        request.setAttribute("electronics", electronics);
        ModelAndView mv = new ModelAndView("elec/detail.jsp", false);
        return mv;
    }

    public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String modelNum = request.getParameter("modelNum");
        String modelName = request.getParameter("modelName");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String password = request.getParameter("password");

        Electronics electronics = new Electronics(modelNum, modelName, price, description, password);
        try
        {
            electronicsService.insert(electronics);
            request.getSession().setAttribute("message", "Insert successful!");
        }
        catch (SQLException e)
        {
            request.getSession().setAttribute("error", "Insert failed: " + e.getMessage());
        }

        ModelAndView mv=list(request, response);
        // 여기서 바로 elec/list.jsp 로 가버리면 리스트가 사라지는 현상이 있음
        return mv;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String modelNum = request.getParameter("modelNum");
        String password = request.getParameter("password");
        // 파일 저장 경로를 얻기, 실제 경로는 환경에 따라 다를 수 있음
        String saveDir = request.getServletContext().getRealPath("/uploads");

        try {
            electronicsService.delete(modelNum, password, saveDir);
            request.getSession().setAttribute("message", "Delete successful!");
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Delete failed: " + e.getMessage());
        }

        ModelAndView mv = new ModelAndView("elec/list.jsp", true);
        return mv;
    }


    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String modelNum = request.getParameter("modelNum");
        String modelName = request.getParameter("modelName");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String password = request.getParameter("password");

        Electronics electronics = new Electronics(modelNum, modelName, price, description, password);
        try {
            electronicsService.update(electronics);
            request.getSession().setAttribute("message", "Update successful!");
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Update failed: " + e.getMessage());
        }

        ModelAndView mv = new ModelAndView("elec/list.jsp", true);
        return mv;
    }
}
