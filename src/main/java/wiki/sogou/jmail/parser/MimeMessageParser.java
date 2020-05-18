package wiki.sogou.jmail.parser;

import com.sun.mail.util.ReadableMime;
import wiki.sogou.jmail.builder.MimeMessageBuilder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author JimYip
 */
public class MimeMessageParser {
    private final MimeMessage mimeMessage;

    private String plainContent;

    private String htmlContent;

    private final List<DataSource> attachmentList;

    private final Map<String, DataSource> cidMap;

    private boolean isMultiPart;

    public static MimeMessageParser of(MimeMessage message) throws Exception {
        MimeMessageParser parser = new MimeMessageParser(message);
        parser.parse(null, message);
        return parser;
    }

    private MimeMessageParser(MimeMessage message) {
        this.attachmentList = new ArrayList<>();
        this.cidMap = new LinkedHashMap<>();
        this.mimeMessage = message;
        this.isMultiPart = false;
    }

    public Set<Address> getTo() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.TO);
        return recipients != null ? new LinkedHashSet<>(Arrays.asList(recipients)) : Collections.emptySet();
    }

    public Set<Address> getCc() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.CC);
        return recipients != null ? new LinkedHashSet<>(Arrays.asList(recipients)) : Collections.emptySet();
    }

    public Set<Address> getBcc() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.BCC);
        return recipients != null ? new LinkedHashSet<>(Arrays.asList(recipients)) : Collections.emptySet();
    }

    public Optional<String> getFrom() throws Exception {
        Address[] addresses = this.mimeMessage.getFrom();
        if (addresses == null || addresses.length == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(((InternetAddress) addresses[0]).getAddress());
    }

    public Optional<String> getSender() throws Exception {
        Address addresses = this.mimeMessage.getSender();
        if (addresses == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(((InternetAddress) addresses).getAddress());
    }

    public Optional<String> getReplyTo() throws Exception {
        Address[] addresses = this.mimeMessage.getReplyTo();
        if (addresses == null || addresses.length == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(((InternetAddress) addresses[0]).getAddress());
    }

    public Optional<String> getSubject() throws Exception {
        return Optional.ofNullable(this.mimeMessage.getSubject());
    }

    protected void parse(Multipart parent, MimePart part)
            throws MessagingException, IOException {
        if (isMimeType(part, "text/plain") && this.plainContent == null
                && !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            this.plainContent = (String) part.getContent();
        } else {
            if (isMimeType(part, "text/html") && this.htmlContent == null
                    && !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                this.htmlContent = (String) part.getContent();
            } else {
                if (isMimeType(part, "multipart/*")) {
                    this.isMultiPart = true;
                    Multipart mp = (Multipart) part.getContent();
                    int count = mp.getCount();

                    // iterate over all MimeBodyPart

                    for (int i = 0; i < count; i++) {
                        parse(mp, (MimeBodyPart) mp.getBodyPart(i));
                    }
                } else {
                    Optional<String> cid = stripContentId(part.getContentID());
                    DataSource ds = createDataSource(parent, part);
                    cid.ifPresent(cidValue -> this.cidMap.put(cidValue, ds));
                    this.attachmentList.add(ds);
                }
            }
        }
    }

    public int getSize() throws MessagingException {
        return this.mimeMessage.getSize();
    }

    public InputStream getMimeStream() throws MessagingException {
        if (this.mimeMessage instanceof ReadableMime) {
            return ((ReadableMime) this.mimeMessage).getMimeStream();
        }
        throw new UnsupportedOperationException("Not ReadableMime");
    }

    private Optional<String> stripContentId(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return Optional.of(contentId.trim().replaceAll("[<>]", ""));
    }


    private boolean isMimeType(MimePart part, String mimeType)
            throws MessagingException {
        // Do not use part.isMimeType(String) as it is broken for MimeBodyPart
        // and does not really check the actual content type.

        try {
            ContentType ct = new ContentType(part.getDataHandler().getContentType());
            return ct.match(mimeType);
        } catch (ParseException ex) {
            return part.getContentType().equalsIgnoreCase(mimeType);
        }
    }


    protected DataSource createDataSource(Multipart parent, MimePart part)
            throws MessagingException, IOException {
        DataHandler dataHandler = part.getDataHandler();
        DataSource dataSource = dataHandler.getDataSource();
        String contentType = getBaseMimeType(dataSource.getContentType());
        byte[] content = this.getContent(dataSource.getInputStream());
        ByteArrayDataSource result = new ByteArrayDataSource(content, contentType);
        String dataSourceName = getDataSourceName(part, dataSource);

        result.setName(dataSourceName);
        return result;
    }


    public MimeMessage getMimeMessage() {
        return this.mimeMessage;
    }


    public boolean isMultipart() {
        return this.isMultiPart;
    }


    public Optional<String> getPlainContent() {
        return Optional.ofNullable(this.plainContent);
    }

    public Optional<String> getText() {
        return Optional.ofNullable(this.plainContent);
    }


    public List<DataSource> getAttachmentList() {
        return this.attachmentList;
    }


    public Collection<String> getContentIds() {
        return Collections.unmodifiableSet(cidMap.keySet());
    }


    public Optional<String> getHtmlContent() {
        return Optional.ofNullable(this.htmlContent);
    }

    public Optional<String> getHtml() {
        return Optional.ofNullable(this.htmlContent);
    }


    public boolean hasPlainContent() {
        return this.plainContent != null;
    }


    public boolean hasHtmlContent() {
        return this.htmlContent != null;
    }


    public boolean hasAttachments() {
        return this.attachmentList.size() > 0;
    }


    public Optional<DataSource> findAttachmentByName(String name) {
        Objects.requireNonNull(name, "name");
        return getAttachmentList()
                .stream()
                .filter(dataSource -> name.equalsIgnoreCase(dataSource.getName()))
                .findFirst();
    }


    public Optional<DataSource> findAttachmentByCid(String cid) {
        return Optional.ofNullable(this.cidMap.get(cid));
    }


    protected String getDataSourceName(Part part, DataSource dataSource)
            throws MessagingException, UnsupportedEncodingException {
        String result = dataSource.getName();

        if (result == null || result.length() == 0) {
            result = part.getFileName();
        }

        if (result != null && result.length() > 0) {
            result = MimeUtility.decodeText(result);
        } else {
            result = null;
        }

        return result;
    }


    private byte[] getContent(InputStream is)
            throws IOException {
        int ch;
        byte[] result;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             BufferedOutputStream osWriter = new BufferedOutputStream(os);
             BufferedInputStream isReader = new BufferedInputStream(is)) {
            while ((ch = isReader.read()) != -1) {
                osWriter.write(ch);
            }
            osWriter.flush();
            result = os.toByteArray();
        }
        return result;
    }


    private String getBaseMimeType(String fullMimeType) {
        int pos = fullMimeType.indexOf(';');
        if (pos >= 0) {
            return fullMimeType.substring(0, pos);
        }
        return fullMimeType;
    }
}
