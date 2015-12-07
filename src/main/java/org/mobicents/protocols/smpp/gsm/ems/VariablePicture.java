/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.smpp.gsm.ems;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Implementation of a variable picture. See 3GPP TS 23.040 9.2.3.24.10.1.9.
 * @version $Id: VariablePicture.java 484 2010-02-08 16:08:50Z orank $
 */
public class VariablePicture extends Picture {

    public VariablePicture(BufferedImage image, int position) {
        super(0x12, image, position);
    }

    public VariablePicture(BufferedImage image) {
        super(0x12, image);
    }

    
    @Override
    protected int getExtraHeaderSize() {
        return 2;
    }

    @Override
    protected void writeHeader(BufferedImage image, ByteBuffer buffer) {
        int width = image.getWidth();
        buffer.put((byte) (width / 8));
        buffer.put((byte) image.getHeight());
    }

    @Override
    protected void checkImage(BufferedImage image) {
        int width = image.getWidth();
        if (width % 8 != 0) {
            throw new IllegalArgumentException(
                    "Image width must be a multiple of 8");
        }
    }

}
