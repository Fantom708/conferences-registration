package com.mycode.conferencesregistration.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.mycode.conferencesregistration.domain.Conference;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.time.LocalDate;
import java.util.Optional;

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
    @DataSet("conferences-by-name.json")
    public void ifThereIsOnlyOneConferenceFoundByNameReturnIt() {
        Conference expected = new Conference("First", "Topic2", LocalDate.of(2020, 4, 10), 100);
        expected.setId(2L);
        assertThat(dao.findByNameIgnoreCase("First"),
                is(samePropertyValuesAs(Optional.of(expected))));
    }

    @Ignore
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    @DataSet("conferences-by-name.xml")
    public void ifSeveralConferencesFoundByNameThrowException() {
        dao.findByNameIgnoreCase("Second");
    }
}
