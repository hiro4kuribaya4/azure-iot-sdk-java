// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageType;

import static com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceTwinOperations.DEVICE_TWIN_OPERATION_UNKNOWN;

public class DeviceTwinMessage extends Message
{
    String version;
    String requestId;
    String status;
    DeviceTwinOperations operationType;


  public DeviceTwinMessage(byte[] data)
  {
      super(data);
      this.setMessageType(MessageType.DeviceTwin);
      this.version = null;
      this.requestId = null;
      this.status = null;
      this.operationType = DEVICE_TWIN_OPERATION_UNKNOWN;
  }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return  this.version;
    }

    public void setRequestId(String id)
    {
        this.requestId = id;
    }

    public String getRequestId()
    {
        return this.requestId;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return this.status;
    }

    public void setDeviceTwinOperationType(DeviceTwinOperations type)
    {
        this.operationType = type;
    }

    public DeviceTwinOperations getDeviceTwinOperationType()
    {
        return this.operationType;
    }

}
