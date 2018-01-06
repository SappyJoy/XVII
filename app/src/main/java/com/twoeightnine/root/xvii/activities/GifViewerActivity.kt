package com.twoeightnine.root.xvii.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.twoeightnine.root.xvii.R
import com.twoeightnine.root.xvii.fragments.WebFragment
import com.twoeightnine.root.xvii.utils.*
import com.twoeightnine.root.xvii.views.LoaderView
import java.io.File

/**
 * now gif viewer uses WebView due to vk jackass hq
 * i hate them
 * je les haïs
 */
class GifViewerActivity: AppCompatActivity() {

    @BindView(R.id.ivGif)
    lateinit var ivGif: ImageView
    @BindView(R.id.tvTitle)
    lateinit var tvTitle: TextView
    @BindView(R.id.loader)
    lateinit var loader: LoaderView
    @BindView(R.id.btnSaveToDocs)
    lateinit var btnSaveToDocs: Button
    @BindView(R.id.btnDownload)
    lateinit var btnDownload: Button
    @BindView(R.id.rlTop)
    lateinit var rlTop: RelativeLayout
    @BindView(R.id.llBottom)
    lateinit var rlBottom: LinearLayout

    companion object {
        val URL = "url"
        val ACCESS_KEY = "accessKey"
        val TTILE = "title"
        val PREVIEW = "preview"
        val OWNER_ID = "ownerId"
        val DOC_ID = "docId"

        fun showGif(context: Context, ownerId: Int, docId: Int, url: String, accessKey: String, title: String, preview: String) {
            val intent = Intent(context, GifViewerActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(ACCESS_KEY, accessKey)
            intent.putExtra(TTILE, title)
            intent.putExtra(PREVIEW, preview)
            intent.putExtra(OWNER_ID, ownerId)
            intent.putExtra(DOC_ID, docId)
            context.startActivity(intent)
        }
    }

    private lateinit var url: String
    private lateinit var accessKey: String
    private lateinit var title: String
    private lateinit var preview: String

    private var ownerId = 0
    private var docId = 0

    private lateinit var name: String

    private var removeAfter = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_viewer)
        ButterKnife.bind(this)
        if (intent.extras != null) {
            url = intent.extras.getString(URL)
            accessKey = intent.extras.getString(ACCESS_KEY)
            title = intent.extras.getString(TTILE)
            preview = intent.extras.getString(PREVIEW)
            ownerId = intent.extras.getInt(OWNER_ID)
            docId = intent.extras.getInt(DOC_ID)
        } else {
            finish()
        }
        tvTitle.text = title
        name = getNameFromTitle(title)
        name = "file://${Environment.getExternalStoragePublicDirectory(DownloadFileAsyncTask.DEFAULT_PATH)}/$name"
        ivGif.setOnClickListener {
            visibilitor(rlTop)
            visibilitor(rlBottom)
        }
        btnSaveToDocs.setOnClickListener {
            ApiUtils().saveDoc(this, ownerId, docId, accessKey)
        }
        btnDownload.setOnClickListener {
            downloadFile(this, url, name, "gif", { showCommon(this, getString(R.string.doenloaded, getString(R.string.doc))) })
            removeAfter = false
        }

        supportFragmentManager
                .beginTransaction()
                .add(R.id.flContent, WebFragment.newInstance(url))
                .commit()
    }

    private fun getNameFromTitle(title: String) =
        if (title.endsWith(".gif", true)) {
            getNameFromUrl(title)
        } else {
            "${getNameFromUrl(title)}.gif"
        }

    private fun visibilitor(vg: ViewGroup) {
        vg.visibility = if (vg.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (removeAfter) {
            File(name).delete()
        }
    }
}