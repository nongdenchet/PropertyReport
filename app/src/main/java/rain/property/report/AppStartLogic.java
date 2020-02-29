package rain.property.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public final class AppStartLogic implements CommandLineRunner {

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    public void run(String... args) {
        criteriaRepository.save(new Criteria("criteria 1", "https://www.realestate.com.au/buy/property-villa-with-2-bedrooms-between-0-900000-in-hurstville,+nsw+2220/list-1?maxBeds=any"))
                .block();
    }
}
