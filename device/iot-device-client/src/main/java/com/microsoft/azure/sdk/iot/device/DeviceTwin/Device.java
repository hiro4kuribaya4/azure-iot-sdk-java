// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

import java.util.HashMap;
import java.util.HashSet;

abstract public class Device implements PropertyCallBack<String, Object>
{
    HashSet<Property> reportedProp = new HashSet<>();
    HashMap<Property, Pair<PropertyCallBack<String, Object>, Object>> desiredProp = new HashMap<>();

    public HashSet<Property> getReportedProp()
    {
        return reportedProp;
    }

    public void hasReportedProp(Property reportedProp)
    {
        this.reportedProp.add(reportedProp);
    }

    public HashMap<Property, Pair<PropertyCallBack<String, Object>, Object>> getDesiredProp()
    {
        return this.desiredProp;
    }

    public void hasDesiredProperty(Property desiredProp, PropertyCallBack<String, Object> desiredPropCallBack, Object desiredPropCallBackContext)
    {
        this.desiredProp.put(desiredProp, new Pair<>(desiredPropCallBack, desiredPropCallBackContext));
    }

}
