package org.lsong.launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.lsong.launcher.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // getInstalledApps
        val appList = ArrayList<AppBlock>()

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        var flags = ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        val activities = packageManager.queryIntentActivities(intent, flags)
        for (resolveInfo in activities) {
            if (resolveInfo.activityInfo.packageName == this.packageName)
                continue
            val app = AppBlock(
                resolveInfo.loadLabel(packageManager).toString(),
                resolveInfo.activityInfo.loadIcon(packageManager),
                resolveInfo.activityInfo.packageName
            )
            appList.add(app)
        }
        mainBinding.appList.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        mainBinding.appList.adapter = Adapter(this).also {
            it.passAppList(appList.sortedWith { o1, o2 ->
                o1?.appName?.compareTo(o2?.appName ?: "", true) ?: 0
            })
        }
    }
}
