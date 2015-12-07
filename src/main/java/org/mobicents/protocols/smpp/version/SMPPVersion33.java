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

package org.mobicents.protocols.smpp.version;

import org.mobicents.protocols.smpp.message.CommandId;

public class SMPPVersion33 extends AbstractSMPPVersion {
    private static final long serialVersionUID = 2L;
    
    /** Maximum number of bytes in an smppv3.3 message. */
    private static final int MAX_MSG_LENGTH = 140;

    SMPPVersion33() {
        super(0x33, "SMPP version 3.3");
    }

    public boolean isSupported(int commandID) {
        // Turn off the msb, which is used to signify a response packet..
        switch (commandID & 0x7fffffff) {
        case CommandId.BIND_RECEIVER:
        case CommandId.BIND_TRANSMITTER:
        case CommandId.CANCEL_SM:
        case CommandId.DELIVER_SM:
        case CommandId.ENQUIRE_LINK:
        case CommandId.GENERIC_NACK:
        case CommandId.PARAM_RETRIEVE:
        case CommandId.QUERY_LAST_MSGS:
        case CommandId.QUERY_MSG_DETAILS:
        case CommandId.QUERY_SM:
        case CommandId.REPLACE_SM:
        case CommandId.SUBMIT_MULTI:
        case CommandId.SUBMIT_SM:
        case CommandId.UNBIND:
            return true;

        default:
            return false;
        }
    }

    public boolean isSupportTLV() {
        return false;
    }

    public int getMaxLength(MandatoryParameter mandatoryParameter) {
        switch (mandatoryParameter) {
        case SHORT_MESSAGE:
            return MAX_MSG_LENGTH;

        default:
            return Integer.MAX_VALUE;
        }
    }

    public void validateMessage(byte[] message, int start, int length) {
        if (message != null && length > MAX_MSG_LENGTH) {
            throw new VersionException("Message is too long: " + length);
        }
    }

    public void validateMessageId(String messageId) {
        try {
            if (messageId != null && messageId.length() > 8) {
                throw new VersionException("Invalid message ID: " + messageId);
            }
            // Message IDs must be valid Hex numbers in v3.3
            Long.parseLong(messageId, 16);
        } catch (NumberFormatException x) {
            throw new VersionException(
                    "Message ID must be a hex number: " + messageId);
        }
    }

    public void validatePriorityFlag(int priority) {
        if (priority < 0 || priority > 1) {
            throw new VersionException(
                    "Priority flag must be 0 or 1: " + priority);
        }
    }

    public void validateRegisteredDelivery(int registeredDelivery) {
        if (registeredDelivery < 0 || registeredDelivery > 1) {
            throw new VersionException(
                    "Registered flag must be 0 or 1: " + registeredDelivery);
        }
    }

    public void validateNumberOfDests(int num) {
        if (num < 0 || num > 0xff) {
            throw new VersionException(
                    "Invalid number of destinations: " + num);
        }
    }

    public void validateParamName(String paramName) {
        if (paramName != null && paramName.length() > 31) {
            throw new VersionException("Parameter name too long: " + paramName);
        }
    }

    public void validateParamValue(String paramValue) {
        if (paramValue != null && paramValue.length() > 100) {
            throw new VersionException("Parameter value too long: " + paramValue);
        }
    }
}
