# MqttDeviceTwin Requirements

## Overview

MqttDeviceTwin is a concrete class extending Mqtt. This class implements all the abstract methods from MQTT and overrides the parseTopic, 
parsePayload, receive, onReconnect, and onReconnectComplete events.

## References

## Exposed API

```java
public final class MqttDeviceTwin extends Mqtt
{
    public MqttDeviceTwin() throws IOException;
        
    public Message receive() throws IOException;
    String parseTopic() throws IOException;
    byte[] parsePayload(String topic) throws IOException;
    void onReconnect() throws IOException;
    void onReconnectComplete(boolean status) throws IOException;

    public void start() throws IOException;
    public void stop() throws IOException;
    public void send(DeviceTwinMessage message) throws IOException;
    public DeviceTwinMessage message receive() throws IOException;

}
```

### MqttDeviceTwin

```java
public MqttDeviceTwin();
```

**SRS_MqttDeviceTwin_25_001: [**The constructor shall instantiate super class without any parameters.**]**

**SRS_MqttDeviceTwin_25_002: [**The constructor shall construct device twin response subscribeTopic.**]**


### parseTopic

```java
String parseTopic() throws IOException;
```

**SRS_MqttDeviceTwin_25_003: [**parseTopic concrete method shall be implemeted by MqttDeviceTwin concrete class.**]**

**SRS_MqttDeviceTwin_25_004: [**parseTopic shall look for the twin topic($iothub/twin) prefix from received message queue as per spec.**]**

**SRS_MqttDeviceTwin_25_005: [**If none of the topics from the received queue match the twin topic prefix then this method shall return null string .**]**

**SRS_MqttDeviceTwin_25_006: [**If received messages queue is empty then parseTopic shall return null string.**]**

**SRS_MqttDeviceTwin_25_007: [**If receiveMessage queue is null then parseTopic shall throw IOException.**]**


### parsePayload

```java
byte[] parsePayload(String topic) throws IOException;
```

**SRS_MqttDeviceTwin_25_008: [**parsePayload concrete method shall be implemeted by MqttDeviceTwin concrete class.**]**

**SRS_MqttDeviceTwin_25_009: [**This parsePayload method look for payload for the corresponding topic from the received messagesqueue.**]**

**SRS_MqttDeviceTwin_25_010: [**If the topic is null then parsePayload shall stop parsing for payload and return.**]**

**SRS_MqttDeviceTwin_25_011: [**If the topic is non-null and received messagesqueue could not locate the payload then this method shall throw IOException**]**

**SRS_MqttDeviceTwin_25_012: [**If receiveMessage queue is null then this method shall throw IOException.**]**

**SRS_MqttDeviceTwin_25_013: [**If the topic is found in the message queue then parsePayload shall delete it from the queue.**]**


### onReconnect

```java
abstract void onReconnect() throws IOException;
```

**SRS_MqttDeviceTwin_25_014: [**onReconnect method shall be implemeted by MqttDeviceTwin class.**]**

**SRS_MqttDeviceTwin_25_015: [**This onReconnect method shall put the entire operation of the MqttDeviceTwin class on hold by acquiring a binary semaphore.**]**


### onReconnectComplete

```java
abstract void onReconnectComplete(boolean status) throws IOException;
```

**SRS_MqttDeviceTwin_25_016: [**This onReconnectComplete method shall be implemeted by MqttDeviceTwin class.**]**

**SRS_MqttDeviceTwin_25_017: [**If the status is true, onReconnectComplete method shall release all the operation of the MqttDeviceTwin class put on hold by releasing the semaphore.**]**

**SRS_MqttDeviceTwin_25_018: [**If the status is false, onReconnectComplete method shall throw IOException and release the semaphore**]**


### start

```java
public void start() throws IOException;
```

**SRS_MqttDeviceTwin_25_019: [**start method shall subscribe to twin response topic ($iothub/twin/res/#) if connected.**]**


### stop

```java
public void stop() throws IOException;
```

**SRS_MqttDeviceTwin_25_020: [**stop method shall unsubscribe from twin response topic ($iothub/twin/res/#).**]**

### send

```java
 public void send(final DeviceTwinMessage message) throws IOException;
```

**SRS_MqttDeviceTwin_25_021: [**send method shall throw an exception if the message is null.**]**

**SRS_MqttDeviceTwin_25_022: [**send method shall return if the message is not of Type DeviceTwin.**]**

**SRS_MqttDeviceTwin_25_023: [**send method shall throw an exception if the getDeviceTwinOperationType() returns DEVICE_TWIN_OPERATION_UNKNOWN.**]**

**SRS_MqttDeviceTwin_25_024: [**send method shall build the get request topic of the format mentioned in spec ($iothub/twin/GET/?$rid={request id}) if the operation is of type DEVICE_TWIN_OPERATION_GET_REQUEST.**]**

**SRS_MqttDeviceTwin_25_025: [**send method shall throw an exception if message contains a null or empty request id if the operation is of type DEVICE_TWIN_OPERATION_GET_REQUEST.**]**

**SRS_MqttDeviceTwin_25_026: [**send method shall build the update reported properties request topic of the format mentioned in spec ($iothub/twin/PATCH/properties/reported/?$rid={request id}&$version={base version}) if the operation is of type DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_REQUEST.**]**

**SRS_MqttDeviceTwin_25_027: [**send method shall throw an exception if message contains a null or empty request id if the operation is of type DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_REQUEST.**]**

**SRS_MqttDeviceTwin_25_028: [**send method shall not throw an exception if message contains a null or empty version if the operation is of type DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_REQUEST as version is optional**]**

**SRS_MqttDeviceTwin_25_029: [**send method shall build the subscribe to desired properties request topic of the format mentioned in spec ($iothub/twin/PATCH/properties/desired/?$version={new version}) if the operation is of type DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_REQUEST.**]**

**SRS_MqttDeviceTwin_25_030: [**send method shall not throw an exception if message contains a null or empty version if the operation is of type DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_REQUEST as version is optional**]**

**SRS_MqttDeviceTwin_25_031: [**send method shall publish a message to the IOT Hub on the respective publish topic by calling method publish().**]**

**SRS_MqttDeviceTwin_25_032: [**send method shall subscribe to desired properties by calling method subscribe() on topic "$iothub/twin/PATCH/properties/desired/#" specified in spec if the operation is DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_REQUEST.**]**


### receive

```java
 public Message receive() throws IOException;
```

**SRS_MqttDeviceTwin_25_033: [**This method shall call parseTopic to parse the topic from the recevived Messages queue corresponding to the messaging client's operation.**]**

**SRS_MqttDeviceTwin_25_034: [**If the call parseTopic returns null or empty string then this method shall do nothing and return null**]**

**SRS_MqttDeviceTwin_25_035: [**This method shall call parsePayload to get the message payload from the recevived Messages queue corresponding to the messaging client's operation.**]**

**SRS_MqttDeviceTwin_25_037: [**This method shall parse topic to look for only either twin response topic or twin patch topic and thorw unsupportedoperation exception other wise.**]**

**SRS_MqttDeviceTwin_25_038: [**If the topic is of type response topic then this method shall parse further for status and set it for the message by calling setStatus for the message**]**

**SRS_MqttDeviceTwin_25_039: [**If the topic is of type response topic and if status is either a non 3 digit number or not found then receive shall throw IOException **]**

**SRS_MqttDeviceTwin_25_040: [**If the topic is of type response topic then this method shall parse further to look for request id which if found is set by calling setRequestId**]**

**SRS_MqttDeviceTwin_25_041: [**If the topic is of type response topic then this method shall parse further to look for version which if found is set by calling setVersion**]**

**SRS_MqttDeviceTwin_25_042: [**If the topic is of type patch for desired properties then this method shall parse further to look for version which if found is set by calling setVersion**]**

**SRS_MqttDeviceTwin_25_043: [**If the topic is not of type response for desired properties then this method shall throw unsupportedoperation exception**]**

**SRS_MqttDeviceTwin_25_044: [**If the topic is of type response then this method shall set data and operation type as DEVICE_TWIN_OPERATION_GET_RESPONSE if data is not null**]**

**SRS_MqttDeviceTwin_25_045: [**If the topic is of type response then this method shall set empty data and operation type as DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_RESPONSE if data is null or empty**]**

**SRS_MqttDeviceTwin_25_046: [**If the topic is of type patch for desired properties then this method shall set the data and operation type as DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_RESPONSE if data is not null or empty**]**

**SRS_MqttDeviceTwin_25_047: [**If the topic is of type patch for desired properties then this method shall throw unsupportedoperation exception if data is null or empty**]**

























    
    
   