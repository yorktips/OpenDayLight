/*
 * Copyright © 2015 Copyright(c) York, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.istuary.impl;

import java.util.concurrent.Future;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.whistest.rev170123.WhistestService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.whistest.rev170123.HelloWhisInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.whistest.rev170123.HelloWhisOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.whistest.rev170123.HelloWhisOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;


public class HelloWhistestImpl implements WhistestService {

  @Override
  public Future<RpcResult<HelloWhisOutput>> helloWhis(HelloWhisInput input) {
    HelloWhisOutputBuilder  helloBuilder = new HelloWhisOutputBuilder();
    helloBuilder.setGreeting("This version 2.0.0: Hello Whistler,welcome " + input.getName());
    return RpcResultBuilder.success(helloBuilder.build()).buildFuture();
  }
}
