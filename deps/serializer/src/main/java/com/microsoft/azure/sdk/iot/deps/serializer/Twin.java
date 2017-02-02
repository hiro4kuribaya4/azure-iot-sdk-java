// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.deps.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Twin Representation
 */
public class Twin
{

    private static final Gson gson = new GsonBuilder().create();
    protected static TwinPropertiesChangeCallback onDesiredCallback = null;
    protected static TwinPropertiesChangeCallback onReportedCallback = null;

    public TwinTags Tags = null;
    public TwinProperties Properties = new TwinProperties();

    public Twin()
    {

    }

    public Twin(TwinPropertiesChangeCallback onDesiredCallback)
    {
        setDesiredCallback(onDesiredCallback);
    }

    public Twin(TwinPropertiesChangeCallback onDesiredCallback, TwinPropertiesChangeCallback onReportedCallback)
    {
        setDesiredCallback(onDesiredCallback);
        setReportedCallback(onReportedCallback);
    }

    public void setDesiredCallback(TwinPropertiesChangeCallback callback)
    {
        this.onDesiredCallback = callback;
    }

    public void setReportedCallback(TwinPropertiesChangeCallback callback)
    {
        this.onReportedCallback = callback;
    }

    public void updateTwin(String json)
    {
        Twin newValues = gson.fromJson(json, Twin.class);
        copyAndReport(newValues);
    }

    public void updateDesiredProperty(String json)
    {
        Twin newValues = new Twin();
        newValues.Properties.Desired.fromJson(json);
        copyAndReport(newValues);
    }

    public void updateReportedProperty(String json)
    {
        Twin newValues = new Twin();
        newValues.Properties.Reported.fromJson(json);
        copyAndReport(newValues);
    }

    public String updateDesiredProperty(HashMap<String, String> property)
    {
        TwinProperty updatedDesiredProperty = new TwinProperty();

        // TODO: change the desired property in Properties.Desired.
        // TODO: populate the updateReportedProperty with the properties new values.

        return updatedDesiredProperty.toJson();
    }

    public String updateReportedProperty(HashMap<String, String> property)
    {
        TwinProperty updatedReportedProperty = new TwinProperty();

        // TODO: change the reported property in Properties.Reported.
        // TODO: populate the updateReportedProperty with the properties new values.

        return updatedReportedProperty.toJson();
    }

    public Integer GetDesiredPropertyVersion()
    {
        return Properties.Desired.GetVersion();
    }

    public Integer GetReportedPropertyVersion()
    {
        return Properties.Reported.GetVersion();
    }

    private void copyAndReport(Twin newValues)
    {
        // TODO: Copy the new properties values to this instance.
        // TODO: fire callback if any changes in the properties.
    }

    protected static void reporteDesiredChanged(TwinProperty property)
    {
        if(onDesiredCallback != null){
            onDesiredCallback.execute(property.GetPropertyMap());
        }
    }

    protected static void reporteReportedChanged(TwinProperty property)
    {
        if(onReportedCallback != null){
            onReportedCallback.execute(property.GetPropertyMap());
        }
    }
}
