package com.example.ninemenout;

import static org.junit.Assert.*;
import com.example.ninemenout.Bets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BetsClassTest {

    private Bets b = new Bets("Alabama", "Clemson", "Alabama", "-5");

    @Test
    public void testActive(){
        b.setActive(5);
        Assert.assertEquals(5, b.getActive());
    }

    @Test
    public void testAmount(){
        b.setAmount(150);
        Assert.assertEquals(150, b.getAmount());
    }

    @Test
    public void testAway(){
        b.setAway("Away Team");
        Assert.assertEquals("Away Team", b.getAway());
    }

    @Test
    public void testBetOnFavorite(){
        b.setBetOnFavorite("Owen");
        Assert.assertEquals("Owen", b.getBetOnFavorite());
    }

    @Test
    public void testBetOnUnderdog(){
        b.setBetOnUnderdog("Saban");
        Assert.assertEquals("Saban", b.getBetOnUnderdog());
    }

    @Test
    public void testDate_expires(){
        b.setDate_expires("Never");
        Assert.assertEquals("Never", b.getDate_expires());
    }

    @Test
    public void testFavorite(){
        b.setFavorite("Chicago Blackhawks");
        Assert.assertEquals("Chicago Blackhawks", b.getFavorite());
    }

    @Test
    public void testHome(){
        b.setHome("Toronto Raptors");
        Assert.assertEquals("Toronto Raptors", b.getHome());
    }

    @Test
    public void testOdds(){
        b.setOdds("+1500");
        Assert.assertEquals("+1500", b.getOdds());
    }

    @Test
    public void testType(){
        b.setType("over under");
        Assert.assertEquals("over under", b.getType());
    }


}
