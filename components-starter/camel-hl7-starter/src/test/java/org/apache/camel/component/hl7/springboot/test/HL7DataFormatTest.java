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
package org.apache.camel.component.hl7.springboot.test;


import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7Charset;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADR_A19;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.QRD;
import ca.uhn.hl7v2.model.v24.segment.MSH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;


@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        HL7DataFormatTest.class,
        HL7DataFormatTest.TestConfiguration.class
    }
)
public class HL7DataFormatTest extends HL7TestSupport {

    
    @Autowired
    ProducerTemplate template;

    @EndpointInject("mock:marshal")
    MockEndpoint mockMarshal;
    
    @EndpointInject("mock:unmarshal")
    MockEndpoint mockUnmarshal;
    
    @EndpointInject("mock:unmarshalBig5")
    MockEndpoint mockUnmarshalBig5;

    
    private static final String NONE_ISO_8859_1 = "\u221a\u00c4\u221a\u00e0\u221a\u00e5\u221a\u00ed\u221a\u00f4\u2248\u00ea";

    private static HL7DataFormat hl7 = new HL7DataFormat();
    private static HL7DataFormat hl7big5 = new HL7DataFormat() {
        @Override
        protected String guessCharsetName(byte[] b, Exchange exchange) {
            return "Big5";
        }
    };

    @Test
    public void testMarshal() throws Exception {
        mockMarshal.reset();
        mockMarshal.expectedMessageCount(1);
        mockMarshal.message(0).body().isInstanceOf(byte[].class);
        mockMarshal.message(0).body(String.class).contains("MSA|AA|123");
        mockMarshal.message(0).body(String.class).contains("QRD|20080805120000");

        Message message = createHL7AsMessage();
        template.sendBody("direct:marshal", message);

        mockMarshal.assertIsSatisfied();
    }

    @Test
    public void testMarshalISO8859() throws Exception {
        mockMarshal.reset();
        mockMarshal.expectedMessageCount(1);
        mockMarshal.message(0).body().isInstanceOf(byte[].class);
        mockMarshal.message(0).body(String.class).contains("MSA|AA|123");
        mockMarshal.message(0).body(String.class).contains("QRD|20080805120000");
        mockMarshal.message(0).body(String.class).not().contains(NONE_ISO_8859_1);
        Message message = createHL7AsMessage();
        template.sendBodyAndProperty("direct:marshal", message, Exchange.CHARSET_NAME, "ISO-8859-1");
        mockMarshal.assertIsSatisfied();
    }

    @Test
    public void testMarshalUTF16InMessage() throws Exception {
        String charsetName = "UTF-16";
        mockMarshal.reset();
        mockMarshal.expectedMessageCount(1);

        Message message = createHL7WithCharsetAsMessage(HL7Charset.getHL7Charset(charsetName));
        template.sendBodyAndProperty("direct:marshal", message, Exchange.CHARSET_NAME, charsetName);
        mockMarshal.assertIsSatisfied();

        byte[] body = (byte[]) mockMarshal.getExchanges().get(0).getIn().getBody();
        String msg = new String(body, Charset.forName(charsetName));
        assertTrue(msg.contains("MSA|AA|123"));
        assertTrue(msg.contains("QRD|20080805120000"));
    }

    @Test
    public void testMarshalUTF8() throws Exception {
        mockMarshal.reset();
        mockMarshal.expectedMessageCount(1);
        mockMarshal.message(0).body().isInstanceOf(byte[].class);
        mockMarshal.message(0).body(String.class).contains("MSA|AA|123");
        mockMarshal.message(0).body(String.class).contains("QRD|20080805120000");
        mockMarshal.message(0).body(String.class).contains(NONE_ISO_8859_1);
        Message message = createHL7AsMessage();
        template.sendBodyAndProperty("direct:marshal", message, Exchange.CHARSET_NAME, "UTF-8");
        mockMarshal.assertIsSatisfied();
    }

    @Test
    public void testUnmarshal() throws Exception {
        mockUnmarshal.reset();
        mockUnmarshal.expectedMessageCount(1);
        mockUnmarshal.message(0).body().isInstanceOf(Message.class);

        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_SENDING_APPLICATION, "MYSENDER");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_SENDING_FACILITY, "MYSENDERAPP");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_RECEIVING_APPLICATION, "MYCLIENT");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_RECEIVING_FACILITY, "MYCLIENTAPP");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_TIMESTAMP, "200612211200");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_SECURITY, null);
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_MESSAGE_TYPE, "QRY");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_TRIGGER_EVENT, "A19");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_MESSAGE_CONTROL, "1234");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_PROCESSING_ID, "P");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_VERSION_ID, "2.4");
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_CONTEXT, hl7.getHapiContext());
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_CHARSET, null);

        mockUnmarshal.expectedHeaderReceived(Exchange.CHARSET_NAME, "UTF-8");

        String body = createHL7AsString();
        template.sendBody("direct:unmarshal", body);

        mockUnmarshal.assertIsSatisfied();

        Message msg = mockUnmarshal.getExchanges().get(0).getIn().getBody(Message.class);
        assertEquals("2.4", msg.getVersion());
        QRD qrd = (QRD) msg.get("QRD");
        assertEquals("0101701234", qrd.getWhoSubjectFilter(0).getIDNumber().getValue());
    }

    @Test
    public void testUnmarshalWithExplicitUTF16Charset() throws Exception {
        String charset = "UTF-16";
        mockUnmarshal.reset();
        mockUnmarshal.expectedMessageCount(1);
        mockUnmarshal.message(0).body().isInstanceOf(Message.class);
        mockUnmarshal.expectedHeaderReceived(HL7Constants.HL7_CHARSET, HL7Charset.getHL7Charset(charset).getHL7CharsetName());
        mockUnmarshal.expectedHeaderReceived(Exchange.CHARSET_NAME, charset);

        // Message with explicit encoding in MSH-18
        byte[] body = createHL7WithCharsetAsString(HL7Charset.UTF_16).getBytes(Charset.forName(charset));
        template.sendBodyAndHeader("direct:unmarshal", new ByteArrayInputStream(body), Exchange.CHARSET_NAME, charset);

        mockUnmarshal.assertIsSatisfied();

        Message msg = mockUnmarshal.getExchanges().get(0).getIn().getBody(Message.class);
        assertEquals("2.4", msg.getVersion());
        QRD qrd = (QRD) msg.get("QRD");
        assertEquals("0101701234", qrd.getWhoSubjectFilter(0).getIDNumber().getValue());
    }

    @Test
    public void testUnmarshalWithImplicitBig5Charset() throws Exception {
        String charset = "Big5";
        
        mockUnmarshalBig5.expectedMessageCount(1);
        mockUnmarshalBig5.message(0).body().isInstanceOf(Message.class);
        mockUnmarshalBig5.expectedHeaderReceived(HL7Constants.HL7_CHARSET, null);
        mockUnmarshalBig5.expectedHeaderReceived(Exchange.CHARSET_NAME, charset);

        // Message without explicit encoding in MSH-18, but the unmarshaller "guesses"
        // this time that it is Big5
        byte[] body = createHL7AsString().getBytes(Charset.forName(charset));
        template.sendBody("direct:unmarshalBig5", new ByteArrayInputStream(body));

        mockUnmarshalBig5.assertIsSatisfied();

        Message msg = mockUnmarshalBig5.getExchanges().get(0).getIn().getBody(Message.class);
        assertEquals("2.4", msg.getVersion());
        QRD qrd = (QRD) msg.get("QRD");
        assertEquals("0101701234", qrd.getWhoSubjectFilter(0).getIDNumber().getValue());
    }

    // *************************************
    // Config
    // *************************************

    @Configuration
    public static class TestConfiguration {

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {

                    from("direct:marshal").marshal().hl7().to("mock:marshal");
                    from("direct:unmarshal").unmarshal(hl7).to("mock:unmarshal");
                    from("direct:unmarshalBig5").unmarshal(hl7big5).to("mock:unmarshalBig5");
                }
            };
        }
    }
    
    private static String createHL7AsString() {
        return createHL7WithCharsetAsString(null);
    }

    private static String createHL7WithCharsetAsString(HL7Charset charset) {
        String hl7Charset = charset == null ? "" : charset.getHL7CharsetName();
        String line1 = String.format(
                "MSH|^~\\&|MYSENDER|MYSENDERAPP|MYCLIENT|MYCLIENTAPP|200612211200||QRY^A19|1234|P|2.4||||||%s", hl7Charset);
        String line2 = "QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||";

        StringBuilder body = new StringBuilder();
        body.append(line1);
        body.append("\r");
        body.append(line2);
        return body.toString();
    }

    private static ADR_A19 createHL7AsMessage() throws Exception {
        ADR_A19 adr = new ADR_A19();

        // Populate the MSH Segment
        MSH mshSegment = adr.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue("200701011539");
        mshSegment.getSendingApplication().getNamespaceID().setValue("MYSENDER");
        mshSegment.getSequenceNumber().setValue("123");
        mshSegment.getMessageType().getMessageType().setValue("ADR");
        mshSegment.getMessageType().getTriggerEvent().setValue("A19");

        // Populate the PID Segment
        MSA msa = adr.getMSA();
        msa.getAcknowledgementCode().setValue("AA");
        msa.getMessageControlID().setValue("123");
        msa.getMsa3_TextMessage().setValue(NONE_ISO_8859_1);

        QRD qrd = adr.getQRD();
        qrd.getQueryDateTime().getTimeOfAnEvent().setValue("20080805120000");

        return adr;
    }

    private static ADR_A19 createHL7WithCharsetAsMessage(HL7Charset charset) throws Exception {
        ADR_A19 adr = createHL7AsMessage();
        adr.getMSH().getCharacterSet(0).setValue(charset.getHL7CharsetName());
        return adr;
    }
}
