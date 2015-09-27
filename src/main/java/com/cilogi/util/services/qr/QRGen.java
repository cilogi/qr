// Copyright (c) 2013 Cilogi. All Rights Reserved.
//
// File:        QRGen.java  (15/04/13)
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

package com.cilogi.util.services.qr;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.logging.Logger;

public class QRGen {
    private static final Logger LOG = Logger.getLogger(QRGen.class.getName());

    public enum EC {
        LOW(ErrorCorrectionLevel.L),
        MEDIUM(ErrorCorrectionLevel.M),
        HIGH(ErrorCorrectionLevel.Q),
        ULTRA(ErrorCorrectionLevel.H);

        private ErrorCorrectionLevel level;

        EC(ErrorCorrectionLevel level) {
            this.level = level;
        }

        private ErrorCorrectionLevel getLevel() {
            return level;
        }
    }

    private static final int DEFAULT_SIZE = 128;
    private static final int DEFAULT_QUIET_ZONE_SIZE = 4;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private int width;
    private int height;
    private int quietZoneSize;

    private final String text;
    private EC errorCorrectionLevel;
    private int onColor;
    private int offColor;

    private QRGen(String text) {
        Preconditions.checkNotNull(text, "The text has to be non null when generating a QR code");
        this.text = text;

        width = DEFAULT_SIZE;
        height = DEFAULT_SIZE;
        quietZoneSize = DEFAULT_QUIET_ZONE_SIZE;
        errorCorrectionLevel = EC.HIGH;
        onColor = BLACK;
        offColor = WHITE;
    }

    public static QRGen from(String text) {
        return new QRGen(text);
    }

    public QRGen errorLevel(EC level) {
        this.errorCorrectionLevel = level;
        return this;
    }

    public QRGen size(int size) {
        return size(size, size);
    }

    public QRGen size(int width, int height) {
        Preconditions.checkArgument(width > 0, "You have to have a width > 0, not " + width);
        Preconditions.checkArgument(height > 0, "You have to have a height > 0, not " + height);
        this.width = width;
        this.height = height;
        return this;
    }

    public QRGen onOff(int onColor, int offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        return this;
    }

    public QRGen quietZone(int quietZone) {
        Preconditions.checkArgument(quietZone > 0, "Quiet zone must be > 0, not: " + quietZone);
        this.quietZoneSize = quietZone;
        return this;
    }

    public BufferedImage toImage() {
        try {
            return toBufferedImage(createMatrix(), createConfig());
        } catch (Exception e) {
            throw new QRException("Failed to create QR image from text, " + text + ": " + e.getMessage());
        }
    }

    public byte[] toData() {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(createMatrix(), "PNG", os, createConfig());
            os.close();
            return os.toByteArray();
        } catch (Exception e) {
            throw new QRException("Failed to create QR image from text, " + text + ": " + e.getMessage());
        }
    }

    private BitMatrix createMatrix() {
        try {
            return new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, createHints());
        } catch (WriterException e) {
            throw new QRException(e);
        }
    }

    private MatrixToImageConfig createConfig() {
        return new MatrixToImageConfig(onColor, offColor);
    }

    private Map<EncodeHintType, ?> createHints() {
        Map<EncodeHintType, ? super Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel.getLevel());
        hints.put(EncodeHintType.MARGIN, quietZoneSize);
        return hints;
    }

    private BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, getBufferedImageColorModel());
        int onColor = config.getPixelOnColor();
        int offColor = config.getPixelOffColor();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
            }
        }
        return image;
    }

    int getBufferedImageColorModel() {
        return onColor == BLACK && offColor == WHITE ? BufferedImage.TYPE_BYTE_BINARY : BufferedImage.TYPE_INT_ARGB;
    }

}
