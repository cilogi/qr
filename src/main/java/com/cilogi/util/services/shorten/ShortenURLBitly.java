// Copyright (c) 2012 Tim Niblett. All Rights Reserved.
//
// File:        ShortenURLBitly.java  (30/09/12)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used, sold, licenced, 
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

import com.cilogi.util.Secrets;
import com.cilogi.util.WebUtil;
import com.google.common.collect.ImmutableMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class ShortenURLBitly extends BaseGen implements IShortenURL {
    static final Logger LOG = LoggerFactory.getLogger(ShortenURLBitly.class);

    private static final String TOKEN = Secrets.get("shorten2go_bitly");

    public ShortenURLBitly() {
        super("https://api-ssl.bitly.com/v3/shorten", TOKEN, "cilogi");
    }

    @Override
    JSONObject shortenJSON(String inputURL) throws IOException {
        try {
            URI uri = buildQueryURI(baseURL, ImmutableMap.<String,String>of(
                "tokenName", "apiKey", "token", token, "userName", "login", "user", user
            ));
            String response = WebUtil.getURL(uri.toURL());
            try {
                return (JSONObject)new JSONParser().parse(response);
            } catch (ParseException e) {
                return new JSONObject();
            }
        } catch (URISyntaxException e) {
            LOG.warn("Can't create short URL for \"" + inputURL + "\", syntax error: " + e.getMessage());
        }
        return new JSONObject();
    }

    @Override
    public String shorten(String urlString) {
        try {
            JSONObject json = cache.get(urlString);
            if (json.size() == 0) {
                return urlString;
            } else {
                JSONObject data = (JSONObject)json.get("data");
                if (data != null && data.containsKey("url")) {
                    return (String)data.get("url");
                }
            }
            LOG.warn("No shortening for " + urlString + " with response " + json.toString());
            return urlString;
        } catch (Exception e) {
            throw new ShortenException(e);
        }
    }
    
}
