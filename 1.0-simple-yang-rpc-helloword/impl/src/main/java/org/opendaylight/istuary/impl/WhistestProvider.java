/*
 * Copyright © 2017 Etherton Technologies and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.istuary.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.whistest.rev170123.WhistestService;

import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhistestProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(WhistestProvider.class);
    private RpcRegistration<WhistestService> whistestService;

    @Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("WhistestProvider Session Initiated");
        whistestService = session.addRpcImplementation(WhistestService.class, new HelloWhistestImpl()); 
    }

    @Override
    public void close() throws Exception {
        LOG.info("WhistestProvider Closed");
    }

}
