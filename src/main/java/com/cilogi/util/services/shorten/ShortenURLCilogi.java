// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        ShortenURLCilogi.java  (29/09/14)
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

import com.cilogi.util.Secrets;
import com.cilogi.util.WebUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ShortenURLCilogi extends BaseGen implements IShortenURL {
    static final Logger LOG = LoggerFactory.getLogger(ShortenURLCilogi.class);

    private static final String TOKEN = Secrets.get("shorten2go_token");
    private static final String USER = Secrets.get("shorten2go_user");

    private final IGetURL getURL;

    public ShortenURLCilogi() {
        this(new DefaultGetURL());
    }

    public ShortenURLCilogi(IGetURL getURL) {
        super("https://shorten2go.appspot.com/secure/api/shorten",
              TOKEN, USER);
        tokenKey = "uuid";
        longUrlKey = "url";
        if (getURL == null) {
            throw new NullPointerException("Must have a nun-null URL getter set");
        }
        this.getURL = getURL;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public String shorten(String longURL) {
        try {
            JSONObject obj = shortenJSON(longURL);
            return (String)obj.get("shortUrl");
        } catch (Exception e) {
            throw new ShortenException(e);
        }
    }

    @Override
    JSONObject shortenJSON(String inputURL) throws IOException {
        try {
            URI uri = queryURI(baseURL, token, null, inputURL);
            String response = getURL.get(uri.toURL());
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

    private static class DefaultGetURL implements IGetURL {
        @Override
        public String get(URL url) throws IOException {
            return WebUtil.getURL(url);
        }
    }
}
