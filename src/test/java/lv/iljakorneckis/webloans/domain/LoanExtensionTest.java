package lv.iljakorneckis.webloans.domain;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.co.it.modular.hamcrest.date.IsWithin.within;

public class LoanExtensionTest {

    private LoanExtension extension;

    @Before
    public void init() {
        extension = new LoanExtension();
    }

    @Test
    public void testExtensionDates() {
        extension.setExtensionDate(null);

        assertThat(extension.getDate(), nullValue());

        DateTime extensionDate = DateTime.now();
        extension.setExtensionDate(extensionDate);

        assertThat(extension.getDate(), within(2, TimeUnit.SECONDS, extensionDate.toDate()));
    }
}