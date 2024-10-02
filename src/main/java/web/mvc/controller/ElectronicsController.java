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
        request.setAttribute("electronicsList", electronicsList);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("elec/list.jsp"); // 전자 제품 목록을 표시할 JSP 페이지
        mv.setRedirect(false);
        return mv;
    }

    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String modelNum = request.getParameter("modelNum");
        boolean increment = Boolean.parseBoolean(request.getParameter("increment"));
        Electronics electronics = electronicsService.selectByModelnum(modelNum, increment);
        request.setAttribute("electronics", electronics);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("elec/detail.jsp"); // 전자 제품 세부 정보를 표시할 JSP 페이지
        mv.setRedirect(false);
        return mv;
    }

    public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // 입력 폼에서 전달된 데이터 처리
        System.out.println("--------- ElectronicsController ---------");
        System.out.println("insert 함수 도착");

        String modelNum = request.getParameter("modelNum");
        System.out.println("modelNum = " + modelNum);
        String modelName = request.getParameter("name");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String password = request.getParameter("password");

//        Electronics electronics = new Electronics(modelNum, modelName, price, description, password);
        Electronics electronics = new Electronics("999", "modelName", 500, "hi", "1234");
        electronicsService.insert(electronics);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("electronics?action=list"); // 삽입 후 목록으로 리다이렉트
        mv.setRedirect(true);
        return mv;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String modelNum = request.getParameter("modelNum");
        String password = request.getParameter("password");
        String saveDir = request.getServletContext().getRealPath("/uploads"); // 파일 저장 경로

        electronicsService.delete(modelNum, password, saveDir);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("electronics?action=list"); // 삭제 후 목록으로 리다이렉트
        mv.setRedirect(true);
        return mv;
    }

    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // 입력 폼에서 전달된 데이터 처리
        String modelNum = request.getParameter("modelNum");
        String modelName = request.getParameter("name");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String password = request.getParameter("password");

        // 생성자에 맞게 Electronics 객체 생성
        Electronics electronics = new Electronics(modelNum, modelName, price, description, password);
        electronicsService.update(electronics);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("electronics?action=list"); // 업데이트 후 목록으로 리다이렉트
        mv.setRedirect(true);
        return mv;
    }
}
