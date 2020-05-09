package lv.iljakorneckis.webloans.component.producer;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

public class DateTimeProducerImplTest {

    @Test
    public void testGetCurrentDate() {
        DateTimeProducer producer = new DateTimeProducerImpl();
        DateTime currentDate = producer.getCurrentDateTime();

        assertThat(currentDate.toDate(), within(1, TimeUnit.SECONDS, new Date()));
    }
}
