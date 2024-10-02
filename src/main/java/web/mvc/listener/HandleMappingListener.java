package web.mvc.listener;

import web.mvc.controller.Controller;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@WebListener
public class HandleMappingListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, Controller> map=new HashMap<String,Controller>();
        Map<String,Class<?>> classMap=new HashMap<>();

        // 외부의 ~.properties 파일 로딩, properties 자동으로 가지고 옴. 기본적으로 resources 에서 찾아서 경로 설정 이렇게
        ResourceBundle rb=ResourceBundle.getBundle("actionMapping");

        System.out.println("------------- Listener -------------");

        try
        {
            for (String key : rb.keySet())
            {
                String value = rb.getString(key);
                System.out.println("key = " + key);
//                System.out.println("key = " + value);

                Class<?> className = Class.forName(value);
                System.out.println("className = " + className);
//              Java 리플렉션 기능을 이용해 문자열로 주어진 클래스 이름(value)을 사용하여
//               해당 클래스의 메타데이터를 반환하는 메서드
                // 해당 클래스 이름에 해당하는 클래스가 jvm 의 클래스 경로에 로드되어 있어야 한다

                Controller con=(Controller)className.getDeclaredConstructor().newInstance();
                // 메타데이터를 통해 실제 객체를 얻는 방법
                System.out.println("key = " + value + " / " + con);

                map.put(key,con);// 여기에는 실제 컨트롤러 객체가 들어감
                classMap.put(key,className);
                // 컨트롤러의 메타데이터(객체의 정보를 담고 있음), 해당 클래스의 메서드,필드,생성자,상속 관계 등등
                // 리플렉션을 위해 메타데이터를 미리 얻는다
                // 메타데이터가 있어도 실제 함수를 호출하기 위해서는 실제 객체가 필요하기 때문에 위에 map 에는 실제 함수가 들어간다
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // map 을 application 영역에 저장한다, was 를 내리기 전까지 계속 유지-> 전체 어디서든 사용 가능
        ServletContext application = sce.getServletContext();
        application.setAttribute("map",map);
        application.setAttribute("classMap",classMap);
        application.setAttribute("path",application.getContextPath());

        System.out.println("HandleMappingListener.contextInitialized");
    }
}