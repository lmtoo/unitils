/*
 * Copyright 2006-2009,  Unitils.org
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
package org.unitils.mock.core;

import org.unitils.mock.proxy.ProxyInvocation;

public class AssertInvokedInSequenceVerifier<T> extends AssertVerifier<T> {


    public AssertInvokedInSequenceVerifier(String name, Class<T> mockedType, Scenario scenario, SyntaxMonitor syntaxMonitor) {
        super(name, mockedType, scenario, syntaxMonitor);
    }


    protected void handleAssertVerificationInvocation(ProxyInvocation proxyInvocation) {
        BehaviorDefiningInvocation behaviorDefiningInvocation = new BehaviorDefiningInvocation(proxyInvocation, mockName, null);
        scenario.assertInvokedInOrder(behaviorDefiningInvocation);
    }

}