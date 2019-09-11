package andrey.shpilevoy.sample

import andrey.shpilevoy.preferences.Preferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Preferences.init(this)

        Preferences.save("a", "1")

    }

}
