### How to use

1. To build a simple email.

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("Hello world!")
        .build();
```

2. To build a HTML email.

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .html("<span style=\"color:#E53333;\">Hello world!</span>")
        .build();
```

3. To build a email with Inline（picture inline）。

```java
String cid = "123456";
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("Hello world!！")
        .html("<span style=\"color:#E53333;\">Hello world!</span><img src=\"cid:" + cid + "\" alt=\"\" />")
        .addInline(cid, new File("../jmail/picture.jpg"))
        .build();
```

4. To build an email with attachment.

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("Hello world!")
        .html("<span style=\"color:#E53333;\">Hello world!</span>")
        .addAttachment(new File("../jmail/test.txt"))
        .build();
```

5. To build an email with inline picture and attachment.

```java
String cid = "123456";
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("Hello world!")
        .html("<span style=\"color:#E53333;\">Hello world!</span><img src=\"cid:" + cid + "\" alt=\"\" />")
        .addInline(cid, new File("../jmail/picture.jpg"))
        .addAttachment(new File("../jmail/test.txt"))
        .build();
```

