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
package org.apache.camel.language.jq.springboot;

import org.apache.camel.spring.boot.LanguageConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Evaluates a JQ expression against a JSON message body.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@ConfigurationProperties(prefix = "camel.language.jq")
public class JqLanguageConfiguration
        extends
            LanguageConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the jq language. This is enabled
     * by default.
     */
    private Boolean enabled;
    /**
     * Name of variable to use as input, instead of the message body It has as
     * higher precedent if other are set.
     */
    private String variableName;
    /**
     * Name of header to use as input, instead of the message body It has as
     * higher precedent than the propertyName if both are set.
     */
    private String headerName;
    /**
     * Name of property to use as input, instead of the message body. It has a
     * lower precedent than the headerName if both are set.
     */
    private String propertyName;
    /**
     * Whether to trim the value to remove leading and trailing whitespaces and
     * line breaks
     */
    private Boolean trim = true;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Boolean getTrim() {
        return trim;
    }

    public void setTrim(Boolean trim) {
        this.trim = trim;
    }
}