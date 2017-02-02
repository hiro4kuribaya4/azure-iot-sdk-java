// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package tests.unit.com.microsoft.azure.sdk.iot.deps.serializer;

import com.microsoft.azure.sdk.iot.deps.serializer.TwinTags;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for TwinTags serializer
 */
public class TwinTagsTest {

    private static final String bigString_150chars =
            "01234567890123456789012345678901234567890123456789" +
            "01234567890123456789012345678901234567890123456789" +
            "01234567890123456789012345678901234567890123456789";
    private static final String specialCharString = "value special chars !@#$%^&*()_";
    private static final String illegalString_Dot = "illegal.key";
    private static final String illegalString_Space = "illegal key";
    private static final String illegalString_Dollar = "illegal$key";

    /**
     * CONSTRUCTOR
     * public TwinTags()
     */

    /* Tests_SRS_TWIN_TAGS_21_001: [The constructor shall initialize an empty HashMap of tags calling the superClass constructor.] */
    @Test
    public void constructorTest_succeed()
    {
        TwinTags tags = new TwinTags();   
    }

    /**
     * CONSTRUCTOR
     * public TwinTags(String tag, HashMap<String, Object> tagProperties) throws IllegalArgumentException
     */

    /* Tests_SRS_TWIN_TAGS_21_002: [The constructor shall initialize an empty HashMap of tags calling the superClass constructor.] */
    /* Tests_SRS_TWIN_TAGS_21_003: [The constructor shall add the provided `tag` and its properties into the superClass.] */
    @Test
    public void constructor_succeed()
    {
        // Arrange
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");
        sampleTag1.put("Key2", "Value2");
        sampleTag1.put("Key3", "Value3");
        sampleTag1.put("Key4", bigString_150chars);
        sampleTag1.put("Key5", specialCharString);

        // Act
        TwinTags tags = new TwinTags("Tag1", sampleTag1);

        // Assert
        HashMap<String,String> resultTag = tags.get("Tag1");
        assertThat(resultTag.get("Key1"), is("Value1"));
        assertThat(resultTag.get("Key2"), is("Value2"));
        assertThat(resultTag.get("Key3"), is("Value3"));
        assertThat(resultTag.get("Key4"), is(bigString_150chars));
        assertThat(resultTag.get("Key5"), is(specialCharString));
    }

    /* Tests_SRS_TWIN_TAGS_21_004: [If the `tag` is null, the constructor shall throw IllegalArgumentException.] */
    /* Tests_SRS_TWIN_TAGS_21_005: [If the `tag` is empty, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_006: [If the `tag` is more than 128 characters long, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_007: [If the `tag` has a illegal  character, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_008: [If the any `tagProperty` key is null, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_009: [If the any `tagProperty` key is empty, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_010: [If the any `tagProperty` key is more than 128 characters long, the constructor shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_011: [If the any `tagProperty` key has a illegal  character, the constructor shall throw IllegalArgumentException.] */
    /* Tests_SRS_TWIN_TAGS_21_053: [If the any `tagProperty` value is null, the addTag shall throw IllegalArgumentException.] */
    /* Tests_SRS_TWIN_TAGS_21_054: [If the any `tagProperty` value is empty, the addTag shall throw IllegalArgumentException.] */


    /**
     * addTag
     * public void addTag(String tag, HashMap<String, Object> tagProperties) throws IllegalArgumentException
     */

    /* Tests_SRS_TWIN_TAGS_21_012: [The addTag shall add the provided `tag` and its properties into the superClass.] */  
    /* Tests_SRS_TWIN_TAGS_21_013: [If the `tag` already exists, the addTag shall add the properties to the existed tag.] */
    /* Tests_SRS_TWIN_TAGS_21_022: [If the any `tagProperty` key already exists, the addTag shall replace the existed value by the new one.] */
    @Test
    public void addTag_hashmap_succeed()
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,String> resultTag;
        String value;

        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");
        sampleTag1.put("Key2", "Value2");
        HashMap<String,Object> sampleTag2 = new HashMap<>();
        sampleTag2.put("Key1", "Value3");
        sampleTag2.put("Key3", "Value3");
        sampleTag2.put("Key4", "Value4");
        sampleTag2.put("Key5", bigString_150chars);
        sampleTag2.put("Key6", specialCharString);

        // Act
        tags.addTag("Tag1", sampleTag1);
        tags.addTag("Tag1", sampleTag2);
        tags.addTag("Tag2", sampleTag2);
        tags.addTag("Tag2", "Key1", "Value1");

        // Assert
        resultTag = tags.get("Tag1");
        value = resultTag.get("Key1");
        assertThat(value, is("Value3"));
        value = resultTag.get("Key2");
        assertThat(value, is("Value2"));

        resultTag = tags.get("Tag2");
        value = resultTag.get("Key1");
        assertThat(value, is("Value1"));
        value = resultTag.get("Key4");
        assertThat(value, is("Value4"));
        value = resultTag.get("Key5");
        assertThat(value, is(bigString_150chars));
        value = resultTag.get("Key6");
        assertThat(value, is(specialCharString));
    }

    /* Tests_SRS_TWIN_TAGS_21_014: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_NULL_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag(null, sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_015: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_empty_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag("", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_016: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_big_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag(bigString_150chars, sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_017: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalDOT_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag("illegal.tag", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_017: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalSPACE_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag("illegal tag", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_017: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalDOLLAR_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "Value1");

        // Act
        tags.addTag("illegal$tag", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_018: [If the any `tagProperty` key is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_NULL_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put(null, "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_019: [If the any `tagProperty` key is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_empty_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("", "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_020: [If the any `tagProperty` key is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_big_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put(bigString_150chars, "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_021: [If the any `tagProperty` key has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalDOT_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put(illegalString_Dot, "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_021: [If the any `tagProperty` key has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalSPACE_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put(illegalString_Space, "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_021: [If the any `tagProperty` key has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_illegalDOLLAR_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put(illegalString_Dollar, "Value1");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_051: [If the any `tagProperty` value is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_NULL_value_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", null);

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_052: [If the any `tagProperty` value is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_hashmap_empty_value_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,Object> sampleTag1 = new HashMap<>();
        sampleTag1.put("Key1", "");

        // Act
        tags.addTag("tag1", sampleTag1);

        // Assert
    }

    /**
     * addTag
     * public void addTag(String tag, String Key, Object value) throws IllegalArgumentException
     */


    /* Tests_SRS_TWIN_TAGS_21_023: [The addTag shall add the provided `tag` and the property into the superClass.] */
    /* Tests_SRS_TWIN_TAGS_21_024: [If the `tag` already exists, the addTag shall add the property to the existed tag.] */
    /* Tests_SRS_TWIN_TAGS_21_033: [If the `key` already exists, the addTag shall replace the existed value by the new one.] */
    @Test
    public void addTag_keyvalue_succeed()
    {
        // Arrange
        TwinTags tags = new TwinTags();
        HashMap<String,String> tag;
        String value;

        // Act
        tags.addTag("Tag1", "Key1", "Value1");
        tags.addTag("Tag1", "Key2", "Value2");
        tags.addTag("Tag1", "Key1", "Value3");
        tags.addTag("Tag2", "Key1", "Value1");
        tags.addTag("Tag2", "Key4", "Value4");
        tags.addTag("Tag2", "Key5", bigString_150chars);
        tags.addTag("Tag2", "Key6", specialCharString);

        // Assert
        tag = tags.get("Tag1");
        value = tag.get("Key1");
        assertThat(value, is("Value3"));
        value = tag.get("Key2");
        assertThat(value, is("Value2"));
        tag = tags.get("Tag2");
        value = tag.get("Key1");
        assertThat(value, is("Value1"));
        value = tag.get("Key4");
        assertThat(value, is("Value4"));
        value = tag.get("Key5");
        assertThat(value, is(bigString_150chars));
        value = tag.get("Key6");
        assertThat(value, is(specialCharString));
    }

    /* Tests_SRS_TWIN_TAGS_21_025: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_NULL_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag(null, "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_026: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_empty_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("", "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_027: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_big_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag(bigString_150chars, "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_028: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalDOT_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("illegal.tag", "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_028: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalSPACE_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("illegal tag", "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_028: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalDOLLAR_tag_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("illegal$tag", "Key2", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_029: [If the `key` is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_NULL_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", null, "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_030: [If the `key` is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_empty_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", "", "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_031: [If the `key` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_big_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", bigString_150chars, "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_032: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalDOT_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", illegalString_Dot, "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_032: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalSPACE_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", illegalString_Space, "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_032: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_illegalDOLLAR_key_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", illegalString_Dollar, "Value2");

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_049: [If the `value` is null, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_NULL_value_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", "key1", null);

        // Assert
    }

    /* Tests_SRS_TWIN_TAGS_21_050: [If the `value` is empty, the addTag shall throw IllegalArgumentException.] */
    @Test (expected = IllegalArgumentException.class)
    public void addTag_keyvalue_empty_value_failed() throws IllegalArgumentException
    {
        // Arrange
        TwinTags tags = new TwinTags();

        // Act
        tags.addTag("tag1", "key1", "");

        // Assert
    }



    /**
     * GetTagProperty
     */

    /* Tests_SRS_TWIN_TAGS_21_034: [The GetTagProperty shall return the string that correspond to the value of the provided key in the provided tag.] */
    /* Tests_SRS_TWIN_TAGS_21_035: [If the `tag` is null, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_036: [If the `tag` is empty, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_037: [If the `tag` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_038: [If the `tag` has a illegal  character, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_039: [If the `tag` do not exists, the addTag shall return null.] */  
    /* Tests_SRS_TWIN_TAGS_21_040: [If the `key` is null, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_041: [If the `key` is empty, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_042: [If the `key` is more than 128 characters long, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_043: [If the `key` has a illegal  character, the addTag shall throw IllegalArgumentException.] */  
    /* Tests_SRS_TWIN_TAGS_21_044: [If the `key` do not exists, the addTag shall return null.] */  

    /**
     * toJson
     */
    /* Tests_SRS_TWIN_TAGS_21_045: [The toJson shall create a String with information in the TwinTags using json format.] */
    /* Tests_SRS_TWIN_TAGS_21_046: [The toJson shall not include null fields.] */  

    /**
     * fromJson
     */

    /* Tests_SRS_TWIN_TAGS_21_047: [The fromJson shall fill the fields in TwinTags with the values provided in the json string.] */
    /* Tests_SRS_TWIN_TAGS_21_048: [The fromJson shall not change fields that is not reported in the json string.] */  
}
