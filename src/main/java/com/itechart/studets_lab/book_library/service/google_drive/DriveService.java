package com.itechart.studets_lab.book_library.service.google_drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DriveService {
    private static final DriveService INSTANCE = new DriveService();
    private static final Logger LOGGER = LogManager.getLogger(DriveService.class);
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String DEFAULT_URL = "https://drive.google.com/uc?id=";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private DriveService() {
    }

    public static DriveService getInstance() {
        return INSTANCE;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = DriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setScopes(Arrays.asList("https://spreadsheets.google.com/feeds", "https://www.googleapis.com/auth/drive"))
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Drive getService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String getURL(String fileName) {
        try {
            FileList result = getService().files().list()
                    .setFields("nextPageToken, files(id, name, webViewLink)")
                    .execute();
            List<File> files = result.getFiles();

            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
                Optional<File> file = files.stream().filter(someFile -> someFile.getName().equals(fileName)).findFirst();
                if (file.isPresent()) {
                    System.out.println("File ID: " + file.get().getId());
                    return DEFAULT_URL + file.get().getId();
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Error while trying to find files: " + e.getLocalizedMessage());
        }
        return "";
    }

    public String upload(String filePath) {
        FileContent mediaContent = new FileContent("image/jpeg", new java.io.File("WebContent/uploaded-files/" + filePath));
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        try {
            File file = getService().files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return getURL(file.getName());
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Error while trying to create file " + filePath + " - " + e.getLocalizedMessage());
        }
        return "";
    }
}