package was.http;

public class Constants {

	// HTTP Response Content type
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String CSS_CONTENT_TYPE = "text/css; charset=utf-8";
    public final static String HTML_CONTENT_TYPE = "text/html; charset=utf-8";
    public final static String JS_CONTENT_TYPE = "text/javascript; charset=utf-8";
    public final static String FONT_CONTENT_TYPE = "application/x-font-ttf";
    public final static String ICON_CONTENT_TYPE = "image/x-icon";

   /// HTTP redirection status codes
    public static final int MULTIPLE_CHOICES = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int SEE_OTHER = 303;
    public static final int USE_PROXY = 305;
    public static final int TEMPORARY_REDIRECT = 307;
    
    
    /**
     * Name of the system property containing
     * the tomcat instance installation path
     */
    public static final String CATALINA_BASE_PROP = "catalina.base";

    /**
     * JSSE and OpenSSL protocol names
     */
    public static final String SSL_PROTO_ALL        = "all";
    public static final String SSL_PROTO_TLS        = "TLS";
    public static final String SSL_PROTO_TLSv1_3    = "TLSv1.3";
    public static final String SSL_PROTO_TLSv1_2    = "TLSv1.2";
    public static final String SSL_PROTO_TLSv1_1    = "TLSv1.1";
    // Two different forms for TLS 1.0
    public static final String SSL_PROTO_TLSv1_0    = "TLSv1.0";
    public static final String SSL_PROTO_TLSv1      = "TLSv1";
    public static final String SSL_PROTO_SSLv3      = "SSLv3";
    public static final String SSL_PROTO_SSLv2      = "SSLv2";
    public static final String SSL_PROTO_SSLv2Hello = "SSLv2Hello";
    
    public static final String HTTP_VERSION_1_1 = "HTTP/1.1";
    
    private Constants() {
        // Hide default constructor
    }
}
