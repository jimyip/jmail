package wiki.sogou.jmail.builder;


import wiki.sogou.jmail.util.IOUtils;
import wiki.sogou.jmail.util.MimeUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

/**
 * @author JimYip
 */
public class MimeMessageBuilder {
    private String from;
    private String replyTo;
    private Set<Address> toSet = new LinkedHashSet<>();
    private Set<Address> ccSet = new LinkedHashSet<>();
    private Set<Address> bccSet = new LinkedHashSet<>();
    private Date sendDate;
    private String subject;

    private InternetHeaders headers = new InternetHeaders();

    private Properties properties = new Properties();
    private Session session;
    private String charset;

    private List<MimeBodyPart> attachmentParts = new ArrayList<>();
    private List<MimeBodyPart> inlineParts = new ArrayList<>();
    private Flags flags;
    private boolean isSetFlag = false;
    private String sender;

    private String textPartTransferEncoding;
    private String htmlText;
    private String plainText;
    private boolean checkRecipients;

    private Function<Set<Address>, Set<Address>> recipientFilter;

    public MimeMessageBuilder() {

    }

    public MimeMessageBuilder(Session session) {
        this.session = session;
    }


    public MimeMessageBuilder textPartTransferEncoding(String textPartTransferEncoding) {
        this.textPartTransferEncoding = textPartTransferEncoding;
        return this;
    }

    public MimeMessageBuilder checkRecipients(boolean checkRecipients) {
        this.checkRecipients = checkRecipients;
        return this;
    }

    public MimeMessageBuilder sender(String sender) {
        Objects.requireNonNull(sender, "sender");
        this.sender = sender;
        return this;
    }

    public MimeMessageBuilder flag(Flags.Flag flag, boolean isSetFlag) {
        Objects.requireNonNull(flag, "flag");
        if (this.flags == null) {
            this.flags = new Flags();
        }
        this.flags.add(flag);
        this.isSetFlag = isSetFlag;
        return this;
    }

    public MimeMessageBuilder flags(Flags flags) {
        Objects.requireNonNull(flags, "flags");
        this.flags = flags;
        return this;
    }

    public MimeMessageBuilder charset(String charset) {
        this.charset = charset;
        return this;
    }

    @Deprecated
    public MimeMessageBuilder username(String username) {
        Objects.requireNonNull(username, "username");
        this.properties.put("mail.smtp.from", username);
        this.properties.put("mail.smtp.auth", "true");
        return this;
    }

    @Deprecated
    public MimeMessageBuilder port(int port) {
        this.properties.put("mail.smtp.port", port);
        return this;
    }

    @Deprecated
    public MimeMessageBuilder host(String host) {
        Objects.requireNonNull(host, "host");
        this.properties.put("mail.smtp.host", host);
        return this;
    }

    public MimeMessageBuilder properties(Properties properties) {
        Objects.requireNonNull(properties, "properties");
        this.properties = properties;
        return this;
    }

    public MimeMessageBuilder createSession(Properties properties, String username, String password) {
        Objects.requireNonNull(properties, "properties");
        this.properties = properties;
        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return this;
    }

    @Deprecated
    public MimeMessageBuilder protocol(String protocol) {
        Objects.requireNonNull(protocol, "protocol");
        this.properties.put("mail.transport.protocol", protocol);
        return this;
    }


    public MimeMessageBuilder from(String from) {
        Objects.requireNonNull(from, "from");
        this.from = from;
        return this;
    }


    public MimeMessageBuilder replyTo(String replyTo) {
        Objects.requireNonNull(replyTo, "replyTo");
        this.replyTo = replyTo;
        return this;
    }

    public MimeMessageBuilder to(String... to) {
        Objects.requireNonNull(to, "to");
        if (this.toSet.size() != 0) {
            this.toSet.clear();
        }
        return addTo(to);
    }

    public MimeMessageBuilder to(Address... to) {
        Objects.requireNonNull(to, "to");
        if (this.toSet.size() != 0) {
            this.toSet.clear();
        }
        this.toSet.addAll(Arrays.asList(to));
        return this;
    }

    public MimeMessageBuilder to(Set<Address> toSet) {
        Objects.requireNonNull(toSet, "toSet");
        this.toSet = toSet;
        return this;
    }

    public MimeMessageBuilder addTo(String... to) {
        Objects.requireNonNull(to, "to");
        parseAddress(Message.RecipientType.TO, this.toSet, to);
        return this;
    }

    public MimeMessageBuilder addTo(Address... to) {
        Objects.requireNonNull(to, "to");
        this.toSet.addAll(Arrays.asList(to));
        return this;
    }

    public MimeMessageBuilder cc(String... cc) {
        Objects.requireNonNull(cc, "cc");
        if (this.ccSet.size() != 0) {
            this.ccSet.clear();
        }
        addCc(cc);
        return this;
    }

    public MimeMessageBuilder cc(Set<Address> ccSet) {
        Objects.requireNonNull(ccSet, "ccSet");
        this.ccSet = ccSet;
        return this;
    }


    public MimeMessageBuilder bcc(String... bcc) {
        Objects.requireNonNull(bcc, "bcc");
        if (this.bccSet.size() != 0) {
            this.bccSet.clear();
        }
        addBcc(bcc);
        return this;
    }

    public MimeMessageBuilder bcc(Address... bcc) {
        Objects.requireNonNull(bcc, "bcc");
        if (this.bccSet.size() != 0) {
            this.bccSet.clear();
        }
        this.bccSet.addAll(Arrays.asList(bcc));
        return this;
    }

    public MimeMessageBuilder bcc(Set<Address> bccSet) {
        Objects.requireNonNull(bccSet, "bccSet");
        this.bccSet = bccSet;
        return this;
    }

    public MimeMessageBuilder addCc(String... cc) {
        Objects.requireNonNull(cc, "cc");
        parseAddress(Message.RecipientType.CC, this.ccSet, cc);
        return this;
    }

    public MimeMessageBuilder addCc(Address... cc) {
        Objects.requireNonNull(cc, "cc");
        this.ccSet.addAll(Arrays.asList(cc));
        return this;
    }


    public MimeMessageBuilder addBcc(String... bcc) {
        Objects.requireNonNull(bcc, "bcc");
        parseAddress(Message.RecipientType.BCC, this.bccSet, bcc);
        return this;
    }


    public MimeMessageBuilder addBcc(Address... bcc) {
        Objects.requireNonNull(bcc, "bcc");
        this.bccSet.addAll(Arrays.asList(bcc));
        return this;
    }


    public MimeMessageBuilder sendDate(Date sendDate) {
        Objects.requireNonNull(sendDate, "sendDate");
        this.sendDate = sendDate;
        return this;
    }


    public MimeMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    @Deprecated
    public MimeMessageBuilder htmlText(String htmlText) {
        return html(htmlText);
    }

    public MimeMessageBuilder html(String html) {
        Objects.requireNonNull(html, "html");
        this.htmlText = html;
        return this;
    }

    public MimeMessageBuilder text(String text) {
        Objects.requireNonNull(text, "text");
        this.plainText = text;
        return this;
    }

    @Deprecated
    public MimeMessageBuilder plainText(String plainText) {
        return text(plainText);
    }

    public MimeMessageBuilder session(Session session) {
        this.session = session;
        return this;
    }

    public MimeMessageBuilder addAttachment(DataSource dataSource) {
        MimeBodyPart part = new MimeBodyPart();
        prepareAttachPart(part, null, dataSource, false);
        this.attachmentParts.add(part);
        return this;
    }

    public MimeMessageBuilder addInline(String id, String name, byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes");
        MimeBodyPart part = new MimeBodyPart();
        ByteArrayDataSource dataSource = getByteArrayDataSource(name, bytes);
        prepareAttachPart(part, id, dataSource, true);
        this.inlineParts.add(part);
        return this;
    }

    private ByteArrayDataSource getByteArrayDataSource(String name, byte[] bytes) {
        String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(name);
        ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes, contentType);
        dataSource.setName(name);
        return dataSource;
    }


    public MimeMessageBuilder addInline(String id, String name, InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream");
        try {
            byte[] bytes = new byte[inputStream.available()];
            IOUtils.readFully(inputStream, bytes);
            return addInline(id, name, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MimeMessageBuilder addInline(String id, DataSource dataSource) {
        Objects.requireNonNull(dataSource, "dataSource");
        MimeBodyPart part = new MimeBodyPart();
        prepareAttachPart(part, id, dataSource, true);
        this.inlineParts.add(part);
        return this;
    }

    public MimeMessageBuilder addInline(String id, File file) {
        Objects.requireNonNull(file, "file");
        return addInline(id, null, file);
    }

    public MimeMessageBuilder addInline(String id, String fileName, File file) {
        Objects.requireNonNull(file, "file");
        checkFileAccess(file);
        MimeBodyPart part = new MimeBodyPart();
        FileDataSource dataSource = getFileDataSource(fileName, file, part);
        prepareAttachPart(part, id, dataSource, true);
        this.inlineParts.add(part);
        return this;
    }

    private void checkFileAccess(File file) {
        if (!file.exists()) {
            throw new RuntimeException("file ``" + file.getAbsolutePath() + "`` doesn't exist");
        }
        if (!file.isFile()) {
            throw new RuntimeException("file ``" + file.getAbsolutePath() + "`` isn't a normal file");
        }
        if (!file.canRead()) {
            throw new RuntimeException("file ``" + file.getAbsolutePath() + "`` isn't readable");
        }
    }

    public MimeMessageBuilder addHeader(String name, String value) {
        this.headers.addHeader(name, value);
        return this;
    }

    public MimeMessageBuilder header(String name, String value) {
        this.headers.setHeader(name, value);
        return this;
    }

    public MimeMessageBuilder headers(InternetHeaders headers) {
        this.headers = headers;
        return this;
    }

    public MimeMessageBuilder addAttachment(String name, byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes");
        MimeBodyPart part = new MimeBodyPart();
        ByteArrayDataSource dataSource = getByteArrayDataSource(name, bytes);
        prepareAttachPart(part, null, dataSource, false);
        this.attachmentParts.add(part);
        return this;
    }

    public MimeMessageBuilder addAttachment(String name, InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream");
        try {
            byte[] bytes = new byte[inputStream.available()];
            IOUtils.readFully(inputStream, bytes);
            return addAttachment(name, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MimeMessageBuilder addAttachment(File file) {
        return addAttachment(null, file);
    }

    public MimeMessageBuilder addAttachment(String fileName, File file) {
        Objects.requireNonNull(file, "file");
        checkFileAccess(file);
        MimeBodyPart part = new MimeBodyPart();
        FileDataSource dataSource = getFileDataSource(fileName, file, part);
        prepareAttachPart(part, null, dataSource, false);
        this.attachmentParts.add(part);
        return this;
    }

    private FileDataSource getFileDataSource(String fileName, File file, MimeBodyPart part) {
        FileDataSource dataSource = new FileDataSource(file);
        try {
            part.setFileName(null == fileName ?
                    MimeUtility.encodeText(dataSource.getName()) : MimeUtility.encodeText(fileName));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }


    /**
     * build an email
     */
    public MimeMessage build() {
        Objects.requireNonNull(this.from, "from");
        checkRecipients();
        MimeMessage message = new MimeMessage(getSession());
        try {
            message.setFrom(this.parseAddress(this.from));
            if (this.sender != null) {
                message.setSender(this.parseAddress(this.sender));
            }
            addRecipients(message);
            message.setSentDate(sendDate == null ? new Date() : sendDate);
            if (this.flags != null) {
                message.setFlags(this.flags, this.isSetFlag);
            }
            addSubject(message);
            setHeaders(message);
            addBody(message);
            if (this.replyTo != null) {
                message.setReplyTo(new Address[]{this.parseAddress(this.replyTo)});
            }
            message.saveChanges();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;

    }

    private void addBody(MimeMessage message) throws MessagingException {
        if (attachmentParts.size() == 0 && inlineParts.size() == 0) {
            buildTextOnly(message);
        } else {
            MimeMultipart bodyPart = new MimeMultipart("mixed");
            //for the body
            bodyPart.addBodyPart(new MimeBodyPart());
            for (MimeBodyPart part : this.attachmentParts) {
                bodyPart.addBodyPart(part);
            }
            if (bodyPart.getCount() > 1) {
                if (!buildText((MimePart) bodyPart.getBodyPart(0))) {
                    bodyPart.removeBodyPart(0);
                }
                setContent(message, bodyPart);
            } else {
                buildText(message);
            }
        }
    }

    private void addSubject(MimeMessage message) throws MessagingException {
        if (this.subject != null) {
            MimeUtils.setSubject(message, this.subject, this.charset);
        }
    }

    private void addRecipients(MimeMessage msg) throws MessagingException {
        addRecipients(msg, toSet, Message.RecipientType.TO);
        addRecipients(msg, ccSet, Message.RecipientType.CC);
        addRecipients(msg, bccSet, Message.RecipientType.BCC);
    }

    private void addRecipients(MimeMessage msg, Set<Address> recipientList,
                               Message.RecipientType recipientType) throws MessagingException {
        if (recipientList.isEmpty()) {
            return;
        }
        final Collection<Address> recipients =
                recipientFilter != null ? recipientFilter.apply(recipientList) : recipientList;
        msg.setRecipients(recipientType, recipients.toArray(new Address[0]));
    }

    private void checkRecipients() {
        if (checkRecipients && this.toSet.isEmpty() && this.ccSet.isEmpty() && this.bccSet.isEmpty()) {
            throw new IllegalStateException("All recipients are empty.");
        }
    }

    private boolean buildText(MimePart part) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(new MimeBodyPart());

        for (MimeBodyPart p : this.inlineParts) {
            multipart.addBodyPart(p);
        }

        if (multipart.getCount() > 1) {
            if (!buildTextOnly((MimePart) multipart.getBodyPart(0))) {
                multipart.removeBodyPart(0);
            }
            setContent(part, multipart);
            return true;
        } else {
            return buildTextOnly(part);
        }
    }

    @SuppressWarnings("unchecked")
    private void setHeaders(MimeMessage message) throws MessagingException {
        Enumeration<Header> allHeaders = this.headers.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = allHeaders.nextElement();
            MimeUtils.setHeaderFold(message, header.getName(), header.getValue(), true);
        }
    }

    private Session getSession() {
        if (this.session == null) {
            this.session = Session.getInstance(this.properties);
        }
        return session;
    }

    public MimeMessageBuilder recipientFilter(Function<Set<Address>, Set<Address>> recipientFilter) {
        this.recipientFilter = recipientFilter;
        return this;
    }

    private boolean buildTextOnly(MimePart part) throws MessagingException {
        if (htmlText != null && plainText != null) {
            MimeMultipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(newPlainPart(plainText));
            multipart.addBodyPart(newHtmlPart(htmlText));
            setContent(part, multipart);
        } else if (htmlText != null) {
            setHtml(part, htmlText);
        } else if (plainText != null) {
            setPlain(part, plainText);
        } else {
            return false;
        }
        return true;
    }

    private void setContent(MimePart part, MimeMultipart multipart) throws MessagingException {
        part.setContent(multipart, multipart.getContentType());
    }

    private void setPlain(MimePart part, String text) throws MessagingException {
        part.setText(text, charset);
        if (textPartTransferEncoding != null) {
            part.setHeader("Content-Transfer-Encoding", textPartTransferEncoding);
        }
    }

    public MimeBodyPart newPlainPart(String text) throws MessagingException {
        MimeBodyPart result = new MimeBodyPart();
        setPlain(result, text);
        return result;
    }

    private MimeBodyPart newHtmlPart(String text) throws MessagingException {
        MimeBodyPart result = new MimeBodyPart();
        setHtml(result, text);
        return result;
    }

    public MimeBodyPart newBodyPart(DataSource ds) throws MessagingException {
        MimeBodyPart result = new MimeBodyPart();
        result.setDataHandler(new DataHandler(ds));
        return result;
    }


    private void setHtml(MimePart part, String text) throws MessagingException {
        part.setText(text, charset, "html");
        if (textPartTransferEncoding != null) {
            part.setHeader("Content-Transfer-Encoding", textPartTransferEncoding);
        }
    }


    private void prepareAttachPart(MimeBodyPart part, String id, DataSource dataSource, boolean isInline) {
        try {
            String name = MimeUtility.encodeText(dataSource.getName(), charset, null);
            String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(name);
            MimeUtils.setHeaderFold(part, "Content-Type", contentType + "; name=\"" + name + "\"");
            MimeUtils.setHeaderFold(part, "Content-Disposition", (isInline ? Part.INLINE : Part.ATTACHMENT)
                    + "; filename=\"" + name + "\"");
            part.setDataHandler(new DataHandler(dataSource));
            part.setHeader("Content-Transfer-Encoding", "base64");
            if (id != null) {
                part.setContentID("<" + id + ">");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding in string ``" + charset + "``", e);
        } catch (Exception e) {
            throw new RuntimeException("Prepare attach part error.", e);
        }
    }


    private void parseAddress(Message.RecipientType type, Set<Address> addressSet, String[] addresses) {
        if (addresses != null && addresses.length > 0) {
            Arrays.stream(addresses).forEach(address -> {
                try {
                    addressSet.add(parseAddress(address));
                } catch (MessagingException e) {
                    throw new IllegalArgumentException("The '" + type
                            + "' addresses has illegal address in string ``" + address + "''", e);
                }
            });
        }
    }

    private Address parseAddress(String address) throws MessagingException {
        InternetAddress[] parsed = InternetAddress.parse(address);
        if (parsed.length != 1) {
            throw new AddressException("Illegal address", address);
        } else {
            InternetAddress raw = parsed[0];

            try {
                return this.charset != null ? new InternetAddress(raw.getAddress(),
                        raw.getPersonal(), this.charset) : raw;
            } catch (UnsupportedEncodingException var5) {
                throw new MessagingException("Failed to parse embedded personal name to correct encoding", var5);
            }
        }
    }

}
