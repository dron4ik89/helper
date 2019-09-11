package andrey.shpilevoy.http_client

import android.preference.PreferenceActivity

interface HttpResponse {

    fun onSuccess(statusCode: Int, responseString: String?)

    fun onFailure(statusCode: Int, responseString: String?, throwable: Throwable?)

}