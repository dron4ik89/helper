package andrey.shpilevoy.sample

import andrey.shpilevoy.http_client.AsyncHttpClient
import andrey.shpilevoy.http_client.Header
import andrey.shpilevoy.http_client.HttpResponse
import andrey.shpilevoy.http_client.RequestParams
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import android.content.res.AssetManager
import android.database.Cursor
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.coroutines.experimental.asReference


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }


        val paramsGet = RequestParams()
        paramsGet.put("a", "1")
        paramsGet.put("b", "2")
        paramsGet.put("c", "3")

        AsyncHttpClient()
            //.setSSLContext(trustCert())
            .putHeader("auth", "token")
            //.get("https://apimobile.welcash.kiev.ua/v1/user/", params, object : HttpResponse{
            .get("http://prog.net.ua/test.php", paramsGet, object : HttpResponse {
                override fun onSuccess(statusCode: Int, responseString: String?) {
                    Log.d("AsyncHttpClient", "onSuccess $statusCode")
                    Log.d("AsyncHttpClient", "onSuccess $responseString")

                    runOnUiThread {
                        get.text = responseString
                    }
                }

                override fun onFailure(statusCode: Int, responseString: String?, throwable: Throwable?) {
                    Log.d("AsyncHttpClient", "onFailure $statusCode")
                    Log.d("AsyncHttpClient", "onFailure $responseString")

                    runOnUiThread {
                        get.text = responseString
                    }
                }

            })


        val paramsPost = RequestParams()
        paramsPost.put("a", "1")
        paramsPost.put("b", "2")
        paramsPost.put("c", "3")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA)

            val cursor = contentResolver.query(uri, null, null, null, null)

            if (cursor != null) {
                val column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                cursor.moveToNext()

                paramsPost.put("file", cursor.getString(column_index_data))

            }




        }



        //paramsPost.put("file2", File("//android_asset/barcode.gif"))
        //paramsPost.put("file", File(Uri.parse("assets:////barcode.gif").toString()))
        paramsPost.json = false

        AsyncHttpClient()
            .putHeader("auth", "token")
            .post("http://prog.net.ua/test.php", paramsPost, object : HttpResponse {
                override fun onSuccess(statusCode: Int, responseString: String?) {
                    Log.d("AsyncHttpClient", "onSuccess $statusCode")
                    Log.d("AsyncHttpClient", "onSuccess $responseString")

                    runOnUiThread {
                        post.text = responseString
                    }
                }

                override fun onFailure(statusCode: Int, responseString: String?, throwable: Throwable?) {
                    Log.d("AsyncHttpClient", "onFailure $statusCode")
                    Log.d("AsyncHttpClient", "onFailure $responseString")

                    runOnUiThread {
                        post.text = responseString
                    }
                }

            })


        val paramsPostJson = RequestParams()
        paramsPostJson.put("a", "1")
        paramsPostJson.put("b", "2")
        paramsPostJson.put("c", "3")
        paramsPostJson.json = true

        AsyncHttpClient()

            .putHeader("auth", "token")
            .post("http://prog.net.ua/test.php", paramsPostJson, object : HttpResponse {
                override fun onSuccess(statusCode: Int, responseString: String?) {
                    Log.d("AsyncHttpClient", "onSuccess $statusCode")
                    Log.d("AsyncHttpClient", "onSuccess $responseString")

                    runOnUiThread {
                        post_json.text = responseString
                    }
                }

                override fun onFailure(statusCode: Int, responseString: String?, throwable: Throwable?) {
                    Log.d("AsyncHttpClient", "onFailure $statusCode")
                    Log.d("AsyncHttpClient", "onFailure $responseString")

                    runOnUiThread {
                        post_json.text = responseString
                    }
                }

            })


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            recreate()
    }

}
