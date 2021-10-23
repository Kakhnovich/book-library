package com.itechart.studets_lab.book_library.service.email;

import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.service.impl.BookService;
import com.itechart.studets_lab.book_library.service.impl.BorrowService;
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
import java.util.Optional;
import java.util.Properties;

public class GmailSender implements Job {
    private static final Logger LOGGER = LogManager.getLogger(GmailSender.class);
    private static final String PROPS_FILE_NAME = "email.properties";
    private final Properties prop = new Properties();
    private final BorrowService borrowService = BorrowService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private String username;
    private String password;
    private String mailSubject;
    private ST mailMessage;

    private void setSmtpProperties() {
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    public void sendMailForReader(Borrow borrow, Book book) {
        //todo code cleanup
        try (InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME)) {
            Properties emailProperties = new Properties();
            emailProperties.load(fileInputStream);
            username = emailProperties.getProperty("gmail.username");
            password = emailProperties.getProperty("gmail.password");
            mailSubject = emailProperties.getProperty("gmail.mail.subject");
            if (borrow.getBorrowDate().plusMonths(borrow.getDuration()).isBefore(LocalDate.now())) {
                mailMessage = new ST(emailProperties.getProperty("gmail.mail.message.billNotification"), '$', '$');
            } else {
                mailMessage = new ST(emailProperties.getProperty("gmail.mail.message.notification"), '$', '$');
                mailMessage.add("leftTime", getDaysLeft(borrow.getBorrowDate(), borrow.getDuration()));
            }
            //todo remove hardcode
            mailMessage.add("firstName", borrow.getReader().getFirstName());
            mailMessage.add("lastName", borrow.getReader().getLastName());
            mailMessage.add("borrowDate", borrow.getBorrowDate());
            mailMessage.add("bookTitle", book.getTitle());
            mailMessage.add("isbn", book.getIsbn());
            mailMessage.add("publisher", book.getPublisher());
            mailMessage.add("publishDate", book.getPublishDate());
            mailMessage.add("pageCount", book.getPageCount());
            mailMessage.add("duration", borrow.getDuration());

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(username));

                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse(borrow.getReader().getEmail()));
                message.setSubject(mailSubject);

                String msg = mailMessage.render();

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(msg, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);
            } catch (MessagingException e) {
                LOGGER.error("MessagingException while trying to send mail for " + borrow.getReader().getEmail() + ':' + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            LOGGER.error("IOException at opening email properties: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (prop.isEmpty()) {
            setSmtpProperties();
        }
        Optional<List<Borrow>> borrows = borrowService.findAll();
        if (borrows.isPresent()) {
            for (Borrow borrow : borrows.get()) {
                long daysLeft = getDaysLeft(borrow.getBorrowDate(), borrow.getDuration());
                if (daysLeft <= 1 || daysLeft == 7) {
                    Optional<Book> book = bookService.findByKey(borrow.getBookId());
                    book.ifPresent(realBook -> sendMailForReader(borrow, realBook));
                }
            }
        }
    }

    private long getDaysLeft(LocalDate borrowDate, int duration) {
        if (borrowDate.plusMonths(duration).isBefore(LocalDate.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), borrowDate.plusMonths(duration));
    }
}
