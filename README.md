代码调用

```java
    File file = new File("C:\\Users\\JimYip\\Desktop\\微信图片_20200314173215.jpg");
    FileInputStream fis = new FileInputStream(file);
    FileInputStream fis2 = new FileInputStream(file);
    String id = MimeUtils.generateContentId("admin@sogou.wiki", "aa.pdf");
    MimeMessage message = JMail.builder()
            .from("admin@sogou.wiki")
            .to("admin@sogou.wiki")
            .cc("admin@sogou.wiki", "test@sogou.wiki", "admin@sogou.wiki", "test@qq.com")
            .bcc("叶沐 <562612654@qq.com>")
            .addBcc(InternetAddress.parse("叶镇武 <562612654@qq.com>"))
            .addBcc(new InternetAddress("562612654@sogou.wiki", "叶镇武", "utf-8"))
            .subject("hello")
            .plainText("plainText中文")
            .text("plainText中文")
            .htmlText("<span class=\"spnEditorSign\">textHtml中文<img src=\"cid:" + id + "\" alt=\"\"><span style=\"font-family:SimSun;\"></span></span>")
            .html("<span class=\"spnEditorSign\">textHtml中文<img src=\"cid:" + id + "\" alt=\"\"><span style=\"font-family:SimSun;\"></span></span>")
            .addAttachment("微信图片_20200314173215.jpg", fis2)
            .addAttachment(new FileDataSource(file))
            .addInline(id, file.getName(), fis)
            .recipientFilter(addresses -> addresses.stream()
                    .filter(address -> !((InternetAddress) address).getAddress().contains("admin"))
                    .collect(Collectors.toSet()))
//                .checkRecipients(true)
            .build();
    FileOutputStream fos = new FileOutputStream("C:\\Users\\JimYip\\Desktop\\stream\\test.eml");
    message.writeTo(fos);
```