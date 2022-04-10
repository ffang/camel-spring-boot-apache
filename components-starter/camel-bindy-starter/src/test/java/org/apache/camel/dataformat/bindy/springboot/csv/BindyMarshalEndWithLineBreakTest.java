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
package org.apache.camel.dataformat.bindy.springboot.csv;


import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.model.csv.MyCsvRecord;
import org.apache.camel.dataformat.bindy.model.csv.MyCsvRecord2;
import org.apache.camel.dataformat.bindy.util.ConverterUtils;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.jupiter.api.Test;


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
        BindyMarshalEndWithLineBreakTest.class,
        BindyMarshalEndWithLineBreakTest.TestConfiguration.class
    }
)
public class BindyMarshalEndWithLineBreakTest {

    @Autowired
    ProducerTemplate template;
    
    @EndpointInject("mock:result")
    MockEndpoint mock;
    
    @Test
    public void testCsvWithEndingLineBreak() throws Exception {
        final CsvRecord record = MyCsvRecord.class.getAnnotation(CsvRecord.class);
        final MyCsvRecord csvRecord = new MyCsvRecord();
        csvRecord.setAddressLine1("221b Baker Street");
        csvRecord.setCity("London");
        csvRecord.setCountry("England");
        csvRecord.setAttention("1");
        mock.reset();
        
        mock.expectedMessageCount(1);
        mock.message(0).body().convertToString().endsWith(ConverterUtils.getStringCarriageReturn(record.crlf()));

        template.sendBody("direct:withlb", csvRecord);

        mock.assertIsSatisfied();
    }

    @Test
    public void testCsvWithoutEndingLineBreak() throws Exception {
        final CsvRecord record = MyCsvRecord2.class.getAnnotation(CsvRecord.class);
        final MyCsvRecord2 csvRecord2 = new MyCsvRecord2();
        csvRecord2.setAddressLine1("221b Baker Street");
        csvRecord2.setCity("London");
        csvRecord2.setCountry("England");
        csvRecord2.setAttention("1");

        mock.reset();
        mock.expectedMessageCount(1);
        mock.message(0).body().convertToString().endsWith(record.separator());

        template.sendBody("direct:withoutlb", csvRecord2);

        mock.assertIsSatisfied();
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
                public void configure() {
                    from("direct:withoutlb")
                            .marshal().bindy(BindyType.Csv, MyCsvRecord2.class)
                            .to("log:after.unmarshal")
                            .to("mock:result");

                    from("direct:withlb")
                            .marshal().bindy(BindyType.Csv, MyCsvRecord.class)
                            .to("log:after.marshal")
                            .to("mock:result");
                }
            };
        }
    }
    
    

}
