### How to use

1. 构建一封简单的邮件。

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("你好，世界！")
        .build();
```

2. 构建一封 html 的邮件。

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .html("<span style=\"color:#E53333;\">你好，世界！</span>")
        .build();
```

3. 构建一封内联邮件（图片嵌入）。

```java
String cid = "123456";
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("你好，世界！")
        .html("<span style=\"color:#E53333;\">你好，世界！</span><img src=\"cid:" + cid + "\" alt=\"\" />")
        .addInline(cid, new File("D:\\hello你好.jpg"))
        .build();
```

4. 添加带有附件的邮件。

```java
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("你好，世界！")
        .html("<span style=\"color:#E53333;\">你好，世界！</span>")
        .addAttachment(new File("D:\\hello你好.txt"))
        .build();
```

5. 创建既有内联图片，又有附件的邮件

```java
String cid = "123456";
MimeMessage message = JMail.builder()
        .from("text@sogou.wiki")
        .to("text2@sogou.wiki")
        .subject("Hello world!")
        .text("你好，世界！")
        .html("<span style=\"color:#E53333;\">你好，世界！</span><img src=\"cid:" + cid + "\" alt=\"\" />")
        .addInline(cid, new File("D:\\hello你好.jpg"))
        .addAttachment(new File("D:\\hello你好.txt"))
        .build();
```

