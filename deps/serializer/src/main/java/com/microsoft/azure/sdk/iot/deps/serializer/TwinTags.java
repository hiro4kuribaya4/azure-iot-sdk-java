// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.deps.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Twin tags representation
 */
public class TwinTags extends HashMap<String, HashMap<String, String>> {

    private static final Gson gson = new GsonBuilder().create();

    public TwinTags()
    {
        /* Codes_SRS_TWIN_TAGS_21_001: [The constructor shall initialize an empty HashMap of tags calling the superClass constructor.] */
        super();
    }

    /* Codes_SRS_TWIN_TAGS_21_002: [The constructor shall initialize an empty HashMap of tags calling the superClass constructor.] */
    /* Codes_SRS_TWIN_TAGS_21_003: [The constructor shall add the provided `tag` and its properties into the superClass.] */
    /* Codes_SRS_TWIN_TAGS_21_004: [If the `tag` is null, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_005: [If the `tag` is empty, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_006: [If the `tag` is more than 128 characters long, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_007: [If the `tag` has a illegal  character, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_008: [If the any `tagProperty` key is null, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_009: [If the any `tagProperty` key is empty, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_010: [If the any `tagProperty` key is more than 128 characters long, the constructor shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_011: [If the any `tagProperty` key has a illegal  character, the constructor shall throw IllegalArgumentException.] */
    public TwinTags(String tag, HashMap<String, Object> tagProperties) throws IllegalArgumentException
    {
        super();
        addTag(tag, tagProperties);
    }

    /* Codes_SRS_TWIN_TAGS_21_012: [The addTag shall add the provided `tag` and its properties into the superClass.] */
    /* Codes_SRS_TWIN_TAGS_21_013: [If the `tag` already exists, the addTag shall add the properties to the existed tag.] */
    /* Codes_SRS_TWIN_TAGS_21_014: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_015: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_016: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_017: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_018: [If the any `tagProperty` key is null, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_019: [If the any `tagProperty` key is empty, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_020: [If the any `tagProperty` key is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_021: [If the any `tagProperty` key has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_022: [If the any `tagProperty` key already exists, the addTag shall replace the existed value by the new one.] */
    public void addTag(String tag, HashMap<String, Object> tagProperties) throws IllegalArgumentException
    {
        for (Map.Entry<String,Object> e : tagProperties.entrySet()) {
            addTag(tag, e.getKey(), e.getValue());
        }
    }

    public void addTag(String tag, String key, Object value) throws IllegalArgumentException
    {
        if(tag == null) {
            /* Codes_SRS_TWIN_TAGS_21_025: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException ("tag shall not be null");
        }
        if(tag.isEmpty()) {
            /* Codes_SRS_TWIN_TAGS_21_026: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("tag is empty");
        }
        if(tag.length()>128) {
            /* Codes_SRS_TWIN_TAGS_21_027: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("tag is too big for json");
        }
        if(tag.contains(".") || tag.contains(" ") || tag.contains("$") ) {
            /* Codes_SRS_TWIN_TAGS_21_028: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("tag contains illegal character");
        }

        if(key == null) {
            /* Codes_SRS_TWIN_TAGS_21_029: [If the `key` is null, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException ("key shall not be null");
        }
        if(key.isEmpty()) {
            /* Codes_SRS_TWIN_TAGS_21_030: [If the `key` is empty, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("key is empty");
        }
        if(key.length()>128) {
            /* Codes_SRS_TWIN_TAGS_21_031: [If the `key` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("key is too big for json");
        }
        if(key.contains(".") || key.contains(" ") || key.contains("$") ) {
            /* Codes_SRS_TWIN_TAGS_21_032: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("key contains illegal character");
        }

        if(value == null) {
            /* Codes_SRS_TWIN_TAGS_21_049: [If the `value` is null, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException ("value shall not be null");
        }
        if(value.toString().isEmpty()) {
            /* Codes_SRS_TWIN_TAGS_21_050: [If the `value` is empty, the addTag shall throw IllegalArgumentException.] */
            throw new IllegalArgumentException("value is empty");
        }

        /* Codes_SRS_TWIN_TAGS_21_024: [If the `tag` already exists, the addTag shall add the property to the existed tag.] */
        HashMap<String, String> tagHash = super.get(tag);

        if(tagHash == null)
        {
            tagHash = new HashMap<>();
        }

        /* Codes_SRS_TWIN_TAGS_21_033: [If the `key` already exists, the addTag shall replace the existed value by the new one.] */
        tagHash.put(key, value.toString());
        /* Codes_SRS_TWIN_TAGS_21_023: [The addTag shall add the provided `tag` and the property into the superClass.] */
        super.put(tag, tagHash);
    }

    /* Codes_SRS_TWIN_TAGS_21_034: [The GetTagProperty shall return the string that correspond to the value of the provided key in the provided tag.] */
    /* Codes_SRS_TWIN_TAGS_21_035: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_036: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_037: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_038: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_039: [If the `tag` do not exists, the addTag shall return null.] */
    /* Codes_SRS_TWIN_TAGS_21_040: [If the `key` is null, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_041: [If the `key` is empty, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_042: [If the `key` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_043: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    /* Codes_SRS_TWIN_TAGS_21_044: [If the `key` do not exists, the addTag shall return null.] */
    public String GetTagProperty(String tag, String key) throws IllegalArgumentException
    {
        HashMap<String, String> property = super.get(tag);
        return property.get(key);
    }

    /* Codes_SRS_TWIN_TAGS_21_045: [The toJson shall create a String with information in the TwinTags using json format.] */
    /* Codes_SRS_TWIN_TAGS_21_046: [The toJson shall not include null fields.] */
    public String toJson()
    {
        return gson.toJson(this);
    }

    /* Codes_SRS_TWIN_TAGS_21_047: [The fromJson shall fill the fields in TwinTags with the values provided in the json string.] */
    /* Codes_SRS_TWIN_TAGS_21_048: [The fromJson shall not change fields that is not reported in the json string.] */
    public void fromJson(String json)
    {
        TwinTags newValues = gson.fromJson(json, TwinTags.class);
        copy(newValues);
    }

    private void copy(TwinTags newValues)
    {

    }

}
