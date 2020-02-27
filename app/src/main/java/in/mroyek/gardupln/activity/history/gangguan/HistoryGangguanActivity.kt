package `in`.mroyek.gardupln.activity.history.gangguan

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.GarduActivity
import `in`.mroyek.gardupln.activity.gangguan.LaporanGangguanResponses
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_history_gangguan.*

class HistoryGangguanActivity : AppCompatActivity() {
    lateinit var adapter: FirestoreRecyclerAdapter<LaporanGangguanResponses, GangguanHolder>
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var idgardu: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_gangguan)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        init()
        val query: Query = db.collection("Gardu").document(idgardu).collection("Gangguan")
        val gangguanResponse = FirestoreRecyclerOptions.Builder<LaporanGangguanResponses>()
                .setQuery(query, LaporanGangguanResponses::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<LaporanGangguanResponses, GangguanHolder>(gangguanResponse) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GangguanHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_gangguan, parent, false)
                return GangguanHolder(view)
            }

            override fun onBindViewHolder(p0: GangguanHolder, p1: Int, p2: LaporanGangguanResponses) {
                p0.bindData(p0.itemView.context, p2)
                val tgl = p2.tanggal.toString()
                val jam = p2.waktu.toString()
                p0.itemView.setOnClickListener {
                    startActivity(Intent(applicationContext, DetailGangguanHistory::class.java).putExtra("tanggal", tgl).putExtra("waktu", jam))
                }

            }
        }
        adapter.notifyDataSetChanged()
        rv_history_gangguan_lapor.adapter = adapter
    }
    private fun init() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_history_gangguan_lapor.layoutManager = linearLayoutManager
    }

    inner class GangguanHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTanggal: TextView = view.findViewById(R.id.tv_gangguan_item_history_tanggal)
        private val tvJam: TextView = view.findViewById(R.id.tv_gangguan_item_history_jam)
        val btnHapus: ImageButton = view.findViewById(R.id.btnHapus_historyGangguan)
        fun bindData(context: Context, response: LaporanGangguanResponses) {
            if (response.tanggal == response.tanggal) {
                tvTanggal.text = response.tanggal
                tvJam.text = response.waktu
            }
            btnHapus.setOnClickListener {
                val mAlerttDialog = AlertDialog.Builder(this@HistoryGangguanActivity)
                mAlerttDialog.setTitle("Hapus History")
                mAlerttDialog.setMessage("Apakah anda yakin?")
                mAlerttDialog.setPositiveButton("YA"){dialog, id ->
                    hapusDokumen(response.tanggal, response.waktu)
                    Toast.makeText(this@HistoryGangguanActivity, "OKE", Toast.LENGTH_SHORT).show()
                }
                mAlerttDialog.setNegativeButton("TIDAK"){dialog, id ->
                    dialog.dismiss()
                }
                mAlerttDialog.show()
            }
        }

        private fun hapusDokumen(tanggal: String?, jam: String?) {
            db.collection("Gardu").document(idgardu).collection("Gangguan").document("$tanggal $jam").delete()
        }
      /*  var deleted = 0
        val koleksi: Query = db.collection("Gardu").document(idgardu).collection("Laporin").document("$tanggal $waktu").collection("Laporr")
        koleksi.get()
        .addOnCompleteListener {
            for (doc in it.result?.documents!!) {
                doc.reference.delete()
                ++deleted
            }
        }
        db.collection("Gardu").document(idgardu).collection("Laporin").document("$tanggal $waktu").delete()*/
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
