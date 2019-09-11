package andrey.shpilevoy.http_client

import android.annotation.SuppressLint
import org.apache.http.conn.ssl.SSLSocketFactory

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class MySSLSocketFactory(truststore: KeyStore) : SSLSocketFactory(truststore){

    var sslContext = SSLContext.getInstance("TLS")

    init{
        val tm = object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        }

        sslContext.init(null, arrayOf<TrustManager>(tm), null)
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(socket: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return sslContext.socketFactory.createSocket(socket, host, port, autoClose)
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return sslContext.socketFactory.createSocket()
    }

}