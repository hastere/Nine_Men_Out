package com.example.ninemenout;

import static org.junit.Assert.*;
import com.example.ninemenout.Bets;
import com.google.firebase.firestore.auth.User;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserClassTest {

    ////
    //  Dr. Gray said we can reuse the same test cases since we have 25 already (don't need new ones)
    //  Some, however, have been fixed per the grading of sprint 2
    ////

    private Users u = new Users("otaz@gmail.com", "otaz", 500, 1000);

    @Test
    public void testEmail(){
        u.setEmail("otaz15@gmail.com");
        Assert.assertEquals("otaz15@gmail.com", u.getEmail());
    }

    @Test
    public void testName(){
        u.setName("Owen");
        Assert.assertEquals("Owen", u.getName());
    }

    @Test
    public void testActivePoints(){
        u.setActivePoints(1000);
        Assert.assertEquals(1000, u.getActivePoints());
    }

    @Test
    public void testPoints(){
        u.setPoints(20000);
        Assert.assertEquals(20000, u.getPoints());
    }
}
