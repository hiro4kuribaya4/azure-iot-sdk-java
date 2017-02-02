// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.deps.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Twin metadata representation
 */
public class TwinMetadata {

    private static final Gson gson = new GsonBuilder().create();

    @SerializedName("$lastUpdated")
    protected String lastUpdated;

    @SerializedName("$lastUpdatedVersion")
    protected Integer lastUpdatedVersion;

    public TwinMetadata(Integer version)
    {
        Date now = new Date();
        lastUpdated.toString();

        this.lastUpdatedVersion = version;
    }

    public String GetLastUpdate() { return lastUpdated; }

    public Integer GetLastUpdateVersion() { return lastUpdatedVersion; }
}
