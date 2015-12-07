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

package org.mobicents.protocols.smpp.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

import org.mobicents.protocols.smpp.util.APIConfig;
import org.mobicents.protocols.smpp.util.APIConfigFactory;
import org.mobicents.protocols.smpp.util.InvalidConfigurationException;
import org.mobicents.protocols.smpp.util.PropertiesAPIConfig;
import org.testng.annotations.Test;

@Test
public class APIConfigFactoryTest {

    public void testPropertiesAPIConfigIsTheDefault() {
        APIConfigFactory.reset();
        APIConfig config = APIConfigFactory.getConfig();
        assertNotNull(config);
        assertEquals(config.getClass(), PropertiesAPIConfig.class);
    }
    
    public void testGetConfigReturnsACachedClass() {
        APIConfigFactory.reset();
        APIConfig config1 = APIConfigFactory.getConfig();
        APIConfig config2 = APIConfigFactory.getConfig();
        assertNotNull(config1);
        assertNotNull(config2);
        assertSame(config1, config2);
    }
    
    public void testNewInstanceIsInstantiatedWhenCachingIsDisabled() {
        try {
            System.setProperty(APIConfigFactory.CACHE_CONFIG_PROP, "false");
            APIConfigFactory.reset();
            APIConfig config1 = APIConfigFactory.getConfig();
            APIConfig config2 = APIConfigFactory.getConfig();
            assertNotNull(config1);
            assertNotNull(config2);
            assertNotSame(config1, config2);
        } finally {
            System.clearProperty(APIConfigFactory.CACHE_CONFIG_PROP);
        }
    }
    
    public void testLoadConfigLoadsSpecifiedConfigClass() throws Exception {
        try {
            System.setProperty(
                    APIConfigFactory.CONFIG_CLASS_PROP,
                    "org.mobicents.protocols.smpp.util.NullAPIConfig");
            APIConfigFactory.reset();
            APIConfig config = APIConfigFactory.loadConfig();
            assertNotNull(config);
            assertEquals(config.getClass(), NullAPIConfig.class);
        } finally {
            System.clearProperty(APIConfigFactory.CONFIG_CLASS_PROP);
        }
    }

    @Test(expectedExceptions = {InvalidConfigurationException.class})
    public void testLoadConfigThrowsExceptionWhenConfigClassDoesNotExist() {
        try {
            System.setProperty(
                    APIConfigFactory.CONFIG_CLASS_PROP,
                    "org.mobicents.smpp.util.NonExistentAPIConfig");
            APIConfigFactory.reset();
            APIConfigFactory.loadConfig();
        } finally {
            System.clearProperty(APIConfigFactory.CONFIG_CLASS_PROP);
        }
    }
}
