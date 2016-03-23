// Copyright (c) 2013 Cilogi. All Rights Reserved.
//
// File:        ShortenURLDefaultTest.java  (15/04/13)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Cilogi (the Author) and may not be used, sold, licenced, 
// transferred, copied or reproduced in whole or in part in 
// any manner or form or in or on any media to any person other than 
// in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.util.services.shorten;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class ShortenURLDefaultTest {
    static final Logger LOG = Logger.getLogger(ShortenURLDefaultTest.class.getName());


    public ShortenURLDefaultTest() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testShorten() {
        String url = "http://cilogi2go.appspot.com/site/colin_prior/contents/portfolio/jura.html";
        IShortenURL shorten = new ShortenURLDefault();
        assertEquals(url, shorten.shorten(url));
    }
}