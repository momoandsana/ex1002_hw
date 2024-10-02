package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 모든 요청을 중앙 집중적으로 관리해줄 진입점 controller = FrontController 이다
 */

@WebServlet(loadOnStartup=1, urlPatterns="/front")
public class DispatcherServlet extends HttpServlet {

    private Map<String, Controller> map;
    private Map<String, Class<?>> classMap;

    @Override
    public void init(ServletConfig config) throws ServletException {
        // application 영역에 저장된 정보를 조회 -> map 을 통해
        ServletContext application = config.getServletContext();

        map = (Map<String, Controller>) application.getAttribute("map");
        /*
         object 로 리턴하니까 다운캐스팅
         위에 map 은 이미 생성된 컨트롤러 객체를 저장하여 사용
         */
        classMap = (Map<String, Class<?>>) application.getAttribute("classMap");
        /*
        HandleMappingListener 에서 올린 정보들 쓰는 중
        컨트롤러의 클래스 정보를 저장하여 리플렉션을 통해 메서드 호출이나 객체 생성에 사용
        쿼리문의 where id = ? 같은 느낌으로 입력 받은 함수를 처리하기 위해 classMap 을 사용
        이미 객체가 만들어져서 모든 경우의 수를 만들면 구현이 가능하지만 case 문이 길어지므로 동적으로 처리

        리플렉션은 메서드 호출을 동적으로 처리하기 위한 것이지, 객체 자체를 생성하는 것이 주 목적이 아니다
        따라서, 객체는 미리 생성해 두고, 리플렉션은 그 객체에 있는 메서드를 동적으로 호출
         */
    }

    /*
    <a href="front?key=userController&methodName=insert">Insert User</a>
    <a href="front?key=userController&methodName=update">Update User</a>
    <a href="front?key=userController&methodName=delete">Delete User</a>

    이런 방식으로 전달 받아야 한다
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 전송된 key 받기
        String key = request.getParameter("key");
        System.out.println("key = " + key);

        String methodName = request.getParameter("methodName");

        ModelAndView mv = null;
        try {
            Controller con = map.get(key);// 해당 컨트롤러 객체를 얻는다
            Class<?> className = classMap.get(key); // 컨트롤러 객체의 메타데이터를 얻는다
            Method method = className.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class); // controller 함수들의 공통적인 파라미터
            // 메타 데이터를 이용해서 호출할려고 하는 메서드를 얻는다. Method 또한 메타데이터이다
            // 우리가 호출하려고 하는 함수의 파라미터는 HttpServletRequest.class, HttpServletResponse.class)
            // Class<?> 는 클래스 전체에 대한 메타데이터이고, Method 는 더 구체적인 메타데이터. 특정 메섣를 호출하기 위해 더 세세하게 뽑아냄
            mv = (ModelAndView) method.invoke(con, request, response);
            // 해당 객체와 해당 객체에 있는 함수에 대한 정보를 이용해서 해당 객체의 특정 함수를 실행한다
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (mv.isRedirect()) {
            response.sendRedirect(mv.getViewName());
        } else {
            // forward 하는 경우
            request.getRequestDispatcher(mv.getViewName()).forward(request, response);
        }
    }
}