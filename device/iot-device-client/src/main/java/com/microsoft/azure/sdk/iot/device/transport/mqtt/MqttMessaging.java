// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device.transport.mqtt;

import com.microsoft.azure.sdk.iot.device.Message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class MqttMessaging extends Mqtt {
    private final int MAX_PERMITS = 1;
    private final Semaphore MESSAGING_SEMAPHORE = new Semaphore(MAX_PERMITS);
    private String subscribeTopic;
    private String publishTopic;
    private String parseTopic;

    @Override
    String parseTopic() throws IOException
    {
        /*
        **Codes_SRS_MqttMessaging_25_004: [**parseTopic concrete method shall be implemeted by MqttMessaging concrete class.**]**
         */
        String topic = null;

        if (allReceivedMessages == null)
        {
            /*
            **Codes_SRS_MqttMessaging_25_008: [**If receiveMessage queue is null then parseTopic shall throw IOException.**]**
             */
            throw new IOException("Queue cannot be null");
        }

        if(!allReceivedMessages.isEmpty())
        {

           for ( Map.Entry<String, byte[]> data : allReceivedMessages.entrySet())
           {
               String topicFound = data.getKey();

               /*
               **Codes_SRS_MqttMessaging_25_005: [**parseTopic shall look for the subscribe topic prefix from received message queue.**]**
                */
               /*
               **Codes_SRS_MqttMessaging_25_006: [**If none of the topics from the received queue match the subscribe topic prefix then this method shall return null string .**]**
                */
               /*
               **Codes_SRS_MqttMessaging_25_007: [**If received messages queue is empty then parseTopic shall return null string.**]**
                */
               if (topicFound != null && topicFound.length() > parseTopic.length() && topicFound.startsWith(parseTopic))
               {
                   topic = topicFound;
                   break;
               }
           }
        }
        return topic;
    }

    @Override
    byte[] parsePayload(String topic) throws IOException
    {
        /*
            This method is called only when you are certain that there is a message in the queue meant for device messaging that needs to be retrieved and then deleted.
         */
        /*
        **Codes_SRS_MqttMessaging_25_009: [**parsePayload concrete method shall be implemeted by MqttMessaging concrete class.**]**
         */

        if (topic == null)
        {
            /*
            **Codes_SRS_MqttMessaging_25_011: [**If the topic is null then parsePayload shall stop parsing for payload and return.**]**
             */
            return null;
        }
        if (allReceivedMessages == null)
        {
            /*
            **Codes_SRS_MqttMessaging_25_013: [**If receiveMessage queue is null then this method shall throw IOException.**]**
             */
            throw new IOException("Invalid State - topic is not null and could not be found in queue");
        }

        if (!allReceivedMessages.containsKey(topic))
        {
            /*
            **Codes_SRS_MqttMessaging_25_012: [**If the topic is non-null and received messagesqueue could not locate the payload then this method shall throw IOException**]**
             */
            throw new IOException("Topic is should be present in received queue at this point");
        }

        /*
        **Codes_SRS_MqttMessaging_25_010: [**This parsePayload method look for payload for the corresponding topic from the received messagesqueue.**]**
         */
        if(!allReceivedMessages.isEmpty())
        {
            /*
            **Codes_SRS_MqttMessaging_25_014: [**If the topic is found in the message queue then parsePayload shall delete it from the queue.**]**
             */
            return allReceivedMessages.remove(topic);
        }

        return null;


    }

    @Override
    public void onReconnect() throws IOException
    {
        try
        {
            /*
            **Codes_SRS_MqttMessaging_25_020: [**onReconnect method shall be implemeted by MqttMessaging class.**]**

            **Codes_SRS_MqttMessaging_25_020: [**This onReconnect method shall put the entire operation of the MqttMessaging class on hold by waiting on the lock.**]**
             */
            System.out.println("Pausing Device Messaging during reconnect");
            MESSAGING_SEMAPHORE.acquire();
        }
        catch (InterruptedException e)
        {
            // Do nothing and log
        }

    }

    @Override
    void onReconnectComplete(boolean status) throws IOException {

        if (status)
        {
            /*

            **Codes_SRS_MqttMessaging_25_017: [**This onReconnectComplete method shall be implemeted by MqttMessaging class.**]**

            **Codes_SRS_MqttMessaging_25_018: [**If the status is true, onReconnectComplete method shall release all the operation of the MqttMessaging class put on hold by notifying the users of the lock.**]**
            * */
            System.out.println("Continue device Messaging after reconnect");
            MESSAGING_SEMAPHORE.release();
        }
        else
        {
            /*
            **Codes_SRS_MqttMessaging_25_019: [**If the status is false, onReconnectComplete method shall throw IOException**]**
             */
            MESSAGING_SEMAPHORE.release();
            throw new IOException("Could not reconnect to IotHub");
        }

    }

    public MqttMessaging(String serverURI, String deviceId, String userName, String password) throws IOException
    {
        /*
        **Codes_SRS_MqttMessaging_25_001: [**The constructor shall throw InvalidParameter Exception if any of the parameters are null or empty .**]**
         */
        /*
        **Codes_SRS_MqttMessaging_25_002: [**The constructor shall use the configuration to instantiate super class and passing the parameters.**]**
         */
        super(serverURI, deviceId, userName, password);
        /*
        **Codes_SRS_MqttMessaging_25_003: [**The constructor construct publishTopic and subscribeTopic from deviceId.**]**
         */
        this.publishTopic = "devices/" + deviceId + "/messages/events/";
        this.subscribeTopic = "devices/" + deviceId + "/messages/devicebound/#";
        this.parseTopic = "devices/" + deviceId + "/messages/devicebound/";

    }

    public void start() throws IOException
    {
        try
        {
            /*
            **Codes_SRS_MqttMessaging_25_020: [**start method shall be call connect to establish a connection to IOT Hub with the given configuration.**]**

            **Codes_SRS_MqttMessaging_25_021: [**start method shall subscribe to messaging subscribe topic once connected.**]**
             */
            MESSAGING_SEMAPHORE.acquire();
            this.connect();
            this.subscribe(subscribeTopic);
        }
        catch (InterruptedException e)
        {

        }
        finally
        {
            MESSAGING_SEMAPHORE.release();
        }

    }

    public void stop() throws IOException
    {

       try
       {
           /*
           **Codes_SRS_MqttMessaging_25_022: [**stop method shall be call disconnect to tear down a connection to IOT Hub with the given configuration.**]**
            */
           MESSAGING_SEMAPHORE.acquire();
           this.disconnect();
       }
       catch (InterruptedException e)
       {

       }
       finally
       {
            /*
            As MQTT connection is controlled by this class, it is important to restart
            base class on exit from this class.
            */
           this.restartBaseMqtt();
           MESSAGING_SEMAPHORE.release();
       }


    }

    public void send(Message message) throws IOException
    {
        try
        {
            MESSAGING_SEMAPHORE.acquire();
            {
                if (message == null || message.getBytes() == null)
                {
                    /*
                    **Codes_SRS_MqttMessaging_25_025: [**send method shall throw an exception if the message is null.**]**
                     */
                    throw new IOException("Message cannot be null");
                }

                /*
                **Codes_SRS_MqttMessaging_25_024: [**send method shall publish a message to the IOT Hub on the publish topic by calling method publish().**]**
                 */
                this.publish(this.publishTopic, message.getBytes());

            }

        }
        catch (InterruptedException e)
        {

        }
        finally
        {
            MESSAGING_SEMAPHORE.release();
        }
    }
}
