/*
 * Copyright 2006 the original author or authors.
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
package org.unitils.database.transaction;

import org.unitils.core.Unitils;
import org.unitils.spring.SpringModule;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class AutoDetectTransactionManagerFactory implements TransactionManagerFactory {

    public TransactionManager getTransactionManager() {
        if (springModuleEnabled()) {
            return new SpringIntegratingTransactionManager();
        } else {
            return new SimpleTransactionManager();
        }
    }

    protected boolean springModuleEnabled() {
        return Unitils.getInstance().getModulesRepository().isModuleEnabled(SpringModule.class);
    }
}