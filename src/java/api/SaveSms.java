/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.Gson;
import helpers.DbHelper;
import helpers.Stopwatch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.SaveSmsResponse;
import models.SmsMinified;

/**
 *
 * @author nazif
 */
@WebServlet(name = "SaveSms", urlPatterns = {"/api/save-sms"})
public class SaveSms extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        Stopwatch stopwatch = new Stopwatch();
        StringBuilder sb = new StringBuilder();
        String line = null;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        SmsMinified sms = new Gson().fromJson(sb.toString(), SmsMinified.class);

        String tableCreationResult = null;
        String insertResult = null;
        SaveSmsResponse resp = new SaveSmsResponse();

        try {

            boolean exist = DbHelper.isDestinationTableExisted(this.getServletContext());
            if (!exist) {
                tableCreationResult = DbHelper.createDestinationTable(this.getServletContext());
            }

            insertResult = DbHelper.insertSms(this.getServletContext(), sms.transId, sms.from, sms.to, sms.content);

            resp.transId = sms.transId;
            resp.destinationTableExist = exist;
            resp.destinationTableCreationStatus = tableCreationResult;
            resp.insertDataStatus = insertResult;
            resp.completedInMillisecond = stopwatch.elapsedTimeMillisecond();
            resp.exception = null;

        } catch (Exception ex) {

            resp.transId = sms.transId;
            resp.destinationTableExist = false;
            resp.destinationTableCreationStatus = tableCreationResult;
            resp.insertDataStatus = insertResult;
            resp.completedInMillisecond = stopwatch.elapsedTimeMillisecond();
            resp.exception = ex;
        }

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(resp));
        out.flush();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
