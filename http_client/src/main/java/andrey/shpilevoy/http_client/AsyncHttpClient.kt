package andrey.shpilevoy.http_client

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.net.URLEncoder
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import okhttp3.RequestBody
import java.io.File
import android.webkit.MimeTypeMap




class AsyncHttpClient {

    private val headers = HashMap<String, String>()

    fun putHeader(header: String, value: String): AsyncHttpClient {
        headers[header] = value
        return this
    }

    private fun setHeaders(builder: Request.Builder){
        if(headers.size > 0)
            headers.forEach{(header, value) -> builder.header(header, value)}
    }

    private fun getParams(params: RequestParams): String {
        val result = StringBuilder()
        var first = true

        params.map.forEach { (key, value) ->
            if (first)
                first = false
            else
                result.append("&")

            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode("$value", "UTF-8"))
        }

        return result.toString()
    }

    private fun setVerifier(httpClient: OkHttpClient.Builder){
        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
        trustStore.load(null, null)

        val trust = object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return arrayOf<X509Certificate>() //java.security.cert.X509Certificate[]{}
            }
        }

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trust), java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        httpClient.sslSocketFactory(sslSocketFactory, trust)
        httpClient.hostnameVerifier { _, _ -> true; };
    }

    private fun getClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        setVerifier(httpClient)

        return httpClient.build()

    }

    fun get(url: String, params: RequestParams, httpResponse: HttpResponse) {

        Thread(Runnable {

            val client = getClient()

            val builder = Request.Builder()

            setHeaders(builder)

            builder.url("$url?${getParams(params)}")
            val request = builder.build()

            try {
                val response = client.newCall(request).execute()

                val statusCode = response.code()
                val responseContent = response.body()?.string()

                if (statusCode in 1..300) {
                    httpResponse.onSuccess(statusCode, responseContent)
                } else {
                    httpResponse.onFailure(statusCode, responseContent, null)
                }


            } catch (e: Exception) {
                e.printStackTrace()

                httpResponse.onFailure(0, null, e)
            }

        }).start()

    }

    fun post(url: String, params: RequestParams, httpResponse: HttpResponse) {

        Thread(Runnable {

            val client = getClient()

            val builder = Request.Builder()

            setHeaders(builder)

            builder.url(url)

            if(params.json){

                val type = MediaType.parse("application/json; charset=utf-8")
                val json = Gson().toJson(params.map)

                val body = RequestBody.create(type, json)
                builder.post(body)

            }else{
                //val type = MediaType.parse("text/html; charset=UTF-8")
                val formBody = MultipartBody.Builder()
                formBody.setType(MultipartBody.FORM)
                params.map.forEach{
                        (key, value) ->

                    if(value is File) {

                        val name = value.name

                        val extension = MimeTypeMap.getFileExtensionFromUrl(value.absolutePath)

                        Log.d("File", " 1 $extension")

                        if (extension != null) {
                            val typeString = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                            Log.d("File", " 2 $typeString")

                            if(typeString != null) {
                                val type = MediaType.parse(typeString)
                                formBody.addFormDataPart(key, name, RequestBody.create(type, value))

                                Log.d("File", " 3 $name")
                            }
                        }

                    }else
                        formBody.addFormDataPart(key, "$value")
                }

                builder.post(formBody.build())
            }

            val request = builder.build()

            try {
                val response = client.newCall(request).execute()

                val statusCode = response.code()
                val responseContent = response.body()?.string()

                if (statusCode in 1..300) {
                    httpResponse.onSuccess(statusCode, responseContent)
                } else {
                    httpResponse.onFailure(statusCode, responseContent, null)
                }


            } catch (e: Exception) {
                e.printStackTrace()

                httpResponse.onFailure(0, null, e)
            }

        }).start()

    }

    fun put(url: String) {

    }

    fun patch(url: String) {

    }

    fun delete(url: String) {

    }

}