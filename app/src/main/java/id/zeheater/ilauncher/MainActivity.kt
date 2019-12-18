package id.zeheater.ilauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.zeheater.ilauncher.decoration.ListSpacingDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var pkglistsrc : List<ResolveInfo>? = null
    var pkglist : List<ResolveInfo>? = null
    private lateinit var apprvadapter : AppRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pkglistsrc = this@MainActivity.packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0)

        pkglist = pkglistsrc?.sortedBy { getApplicationName(it.activityInfo.packageName) }
        //pkglist = pkglistsrc?.filter { it.activityInfo.packageName.startsWith("id") }?.sortedBy { getApplicationName(it.activityInfo.packageName) }

        apprvadapter = AppRvAdapter()
        apprv.layoutManager = GridLayoutManager(this@MainActivity, resources.getInteger(R.integer.spancount))
        apprv.addItemDecoration(ListSpacingDecoration(this@MainActivity, R.dimen.spacingdecor))
        apprv.adapter = apprvadapter
        apprv.setHasFixedSize(true)
    }

    fun getApplicationName(packagename: String) : String? {
        return try { packageManager.getApplicationLabel(packageManager.getApplicationInfo(packagename, PackageManager.GET_META_DATA)).toString() } catch (e: PackageManager.NameNotFoundException) { "NoName1" }
    }


    inner class AppRvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val rootview : LinearLayout = view.findViewById(R.id.rootview)
            val appiconview : ImageView = view.findViewById(R.id.appiconview)
            val apptextview : TextView = view.findViewById(R.id.apptextview)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootview = LayoutInflater.from(this@MainActivity).inflate(R.layout.griditem, parent, false)
            return AppViewHolder(rootview)
        }

        override fun getItemCount(): Int {
            return pkglist?.size?:0
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as AppViewHolder
            holder.appiconview.setImageDrawable(pkglist?.get(position)?.loadIcon(packageManager))
            holder.apptextview.text = getApplicationName(pkglist?.get(position)?.activityInfo?.packageName?:"NoName2")
            holder.rootview.setOnClickListener {
                startActivity(packageManager.getLaunchIntentForPackage(pkglist?.get(position)?.activityInfo?.packageName!!))
            }
            //android.util.Log.e(BuildConfig.APPLICATION_ID, pkglist?.get(position)?.activityInfo?.name)
            //android.util.Log.e(BuildConfig.APPLICATION_ID, pkglist?.get(position)?.activityInfo?.packageName)
            //android.util.Log.e(BuildConfig.APPLICATION_ID, pkglist?.get(position)?.activityInfo?.)
        }
    }
}
