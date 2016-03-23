// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        ShortenURLGoogle.java  (03-Oct-2011)
// Author:      tim
// $Id$
//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used,
// sold, licenced, transferred, copied or reproduced in whole or in
// part in any manner or form or in or on any media to any person
// other than in accordance with the terms of The Author's agreement
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
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;


public class ShortenURLGoogle extends BaseGen implements IShortenURL {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(ShortenURLGoogle.class);

    private static final String DEFAULT_REFERER = "http://guides.cilogi.com";
    private static final String KEY = Secrets.get("shorten2go_google");

    private String referer;

    public ShortenURLGoogle() {
        super("https://www.googleapis.com/urlshortener/v1/url?key="+KEY, null, null);
        this.referer = DEFAULT_REFERER;
    }

    @SuppressWarnings({"unused"})
    public ShortenURLGoogle referer(@NonNull String referer) {
        this.referer = referer; return this;
    }

    @Override
    JSONObject shortenJSON(String inputURL) throws IOException {
        String response = WebUtil.postURL(new URL(baseURL), "{\"longUrl\" : \"" + inputURL + "\"}", ImmutableMap.of(
            "Content-type", "application/json",
            "Referer", referer
        ));
        if (response != null) {
            try {
                return (JSONObject)new JSONParser().parse(response);
            } catch (ParseException e) {
                return new JSONObject();
            }
        }
        return new JSONObject();
    }

    @Override
    public String shorten(String urlString) {
        try {
            JSONObject json = cache.get(urlString);
            return (json.size() == 0) ? urlString : (String)json.get("id");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
