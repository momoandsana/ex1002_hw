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

        ModelAndView mv = new ModelAndView("elec/list.jsp", false);
        return mv;
    }

    //상세보기-완료
    public ModelAndView selectByModelNum(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        System.out.println("상세보기 도착");
        String modelNum = request.getParameter("modelNum");
        // 'incrementRead' 파라미터를 확인하여 boolean 값으로 변환
        boolean incrementRead = Boolean.parseBoolean(request.getParameter("incrementRead"));
        System.out.println(incrementRead);
        Electronics electronics = electronicsService.selectByModelnum(modelNum, incrementRead);
        request.setAttribute("elec", electronics);
        /*
          여기서 elec 으로 전달하면 여기에 게시글+댓글 정보가 모두 들어 있음
           댓글에 대한 정보는 다른 테이블에 있지만 electronics dto 에 replies 가 있으므로 여기에다가 추가할 수 있다
         */


//        System.out.println(electronics.getModelNum());
//        System.out.println(electronics.getModelName());;
        ModelAndView mv = new ModelAndView("elec/read.jsp", false);
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
            request.getSession().setAttribute("message", "Insert 성공!");
        }
        catch (SQLException e)
        {
            request.getSession().setAttribute("error", "Insert 실패: " + e.getMessage());
        }

        ModelAndView mv=list(request, response);
        // 여기서 바로 elec/list.jsp 로 가버리면 리스트가 사라지는 현상이 있음. list 함수를 통해 다시 디비에서 조회해서 반영
        return mv;
    }

    //완료
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        System.out.println("delete 도착");
        String modelNum = request.getParameter("modelNum");
        String password = request.getParameter("password");
        System.out.println("password = " + password);
        String saveDir = request.getParameter("saveDir"); // 파일은 저장x

        try
        {
            electronicsService.delete(modelNum, password, saveDir);
            request.getSession().setAttribute("message", "삭제 성공");
        }
        catch (SQLException e)
        {
            request.getSession().setAttribute("error", "삭제 실패 : " + e.getMessage());
        }
        System.out.println("delete try 문 끝");
        ModelAndView mv=list(request, response);
        return mv;
    }

    // 미완성-> 수정칸에서 수정이 불가능, 칸에 마우스 커서가 안 들어감
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        System.out.println("update 도착");

        // 수정할 모델 번호 받아오기
        String modelNum = request.getParameter("modelNum");

        // 데이터베이스에서 모델 번호를 통해 Electronics 정보를 조회
        Electronics electronics = electronicsService.selectByModelnum(modelNum, false);

        // 조회된 모델 정보를 update.jsp로 전달
        request.setAttribute("elec", electronics);

        // update.jsp 수정 폼 페이지로 이동
        return new ModelAndView("elec/update.jsp", false);
    }

}
