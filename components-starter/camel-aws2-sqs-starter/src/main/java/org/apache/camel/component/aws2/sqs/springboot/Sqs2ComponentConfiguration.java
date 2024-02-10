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
package org.apache.camel.component.aws2.sqs.springboot;

import org.apache.camel.component.aws2.sqs.Sqs2Component;
import org.apache.camel.component.aws2.sqs.Sqs2Configuration;
import org.apache.camel.component.aws2.sqs.Sqs2Operations;
import org.apache.camel.spring.boot.ComponentConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.core.Protocol;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Send and receive messages to/from AWS SQS.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@ConfigurationProperties(prefix = "camel.component.aws2-sqs")
public class Sqs2ComponentConfiguration
        extends
            ComponentConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the aws2-sqs component. This is
     * enabled by default.
     */
    private Boolean enabled;
    /**
     * The hostname of the Amazon AWS cloud.
     */
    private String amazonAWSHost = "amazonaws.com";
    /**
     * Setting the auto-creation of the queue
     */
    private Boolean autoCreateQueue = false;
    /**
     * The AWS SQS default configuration. The option is a
     * org.apache.camel.component.aws2.sqs.Sqs2Configuration type.
     */
    private Sqs2Configuration configuration;
    /**
     * Set the need for overriding the endpoint. This option needs to be used in
     * combination with the uriEndpointOverride option
     */
    private Boolean overrideEndpoint = false;
    /**
     * The underlying protocol used to communicate with SQS
     */
    private String protocol = "https";
    /**
     * Specify the queue owner aws account id when you need to connect the queue
     * with a different account owner.
     */
    private String queueOwnerAWSAccountId;
    /**
     * The region in which SQS client needs to work. When using this parameter,
     * the configuration will expect the lowercase name of the region (for
     * example, ap-east-1) You'll need to use the name Region.EU_WEST_1.id()
     */
    private String region;
    /**
     * Set the overriding uri endpoint. This option needs to be used in
     * combination with overrideEndpoint option
     */
    private String uriEndpointOverride;
    /**
     * A list of attribute names to receive when consuming. Multiple names can
     * be separated by comma.
     */
    private String attributeNames;
    /**
     * Allows for bridging the consumer to the Camel routing Error Handler,
     * which mean any exceptions (if possible) occurred while the Camel consumer
     * is trying to pickup incoming messages, or the likes, will now be
     * processed as a message and handled by the routing Error Handler.
     * Important: This is only possible if the 3rd party component allows Camel
     * to be alerted if an exception was thrown. Some components handle this
     * internally only, and therefore bridgeErrorHandler is not possible. In
     * other situations we may improve the Camel component to hook into the 3rd
     * party component and make this possible for future releases. By default
     * the consumer will use the org.apache.camel.spi.ExceptionHandler to deal
     * with exceptions, that will be logged at WARN or ERROR level and ignored.
     */
    private Boolean bridgeErrorHandler = false;
    /**
     * Allows you to use multiple threads to poll the sqs queue to increase
     * throughput
     */
    private Integer concurrentConsumers = 1;
    /**
     * The default visibility timeout (in seconds)
     */
    private Integer defaultVisibilityTimeout;
    /**
     * Delete message from SQS after it has been read
     */
    private Boolean deleteAfterRead = true;
    /**
     * Whether to send the DeleteMessage to the SQS queue if the exchange has
     * property with key Sqs2Constants#SQS_DELETE_FILTERED
     * (CamelAwsSqsDeleteFiltered) set to true.
     */
    private Boolean deleteIfFiltered = true;
    /**
     * If enabled, then a scheduled background task will keep extending the
     * message visibility on SQS. This is needed if it takes a long time to
     * process the message. If set to true defaultVisibilityTimeout must be set.
     * See details at Amazon docs.
     */
    private Boolean extendMessageVisibility = false;
    /**
     * The length of time, in seconds, for which Amazon SQS can reuse a data key
     * to encrypt or decrypt messages before calling AWS KMS again. An integer
     * representing seconds, between 60 seconds (1 minute) and 86,400 seconds
     * (24 hours). Default: 300 (5 minutes).
     */
    private Integer kmsDataKeyReusePeriodSeconds;
    /**
     * The ID of an AWS-managed customer master key (CMK) for Amazon SQS or a
     * custom CMK.
     */
    private String kmsMasterKeyId;
    /**
     * A list of message attribute names to receive when consuming. Multiple
     * names can be separated by comma.
     */
    private String messageAttributeNames;
    /**
     * Define if Server Side Encryption is enabled or not on the queue
     */
    private Boolean serverSideEncryptionEnabled = false;
    /**
     * The duration (in seconds) that the received messages are hidden from
     * subsequent retrieve requests after being retrieved by a ReceiveMessage
     * request to set in the
     * com.amazonaws.services.sqs.model.SetQueueAttributesRequest. This only
     * makes sense if it's different from defaultVisibilityTimeout. It changes
     * the queue visibility timeout attribute permanently.
     */
    private Integer visibilityTimeout;
    /**
     * Duration in seconds (0 to 20) that the ReceiveMessage action call will
     * wait until a message is in the queue to include in the response.
     */
    private Integer waitTimeSeconds;
    /**
     * Set the separator when passing a String to send batch message operation
     */
    private String batchSeparator = ",";
    /**
     * Delay sending messages for a number of seconds.
     */
    private Integer delaySeconds;
    /**
     * Whether the producer should be started lazy (on the first message). By
     * starting lazy you can use this to allow CamelContext and routes to
     * startup in situations where a producer may otherwise fail during starting
     * and cause the route to fail being started. By deferring this startup to
     * be lazy then the startup failure can be handled during routing messages
     * via Camel's routing error handlers. Beware that when the first message is
     * processed then creating and starting the producer may take a little time
     * and prolong the total processing time of the processing.
     */
    private Boolean lazyStartProducer = false;
    /**
     * Only for FIFO queues. Strategy for setting the messageDeduplicationId on
     * the message. It can be one of the following options: useExchangeId,
     * useContentBasedDeduplication. For the useContentBasedDeduplication
     * option, no messageDeduplicationId will be set on the message.
     */
    private String messageDeduplicationIdStrategy = "useExchangeId";
    /**
     * Only for FIFO queues. Strategy for setting the messageGroupId on the
     * message. It can be one of the following options: useConstant,
     * useExchangeId, usePropertyValue. For the usePropertyValue option, the
     * value of property CamelAwsMessageGroupId will be used.
     */
    private String messageGroupIdStrategy;
    /**
     * What to do if sending to AWS SQS has more messages than AWS allows
     * (currently only maximum 10 message headers are allowed). WARN will log a
     * WARN about the limit is for each additional header, so the message can be
     * sent to AWS. WARN_ONCE will only log one time a WARN about the limit is
     * hit, and drop additional headers, so the message can be sent to AWS.
     * IGNORE will ignore (no logging) and drop additional headers, so the
     * message can be sent to AWS. FAIL will cause an exception to be thrown and
     * the message is not sent to AWS.
     */
    private String messageHeaderExceededLimit = "WARN";
    /**
     * The operation to do in case the user don't want to send only a message
     */
    private Sqs2Operations operation;
    /**
     * To use the AmazonSQS client. The option is a
     * software.amazon.awssdk.services.sqs.SqsClient type.
     */
    private SqsClient amazonSQSClient;
    /**
     * Whether autowiring is enabled. This is used for automatic autowiring
     * options (the option must be marked as autowired) by looking up in the
     * registry to find if there is a single instance of matching type, which
     * then gets configured on the component. This can be used for automatic
     * configuring JDBC data sources, JMS connection factories, AWS Clients,
     * etc.
     */
    private Boolean autowiredEnabled = true;
    /**
     * Define if you want to apply delaySeconds option to the queue or on single
     * messages
     */
    private Boolean delayQueue = false;
    /**
     * Used for enabling or disabling all consumer based health checks from this
     * component
     */
    private Boolean healthCheckConsumerEnabled = true;
    /**
     * Used for enabling or disabling all producer based health checks from this
     * component. Notice: Camel has by default disabled all producer based
     * health-checks. You can turn on producer checks globally by setting
     * camel.health.producersEnabled=true.
     */
    private Boolean healthCheckProducerEnabled = true;
    /**
     * To define a proxy host when instantiating the SQS client
     */
    private String proxyHost;
    /**
     * To define a proxy port when instantiating the SQS client
     */
    private Integer proxyPort;
    /**
     * To define a proxy protocol when instantiating the SQS client
     */
    private Protocol proxyProtocol = Protocol.HTTPS;
    /**
     * The maximumMessageSize (in bytes) an SQS message can contain for this
     * queue.
     */
    private Integer maximumMessageSize;
    /**
     * The messageRetentionPeriod (in seconds) a message will be retained by SQS
     * for this queue.
     */
    private Integer messageRetentionPeriod;
    /**
     * The policy for this queue. It can be loaded by default from classpath,
     * but you can prefix with classpath:, file:, or http: to load the resource
     * from different systems.
     */
    private String policy;
    /**
     * To define the queueUrl explicitly. All other parameters, which would
     * influence the queueUrl, are ignored. This parameter is intended to be
     * used to connect to a mock implementation of SQS, for testing purposes.
     */
    private String queueUrl;
    /**
     * If you do not specify WaitTimeSeconds in the request, the queue attribute
     * ReceiveMessageWaitTimeSeconds is used to determine how long to wait.
     */
    private Integer receiveMessageWaitTimeSeconds;
    /**
     * Specify the policy that send message to DeadLetter queue. See detail at
     * Amazon docs.
     */
    private String redrivePolicy;
    /**
     * Amazon AWS Access Key
     */
    private String accessKey;
    /**
     * If using a profile credentials provider, this parameter will set the
     * profile name
     */
    private String profileCredentialsName;
    /**
     * Amazon AWS Secret Key
     */
    private String secretKey;
    /**
     * Amazon AWS Session Token used when the user needs to assume an IAM role
     */
    private String sessionToken;
    /**
     * If we want to trust all certificates in case of overriding the endpoint
     */
    private Boolean trustAllCertificates = false;
    /**
     * Set whether the SQS client should expect to load credentials on an AWS
     * infra instance or to expect static credentials to be passed in.
     */
    private Boolean useDefaultCredentialsProvider = false;
    /**
     * Set whether the SQS client should expect to load credentials through a
     * profile credentials provider.
     */
    private Boolean useProfileCredentialsProvider = false;
    /**
     * Set whether the SQS client should expect to use Session Credentials. This
     * is useful in a situation in which the user needs to assume an IAM role
     * for doing operations in SQS.
     */
    private Boolean useSessionCredentials = false;

    public String getAmazonAWSHost() {
        return amazonAWSHost;
    }

    public void setAmazonAWSHost(String amazonAWSHost) {
        this.amazonAWSHost = amazonAWSHost;
    }

    public Boolean getAutoCreateQueue() {
        return autoCreateQueue;
    }

    public void setAutoCreateQueue(Boolean autoCreateQueue) {
        this.autoCreateQueue = autoCreateQueue;
    }

    public Sqs2Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Sqs2Configuration configuration) {
        this.configuration = configuration;
    }

    public Boolean getOverrideEndpoint() {
        return overrideEndpoint;
    }

    public void setOverrideEndpoint(Boolean overrideEndpoint) {
        this.overrideEndpoint = overrideEndpoint;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getQueueOwnerAWSAccountId() {
        return queueOwnerAWSAccountId;
    }

    public void setQueueOwnerAWSAccountId(String queueOwnerAWSAccountId) {
        this.queueOwnerAWSAccountId = queueOwnerAWSAccountId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUriEndpointOverride() {
        return uriEndpointOverride;
    }

    public void setUriEndpointOverride(String uriEndpointOverride) {
        this.uriEndpointOverride = uriEndpointOverride;
    }

    public String getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(String attributeNames) {
        this.attributeNames = attributeNames;
    }

    public Boolean getBridgeErrorHandler() {
        return bridgeErrorHandler;
    }

    public void setBridgeErrorHandler(Boolean bridgeErrorHandler) {
        this.bridgeErrorHandler = bridgeErrorHandler;
    }

    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public Integer getDefaultVisibilityTimeout() {
        return defaultVisibilityTimeout;
    }

    public void setDefaultVisibilityTimeout(Integer defaultVisibilityTimeout) {
        this.defaultVisibilityTimeout = defaultVisibilityTimeout;
    }

    public Boolean getDeleteAfterRead() {
        return deleteAfterRead;
    }

    public void setDeleteAfterRead(Boolean deleteAfterRead) {
        this.deleteAfterRead = deleteAfterRead;
    }

    public Boolean getDeleteIfFiltered() {
        return deleteIfFiltered;
    }

    public void setDeleteIfFiltered(Boolean deleteIfFiltered) {
        this.deleteIfFiltered = deleteIfFiltered;
    }

    public Boolean getExtendMessageVisibility() {
        return extendMessageVisibility;
    }

    public void setExtendMessageVisibility(Boolean extendMessageVisibility) {
        this.extendMessageVisibility = extendMessageVisibility;
    }

    public Integer getKmsDataKeyReusePeriodSeconds() {
        return kmsDataKeyReusePeriodSeconds;
    }

    public void setKmsDataKeyReusePeriodSeconds(
            Integer kmsDataKeyReusePeriodSeconds) {
        this.kmsDataKeyReusePeriodSeconds = kmsDataKeyReusePeriodSeconds;
    }

    public String getKmsMasterKeyId() {
        return kmsMasterKeyId;
    }

    public void setKmsMasterKeyId(String kmsMasterKeyId) {
        this.kmsMasterKeyId = kmsMasterKeyId;
    }

    public String getMessageAttributeNames() {
        return messageAttributeNames;
    }

    public void setMessageAttributeNames(String messageAttributeNames) {
        this.messageAttributeNames = messageAttributeNames;
    }

    public Boolean getServerSideEncryptionEnabled() {
        return serverSideEncryptionEnabled;
    }

    public void setServerSideEncryptionEnabled(
            Boolean serverSideEncryptionEnabled) {
        this.serverSideEncryptionEnabled = serverSideEncryptionEnabled;
    }

    public Integer getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public void setVisibilityTimeout(Integer visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
    }

    public Integer getWaitTimeSeconds() {
        return waitTimeSeconds;
    }

    public void setWaitTimeSeconds(Integer waitTimeSeconds) {
        this.waitTimeSeconds = waitTimeSeconds;
    }

    public String getBatchSeparator() {
        return batchSeparator;
    }

    public void setBatchSeparator(String batchSeparator) {
        this.batchSeparator = batchSeparator;
    }

    public Integer getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(Integer delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public Boolean getLazyStartProducer() {
        return lazyStartProducer;
    }

    public void setLazyStartProducer(Boolean lazyStartProducer) {
        this.lazyStartProducer = lazyStartProducer;
    }

    public String getMessageDeduplicationIdStrategy() {
        return messageDeduplicationIdStrategy;
    }

    public void setMessageDeduplicationIdStrategy(
            String messageDeduplicationIdStrategy) {
        this.messageDeduplicationIdStrategy = messageDeduplicationIdStrategy;
    }

    public String getMessageGroupIdStrategy() {
        return messageGroupIdStrategy;
    }

    public void setMessageGroupIdStrategy(String messageGroupIdStrategy) {
        this.messageGroupIdStrategy = messageGroupIdStrategy;
    }

    public String getMessageHeaderExceededLimit() {
        return messageHeaderExceededLimit;
    }

    public void setMessageHeaderExceededLimit(String messageHeaderExceededLimit) {
        this.messageHeaderExceededLimit = messageHeaderExceededLimit;
    }

    public Sqs2Operations getOperation() {
        return operation;
    }

    public void setOperation(Sqs2Operations operation) {
        this.operation = operation;
    }

    public SqsClient getAmazonSQSClient() {
        return amazonSQSClient;
    }

    public void setAmazonSQSClient(SqsClient amazonSQSClient) {
        this.amazonSQSClient = amazonSQSClient;
    }

    public Boolean getAutowiredEnabled() {
        return autowiredEnabled;
    }

    public void setAutowiredEnabled(Boolean autowiredEnabled) {
        this.autowiredEnabled = autowiredEnabled;
    }

    public Boolean getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(Boolean delayQueue) {
        this.delayQueue = delayQueue;
    }

    public Boolean getHealthCheckConsumerEnabled() {
        return healthCheckConsumerEnabled;
    }

    public void setHealthCheckConsumerEnabled(Boolean healthCheckConsumerEnabled) {
        this.healthCheckConsumerEnabled = healthCheckConsumerEnabled;
    }

    public Boolean getHealthCheckProducerEnabled() {
        return healthCheckProducerEnabled;
    }

    public void setHealthCheckProducerEnabled(Boolean healthCheckProducerEnabled) {
        this.healthCheckProducerEnabled = healthCheckProducerEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Protocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(Protocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public Integer getMaximumMessageSize() {
        return maximumMessageSize;
    }

    public void setMaximumMessageSize(Integer maximumMessageSize) {
        this.maximumMessageSize = maximumMessageSize;
    }

    public Integer getMessageRetentionPeriod() {
        return messageRetentionPeriod;
    }

    public void setMessageRetentionPeriod(Integer messageRetentionPeriod) {
        this.messageRetentionPeriod = messageRetentionPeriod;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public Integer getReceiveMessageWaitTimeSeconds() {
        return receiveMessageWaitTimeSeconds;
    }

    public void setReceiveMessageWaitTimeSeconds(
            Integer receiveMessageWaitTimeSeconds) {
        this.receiveMessageWaitTimeSeconds = receiveMessageWaitTimeSeconds;
    }

    public String getRedrivePolicy() {
        return redrivePolicy;
    }

    public void setRedrivePolicy(String redrivePolicy) {
        this.redrivePolicy = redrivePolicy;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getProfileCredentialsName() {
        return profileCredentialsName;
    }

    public void setProfileCredentialsName(String profileCredentialsName) {
        this.profileCredentialsName = profileCredentialsName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Boolean getTrustAllCertificates() {
        return trustAllCertificates;
    }

    public void setTrustAllCertificates(Boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }

    public Boolean getUseDefaultCredentialsProvider() {
        return useDefaultCredentialsProvider;
    }

    public void setUseDefaultCredentialsProvider(
            Boolean useDefaultCredentialsProvider) {
        this.useDefaultCredentialsProvider = useDefaultCredentialsProvider;
    }

    public Boolean getUseProfileCredentialsProvider() {
        return useProfileCredentialsProvider;
    }

    public void setUseProfileCredentialsProvider(
            Boolean useProfileCredentialsProvider) {
        this.useProfileCredentialsProvider = useProfileCredentialsProvider;
    }

    public Boolean getUseSessionCredentials() {
        return useSessionCredentials;
    }

    public void setUseSessionCredentials(Boolean useSessionCredentials) {
        this.useSessionCredentials = useSessionCredentials;
    }
}