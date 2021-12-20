/**
 * http://toolkas.blogspot.com/2019/02/java.html
 * https://yandex.ru/support/mail/mail-clients/others.html
 * https://www.smtper.net/
 *
 * ПОМОГЛО: добавил галочек в чекбоксы в настройке на странице своей почты:
 * https://searchengines.guru/ru/forum/1037543
 * и это:
 * https://myaccount.google.com/lesssecureapps
 */

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class HelloEmail {

    public static void main(String[] args) throws MessagingException {
        //Объект properties хранит параметры соединения.
        //Для каждого почтового сервера они разные.
        //Если не знаете нужные - обратитесь к администратору почтового сервера.
        //Ну или гуглите;=)
        //Конкретно для Yandex параметры соединения можно подсмотреть тут:
        //https://yandex.ru/support/mail/mail-clients.html (раздел "Исходящая почта")
        Properties properties = new Properties();
        //Хост или IP-адрес почтового сервера
        properties.put("mail.smtp.host", "smtp.yandex.ru");
        //Требуется ли аутентификация для отправки сообщения
        properties.put("mail.smtp.auth", "true");
        //Порт для установки соединения
        properties.put("mail.smtp.socketFactory.port", "465");
        //Фабрика сокетов, так как при отправке сообщения Yandex требует SSL-соединения
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Создаем соединение для отправки почтового сообщения
        Session session = Session.getDefaultInstance(properties,
                //Аутентификатор - объект, который передает логин и пароль
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("drugst@yandex.ru", "0821");
                    }
                });

        //Создаем новое почтовое сообщение
        Message message = new MimeMessage(session);
        //От кого
        message.setFrom(new InternetAddress("drugst@yandex.ru"));
        //Кому
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("sosedvovan@list.ru"));
        //Тема письма
        message.setSubject("Очень важное письмо!!!");

        //-----------------------------------------------------------------------------------
        //Текст письма
        //message.setText("Hello, Email!");
        //заменим эту струку на два абзатца для посылки вложенного текстового файла:
        //-----------------------------------------------------------------------------------

        //Файл вложения
        File file = new File("D:/1.txt");
        //Собираем содержимое письма из кусочков
        MimeMultipart multipart = new MimeMultipart();
        //Первый кусочек - текст письма
        MimeBodyPart part1 = new MimeBodyPart();
        part1.addHeader("Content-Type", "text/plain; charset=UTF-8");
        part1.setDataHandler(new DataHandler("Письмо с файлом!!", "text/plain; charset=\"utf-8\""));
        multipart.addBodyPart(part1);

        //Второй кусочек - файл
        MimeBodyPart part2 = new MimeBodyPart();
        try {
            part2.setFileName(MimeUtility.encodeWord(file.getName()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        part2.setDataHandler(new DataHandler(new FileDataSource(file)));
        multipart.addBodyPart(part2);
        //Добавляем оба кусочка в сообщение
        message.setContent(multipart);

        //-----------------------------------------------------------------------------------
        //Поехали!!!
        Transport.send(message);
    }
}
