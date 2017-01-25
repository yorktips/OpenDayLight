Reference:
https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Toaster_Step-By-Step
http://blog.csdn.net/aaa_aa000/article/details/45840977
https://github.com/sniggel/odl-mdsal-cup-example.git



1. Create maven project:
   mvn archetype:generate -DarchetypeGroupId=org.opendaylight.controller -DarchetypeArtifactId=opendaylight-startup-archetype \
   -DarchetypeRepository=http://nexus.opendaylight.org/content/repositories/opendaylight.snapshot/ \
   -DarchetypeCatalog=http://nexus.opendaylight.org/content/repositories/opendaylight.snapshot/archetype-catalog.xml

   Define value for property 'groupId': : org.opendaylight.hello
   Define value for property 'artifactId': : hello
   Define value for property 'version': 1.0-SNAPSHOT: : 1.0.0-SNAPSHOT
   Define value for property 'package': org.opendaylight.hello: :
   Define value for property 'classPrefix': ${artifactId.substring(0,1).toUpperCase()}${artifactId.substring(1)}
   Define value for property 'copyright': : Copyright(c) Etherton Technologies

2. Build hello project by using the following command
   mvn clean install -DskipTests


3. Vi api/src/main/yang/hello.yang:

   module hello {
     yang-version 1;
     namespace "urn:opendaylight:params:xml:ns:yang:hello";
     prefix "hello";

     revision "2015-01-05" {
        description "Initial revision of hello model";
     }

     rpc hello-world {
        input {
            leaf name {
                type string;
            }
        }
        output {
            leaf greeting {
                type string;
            }
        }
    }
  }

4. cd hello/api
  mvn clean install #compile yang file for converting YANG model file to java code files


5. Create HelloWorldImpl to implemente the HelloService:
 vi impl/src/main/java/org/opendaylight/hello/impl/HelloWorldImpl.java

/*
 * Copyright © 2015 Copyright(c) Etherton Technologies and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.hello.impl;

import java.util.concurrent.Future;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;


public class HelloWorldImpl implements HelloService {

  @Override
  public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
    HelloWorldOutputBuilder helloBuilder = new HelloWorldOutputBuilder();
    helloBuilder.setGreeting("Hello ,welcome " + input.getName());
    return RpcResultBuilder.success(helloBuilder.build()).buildFuture();
  }
}


6.The HelloProvider.java file is in the current directory. Register the RPC that you created in the hello.yang file in
the HelloProvider.java file. You can either edit the HelloProvider.java to match what is below or you can simple
replace it with the code below.

/*
 * Copyright © 2015 Etherton Technologies and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.hello.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(HelloProvider.class);

    private RpcRegistration<HelloService> helloService;

    @Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("HelloProvider Session Initiated- York Added");
        helloService = session.addRpcImplementation(HelloService.class, new HelloWorldImpl());
    }

    @Override
    public void close() throws Exception {
        LOG.info("HelloProvider Closed");
        if (helloService != null) {
           helloService.close();
        }
    }

}

7. cd hello/impl
  mvn clean install #confirm the impl classed compilation will be OK


7. Start to build the distributation:
  cd hello
  mvn clean install -DskipTests

8. cd ./karaf/target/assembly/bin
  ./karaf

9. Confirm Hello module loaded:
  log:display | grep hello

10. Test the hello-world RPC via REST:
   (1). localhost:8181/apidoc/explorer/index.html
   (2). select hello(2015-01-05)
   (3). select POST /operations/hello:hello-world
   (4). Provide the required value:
        {"hello:input": { "name":"Your Name"}}


        
11. Add your feature into ODL distribution project:
  (1). git clone ssh://${ODL_USERNAME}@git.opendaylight.org:29418/integration/distribution.git
  (2). vi ./features-test/src/main/resources/features.xml
       vi ./features-index/src/main/resources/features.xml
       
      ......
      <repository>mvn:org.opendaylight.istuary/whistest-features/${feature.whistest.version}/xml/features</repository>
      ......
  (3). vi distribution/features-index/pom.xml
        <dependency>
            <groupId>org.opendaylight.istuary</groupId>
            <artifactId>whistest-features</artifactId>
            <version>${feature.whistest.version}</version>
            <classifier>features</classifier>
            <type>xml</type>
        </dependency>

  (4) vi pom.xml
      <feature.whistest.version>1.0.0-SNAPSHOT</feature.whistest.version>


