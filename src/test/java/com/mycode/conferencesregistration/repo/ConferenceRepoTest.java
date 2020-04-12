package com.mycode.conferencesregistration.repo;

import com.github.database.rider.core.api.dataset.DataSet;
import com.mycode.conferencesregistration.domain.Conference;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Yurii Kovtun
 */
public class ConferenceRepoTest extends AbstractDaoTest<ConferenceRepo> {

    @Test
    public void ifThereIsNoConferenceWithSuchNameEmptyOptionalIsReturned() {
        assertThat(dao.findByNameIgnoreCase("unknown"), is(equalTo(Optional.empty())));
    }

    @Test
    @DataSet("conferences-by-name.xml")
    public void ifThereIsOnlyOneBookFoundByNameReturnIt() {
        Conference expected = new Conference("First", "Topic2", LocalDate.of(2020, 4, 10), 100);
        expected.setId(2L);
        assertThat(dao.findByNameIgnoreCase("first"),
                is(samePropertyValuesAs(Optional.of(expected))));
    }
}
