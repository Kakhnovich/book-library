package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum LibraryInfoPage implements Command {
    INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger(LibraryInfoPage.class);
    private static final String PROPS_FILE_NAME = "gmail.properties";
    private static final String LIBRARY_NAME_PROPERTY = "library.name";
    private static final String LIBRARY_SIGNATURE_PROPERTY = "library.signature";
    private static final String LIBRARY_ADDRESS_PROPERTY = "library.address";
    private static final String LIBRARY_NAME_ATTRIBUTE_NAME = "name";
    private static final String LIBRARY_SIGNATURE_ATTRIBUTE_NAME = "signature";
    private static final String LIBRARY_ADDRESS_ATTRIBUTE_NAME = "address";

    private static final ResponseContext INFO_PAGE_RESPONSE = new ResponseContext(UrlPatterns.INFO, false);

    @Override
    public ResponseContext execute(RequestContext request) {
        try (InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME)) {
            Properties props = new Properties();
            props.load(fileInputStream);
            String libraryName = props.getProperty(LIBRARY_NAME_PROPERTY);
            request.setAttribute(LIBRARY_NAME_ATTRIBUTE_NAME, libraryName);
            String librarySignature = props.getProperty(LIBRARY_SIGNATURE_PROPERTY);
            request.setAttribute(LIBRARY_SIGNATURE_ATTRIBUTE_NAME, librarySignature);
            String libraryAddress = props.getProperty(LIBRARY_ADDRESS_PROPERTY);
            request.setAttribute(LIBRARY_ADDRESS_ATTRIBUTE_NAME, libraryAddress);
        } catch (IOException e) {
            LOGGER.error("IOException at opening email properties: " + e.getLocalizedMessage());
        }
        return INFO_PAGE_RESPONSE;
    }
}
