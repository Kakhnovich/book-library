package com.itechart.studets_lab.book_library.controller;

import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.WrappingRequestContext;
import com.itechart.studets_lab.book_library.command.page.ShowBookPage;
import com.itechart.studets_lab.book_library.service.google_drive.DriveService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
public class FileUploadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Part filePart = req.getPart("cover");
        InputStream fileInputStream = filePart.getInputStream();
        File fileToSave = new File("WebContent/uploaded-files/" + filePart.getSubmittedFileName());
        Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String id = DriveService.getInstance().upload(fileName);
        final ResponseContext result = ShowBookPage.INSTANCE.execute(WrappingRequestContext.of(req));
        if (result.isRedirect()) {
            resp.sendRedirect(result.getPage());
        } else {
            final RequestDispatcher dispatcher = req.getRequestDispatcher(result.getPage());
            dispatcher.forward(req, resp);
        }
    }
}
