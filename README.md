# Mailgun Java Client Library

Mailgun is an awesome email API.  It supports SMTP and API sending, although this library only currently supports the API.

You can sign up for an account at https://mailgun.com/signup

Requirements
------------

Java 1.7 and later

Installation
------------

I am currently working on getting this project added to maven central, in the meantime, you can add it to your mvn/gradle project using https://jitpack.io

gradle:
    
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
    ...
    dependencies {
        compile 'com.github.blairmotchan:mailgun:1.0'
    }
    
    
maven:

    <dependencies>
        ...
        <dependency>
            <groupId>com.github.blairmotchan</groupId>
            <artifactId>mailgun</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
    ...
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

Usage
-----

This library is essentially a simple to use wrapper around the mailgun API.

Currently, the library only supports sending emails via the API, but more features will be coming soon.

Example
-------

```java

import com.samurai.mailgun.builder.Message;
import com.samurai.mailgun.data.MessageResponse;
import com.samurai.mailgun.service.MailgunService;
import com.samurai.mailgun.service.impl.MailgunServiceImpl;

public class MailgunApplication {

    public static void main(String[] args) {
        MailgunService service = new MailgunServiceImpl("YOUR_API_KEY", "YOUR BASE URL");
        
        Message message = new Message()
            .addTo("to_recipient@emailaddress.com")
            .setFrom("from@email@address.com")
            .setSubject("Email Test")
            .setText("my email message to you")
            .setHtml("<html><body>My html email message to you</body></html>");
            
        MessageResponse response = service.sendEmail(message);
        
        System.out.println("ID: " + response.getId() + ", MESSAGE: " + response.getMessage());
    }

}

```

Validation
----------

At this point, the library does not provide validation of inputs.  I decided that it makes more sense for users of the library to provide the type of validation that their application needs.

The library will throw a MailgunException in the event of any errors or error response from the API.

