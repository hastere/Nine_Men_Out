package com.example.ninemenout;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SearchBetsTest {

    ////
    //  Dr. Gray said we can reuse the same test cases since we have 25 already (don't need new ones)
    //  Some, however, have been fixed per the grading of sprint 2
    ////

    @Test
    public void testTeamNameSanitizer(){
        String testTeamName = "toroNTO RAptoRS";
        Assert.assertEquals("Toronto Raptors"
                , SearchBetsActivity.updateStringToExactMatch(testTeamName));
    }

}
