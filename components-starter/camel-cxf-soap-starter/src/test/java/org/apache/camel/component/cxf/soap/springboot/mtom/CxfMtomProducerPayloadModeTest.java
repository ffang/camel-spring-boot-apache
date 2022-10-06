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




import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Element;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.CxfPayload;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.apache.camel.component.cxf.mtom.HelloImpl;
import org.apache.camel.component.cxf.spring.jaxws.CxfSpringEndpoint;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.helpers.XPathUtils;
import org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration;
import org.apache.cxf.staxutils.StaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        CxfMtomProducerPayloadModeTest.class,
        CxfMtomProducerPayloadModeTest.TestConfiguration.class,
        CxfAutoConfiguration.class
    }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CxfMtomProducerPayloadModeTest {

    protected final QName SERVICE_QNAME = new QName("http://apache.org/camel/cxf/mtom_feature", "HelloService");
    protected final QName PORT_QNAME = new QName("http://apache.org/camel/cxf/mtom_feature", "HelloPort");
    private static final Logger LOG = LoggerFactory.getLogger(CxfMtomProducerPayloadModeTest.class);

    @Autowired
    protected CamelContext context;
    private Endpoint endpoint;
    
    
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new UndertowServletWebServerFactory();
    }
    
    
   
    @Bean
    private CxfEndpoint serviceEndpoint() {
        CxfSpringEndpoint cxfEndpoint = new CxfSpringEndpoint();
        cxfEndpoint.setServiceNameAsQName(SERVICE_QNAME);
        cxfEndpoint.setEndpointNameAsQName(PORT_QNAME);
        cxfEndpoint.setAddress("http://localhost:8080/services/" 
        + getClass().getSimpleName() + "/jaxws-mtom/hello");
        cxfEndpoint.setWsdlURL("mtom.wsdl");
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("dataFormat", "PAYLOAD");
        properties.put("mtom-enabled", true);
        properties.put("loggingFeatureEnabled", false);
        cxfEndpoint.setProperties(properties);
        return cxfEndpoint;
    }
    

    @BeforeEach
    public void setUp() throws Exception {
        endpoint = Endpoint.publish("/" + getClass().getSimpleName()
                                    + "/jaxws-mtom/hello",
                getServiceImpl());
        SOAPBinding binding = (SOAPBinding) endpoint.getBinding();
        binding.setMTOMEnabled(isMtomEnabled());
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (endpoint != null) {
            endpoint.stop();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProducer() throws Exception {
        if (MtomTestHelper.isAwtHeadless(null, LOG)) {
            return;
        }

        // START SNIPPET: producer

        Exchange exchange = context.createProducerTemplate().send("direct:testEndpoint", new Processor() {

            public void process(Exchange exchange) throws Exception {
                exchange.setPattern(ExchangePattern.InOut);
                List<Source> elements = new ArrayList<>();
                elements.add(new DOMSource(StaxUtils.read(new StringReader(MtomTestHelper.REQ_MESSAGE)).getDocumentElement()));
                CxfPayload<SoapHeader> body = new CxfPayload<>(
                        new ArrayList<SoapHeader>(),
                        elements, null);
                exchange.getIn().setBody(body);
                exchange.getIn(AttachmentMessage.class).addAttachment(MtomTestHelper.REQ_PHOTO_CID,
                        new DataHandler(new ByteArrayDataSource(MtomTestHelper.REQ_PHOTO_DATA, "application/octet-stream")));

                exchange.getIn(AttachmentMessage.class).addAttachment(MtomTestHelper.REQ_IMAGE_CID,
                        new DataHandler(new ByteArrayDataSource(MtomTestHelper.requestJpeg, "image/jpeg")));

            }

        });

        // process response 

        CxfPayload<SoapHeader> out = exchange.getMessage().getBody(CxfPayload.class);
        assertEquals(1, out.getBody().size());

        Map<String, String> ns = new HashMap<>();
        ns.put("ns", MtomTestHelper.SERVICE_TYPES_NS);
        ns.put("xop", MtomTestHelper.XOP_NS);

        XPathUtils xu = new XPathUtils(ns);
        Element oute = new XmlConverter().toDOMElement(out.getBody().get(0));
        Element ele = (Element) xu.getValue("//ns:DetailResponse/ns:photo/xop:Include", oute,
                XPathConstants.NODE);
        String photoId = ele.getAttribute("href").substring(4); // skip "cid:"

        ele = (Element) xu.getValue("//ns:DetailResponse/ns:image/xop:Include", oute,
                XPathConstants.NODE);
        String imageId = ele.getAttribute("href").substring(4); // skip "cid:"

        DataHandler dr = exchange.getMessage(AttachmentMessage.class).getAttachment(decodingReference(photoId));
        assertEquals("application/octet-stream", dr.getContentType());
        assertArrayEquals(MtomTestHelper.RESP_PHOTO_DATA, IOUtils.readBytesFromStream(dr.getInputStream()));

        dr = exchange.getMessage(AttachmentMessage.class).getAttachment(decodingReference(imageId));
        assertEquals("image/jpeg", dr.getContentType());

        BufferedImage image = ImageIO.read(dr.getInputStream());
        assertEquals(560, image.getWidth());
        assertEquals(300, image.getHeight());

        // END SNIPPET: producer

    }

    // CXF encoding the XOP reference since 3.0.1
    private String decodingReference(String reference) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(reference, "UTF-8");
    }

    protected boolean isMtomEnabled() {
        return true;
    }

    protected Object getServiceImpl() {
        return new HelloImpl();
    }

    
    // *************************************
    // Config
    // *************************************

    @Configuration
    public class TestConfiguration {

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:testEndpoint").
                    to("cxf:bean:serviceEndpoint?defaultOperationName=Detail");
                }
            };
        }
    }
    
    
}
