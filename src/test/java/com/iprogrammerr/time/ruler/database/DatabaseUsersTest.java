package com.iprogrammerr.time.ruler.database;

import com.iprogrammerr.time.ruler.TestDatabaseSetup;
import com.iprogrammerr.time.ruler.model.DatabaseUsers;
import com.iprogrammerr.time.ruler.model.User;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseUsersTest {

    private TestDatabaseSetup setup;
    private DatabaseUsers users;

    @Before
    public void setup() {
        setup = new TestDatabaseSetup();
        users = new DatabaseUsers(new SqlDatabaseSession(setup.database(), new QueryTemplates()));
        setup.setup();
    }

    @After
    public void cleanup() {
        setup.database().close();
    }

    @Test
    public void returnsList() {
        users.create("Igor", "ceigor@gmail.com", "SECRET");
        users.create("Olek", "olek@super.com", "password2");
        int expectedSize = 2;
        MatcherAssert.assertThat(
            "Does not contain proper elements",
            users.all().size(), Matchers.equalTo(expectedSize)
        );
    }

    @Test
    public void returnsEmpty() {
        MatcherAssert.assertThat("Does not return empty list", users.all(), Matchers.empty());
    }

    @Test
    public void updates() {
        long id = users.create("Yegor", "yegor@gmail.com", "lovesSpringSecretly");
        User user = new User(id, "Yegor3", "yegor@gmail.com", "lovesHibernateSecretly", true);
        users.update(user);
        MatcherAssert.assertThat("Update failure", users.user(id), Matchers.equalTo(user));
    }
}