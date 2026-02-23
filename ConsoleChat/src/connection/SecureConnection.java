package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SecureConnection {

    private Properties properties = new Properties();
    private static int port;

    public SecureConnection() throws IOException {
        this.properties.load(new FileInputStream("./application.properties"));
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
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // SSL Server Socket
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
        return serverSocket;
    }
}
