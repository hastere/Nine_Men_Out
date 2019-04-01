package com.example.ninemenout;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SearchBetsUnitTest {

    @Test
    public void testTeamNameSanitizer(){
        String testTeamName = "toroNTO RAptoRS";
        Assert.assertEquals("Toronto Raptors"
                , SearchBetsActivity.updateStringToExactMatch(testTeamName));
    }

}
