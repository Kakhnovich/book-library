package com.itechart.studets_lab.book_library.service.cover;

import org.apache.commons.io.input.BoundedInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {
    private static final FileReader INSTANCE = new FileReader();
    private static final Logger LOGGER = LogManager.getLogger(FileReader.class);

    FileReader() {
    }

    public String read(String packagePath, Part filePart, String fileName) {
        try (FileOutputStream file = new FileOutputStream(packagePath + fileName)) {
            InputStream in = new BoundedInputStream(filePart.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            file.write(response);
            return fileName;
        } catch (IOException e) {
            LOGGER.error("Error while trying to read a file \"" + fileName + "\" - " + e.getLocalizedMessage());
            return "";
        }
    }

    public static FileReader getInstance() {
        return INSTANCE;
    }
}
