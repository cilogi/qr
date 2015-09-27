// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        DemoShortenURLBitly.java  (20/02/14)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;


public class DemoShortenURLBitly {
    static final Logger LOG = LoggerFactory.getLogger(DemoShortenURLBitly.class);

    public DemoShortenURLBitly() {

    }

    public static void main(String[] args) {
        String url = "http://cilogi2go.appspot.com/site/colin_prior/contents/portfolio/jura.html";
        IShortenURL shorten = new ShortenURLBitly();
        String shortened = shorten.shorten(url);
        assertEquals(url, "http://glas.gy/1glgXew");

    }
}
