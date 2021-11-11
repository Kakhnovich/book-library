package com.itechart.studets_lab.book_library.service.email;

import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.BorrowService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;
import com.itechart.studets_lab.book_library.service.impl.BorrowServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.stringtemplate.v4.ST;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

public class GmailSender implements Job {
    private static final Logger LOGGER = LogManager.getLogger(GmailSender.class);
    private static final String PROPS_FILE_NAME = "gmail.properties";
    private static final String USER_NAME_PROPERTY = "gmail.username";
    private static final String PASSWORD_PROPERTY = "gmail.password";
    private static final String MAIL_SUBJECT_PROPERTY = "gmail.mail.subject";
    private static final String BILL_NOTIFICATION_PROPERTY = "gmail.mail.message.billNotification";
    private static final String NOTIFICATION_PROPERTY ="gmail.mail.message.notification";
    private static final String MESSAGE_READER_FIRST_NAME_ATTRIBUTE_NAME = "firstName";
    private static final String MESSAGE_READER_LAST_NAME_ATTRIBUTE_NAME = "lastName";
    private static final String MESSAGE_BORROW_DATE_ATTRIBUTE_NAME = "borrowDate";
    private static final String MESSAGE_BOOK_TITLE_ATTRIBUTE_NAME = "bookTitle";
    private static final String MESSAGE_BOOK_ISBN_ATTRIBUTE_NAME = "isbn";
    private static final String MESSAGE_BOOK_PUBLISHER_ATTRIBUTE_NAME = "publisher";
    private static final String MESSAGE_BOOK_PUBLISH_DATE_ATTRIBUTE_NAME = "publishDate";
    private static final String MESSAGE_BOOK_PAGE_COUNT_ATTRIBUTE_NAME = "pageCount";
    private static final String MESSAGE_BORROW_DURATION_ATTRIBUTE_NAME = "duration";
    private static final String MESSAGE_BORROW_LEFT_TIME_ATTRIBUTE_NAME = "leftTime";
    private static final char DELIMITER_CHAR = '$';
    private final Properties prop = new Properties();
    private final BorrowService borrowService = BorrowServiceImpl.getInstance();
    private final BookService bookService = BookServiceImpl.getInstance();
    private String username;
    private String password;

    private void setSmtpProperties() {
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (prop.isEmpty()) {
            setSmtpProperties();
        }
        List<BorrowDto> borrows = borrowService.findAll();
        if (!borrows.isEmpty()) {
            for (BorrowDto borrow : borrows) {
                long daysLeft = getDaysLeft(borrow.getBorrowDate(), borrow.getDuration());
                if (daysLeft <= 1 || daysLeft == 7) {
                    BookDto book = bookService.findByKey(borrow.getBookId());
                    if (book != null) {
                        sendMailForReader(borrow, book);
                    }
                }
            }
        }
    }

    private void sendMailForReader(BorrowDto borrow, BookDto book) {
        try (InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME)) {
            Properties emailProperties = new Properties();
            emailProperties.load(fileInputStream);
            username = emailProperties.getProperty(USER_NAME_PROPERTY);
            password = emailProperties.getProperty(PASSWORD_PROPERTY);
            String mailSubject = emailProperties.getProperty(MAIL_SUBJECT_PROPERTY);
            ST mailMessage = addMailMessageAttributes(emailProperties, borrow, book);
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            Message message = new MimeMessage(session);
            try {
                setMessageElements(message, mailSubject, mailMessage, borrow.getReader().getEmail());
                Transport.send(message);
            } catch (MessagingException e) {
                LOGGER.error("MessagingException while trying to send mail for " + borrow.getReader().getEmail() + ':' + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            LOGGER.error("IOException at opening email properties: " + e.getLocalizedMessage());
        }
    }

    private ST addMailMessageAttributes(Properties emailProperties, BorrowDto borrow, BookDto book){
        ST mailMessage;
        if (borrow.getBorrowDate().plusMonths(borrow.getDuration()).isBefore(LocalDate.now())) {
            mailMessage = new ST(emailProperties.getProperty(BILL_NOTIFICATION_PROPERTY), DELIMITER_CHAR, DELIMITER_CHAR);
        } else {
            mailMessage = new ST(emailProperties.getProperty(NOTIFICATION_PROPERTY), DELIMITER_CHAR, DELIMITER_CHAR);
            mailMessage.add(MESSAGE_BORROW_LEFT_TIME_ATTRIBUTE_NAME, getDaysLeft(borrow.getBorrowDate(), borrow.getDuration()));
        }
        mailMessage.add(MESSAGE_READER_FIRST_NAME_ATTRIBUTE_NAME, borrow.getReader().getFirstName());
        mailMessage.add(MESSAGE_READER_LAST_NAME_ATTRIBUTE_NAME, borrow.getReader().getLastName());
        mailMessage.add(MESSAGE_BORROW_DATE_ATTRIBUTE_NAME, borrow.getBorrowDate());
        mailMessage.add(MESSAGE_BOOK_TITLE_ATTRIBUTE_NAME, book.getTitle());
        mailMessage.add(MESSAGE_BOOK_ISBN_ATTRIBUTE_NAME, book.getIsbn());
        mailMessage.add(MESSAGE_BOOK_PUBLISHER_ATTRIBUTE_NAME, book.getPublisher());
        mailMessage.add(MESSAGE_BOOK_PUBLISH_DATE_ATTRIBUTE_NAME, book.getPublishDate());
        mailMessage.add(MESSAGE_BOOK_PAGE_COUNT_ATTRIBUTE_NAME, book.getPageCount());
        mailMessage.add(MESSAGE_BORROW_DURATION_ATTRIBUTE_NAME, borrow.getDuration());
        return mailMessage;
    }

    private void setMessageElements(Message message, String mailSubject, ST mailMessage, String email) throws MessagingException {
        message.setFrom(new InternetAddress(username));

        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject(mailSubject);

        String msg = mailMessage.render();

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
    }

    private long getDaysLeft(LocalDate borrowDate, int duration) {
        if (borrowDate.plusMonths(duration).isBefore(LocalDate.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), borrowDate.plusMonths(duration));
    }
}
