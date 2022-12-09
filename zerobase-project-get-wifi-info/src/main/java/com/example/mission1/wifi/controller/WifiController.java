package com.example.mission1.wifi.controller;

import com.example.mission1.wifi.dto.WifiDto;
import com.example.mission1.wifi.service.HistoryService;
import com.example.mission1.wifi.service.WifiService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class WifiController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String[] uri = req.getRequestURI().split("/WIFI");
        System.out.println("url = " + Arrays.toString(uri));
        String command = uri.length < 2 ? uri[0] : uri[1];
        String path = "/";
        WifiService ws = new WifiService();
        
        if (command.equals("/get-wifi")) {
            path = "/wifiPrint.jsp";
            try {
                String lat = req.getParameter("lat");
                String lnt = req.getParameter("lnt");

                System.out.println("lat : " + lat + " lnt : " + lnt);

                List<WifiDto> wifiList = ws.wifiSelect(lat, lnt);
                System.out.println("Select 완료");

                req.setAttribute("wifiList", wifiList);

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        
        if (command.equals("/history")) {
            path = "/history.jsp";

            HistoryService save_history = new HistoryService();
            req.setAttribute("historyList", save_history.getHistory());
        }


        RequestDispatcher dispatcher = req.getRequestDispatcher(path);
        dispatcher.forward(req, resp);


    }
}
