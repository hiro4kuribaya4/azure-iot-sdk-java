// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;

public interface DeviceTwinCallback
{
    public void DeviceTwinCall(IotHubStatusCode status, String data, Object context);
}
