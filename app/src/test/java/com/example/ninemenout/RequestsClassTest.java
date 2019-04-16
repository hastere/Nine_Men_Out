package com.example.ninemenout;

import static org.junit.Assert.*;
import com.example.ninemenout.Bets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RequestsClassTest {

    private Requests r = new Requests("Owen");

    @Test
    public void testName(){
        r.setName("Andrew");
        Assert.assertEquals("Andrew", r.getName());
    }


}
