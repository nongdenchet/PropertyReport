package rain.property.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Component
public final class PropertyReporter {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyReporter.class);

    private final JavaMailSender javaMailSender;
    private final SubscriptionRepository subscriptionRepository;
    private final PropertyRepository propertyRepository;
    private final Environment environment;

    @Autowired
    public PropertyReporter(
            JavaMailSender javaMailSender,
            SubscriptionRepository subscriptionRepository,
            PropertyRepository propertyRepository,
            Environment environment
    ) {
        this.javaMailSender = javaMailSender;
        this.subscriptionRepository = subscriptionRepository;
        this.propertyRepository = propertyRepository;
        this.environment = environment;
    }

    @Scheduled(fixedDelay = 60 * 15 * 1000L)
    public void execute() {
        final List<Property> latestProperties = propertyRepository.findAll()
                .take(15)
                .collectList()
                .block();
        if (latestProperties.isEmpty()) {
            LOG.info("Skipping, no properties found");
            return;
        }
        subscriptionRepository.findAll()
                .map(s -> Pair.of(s, latestProperties))
                .flatMap(p -> checkAndSendEmail(p.getFirst(), p.getSecond()))
                .blockLast();
    }

    private Mono<Subscription> checkAndSendEmail(Subscription subscription, List<Property> properties) {
        final Mono<Subscription> updateSub = subscriptionRepository.save(
                subscription.builder()
                        .sentReportAt(new Date())
                        .build()
        );

        return Mono.fromCallable(() -> {
            final Date sentReportAt = subscription.getSentReportAt();
            if (sentReportAt != null) {
                final Calendar sentDate = Calendar.getInstance();
                sentDate.setTime(sentReportAt);
                sentDate.add(Calendar.DATE, subscription.getType().toDays());

                final Calendar today = Calendar.getInstance();
                LOG.info("sentDate: " + sentDate.getTimeInMillis() + ", current: " + today.getTimeInMillis());
                if (sentDate.compareTo(today) > 0) {
                    LOG.info("Skipping: " + subscription.getEmail());
                    return false;
                }
            }

            LOG.info("Sending: " + subscription.getEmail());
            sentEmail(subscription, properties);
            return true;
        }).flatMap(sent -> {
            if (sent) {
                return updateSub;
            } else {
                return Mono.empty();
            }
        }).doOnError(e -> LOG.error("error", e))
                .onErrorStop();
    }

    private void sentEmail(Subscription subscription, List<Property> properties) throws MessagingException {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>Hi ")
                .append(subscription.getName())
                .append(",</h1>");
        for (Property property : properties) {
            stringBuilder.append("<h3>Address: ")
                    .append(property.getAddress())
                    .append(". Type: ")
                    .append(property.getType())
                    .append("</h3>");
            stringBuilder.append("<h3>Price: ")
                    .append(property.getPrice())
                    .append(", bed: ")
                    .append(property.getBedrooms())
                    .append(", bath: ")
                    .append(property.getBathrooms())
                    .append(", parking: ")
                    .append(property.getCarSpaces())
                    .append("</h3>");
            stringBuilder.append("<p><a href=\"")
                    .append(property.getUrl())
                    .append("\">")
                    .append("<img src=\"")
                    .append(property.getImageUrl())
                    .append("\"/>")
                    .append("</a></p>")
                    .append("</br></br>");
        }
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(stringBuilder.toString(), true);
        helper.setTo(subscription.getEmail());
        helper.setSubject("NSW Property reports");
        helper.setFrom(environment.getProperty("spring.mail.username"));
        javaMailSender.send(mimeMessage);
    }
}
