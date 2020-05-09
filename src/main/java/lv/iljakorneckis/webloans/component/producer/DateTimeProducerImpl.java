package lv.iljakorneckis.webloans.component.producer;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class DateTimeProducerImpl implements DateTimeProducer {

    @Override
    public DateTime getCurrentDateTime() {
        return DateTime.now();
    }
}
