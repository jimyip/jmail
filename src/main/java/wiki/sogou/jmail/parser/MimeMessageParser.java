package wiki.sogou.jmail.parser;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.*;

/**
 * @author JimYip
 */
public class MimeMessageParser {
    private MimeMessage mimeMessage;

    private String plainContent;

    private String htmlContent;

    private List<DataSource> attachmentList;

    private Map<String, DataSource> cidMap;

    private boolean isMultiPart;

    public static MimeMessageParser of(MimeMessage message) {
        return new MimeMessageParser(message);
    }

    private MimeMessageParser(MimeMessage message) {
        this.attachmentList = new ArrayList<>();
        this.cidMap = new LinkedHashMap<>();
        this.mimeMessage = message;
        this.isMultiPart = false;
    }

    public MimeMessageParser parse() throws Exception {
        this.parse(null, this.mimeMessage);
        return this;
    }

    public List<Address> getTo() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.TO);
        return recipients != null ? Arrays.asList(recipients) : new ArrayList<>();
    }

    public List<Address> getCc() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.CC);
        return recipients != null ? Arrays.asList(recipients) : new ArrayList<>();
    }

    public List<Address> getBcc() throws Exception {
        Address[] recipients = this.mimeMessage.getRecipients(Message.RecipientType.BCC);
        return recipients != null ? Arrays.asList(recipients) : new ArrayList<>();
    }

    public String getFrom() throws Exception {
        Address[] addresses = this.mimeMessage.getFrom();
        if (addresses == null || addresses.length == 0) {
            return null;
        }
        return ((InternetAddress) addresses[0]).getAddress();
    }

    public String getSender() throws Exception {
        Address addresses = this.mimeMessage.getSender();
        if (addresses == null) {
            return null;
        }
        return ((InternetAddress) addresses).getAddress();
    }

    public String getReplyTo() throws Exception {
        Address[] addresses = this.mimeMessage.getReplyTo();
        if (addresses == null || addresses.length == 0) {
            return null;
        }
        return ((InternetAddress) addresses[0]).getAddress();
    }

    public String getSubject() throws Exception {
        return this.mimeMessage.getSubject();
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
                    String cid = stripContentId(part.getContentID());
                    DataSource ds = createDataSource(parent, part);
                    if (cid != null) {
                        this.cidMap.put(cid, ds);
                    }
                    this.attachmentList.add(ds);
                }
            }
        }
    }

    private String stripContentId(String contentId) {
        if (contentId == null) {
            return null;
        }
        return contentId.trim().replaceAll("[<>]", "");
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


    public String getPlainContent() {
        return this.plainContent;
    }

    public String getText() {
        return this.plainContent;
    }


    public List<DataSource> getAttachmentList() {
        return this.attachmentList;
    }


    public Collection<String> getContentIds() {
        return Collections.unmodifiableSet(cidMap.keySet());
    }


    public String getHtmlContent() {
        return this.htmlContent;
    }

    public String getHtml() {
        return this.htmlContent;
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
