package com.ortosoft.ortodoxworship.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by admin on 11.05.2016.
 */
public class GroupTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create_new_group() throws Exception {
        String name = "111";
        Group group = new Group(name);

        assertEquals(group.get_name(), "111");
    }

    @Test
    public void find_group_by_name()throws Exception {
        String name = "222";
        Group group = new Group(name);
        Group found_group = Group.FindByName(name);

        assertEquals(found_group.get_name(), name);
    }
}