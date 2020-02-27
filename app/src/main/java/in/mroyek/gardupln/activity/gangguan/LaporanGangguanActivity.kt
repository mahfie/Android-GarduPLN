package `in`.mroyek.gardupln.activity.gangguan

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.GarduActivity
import `in`.mroyek.gardupln.activity.history.gangguan.HistoryGangguanActivity
import `in`.mroyek.gardupln.activity.inspeksi1.Trafo1Activity
import `in`.mroyek.gardupln.activity.inspeksi1.Transmisi1Activity
import `in`.mroyek.gardupln.key
import `in`.mroyek.gardupln.model.BayResponse
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_inspeksi1.*

class LaporanGangguanActivity : AppCompatActivity() {
    private var adapter: FirestoreRecyclerAdapter<BayResponse, Holder>? = null
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog
    lateinit var idgardu: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspeksi1)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        init()
        showList(idgardu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    @Suppress("UNREACHABLE_CODE")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ab_history -> startActivity(Intent(applicationContext, HistoryGangguanActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showList(idgardu: String) {
        progressDialog.setTitle("loading...")
        progressDialog.show()
        val query: Query = db!!.collection("Gardu").document(idgardu).collection("Bay")
        val response = FirestoreRecyclerOptions.Builder<BayResponse>()
                .setQuery(query, BayResponse::class.java)
                .build()
        adapter = object : FirestoreRecyclerAdapter<BayResponse, Holder>(response) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_bay, parent, false)
                return Holder(view)
            }

            override fun onBindViewHolder(holder: Holder, position: Int, bayResponse: BayResponse) {
                progressDialog.dismiss()
                holder.binding(holder.itemView.context, bayResponse)
                val namabay = bayResponse.namabay.toString()
                holder.itemView.setOnClickListener {
                    startActivity(Intent(applicationContext, LaporinGangguanActivity2::class.java).putExtra("namabay", namabay))                }
            }
        }
        adapter!!.notifyDataSetChanged()
        rv_bay_inspeksi1.adapter = adapter
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvListBay: TextView = view.findViewById(R.id.tv_list_bay)
        fun binding(context: Context, response: BayResponse) {
            if (response.namabay == response.namabay) {
                tvListBay.text = response.namabay
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        progressDialog = ProgressDialog(this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_bay_inspeksi1.layoutManager = linearLayoutManager
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
