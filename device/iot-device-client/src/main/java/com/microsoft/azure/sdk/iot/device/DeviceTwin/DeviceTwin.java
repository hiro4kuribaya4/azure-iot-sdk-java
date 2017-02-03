// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.deps.serializer.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

public class DeviceTwin
{
    private int requestId;
    private Twin twinObject = null;
    private DeviceClient deviceClient = null;
    private DeviceClientConfig config = null;

    /*
        Callback to respond to user on all of its status
     */
    private IotHubEventCallback deviceTwinStatusCallback;
    private Object deviceTwinStatusCallbackContext;

    /*
        Callbacks to respond to its user on desired property changes
     */
    private PropertyCallBack<String, Object> deviceTwinGenericPropertyChangeCallback;
    private Object deviceTwinGenericPropertyChangeCallbackContext;

    /*
        Map of callbacks to call when a particular desired property changed
     */

    private TreeMap<String, Pair<PropertyCallBack<String, Object>, Object>> onDesiredPropertyChangeMap;


    /*
        Callback invoked by serializer when desired property changes
    */
    private final class OnDesiredPropertyChange implements TwinPropertiesChangeCallback
    {
        @Override
        public void execute(HashMap<String, String> desiredPropertyMap)
        {
            if (onDesiredPropertyChangeMap != null)
            {
                for(Map.Entry<String, String> desiredProperty : desiredPropertyMap.entrySet())
                {
                    if (onDesiredPropertyChangeMap.containsKey(desiredProperty.getKey()))
                    {
                        Pair<PropertyCallBack<String, Object>, Object> callBackObjectPair = onDesiredPropertyChangeMap.get(desiredProperty.getKey());
                        if (callBackObjectPair != null)
                        {
                            callBackObjectPair.getKey().PropertyCall(desiredProperty.getKey(),
                                    desiredProperty.getValue(), callBackObjectPair.getValue());
                        }
                        else
                        {
                            deviceTwinGenericPropertyChangeCallback.PropertyCall(desiredProperty.getKey(),
                                    desiredProperty.getValue(), deviceTwinGenericPropertyChangeCallbackContext);
                        }

                    }
                    else
                    {
                        deviceTwinGenericPropertyChangeCallback.PropertyCall(desiredProperty.getKey(),
                                desiredProperty.getValue(), deviceTwinGenericPropertyChangeCallbackContext);

                    }
                    desiredPropertyMap.remove(desiredProperty);
                }

            }

        }
    }

    /*
        Callback invoked by serializer when reported property changes
     */
    private final class OnReportedPropertyChange implements TwinPropertiesChangeCallback
    {
        @Override
        public void execute(HashMap<String, String> hashMap)
        {


        }
    }

    /*
        Callback invoked when a response to device twin operation is issued by iothub
     */
    private final class deviceTwinResponseMessageCallback implements MessageCallback
    {
        @Override
        public IotHubMessageResult execute(Message message, Object callbackContext)
        {
            if (message.getMessageType() != MessageType.DeviceTwin)
            {
                System.out.print("Unexpected message type received");
            }

            DeviceTwinMessage dtMessage = (DeviceTwinMessage) message;
            String status = dtMessage.getStatus();
            IotHubStatusCode iotHubStatus = null;

            if (status != null)
            {
                iotHubStatus = IotHubStatusCode.getIotHubStatusCode(Integer.parseInt(status));
                deviceTwinStatusCallback.execute(iotHubStatus , deviceTwinStatusCallbackContext);
            }

            switch  (dtMessage.operationType)
            {
                case DEVICE_TWIN_OPERATION_GET_RESPONSE:
                {
                    if (iotHubStatus == IotHubStatusCode.OK)
                    {
                        twinObject.updateTwin(message.getBytes().toString());
                    }
                    break;
                }
                case DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_RESPONSE:
                {
                    break;
                }
                case DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_RESPONSE:
                {
                    if (iotHubStatus == IotHubStatusCode.OK)
                    {
                        twinObject.updateDesiredProperty(dtMessage.getBytes().toString());
                    }
                    break;
                }
                default:
                    break;
            }
            return null;
        }
    }

    /*
        Callback invoked when device twin operation request has successfully completed
    */
    private final class deviceTwinRequestMessageCallback implements IotHubEventCallback
    {
        @Override
        public void execute(IotHubStatusCode responseStatus, Object callbackContext)
        {
            /*
                Don't worry about this....this is just delivery complete. Actual response is
                another message received in deviceTwinResponseMessageCallback.
             */
            System.out.println("DeviceTwin request operation completed with status - " + responseStatus);
            deviceTwinStatusCallback.execute(responseStatus, deviceTwinStatusCallbackContext);
        }
    }

    public DeviceTwin(DeviceClient client, DeviceClientConfig config, IotHubEventCallback deviceTwinCallback, Object deviceTwinCallbackContext,
                      PropertyCallBack genericPropertyCallback, Object genericPropertyCallbackContext) throws IOException
    {

        this.deviceClient = client;
        this.config = config;

        this.config.setDeviceTwinMessageCallback(new deviceTwinResponseMessageCallback());
        this.requestId = 0;

        this.deviceTwinStatusCallback = deviceTwinCallback;
        this.deviceTwinStatusCallbackContext = deviceTwinCallbackContext;

        this.deviceTwinGenericPropertyChangeCallback = genericPropertyCallback;
        this.deviceTwinGenericPropertyChangeCallbackContext = genericPropertyCallbackContext;

        this.twinObject = new Twin(new OnDesiredPropertyChange(), new OnReportedPropertyChange());
    }


    public void getDeviceTwin()
    {
        DeviceTwinMessage getTwinRequestMessage = new DeviceTwinMessage(new byte[0]);
        getTwinRequestMessage.setRequestId(String.valueOf(requestId++));

        getTwinRequestMessage.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_GET_REQUEST);

        this.deviceClient.sendEventAsync(getTwinRequestMessage,new deviceTwinRequestMessageCallback(), null); // Can context be twinObject ?
    }

    public void updateReportedProperties(HashSet<Property> reportedProperties) throws IOException // update to object for value
    {
        if (reportedProperties == null)
            throw new InvalidParameterException("Reported properties cannot be null");

        if (this.twinObject == null)
            throw new IOException("Initilaize twin object before using it");

        //HashMap<String, Object> reportedPropertiesMap = new HashMap<>();
        HashMap<String, String> reportedPropertiesMap = new HashMap<>();

        for(Property p : reportedProperties)
        {
            //reportedPropertiesMap.put(p.getKey(), p.getValue());
            reportedPropertiesMap.put(p.getKey(), (String) p.getValue());
        }

        String serializedReportedProperties = this.twinObject.updateReportedProperty(reportedPropertiesMap);

        DeviceTwinMessage updateReportedPropertiesRequest = new DeviceTwinMessage(serializedReportedProperties.getBytes());

        updateReportedPropertiesRequest.setRequestId(String.valueOf(requestId++));

        updateReportedPropertiesRequest.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_REQUEST);

        this.deviceClient.sendEventAsync(updateReportedPropertiesRequest, new deviceTwinRequestMessageCallback(), null);

    }

    public void subscribeDesiredPropertiesNotification(Map<Property, Pair<PropertyCallBack<String, Object>, Object>> onDesiredPropertyChange) throws IOException
    {
        if (onDesiredPropertyChangeMap == null)
        {
            onDesiredPropertyChangeMap = new TreeMap<>();
            for (Map.Entry<Property, Pair<PropertyCallBack<String, Object>, Object>> desired : onDesiredPropertyChange.entrySet())
            {
                onDesiredPropertyChangeMap.put(desired.getKey().getKey(), desired.getValue());
            }
        }
        else
        {
            throw new IOException("You have already subscribed to desired property changes");
        }

        DeviceTwinMessage desiredPropertiesNotificationRequest = new DeviceTwinMessage(/*Figure out what ? */ new byte[0]);

        desiredPropertiesNotificationRequest.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_REQUEST);

        this.deviceClient.sendEventAsync(desiredPropertiesNotificationRequest, new deviceTwinRequestMessageCallback(), null);

    }

}
