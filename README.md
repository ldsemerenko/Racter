Racter
=========

Racter is A Java Framework for Building Bots on Facebook's Messenger Platform.

*Current Version: 1.0.2*

[![Build Status](https://travis-ci.org/Clivern/Racter.svg?branch=master)](https://travis-ci.org/Clivern/Racter)
![](https://img.shields.io/maven-central/v/com.clivern/racter.svg)
[![Javadocs](http://www.javadoc.io/badge/com.clivern/racter.svg)](http://www.javadoc.io/doc/com.clivern/racter)
![](https://img.shields.io/github/license/clivern/racter.svg)


Installation
------------
To add a dependency using Maven, use the following:
```xml
<dependency>
  <groupId>com.clivern</groupId>
  <artifactId>racter</artifactId>
  <version>1.0.2</version>
</dependency>
```

To add a dependency using Gradle, use the following:
```java
dependencies {
  compile 'com.clivern:racter:1.0.2'
}
```

To add a dependency using Scala SBT, use the following:
```java
libraryDependencies += "com.clivern" % "racter" % "1.0.2"
```

Usage
-----
After adding the package as a dependency, Please read the following steps:

### Basic Configurations
In order to cofigure the package create `config.properties` file with the following data

```
app_id=App ID Goes Here
verify_token=Verify Token Goes Here
page_access_token=Page Access Token Goes Here
log_console_status=true or false
log_console_level=ALL, CONFIG, FINE, FINER, FINEST, INFO, SEVERE, WARNING or OFF
log_file_status=true or false
log_file_level=ALL, CONFIG, FINE, FINER, FINEST, INFO, SEVERE, WARNING or OFF
log_file_path=app.log
log_file_limit=1
log_file_count=200000
log_file_append=true or false
```

Then import all required classes

```java
import com.clivern.racter.BotPlatform;

import com.clivern.racter.receivers.*;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
```

then pass the `config.properties` file to the bot platform instance

```java
BotPlatform platform = new BotPlatform("config.properties");
```

or Configure it manually
```java
Map<String, String> options = new HashMap<String, String>();

options.put("app_id", "App ID Goes Here");
options.put("verify_token", "Verify Token Goes Here");
options.put("page_access_token", "Page Access Token Goes Here");
options.put("log_console_status", "true or false");
options.put("log_console_level", "ALL, CONFIG, FINE, FINER, FINEST, INFO, SEVERE, WARNING or OFF");
options.put("log_file_status", "true or false");
options.put("log_file_level", "ALL, CONFIG, FINE, FINER, FINEST, INFO, SEVERE, WARNING or OFF");
options.put("log_file_path", "app.log");
options.put("log_file_limit", "1");
options.put("log_file_count", "200000");
options.put("log_file_append", "true or false");

BotPlatform platform = new BotPlatform(options);
```

Setup Webhook
-------------
Create a route to verify your verify token, Facebook will perform a GET request to this route URL with some URL parameters to make sure that verify token is correct.

```java
BotPlatform platform = new BotPlatform("config.properties");

String hubMode = // Get hub.mode query parameter value from the current URL
String hubVerifyToken = // Get hub.verify_token query parameter value from the current URL
String hubChallenge = // Get hub.challenge query parameter value from the current URL


platform.getVerifyWebhook().setHubMode(hubMode);
platform.getVerifyWebhook().setHubVerifyToken(hubVerifyToken);
platform.getVerifyWebhook().setHubChallenge(hubChallenge);

if( platform.getVerifyWebhook().challenge() ){
    platform.finish();

    // Set Response to be hubChallenge value and status code is 200 like
    // response.status(200);
    // return ( hubChallenge != null ) ? hubChallenge : "";
}

platform.finish();

// Set Response to be 'Verification token mismatch' and status code is 403 like
// response.status(403);
// return "Verification token mismatch";
```

So let's say we use [Spark Java Framework](http://sparkjava.com/) for our bot, Our route and callback will look like the following:

```java
import static spark.Spark.*;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.*;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException
    {
        // Verify Token Route
        get("/", (request, response) -> {
            BotPlatform platform = new BotPlatform("config.properties");
            platform.getVerifyWebhook().setHubMode(( request.queryParams("hub.mode") != null ) ? request.queryParams("hub.mode") : "");
            platform.getVerifyWebhook().setHubVerifyToken(( request.queryParams("hub.verify_token") != null ) ? request.queryParams("hub.verify_token") : "");
            platform.getVerifyWebhook().setHubChallenge(( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "");

            if( platform.getVerifyWebhook().challenge() ){
                platform.finish();
                response.status(200);
                return ( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "";
            }

            platform.finish();
            response.status(403);
            return "Verification token mismatch";
        });
    }
}
```

Also if we use [Spring Boot Framework](https://projects.spring.io/spring-boot/) for our bot, Our route and callback will look like the following:

```java
package com.racter.example;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
@EnableAutoConfiguration
public class Main {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    String verifyToken(@RequestParam(value="hub.mode", defaultValue="") String hub_mode, @RequestParam(value="hub.verify_token", defaultValue="") String hub_verify_token, @RequestParam(value="hub.challenge", defaultValue="") String hub_challenge ) throws IOException {

        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getVerifyWebhook().setHubMode(hub_mode);
        platform.getVerifyWebhook().setHubVerifyToken(hub_verify_token);
        platform.getVerifyWebhook().setHubChallenge(hub_challenge);

        if( platform.getVerifyWebhook().challenge() ){
            platform.finish();
            return ( hub_challenge != "" ) ? hub_challenge : "";
        }

        platform.finish();
        return "Verification token mismatch";
    }
}
```

Also if we use [Play Framework](https://www.playframework.com/) for our bot, Our route and callback will look like the following:

```java
package controllers;

import play.mvc.*;
import play.mvc.Http.RequestBody;

import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Home Controller
 */
public class HomeController extends Controller {

    protected String basePath = System.getProperty("user.dir");

    public Result verifyToken() throws IOException
    {
        BotPlatform platform = new BotPlatform(this.basePath + "/conf/racter.properties");
        platform.getVerifyWebhook().setHubMode(request().getQueryString("hub.mode"));
        platform.getVerifyWebhook().setHubVerifyToken(request().getQueryString("hub.verify_token"));
        platform.getVerifyWebhook().setHubChallenge(request().getQueryString("hub.challenge"));

        if( platform.getVerifyWebhook().challenge() ){
            platform.finish();
            return ( request().getQueryString("hub.challenge") != null ) ? ok(request().getQueryString("hub.challenge")) : ok();
        }

        platform.finish();
        return ok("Verification token mismatch");
    }
}
```

Message Received
----------------
In order to receive and parse messages, You will need to create another route that receives post requests from Facebook. Our Route should contain a code look like the following:

```java
String body = // Get current Request Body
BotPlatform platform = new BotPlatform("config.properties");
platform.getBaseReceiver().set(body).parse();
HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
for (MessageReceivedWebhook message : messages.values()) {

    String user_id = (message.hasUserId()) ? message.getUserId() : "";
    String page_id = (message.hasPageId()) ? message.getPageId() : "";
    String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
    String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
    String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
    Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
    HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

}
```

So let's say we use [Spark Java Framework](http://sparkjava.com/) for our bot, Our route should look like the following:

```java
import static spark.Spark.*;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.*;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException
    {
        // Verify Token Route
        get("/", (request, response) -> {
            BotPlatform platform = new BotPlatform("config.properties");
            platform.getVerifyWebhook().setHubMode(( request.queryParams("hub.mode") != null ) ? request.queryParams("hub.mode") : "");
            platform.getVerifyWebhook().setHubVerifyToken(( request.queryParams("hub.verify_token") != null ) ? request.queryParams("hub.verify_token") : "");
            platform.getVerifyWebhook().setHubChallenge(( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "");

            if( platform.getVerifyWebhook().challenge() ){
                platform.finish();
                response.status(200);
                return ( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "";
            }

            platform.finish();
            response.status(403);
            return "Verification token mismatch";
        });

        post("/", (request, response) -> {
            String body = request.body();
            BotPlatform platform = new BotPlatform("config.properties");
            platform.getBaseReceiver().set(body).parse();
            HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
            for (MessageReceivedWebhook message : messages.values()) {

                String user_id = (message.hasUserId()) ? message.getUserId() : "";
                String page_id = (message.hasPageId()) ? message.getPageId() : "";
                String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
                String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
                String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
                Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
                HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

                // Use Logger To Log Incoming Data
                platform.getLogger().info("User ID#:" + user_id);
                platform.getLogger().info("Page ID#:" + page_id);
                platform.getLogger().info("Message ID#:" + message_id);
                platform.getLogger().info("Message Text#:" + message_text);
                platform.getLogger().info("Quick Reply Payload#:" + quick_reply_payload);

                for (String attachment : attachments.values()) {
                    platform.getLogger().info("Attachment#:" + attachment);
                }

                return "ok";
            }

            // ..
            // Other Receive Webhooks Goes Here
            // ..

            return "No Messages";
        });
    }
}
```

Also if we use [Spring Boot Framework](https://projects.spring.io/spring-boot/) for our bot, Our route and callback will look like the following:

```java
package com.racter.example;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
@EnableAutoConfiguration
public class Main {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    String verifyToken(@RequestParam(value="hub.mode", defaultValue="") String hub_mode, @RequestParam(value="hub.verify_token", defaultValue="") String hub_verify_token, @RequestParam(value="hub.challenge", defaultValue="") String hub_challenge ) throws IOException {

        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getVerifyWebhook().setHubMode(hub_mode);
        platform.getVerifyWebhook().setHubVerifyToken(hub_verify_token);
        platform.getVerifyWebhook().setHubChallenge(hub_challenge);

        if( platform.getVerifyWebhook().challenge() ){
            platform.finish();
            return ( hub_challenge != "" ) ? hub_challenge : "";
        }

        platform.finish();
        return "Verification token mismatch";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    @ResponseBody
    String webHook(@RequestBody String body) throws IOException, UnirestException {
        BotPlatform platform = new BotPlatform("config.properties");
        platform.getBaseReceiver().set(body).parse();
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
        for (MessageReceivedWebhook message : messages.values()) {

            String user_id = (message.hasUserId()) ? message.getUserId() : "";
            String page_id = (message.hasPageId()) ? message.getPageId() : "";
            String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
            String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
            String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
            HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

            // Use Logger To Log Incoming Data
            platform.getLogger().info("User ID#:" + user_id);
            platform.getLogger().info("Page ID#:" + page_id);
            platform.getLogger().info("Message ID#:" + message_id);
            platform.getLogger().info("Message Text#:" + message_text);
            platform.getLogger().info("Quick Reply Payload#:" + quick_reply_payload);

            for (String attachment : attachments.values()) {
                platform.getLogger().info("Attachment#:" + attachment);
            }

            return "ok";
        }

        // ..
        // Other Receive Webhooks Goes Here
        // ..

        return "No Messages";
    }
}
```

Also if we use [Play Framework](https://www.playframework.com/) for our bot, Our route and callback will look like the following:

```java
package controllers;

import play.mvc.*;
import play.mvc.Http.RequestBody;

import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Home Controller
 */
public class HomeController extends Controller {

    protected String basePath = System.getProperty("user.dir");

    public Result verifyToken() throws IOException
    {
        BotPlatform platform = new BotPlatform(this.basePath + "/conf/racter.properties");
        platform.getVerifyWebhook().setHubMode(request().getQueryString("hub.mode"));
        platform.getVerifyWebhook().setHubVerifyToken(request().getQueryString("hub.verify_token"));
        platform.getVerifyWebhook().setHubChallenge(request().getQueryString("hub.challenge"));

        if( platform.getVerifyWebhook().challenge() ){
            platform.finish();
            return ( request().getQueryString("hub.challenge") != null ) ? ok(request().getQueryString("hub.challenge")) : ok();
        }

        platform.finish();
        return ok("Verification token mismatch");
    }

    public Result webHook()  throws IOException, UnirestException
    {
        String body = request().body().asJson().toString();
        BotPlatform platform = new BotPlatform(this.basePath + "/conf/racter.properties");
        platform.getBaseReceiver().set(body.asText()).parse();
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
        for (MessageReceivedWebhook message : messages.values()) {

            String user_id = (message.hasUserId()) ? message.getUserId() : "";
            String page_id = (message.hasPageId()) ? message.getPageId() : "";
            String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
            String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
            String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
            HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

            platform.getLogger().info("User ID#:" + user_id);
            platform.getLogger().info("Page ID#:" + page_id);
            platform.getLogger().info("Message ID#:" + message_id);
            platform.getLogger().info("Message Text#:" + message_text);
            platform.getLogger().info("Quick Reply Payload#:" + quick_reply_payload);

            for (String attachment : attachments.values()) {
                platform.getLogger().info("Attachment#:" + attachment);
            }

            return ok("ok");
        }
        // ..
        // Other Receive Webhooks Goes Here
        // ..


        return ok("No Messages");
    }
}
```

Send API
--------

### Sending Message

Let's create an empty message first and fill it with the required data. We can get a new message container from Bot Platform Instance:

```java
MessageTemplate message_tpl = platform.getBaseSender().getMessageTemplate();

// Let's start to fill the required data here
// ..
```

Here's some of the usage cases:

```java
// get the message.getUserId() from the message received


// To send a seen mark
message_tpl.setRecipientId(message.getUserId());
message_tpl.setSenderAction("mark_seen");
platform.getBaseSender().send(message_tpl);


// To send a typing on
message_tpl.setRecipientId(message.getUserId());
message_tpl.setSenderAction("typing_on");
platform.getBaseSender().send(message_tpl);


// To send a typing off
message_tpl.setRecipientId(message.getUserId());
message_tpl.setSenderAction("typing_off");
platform.getBaseSender().send(message_tpl);


// To send text message
message_tpl.setRecipientId(message.getUserId());
message_tpl.setMessageText("Hello World");
message_tpl.setNotificationType("REGULAR");
platform.getBaseSender().send(message_tpl);


// To send an image
message_tpl.setRecipientId(message.getUserId());
message_tpl.setAttachment("image", "http://techslides.com/demos/samples/sample.jpg", false);
message_tpl.setNotificationType("SILENT_PUSH");
platform.getBaseSender().send(message_tpl);


// To send file attachment
message_tpl.setRecipientId(message.getUserId());
message_tpl.setAttachment("file", "http://techslides.com/demos/samples/sample.pdf", false);
message_tpl.setNotificationType("NO_PUSH");
platform.getBaseSender().send(message_tpl);


// To send a video
message_tpl.setRecipientId(message.getUserId());
message_tpl.setAttachment("video", "http://techslides.com/demos/samples/sample.mp4", false);
platform.getBaseSender().send(message_tpl);


// To send an audio
message_tpl.setRecipientId(message.getUserId());
message_tpl.setAttachment("audio", "http://techslides.com/demos/samples/sample.mp3", false);
platform.getBaseSender().send(message_tpl);


// To send a quick text reply with payload buttons
message_tpl.setRecipientId(message.getUserId());
message_tpl.setMessageText("Select a Color!");
message_tpl.setQuickReply("text", "Red", "text_reply_red_click", "");
message_tpl.setQuickReply("text", "Green", "text_reply_green_click", "");
message_tpl.setQuickReply("text", "Black", "text_reply_black_click", "");
platform.getBaseSender().send(message_tpl);


// To send a quick text reply with payload buttons (Button with images)
message_tpl.setRecipientId(message.getUserId());
message_tpl.setMessageText("Select a Color!");
message_tpl.setQuickReply("text", "Red", "text_reply_red_click", "http://static.wixstatic.com/media/f0a6df_9ae4c70963244e16ba0d89d021407335.png");
message_tpl.setQuickReply("text", "Green", "text_reply_green_click", "http://static.wixstatic.com/media/f0a6df_9ae4c70963244e16ba0d89d021407335.png");
message_tpl.setQuickReply("text", "Black", "text_reply_black_click", "http://static.wixstatic.com/media/f0a6df_9ae4c70963244e16ba0d89d021407335.png");
platform.getBaseSender().send(message_tpl);


// To send location reply
message_tpl.setRecipientId(message.getUserId());
message_tpl.setMessageText("Please share your location!");
message_tpl.setQuickReply("location", "", "", "");
platform.getBaseSender().send(message_tpl);
```

Please note that to respond to custom payloads, Please do the following:

```java
// To get the payload value, Use this code
//   String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
// as shown before

if( quick_reply_payload.equals("text_reply_red_click") ){

    message_tpl.setRecipientId(message.getUserId());
    message_tpl.setMessageText("Red Clicked");
    platform.getBaseSender().send(message_tpl);

}else if( quick_reply_payload.equals("text_reply_green_click") ){

    message_tpl.setRecipientId(message.getUserId());
    message_tpl.setMessageText("Green Clicked");
    platform.getBaseSender().send(message_tpl);

}else if( quick_reply_payload.equals("text_reply_black_click") ){

    message_tpl.setRecipientId(message.getUserId());
    message_tpl.setMessageText("Black Clicked");
    platform.getBaseSender().send(message_tpl);

}
```

### Sending Button Message

Let's create an empty message first and fill it with the required data. We can get a new message container from Bot Platform Instance:

```java
ButtonTemplate button_message_tpl = platform.getBaseSender().getButtonTemplate();

// Let's start to fill the required data here
// ..
```

Here's some of the usage cases:

```java
// get the message.getUserId() from the message received


// To send a web url button
button_message_tpl.setRecipientId(message.getUserId());
button_message_tpl.setMessageText("Click Below!");
button_message_tpl.setButton("web_url", "Take the Hat Quiz", "https://m.me/petershats?ref=take_quiz", "");
platform.getBaseSender().send(button_message_tpl);


// To send a postback button
button_message_tpl.setRecipientId(message.getUserId());
button_message_tpl.setMessageText("Click Below!");
button_message_tpl.setButton("postback", "Bookmark Item", "", "DEVELOPER_DEFINED_PAYLOAD");
platform.getBaseSender().send(button_message_tpl);


// To send a phone number button
button_message_tpl.setRecipientId(message.getUserId());
button_message_tpl.setMessageText("Click Below!");
button_message_tpl.setButton("phone_number", "Call Representative", "", "+15105551234");
platform.getBaseSender().send(button_message_tpl);


// To send account link button
button_message_tpl.setRecipientId(message.getUserId());
button_message_tpl.setMessageText("Click Below!");
button_message_tpl.setButton("account_link", "", "https://www.example.com/authorize", "");
platform.getBaseSender().send(button_message_tpl);


// To send account unlink button
button_message_tpl.setRecipientId(message.getUserId());
button_message_tpl.setMessageText("Click Below!");
button_message_tpl.setButton("account_unlink", "", "", "");
platform.getBaseSender().send(button_message_tpl);
```

### Sending List Message

Let's create an empty list message first and fill it with the required data. We can get a new list message container from Bot Platform Instance:

```java
ListTemplate list_message_tpl = platform.getBaseSender().getListTemplate();

// Let's start to fill the required data here
// ..
```

### Sending Generic Message

Let's create an empty generic message first and fill it with the required data. We can get a new generic message container from Bot Platform Instance:

```java
GenericTemplate generic_message_tpl = platform.getBaseSender().getGenericTemplate();

// Let's start to fill the required data here
// ..
```

### Sending Receipt Message

Let's create an empty receipt message first and fill it with the required data. We can get a new receipt message container from Bot Platform Instance:

```java
ReceiptTemplate receipt_message_tpl = platform.getBaseSender().getReceiptTemplate();

// Let's start to fill the required data here
// ..
```

Here's some of the usage cases:

```java
// get the message.getUserId() from the message received

// To send a receipt message
receipt_message_tpl.setRecipientId(message.getUserId());
receipt_message_tpl.setRecipientName("Stephane Crozatier");
receipt_message_tpl.setOrderNumber("12345678902");
receipt_message_tpl.setCurrency("USD");
receipt_message_tpl.setPaymentMethod("Visa 2345");
receipt_message_tpl.setOrderUrl("http://petersapparel.parseapp.com/order?order_id=123456");
receipt_message_tpl.setTimestamp("1428444852");
receipt_message_tpl.setElement("Classic White T-Shirt", "100% Soft and Luxurious Cotton", "2", "50", "USD", "https://image.spreadshirtmedia.com/image-server/v1/products/1001491830/views/1,width=800,height=800,appearanceId=2,version=1473664654/black-rap-nation-t-shirt-men-s-premium-t-shirt.png");
receipt_message_tpl.setElement("Classic Gray T-Shirt", "100% Soft and Luxurious Cotton", "2", "50", "USD", "https://static1.squarespace.com/static/57a088e05016e13b82b0beac/t/584fe89720099e4b5211c624/1481631899763/darts-is-my-religion-ally-pally-is-my-church-t-shirt-maenner-maenner-t-shirt.png");
receipt_message_tpl.setAddress("1 Hacker Way", "", "Menlo Park", "94025", "CA", "US");
receipt_message_tpl.setSummary("75.00", "4.95", "6.19", "56.14");
receipt_message_tpl.setAdjustment("New Customer Discount", "20");
receipt_message_tpl.setAdjustment("$10 Off Coupon", "10");
platform.getBaseSender().send(receipt_message_tpl);
```


Misc
====

Tutorials & Examples
--------------------

> For almost all supported features you can take a look at [`examples/`](https://github.com/Clivern/Racter/tree/master/examples) folder for working examples.

Also check the following tutorials:

1. [Building Your Messenger Chat Bot with Racter & SparkJava Framework.](http://clivern.com/how-to-create-a-facebook-messenger-bot-with-java/)


Changelog
---------
Version 1.0.2:
```
Some Issues Fixed.
PlayFramework Integration.
```

Version 1.0.1:
```
Major enhancements in documentations.
Some classes refactored.
```

Version 1.0.0:
```
Initial Release
```

Acknowledgements
----------------

© 2017, Clivern. Released under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

**Racter** is authored and maintained by [@clivern](http://github.com/clivern).