// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        GenQRCodes.java  (03-Oct-2011)
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


package com.cilogi.util.services.qr;


import com.cilogi.util.WebUtil;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Formatter;

public class QRGenGoogle {
    static final Logger LOG = LoggerFactory.getLogger(QRGenGoogle.class);

    private static final int DEFAULT_SIZE = 128;

    private final String text;
    private int width;
    private int height;

    public static QRGenGoogle from(String text) {
        return new QRGenGoogle(text);
    }

    private QRGenGoogle(String text) {
        Preconditions.checkNotNull(text, "Can't generate QR code for null text");

        this.text = text;
        width = DEFAULT_SIZE;
        height = DEFAULT_SIZE;
    }

    public QRGenGoogle size(int size) {
        return size(size, size);
    }

    public QRGenGoogle size(int width, int height) {
        Preconditions.checkArgument(width > 0, "You have to have a width > 0, not " + width);
        Preconditions.checkArgument(height > 0, "You have to have a height > 0, not " + height);
        this.width = width;
        this.height = height;
        return this;
    }

    public String toURL() {
        return uriFor(text, width, height);
    }

    public byte[] toData()  {
        try {
            String uri = uriFor(text, width, height);
            String response = WebUtil.getURL(new URL(uri));
            return response.getBytes(Charsets.UTF_8);
        } catch (IOException e) {
            throw new QRException(e);
        }
    }

    static String uriFor(String message, int width, int height) {
        try {
            String query = new Formatter().format("chs=%dx%d&cht=qr&chl=%s", width, height, message).toString();
            URI uri  = new URI("http", null, "chart.googleapis.com", 80, "/chart", query, null);
            return uri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
