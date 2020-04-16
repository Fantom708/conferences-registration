package com.mycode.conferencesregistration.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.mycode.conferencesregistration.domain.Conference;
import org.junit.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.time.LocalDate;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Yurii Kovtun
 */
public class ConferenceDaoTest extends AbstractDaoTest<ConferenceDao> {

    @Test
    public void ifThereIsNoConferenceWithSuchNameEmptyOptionalIsReturned() {
        assertThat(dao.findByNameIgnoreCase("unknown"), is(equalTo(emptyList())));
    }

    @Test
    @DataSet("conferences-by-name.xml")
    public void ifThereIsOnlyOneConferenceFoundByNameReturnIt() {
        Conference expected = new Conference("First", "Topic2", LocalDate.of(2020, 4, 10), 100);
        expected.setId(2L);
        assertThat(dao.findByNameIgnoreCase("First"),
                hasItem(samePropertyValuesAs(expected)));
    }

    @Test
    @DataSet("conferences-by-name.xml")
    public void ifSeveralConferencesFoundByNameThrowException() {
        Conference expected = new Conference("First", "Topic2", LocalDate.of(2020, 4, 10), 100);
        expected.setId(2L);
        assertThat(dao.findByDateStart(LocalDate.of(2020, 4, 10)),
                hasItem(samePropertyValuesAs(expected)));
    }
}
