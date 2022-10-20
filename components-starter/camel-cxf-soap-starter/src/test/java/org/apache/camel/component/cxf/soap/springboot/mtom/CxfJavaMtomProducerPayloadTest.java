/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.cxf.soap.springboot.mtom;

import java.awt.Image;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.spring.boot.CamelAutoConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(classes = {
                           CamelAutoConfiguration.class, CxfJavaMtomProducerPayloadTest.class,
                           CxfAutoConfiguration.class,
                           CxfMtomConsumerTest.TestConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CxfJavaMtomProducerPayloadTest extends CxfMtomConsumerTest {
    protected String MTOM_ENDPOINT_URI_MTOM_ENABLE = "cxf://http://localhost:8080/services" 
                                                                  + MTOM_ENDPOINT_ADDRESS
                                                                  + "?serviceClass=org.apache.camel.cxf.mtom_feature.Hello"
                                                                  + "&properties.mtom-enabled=true"
                                                                  + "&defaultOperationName=Detail";
    private static final Logger LOG = LoggerFactory.getLogger(CxfJavaMtomProducerPayloadTest.class);

    @Autowired
    CamelContext context;
    
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new UndertowServletWebServerFactory();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    @Test
    public void testInvokingService() throws Exception {
        if (MtomTestHelper.isAwtHeadless(null, LOG)) {
            return;
        }

        final Holder<byte[]> photo = new Holder<>("RequestFromCXF".getBytes("UTF-8"));
        final Holder<Image> image = new Holder<>(getImage("/java.jpg"));

        Exchange exchange = context.createProducerTemplate().send(MTOM_ENDPOINT_URI_MTOM_ENABLE,
                                                                  new Processor() {

                                                                      @Override
                                                                      public void process(Exchange exchange)
                                                                          throws Exception {
                                                                          exchange.getIn()
                                                                              .setBody(new Object[] {
                                                                                                     photo,
                                                                                                     image
                                                                          });

                                                                      }

                                                                  });

        AttachmentMessage out = exchange.getMessage(AttachmentMessage.class);
        assertEquals(2, out.getAttachments().size(), "We should get 2 attachements here.");
        assertEquals("application/xop+xml", out.getHeader("Content-Type"), "Get a wrong Content-Type header");
        // Get the parameter list
        List<?> parameter = out.getBody(List.class);
        // Get the operation name
        final Holder<byte[]> responsePhoto = (Holder<byte[]>)parameter.get(1);
        assertNotNull(responsePhoto.value, "The photo should not be null");
        assertEquals(new String(responsePhoto.value, "UTF-8"), "ResponseFromCamel",
                     "Should get the right response");

        final Holder<Image> responseImage = (Holder<Image>)parameter.get(2);
        assertNotNull(responseImage.value, "We should get the image here");
    }

}
