package kiyosuke.com.csvparser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kiyosuke.com.water.core.Water

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // load from assets
        Water.with(this)
                .load("dummy.csv")
                .parse(Dummy::class)
                .forEach { Log.d("MainActivity", "dummy: $it") }

        // load from raw
        Water.with(this)
                .load(R.raw.dummy)
                .parse(Dummy::class)
                .forEach { Log.d("MainActivity", "dummy: $it") }

    }

}
