package `in`.mroyek.gardupln.activity.history.beban

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.GarduActivity
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_history_beban.*

class DetailHistoryBebanActivity : AppCompatActivity() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var adapter: FirestoreRecyclerAdapter<HistoryBebanResponse, BebanHistoryHolder>
    lateinit var context: Context
    lateinit var gardu: String
    lateinit var tanggal: String
    lateinit var waktu: String
    private var getKondisi: String? = ""
    private var getCuaca: String? = ""
    lateinit var idgardu: String
    private var isClicked: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history_beban)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        init()
        bulkText += "*Realisasi Beban $gardu* \n"
        bulkText += "Tanggal ${tanggal.replace("-", "/")}, "
        bulkText += "Pukul $waktu \n \n"
//        loadData()
        getDataHistory(idgardu)
        getKondisi(idgardu)
        getCuaca(idgardu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.copy, menu)
        return true
    }

    @Suppress("UNREACHABLE_CODE")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.copy -> {
                if (!isClicked){
                    isClicked = true
                    copyTextToClipboard()
                    Toast.makeText(applicationContext, "TERSALIN", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun copyTextToClipboard() {
        bulkText += "Sistem dan Jaringan kondisi *$getKondisi*, cuaca *$getCuaca*"
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("Laporan", bulkText)
        clipboardManager.setPrimaryClip(clipData)
    }

    @SuppressLint("SetTextI18n")
    private fun getCuaca(idgardu: String) {
        val docRef = db.collection("Gardu").document(idgardu).collection("Laporin").document("$tanggal $waktu")
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val doc: DocumentSnapshot? = it.result
                getCuaca = doc?.get("cuaca").toString()
                tv_lapor_cuaca.text = "Cuaca : $getCuaca"
            }
        }

/*        val resposeQuery = FirestoreRecyclerOptions.Builder<HistoryBebanResponse>()
                .setQuery(query, HistoryBebanResponse::class.java)
                .build()*/
    }

    private fun getKondisi(idgardu: String) {
        val docRef = db.collection("Gardu").document(idgardu).collection("Laporin").document("$tanggal $waktu")
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val doc: DocumentSnapshot? = it.result
                getKondisi = doc?.get("kondisi").toString()
                tv_lapor_kondisi.text = "Kondisi : $getKondisi"
            }
        }
    }


    private fun getDataHistory(idgardu: String) {
        val query = db.collection("Gardu").document(idgardu).collection("Laporin").document("$tanggal $waktu").collection("Laporr")
        val resposeQuery = FirestoreRecyclerOptions.Builder<HistoryBebanResponse>()
                .setQuery(query, HistoryBebanResponse::class.java)
                .build()
        adapter = object : FirestoreRecyclerAdapter<HistoryBebanResponse, BebanHistoryHolder>(resposeQuery) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BebanHistoryHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_detail_beban, parent, false)
                context = parent.context
                return BebanHistoryHolder(view)
            }

            override fun onBindViewHolder(p0: BebanHistoryHolder, p1: Int, p2: HistoryBebanResponse) {
                if (!p2.namabay.equals("null")) {
                    p0.bindData(p2, context)
                }
//                tv_lapor_cuaca.text = p2.cuaca
            }
        }
        adapter.notifyDataSetChanged()
        rv_historyDetailBeban.adapter = adapter
        rv_historyDetailBeban.viewTreeObserver.addOnGlobalLayoutListener { scrollToEnd() }
    }

    private fun scrollToEnd() = (adapter.itemCount - 1).takeIf { it > 0 }?.let(rv_historyDetailBeban::smoothScrollToPosition)

    private fun init() {
        val intent = intent.extras
        gardu = intent?.get("gardu").toString()
        tanggal = intent?.get("tanggal").toString()
        waktu = intent?.get("waktu").toString()
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_historyDetailBeban.layoutManager = linearLayoutManager
        item_detail_history_gardu.text = gardu
        item_detail_history_tanggal.text = tanggal
        item_detail_history_jam.text = waktu
    }

    inner class BebanHistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val tanggal: TextView = view.findViewById(R.id.item_detail_history_tanggal)
        val waktu: TextView = view.findViewById(R.id.item_detail_history_jam)*/
        val namabay: TextView = view.findViewById(R.id.tv_item_title_laporan)
        val u: TextView = view.findViewById(R.id.tv_item_U)
        var ihv: TextView = view.findViewById(R.id.tv_item_I_HV)
        val ilv: TextView = view.findViewById(R.id.tv_item_I_LV)
        val p: TextView = view.findViewById(R.id.tv_item_P)
        val q: TextView = view.findViewById(R.id.tv_item_Q)
        val beban: TextView = view.findViewById(R.id.tv_item_Beban)
        val inhv: TextView = view.findViewById(R.id.tv_item_In_HV)
        var inlv: TextView = view.findViewById(R.id.tv_item_In_LV)
        val kondisi = tv_lapor_kondisi.text.toString()
        val cuaca = tv_lapor_cuaca.text.toString()
        //        var rvItemLaporanHistory: RecyclerView = view.findViewById(R.id.rv_item_laporanbeban_history)
        fun bindData(response: HistoryBebanResponse, context: Context) {
            copyText(response, kondisi, cuaca)
            namabay.text = response.namabay
            u.text = response.u
            ihv.text = response.ihv
            ilv.text = response.ilv
            p.text = response.p
            q.text = response.q
            beban.text = response.beban
            inhv.text = response.inhv
            inlv.text = response.inlv
//            cuaca.text = tv_lapor_cuaca.text
            Log.d("CUACA", "mbuh ki ${response.cuaca}")
            Log.d("CUACA", "${response.namabay}")
            /*tanggal.text = response.tanggal
            waktu.text = response.waktu*/

//            rvItemLaporanHistory = response.laporan

            Log.d("TESS", "LAPORAN = ${response}")

            /* if (response.laporan != null) {
                 loadItem(context, response.laporan)
 //                Log.d("TESS", "LAPORAN = ${response.laporan}")
             }*/
        }

        /* private fun loadItem(context: Context, laporan: List<Lapor>?) {
             lateinit var adapter: ItemLaporanHistoryAdapter
             val layoutmanager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
             rvItemLaporanHistory.layoutManager = layoutmanager

             adapter = ItemLaporanHistoryAdapter(laporan)
             rvItemLaporanHistory.adapter = adapter
             adapter.notifyDataSetChanged()
         }*/
    }

    var bulkText: String = ""

    private fun copyText(response: HistoryBebanResponse, kondisi: String, cuaca: String) {
        bulkText += "*▶ ${response.namabay}*\n"
        bulkText += "U = ${response.u} KV\n"
        bulkText += "I = ${response.ihv}${response.ilv} A\n"
        bulkText += "P = ${response.p} MW\n"
        bulkText += "Q = ${response.q} MVar\n"
        bulkText += "_Beban = ${response.beban} %_\n"
        bulkText += "In = ${response.inhv}${response.inlv} A\n \n"
//        bulkText += "$getCuaca \n"
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
