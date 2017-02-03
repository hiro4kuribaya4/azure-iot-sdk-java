// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.DeviceTwin;

public class Pair<Type1, Type2>
{
    Type1 key;
    Type2 value;

    Pair(Type1 t1, Type2 t2)
    {
        this.key = t1;
        this.value = t2;
    }

    public Type1 getKey()
    {
        return key;
    }

    public Type2 getValue()
    {
        return value;
    }
}
