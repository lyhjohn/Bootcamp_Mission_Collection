package WIFI;

import dto.historyDto;
import dto.wifiDto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1. 근처 와이파이 정보 보기,
 * 2. 위치 히스토리 목록 가져오기
 * 위 두 서비스와 연결된 컨트롤러
 */
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String[] uri = req.getRequestURI().split("/WIFI");
        System.out.println("url = " + Arrays.toString(uri));
        String command = uri.length < 2 ? uri[0] : uri[1];
        String path = "/";
        wifiService ws = new wifiService();
        
        if (command.equals("/get-wifi")) {
            path = "/wifiPrint.jsp";
            System.out.println("get-wifi 서블릿 매핑 성공");
            try {
                String lat = req.getParameter("lat");
                String lnt = req.getParameter("lnt");

                System.out.println("lat : " + lat + " lnt : " + lnt);

                List<wifiDto> wifiList = ws.WIFI_Select(lat, lnt);
                System.out.println("Select 완료");

                req.setAttribute("wifiList", wifiList);

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        
        if (command.equals("/history")) {
            path = "/history.jsp";
            System.out.println("history 서블릿 매핑 성공");

            saveHistory save_history = new saveHistory();
            req.setAttribute("historyList", save_history.getHistory());
        }


        RequestDispatcher dispatcher = req.getRequestDispatcher(path);
        dispatcher.forward(req, resp);


    }
}
