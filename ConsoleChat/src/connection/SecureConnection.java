package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SecureConnection {

    private Properties properties = new Properties();
    private static int port;
    private static String PROTOCOL = "TLS";
    private static String HOST = "localhost";

    public SecureConnection() throws IOException {
        // this.properties.load(new FileInputStream("./application.properties"));
        this.properties.load(SecureConnection.class.getClassLoader().getResourceAsStream("application.properties"));
        SecureConnection.port = Integer.parseInt(this.properties.getProperty("port"));
    }

    public KeyManagerFactory keyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException{
        char[] password = this.properties.getProperty("ks.password").toCharArray();
        String keyStoreFile = this.properties.getProperty("keystore");

        // KeyStore Process
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreFile),password);

        // KeyManagerFactory 
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore,password);
        return keyManagerFactory;
    }

    public SSLServerSocket sslSocketConnection(KeyManagerFactory keyManagerFactory) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        // SSL Context
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // SSL Server Socket
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
        return serverSocket;
    }

    public SSLSocket byPassSecurityCheckForClient() throws NoSuchAlgorithmException, KeyManagementException, UnknownHostException, IOException{
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(null, new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() { return null; }
                @Override
                public void checkClientTrusted(X509Certificate[] c, String a){}
                @Override
                public void checkServerTrusted(X509Certificate[] c, String a){}
            }
        }, new SecureRandom());
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket(HOST,port);
        socket.startHandshake();
        return socket;
    }
}
