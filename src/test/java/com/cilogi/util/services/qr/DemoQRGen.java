// Copyright (c) 2013 Cilogi. All Rights Reserved.
//
// File:        DemoQRGen.java  (15/04/13)
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


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DemoQRGen {
    static final Logger LOG = Logger.getLogger(DemoQRGen.class);

    public DemoQRGen() {

    }

    BufferedImage create() {
        return QRGen.from("http://goo.gl/E1MHX").size(256).quietZone(2).errorLevel(QRGen.EC.MEDIUM).onOff(0xff0000ff, 0x00ffffff).toImage();
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(DemoQRGen.class.getResource("testlog.cfg"));
        try {
            DemoQRGen demo = new DemoQRGen();
            BufferedImage src = demo.create();
            JFrame frame = new JFrame("Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(Color.pink);
            ImagePanel panel = new ImagePanel(src);
            panel.setBackground(Color.pink);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            LOG.error("oops", e);
            System.exit(1);
        }
    }
}
