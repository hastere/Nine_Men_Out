package com.example.ninemenout;

import static org.junit.Assert.*;
import com.example.ninemenout.Bets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class GamesClassTest {

    private games g = new games("home", "away");

    private double over_under, home_spread, away_spread;



    @Test
    public void testAway_team(){
        g.setAway_team("AT");
        Assert.assertEquals("AT", g.getAway_team());
    }

    @Test
    public void testHome_team(){
        g.setHome_team("HT");
        Assert.assertEquals("HT", g.getHome_team());
    }

    @Test
    public void testEvent_date(){
        g.setEvent_date("ED");
        Assert.assertEquals("ED", g.getEvent_date());
    }

    @Test
    public void testEvent_id(){
        g.setEvent_id("EID");
        Assert.assertEquals("EID", g.getEvent_id());
    }

    @Test
    public void testOver_under(){
        g.setOver_under(15.15);
        Assert.assertEquals(15.15, g.getOver_under(), 0);
    }

    @Test
    public void testHome_spread(){
        g.setHome_spread(150.555);
        Assert.assertEquals(150.555, g.getHome_spread(), 0);
    }

    @Test
    public void testAway_spread(){
        g.setAway_spread(250.5511);
        Assert.assertEquals(250.5511, g.getAway_spread(), 0);
    }
}
