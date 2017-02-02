# Twin Requirements

## Overview

Twin is a representation of the device twin database as described on session 7.3.1 of Azure IoT Hub - Device Twin.

## References

## Exposed API

```java
public class Twin extends IoTHubDeviceProperties
{
    public TwinTags tags
    public TwinProperties properties
    
    public Twin(String deviceId, String generationId) throws IllegalArgumentException

    public String toJson()
    public void fromJson(String json)
}
```


### Twin

```java
    public Twin(String deviceId, String generationId) throws IllegalArgumentException
```

**SRS_TWIN_21_001: [**The constructor shall bypass the deviceId and the generationId to the superClass.**]**  
**SRS_TWIN_21_002: [**If the superClass throws an exception, the constructor shall throws the same exception.**]**  
**SRS_TWIN_21_003: [**The constructor shall create an instance of the tags.**]**  
**SRS_TWIN_21_004: [**The constructor shall create an instance of the properties.**]**  


### toJson

```java
    public String toJson();
```

**SRS_TWIN_21_005: [**The toJson shall create a String with information in the Twin using json format.**]**  
**SRS_TWIN_21_006: [**The toJson shall not include null fields.**]**  


### fromJson

```java
    public void fromJson(String json);
```

**SRS_TWIN_21_007: [**The fromJson shall fill the fields in Twin with the values provided in the json string.**]**  
**SRS_TWIN_21_008: [**The fromJson shall not change fields that is not reported in the json string.**]**  
