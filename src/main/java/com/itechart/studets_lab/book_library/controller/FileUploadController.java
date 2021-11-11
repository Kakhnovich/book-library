package com.itechart.studets_lab.book_library.controller;

import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.WrappingRequestContext;
import com.itechart.studets_lab.book_library.command.page.ShowBookPage;
import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.cover.FileReader;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
public class FileUploadController extends HttpServlet {
    private static final String BOOK_ID_PARAMETER_NAME = "bookId";
    private static final String COVER_PARAMETER_NAME = "cover";
    private final BookService bookService = BookServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart(COVER_PARAMETER_NAME);
        String packagePath = req.getSession().getServletContext().getRealPath("/") + "img/";
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String coverPath = FileReader.getInstance().read(packagePath, filePart, fileName);
        int bookId = Integer.parseInt(String.valueOf(req.getParameter(BOOK_ID_PARAMETER_NAME)));
        BookDto book = bookService.findByKey(bookId);
        book.setCoverLink(coverPath);
        bookService.update(book);
        final ResponseContext result = ShowBookPage.INSTANCE.execute(WrappingRequestContext.of(req));
        if (result.isRedirect()) {
            resp.sendRedirect(result.getPage());
        } else {
            final RequestDispatcher dispatcher = req.getRequestDispatcher(result.getPage());
            dispatcher.forward(req, resp);
        }
    }
}
