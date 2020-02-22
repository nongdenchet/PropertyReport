package rain.property.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public final class PropertyReporter {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyReporter.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(fixedDelay = 60 * 15 * 1000L)
    public void execute() {
        LOG.info("started");
        sendmail();
        LOG.info("done");
    }

    private void sendmail() {
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("quanvuhuy2008@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        javaMailSender.send(msg);
    }
}
