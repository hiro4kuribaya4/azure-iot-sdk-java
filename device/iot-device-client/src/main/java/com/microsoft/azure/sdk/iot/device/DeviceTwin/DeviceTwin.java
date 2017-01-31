// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

import com.microsoft.azure.sdk.iot.device.*;

import java.security.InvalidParameterException;

public class DeviceTwin extends DeviceClient
{

    private DeviceTwinCallback getTwinResponseCallback;
    private DeviceTwinCallback updateReportedPropertiesResponseCallBack;
    private DeviceTwinCallback desiredPropertiesNotificationResponseCallBack;

    private MessageCallback deviceTwinResponseMessageCallback;
    private IotHubEventCallback deviceTwinRequestMessageCallback;
    private int requestId;


    private final class deviceTwinResponseMessageCallback implements MessageCallback
    {
        @Override
        public IotHubMessageResult execute(Message message, Object callbackContext)
        {
            DeviceTwinMessage dtMessage = (DeviceTwinMessage) message;
            switch  (dtMessage.operationType)
            {
                case DEVICE_TWIN_OPERATION_GET_RESPONSE:
                    /*
                        Response property = status -> check valid status
                        Response Body = json properties -> parse it
                        // add the response body to the twin object ??? and trigger callback
                     */
                    if (getTwinResponseCallback != null)
                    {
                        getTwinResponseCallback.DeviceTwinCall(null, null, null/* response body */);
                        getTwinResponseCallback = null;
                    }

                    break;
                case DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_RESPONSE:
                    /*
                        Response property = $version and status -> check valid status
                        Response body none
                     */
                    if (updateReportedPropertiesResponseCallBack != null)
                    {
                        updateReportedPropertiesResponseCallBack.DeviceTwinCall(null, null, null /*Status */);
                        updateReportedPropertiesResponseCallBack = null;
                    }
                    break;
                case DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_RESPONSE:
                    /*
                        Response property = none
                        Response body = updated properties
                     */
                    if (desiredPropertiesNotificationResponseCallBack != null)
                    {
                        desiredPropertiesNotificationResponseCallBack.DeviceTwinCall(null, null, null /* updated properties*/);
                    }

                    break;
                default:
                    break;
            }
            return null;
        }

    }

    private final class deviceTwinRequestMessageCallback implements IotHubEventCallback
    {
        @Override
        public void execute(IotHubStatusCode responseStatus, Object callbackContext)
        {
            /*
                Don't worry about this....this is just delivery complete. Actual response is
                another message received in deviceTwinResponseMessageCallback.
             */

            System.out.print("DeviceTwin operation completed with status - " + responseStatus);
        }
    }

    public DeviceTwin(/*Input Twin Class*/)
    {
        this.config.setDeviceTwinMessageCallback(deviceTwinResponseMessageCallback);
        this.requestId = 0;
        // save the twin class ?

    }


    public void getDeviceTwin(DeviceTwinCallback getTwinResponseCallback, Object context)
    {
        /*
            Set
            1. Requestid
            2.  Request property -> none
            3. Request Body -> none
         */
        if (getTwinResponseCallback == null)
            throw new InvalidParameterException("Please provide a call back to receive twin properties");

        this.getTwinResponseCallback = getTwinResponseCallback;

        DeviceTwinMessage getTwinRequestMessage = new DeviceTwinMessage(new byte[0]);
        getTwinRequestMessage.setRequestId(String.valueOf(requestId++));

        getTwinRequestMessage.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_GET_REQUEST);

        this.sendEventAsync(getTwinRequestMessage, deviceTwinRequestMessageCallback, null );
    }

    public void updateReportedProperties(String reportedPropertiesDocument, String version,
                                           DeviceTwinCallback updateReportedPropertiesResponseCallBack,
                                           Object context)
    {
        /*
        Set
            1. Requestid
            2. Request property -> version (base version)
            3. Request Body -> property

         */
        this.updateReportedPropertiesResponseCallBack = updateReportedPropertiesResponseCallBack;

        DeviceTwinMessage updateReportedPropertiesRequest = new DeviceTwinMessage(reportedPropertiesDocument.getBytes());

        updateReportedPropertiesRequest.setRequestId(String.valueOf(requestId++));

        if (version != null && version.length() != 0)
        {
            updateReportedPropertiesRequest.setProperty("version", version);
        }

        updateReportedPropertiesRequest.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_UPDATE_REPORTED_PROPERTIES_REQUEST);

        this.sendEventAsync(updateReportedPropertiesRequest, deviceTwinRequestMessageCallback, null);

    }

    public void subscribeDesiredPropertiesNotification(String version, DeviceTwinCallback notificationCallBack)
    {
        /*
        Set
            1. Request property -> new version
            2. Request Body -> property  ???????

         */
        if (notificationCallBack == null)
            throw new InvalidParameterException("Please provide a call back to receive desired properties notifications on");

        this.desiredPropertiesNotificationResponseCallBack = notificationCallBack;

        DeviceTwinMessage desiredPropertiesNotificationRequest = new DeviceTwinMessage(/*Figure out what ? */ new byte[0]);

        if (version != null && version.length() != 0)
        {
            desiredPropertiesNotificationRequest.setProperty("version", version);
        }

        desiredPropertiesNotificationRequest.setDeviceTwinOperationType(DeviceTwinOperations.DEVICE_TWIN_OPERATION_SUBSCRIBE_DESIRED_PROPERTIES_REQUEST);

        this.sendEventAsync(desiredPropertiesNotificationRequest, deviceTwinRequestMessageCallback, null);

    }

}
