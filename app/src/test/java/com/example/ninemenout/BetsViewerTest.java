package com.example.ninemenout;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BetsViewerTest {

    @Test
    public void testGetOpenTeam(){
        String testUserFavorite = "otazic@tazic.com";
        String testEmptyFavorite = "";
        Assert.assertEquals("underdog"
                , BetsViewerActivity.getOpenTeam(testUserFavorite));
        Assert.assertEquals("favorite"
                , BetsViewerActivity.getOpenTeam(testEmptyFavorite));
    }

    @Test
    public void testGetOpenTeamField(){
        String testUnderdog = "underdog";
        String testFavorite = "favorite";
        Assert.assertEquals("home"
                , BetsViewerActivity.getOpenTeamField(testUnderdog, "Alabama", "Auburn"));
        Assert.assertEquals("away"
                , BetsViewerActivity.getOpenTeamField(testUnderdog, "Alabama", "Alabama"));
        Assert.assertEquals("home"
                , BetsViewerActivity.getOpenTeamField(testFavorite, "Alabama", "Alabama"));
        Assert.assertEquals("away"
                , BetsViewerActivity.getOpenTeamField(testFavorite, "Alabama", "Auburn"));
    }
}
