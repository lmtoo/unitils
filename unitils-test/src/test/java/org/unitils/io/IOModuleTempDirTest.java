/*
 * Copyright 2011,  Unitils.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.unitils.io;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.io.annotation.TempDir;

import java.io.File;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Jeroen Horemans
 * @author Tim Ducheyne
 * @author Thomas De Rycke
 * @since 3.3
 */
public class IOModuleTempDirTest extends UnitilsJUnit4 {

    @TempDir
    private File defaultDir;

    @TempDir(value = "customDir")
    private File customDir;


    @Test
    public void defaultTempDir() {
        assertTrue(defaultDir.isDirectory());
        assertEquals(IOModuleTempDirTest.class.getName() + "-defaultTempDir", defaultDir.getName());
    }

    @Test
    public void customTempDir() {
        assertTrue(customDir.isDirectory());
        assertEquals("customDir", customDir.getName());
    }
}
