package com.example.ninemenout;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BetUtilityTest {

    // SHOULD WORK THE SAME AS THE BETS VIEWER TESTS

    @Test
    public void testGetOpenTeam(){
        String testUserFavorite = "joe@joe.joe";
        String testEmptyFavorite = "";
        Assert.assertEquals("underdog"
                , BetUtility.getOpenTeam(testUserFavorite));
        Assert.assertEquals("favorite"
                , BetUtility.getOpenTeam(testEmptyFavorite));
        testUserFavorite = "";
        testEmptyFavorite = "joe@joe.joe";
        Assert.assertEquals("favorite"
                , BetUtility.getOpenTeam(testUserFavorite));
        Assert.assertEquals("underdog"
                , BetUtility.getOpenTeam(testEmptyFavorite));
    }

    @Test
    public void testGetOpenTeamField(){
        String testUnderdog = "underdog";
        String testFavorite = "favorite";
        Assert.assertEquals("home"
                , BetUtility.getOpenTeamField(testUnderdog, "Alabama", "Auburn"));
        Assert.assertEquals("away"
                , BetUtility.getOpenTeamField(testUnderdog, "Alabama", "Alabama"));
        Assert.assertEquals("home"
                , BetUtility.getOpenTeamField(testFavorite, "Alabama", "Alabama"));
        Assert.assertEquals("away"
                , BetUtility.getOpenTeamField(testFavorite, "Alabama", "Auburn"));
    }
}
