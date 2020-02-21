package `in`.mroyek.gardupln.activity.fuad

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.GarduActivity
import `in`.mroyek.gardupln.activity.beban.LaporanBebanResponses
import `in`.mroyek.gardupln.activity.history.beban.HistoryBebanActivity
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_laporan_beban.tv_date
import kotlinx.android.synthetic.main.activity_laporan_beban2.*
import kotlinx.android.synthetic.main.item_transmisi.view.*
import java.text.SimpleDateFormat
import java.util.*


class LaporanBebanActivity2 : AppCompatActivity(), View.OnClickListener {
    //    lateinit var adapterTransmisi: FirestoreRecyclerAdapter<LaporanBebanResponses, TransmisiHolder>
    lateinit var adapterTransmisi: LaporanBebanAdapter
    //    val modelist: MutableList<LaporanBebanResponses>? = mutableListOf()
    private val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    lateinit var idgardu: String
    private var getGardu: String? = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_beban2)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        init()
        getGardu(idgardu)
        setTanggal()
        getdata(idgardu)
        btnUpload.setOnClickListener(this)
    }

    private fun getGardu(idgardu: String) {
        val docRef = db!!.collection("Gardu").document(idgardu)
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val doc: DocumentSnapshot? = it.result
                getGardu = doc?.get("gardu").toString()
                nama_gardu.text = "$getGardu"
            }
        }
    }

    private fun setTanggal() {
        val myCalendar = Calendar.getInstance()
        date = DatePickerDialog.OnDateSetListener {datePicker, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            tv_date.text = sdf.format(myCalendar.time)
        }
        tv_date.setOnClickListener {
            DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    @Suppress("UNREACHABLE_CODE")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ab_history -> startActivity(Intent(applicationContext, HistoryBebanActivity::class.java).putExtra("gardu", getGardu))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getdata(idgardu: String) {
        val query = db!!.collection("Gardu").document(idgardu).collection("Bay")
                .whereGreaterThanOrEqualTo("namabay", "PENGHANTAR")
        query.whereLessThanOrEqualTo("namabay", "TRAFO")
                .whereEqualTo("inhv", "inhv")
                .whereEqualTo("inlv", "inlv")
        val queryResponse = FirestoreRecyclerOptions.Builder<LaporanBebanResponses>()
                .setQuery(query, LaporanBebanResponses::class.java)
                .build()
        adapterTransmisi = LaporanBebanAdapter(queryResponse)

        /*adapterTransmisi = object : FirestoreRecyclerAdapter<LaporanBebanResponses, TransmisiHolder>(queryResponse) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransmisiHolder {
                val viewTransmisi = LayoutInflater.from(parent.context).inflate(R.layout.item_transmisi, parent, false)
                return TransmisiHolder(viewTransmisi)
            }

            override fun onBindViewHolder(transmisiHolder: TransmisiHolder, position: Int, response: LaporanBebanResponses) {
                transmisiHolder.bindData(response)
            }

            var holderHashMap: HashMap<Int, RecyclerView.ViewHolder> = HashMap()

            fun getHolder():HashMap<Int, RecyclerView.ViewHolder>{
                return holderHashMap
            }

            override fun onViewDetachedFromWindow(holder: TransmisiHolder) {
                holderHashMap.put(holder.adapterPosition, holder)
                super.onViewDetachedFromWindow(holder)
            }

            override fun onViewAttachedToWindow(holder: TransmisiHolder) {
                holderHashMap.remove(holder.adapterPosition)
                super.onViewAttachedToWindow(holder)
            }
        }*/

        adapterTransmisi.notifyDataSetChanged()
        rvBebanTransmisi.adapter = adapterTransmisi
    }

    private fun upload() {
        val itemCount = rvBebanTransmisi.adapter?.itemCount
        val childCountsRV = 0.rangeTo(itemCount!!)
        childCountsRV.forEach {

            //COBA
/*
            var namabay = ""
            var u = ""
            var i = ""
            var p = ""
            var q = ""
            var `in` = ""
            var beban = ""

            rvBebanTransmisi.post(Runnable {
                val childHolder = rvBebanTransmisi.layoutManager?.findViewByPosition(it)
                namabay = childHolder?.tv_beban_transmisi?.text.toString().trim()
                u = childHolder?.et_transmisi_U?.text.toString().trim()
                i = childHolder?.et_transmisi_I?.text.toString().trim()
                p = childHolder?.et_transmisi_P?.text.toString().trim()
                q = childHolder?.et_transmisi_Q?.text.toString().trim()
                `in` = childHolder?.et_transmisi_In?.text.toString().trim()
                beban = childHolder?.et_transmisi_beban?.text.toString().trim()
            })
*/

            //END COBA

//            val childHolder = rvBebanTransmisi.findViewHolderForLayoutPosition(it)
//            val childHolder = rvBebanTransmisi.findViewHolderForAdapterPosition(it)
//            val childHolder = rvBebanTransmisi.layoutManager?.findViewByPosition(it)
            var childHolder = rvBebanTransmisi.findViewHolderForAdapterPosition(it)
            if (childHolder == null) {
                childHolder = adapterTransmisi.holderHashMap[it]
            }

//            val namabay = childHolder?.itemView?.tv_beban_transmisi?.text.toString().trim()
            val namabay = childHolder?.itemView?.tv_beban_transmisi?.text.toString().trim()
            val u = childHolder?.itemView?.et_transmisi_U?.text.toString().trim()
            val ihv = childHolder?.itemView?.et_transmisi_I_HV?.text.toString().trim()
            val ilv = childHolder?.itemView?.et_transmisi_I_LV?.text.toString().trim()
            val p = childHolder?.itemView?.et_transmisi_P?.text.toString().trim()
            val q = childHolder?.itemView?.et_transmisi_Q?.text.toString().trim()
            val beban = childHolder?.itemView?.et_transmisi_Beban?.text.toString().trim()
            val inhv = childHolder?.itemView?.et_transmisi_In_HV?.text.toString().trim()
            val inlv = childHolder?.itemView?.et_transmisi_In_LV?.text.toString().trim()

            val cheid = rg_time_beban.checkedRadioButtonId
            val valueRg = findViewById<RadioButton>(cheid)
            val date = tv_date.text.toString().trim()
            val kondisi = et_lapor_kondisi.text.toString().trim()
            val cuaca = et_lapor_cuaca.text.toString().trim()
            /*val doc = hashMapOf(
                    "namabay" to namabay,
                    "tanggal" to tv_date.text.toString().trim(),
                    "waktu" to valueRg.text.toString().trim(),
                    listArraynya to arrayListOf(u, i, p, q, beban, `in`)
            )*/
            val doc = hashMapOf(
                    "namabay" to namabay,
                    "u" to u,
                    "ihv" to ihv,
                    "ilv" to ilv,
                    "p" to p,
                    "q" to q,
                    "beban" to beban,
                    "inhv" to inhv,
                    "inlv" to inlv
            )
            val docpor = hashMapOf(
                    "tanggal" to tv_date.text.toString().trim(),
                    "waktu" to valueRg.text.toString().trim(),
                    "kondisi" to kondisi,
                    "cuaca" to cuaca,
                    "timeStamp" to System.currentTimeMillis().toString().trim()
            )
            /*val docCuaca = hashMapOf(
                    "cuaca" to cuaca
            )*/
            var row = it
            db!!.collection("Gardu").document(idgardu).collection("Laporin").document("$date ${valueRg.text}").set(docpor)
            Log.d("CUK Namabay", "Nama = $namabay , Index ke $it")
            if (namabay != "null") {
                db.collection("Gardu").document(idgardu).collection("Laporin").document("$date ${valueRg.text}").collection("Laporr").document(namabay.trim()).set(doc, SetOptions.merge())
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext, "UPLOAD BEBAN", Toast.LENGTH_SHORT).show()
//                            Log.d("CUK", "Row ke $row")
                        }.addOnFailureListener {
                            Log.d("CUK ERROR", "ERROR ke $row dan $it")
//                            db.collection("Laporin").document(date).collection("Laporr").document(namabay.trim()).set(doc, SetOptions.merge())
                        }
            } else {
//                    db.collection("Laporin").document(date).collection("Laporr").document(namabay.trim()).set(doc, SetOptions.merge())
                Log.d("CUK dikiro Null", "ERROR ke $row")
            }
//            db.collection("Laporin").document("$date ${valueRg.text}").collection("Laporr").document("cuaca").set(docCuaca, SetOptions.merge())
//            return@forEach

            /*val documentReference = db!!.collection("Laporin").document(date)
            documentReference.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
//                            Toast.makeText(applicationContext, "Document = ISI", Toast.LENGTH_SHORT).show()
                            if (cekduplikat2(document)) {
                                Toast.makeText(applicationContext, "Tanggal Duplikat", Toast.LENGTH_SHORT).show()
                                db.collection("Laporin").document(date)
                                        .update(listArraynya, FieldValue.delete())
                                        .addOnCompleteListener { Toast.makeText(applicationContext, "updated", Toast.LENGTH_SHORT).show() }
                                        .addOnFailureListener {
                                            Toast.makeText(applicationContext, "gagal ini", Toast.LENGTH_SHORT).show()
                                        }
                                db.collection("Laporin").document(date)
                                        .set(doc, SetOptions.merge())
                                        .addOnCompleteListener { Toast.makeText(applicationContext, "okeeh hehe", Toast.LENGTH_SHORT).show() }
                                        .addOnFailureListener {
                                            Toast.makeText(applicationContext, "gagal ini 2", Toast.LENGTH_SHORT).show()
                                        }

                                Log.d("MASUK", "MASUK DUPLIKAT")
                            } else {
                                db.collection("Laporin").document(date)
                                        .update(listArraynya, FieldValue.arrayUnion(u, i, p, q, beban, `in`))
                                        .addOnCompleteListener { Toast.makeText(applicationContext, "updateddddd", Toast.LENGTH_SHORT).show() }
                                        .addOnFailureListener {
                                            Toast.makeText(applicationContext, "gagal ini", Toast.LENGTH_SHORT).show()
                                        }
                                Toast.makeText(applicationContext, "Tidak ada field", Toast.LENGTH_SHORT).show()
                                Log.d("MASUK", "MASUK TIDAK DUPLIKAT")
                            }
                        } else {
                            Log.d("MASUK", "MASUK TIDAK ADA DOKUMEN $it")

                            db.collection("Laporin").document(date)
                                    .set(doc, SetOptions.merge())
                                    .addOnCompleteListener {
                                        Toast.makeText(applicationContext, "okeeh 2", Toast.LENGTH_SHORT).show()
                                        Log.d("MASUK", "OKED $it")
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(applicationContext, "gagal ini 2", Toast.LENGTH_SHORT).show()
                                    }

                            Toast.makeText(applicationContext, "Document = NULL $it", Toast.LENGTH_SHORT).show()
                        }

                    }*/
        }
    }

    private fun checkduplicat(listarraynya: String): Boolean {
        val count = rvBebanTransmisi.adapter?.itemCount
        var hasil = false

        for (posisine in 0..count!!) {
            val namalist = rvBebanTransmisi.findViewHolderForAdapterPosition(posisine)?.itemView?.findViewById<TextView>(R.id.tv_beban_transmisi)?.text.toString()
            if (listarraynya.contains(namalist)) {
                hasil = true
            }
        }
        return hasil
    }

    fun cekduplikat2(document: DocumentSnapshot): Boolean {
        var hasil = false
        val count = rvBebanTransmisi.adapter?.itemCount
//        count?.minus(1)

//        var string = ""
        for (i in 0..count!!) {
            val namalist = rvBebanTransmisi.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<TextView>(R.id.tv_beban_transmisi)?.text.toString()
//            string += "FIELD $namalist = ${document.contains("laporan $namalist")} | "
//            Toast.makeText(applicationContext, "Duplikasi", Toast.LENGTH_SHORT).show()
            if (document.contains("laporan $namalist")) {
//                Toast.makeText(applicationContext, "Duplikasi", Toast.LENGTH_SHORT).show()
                hasil = true
            }
        }

//        Toast.makeText(applicationContext, "DAFTAR = $string", Toast.LENGTH_SHORT).show()

        return hasil
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        val layoutTransmisi = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBebanTransmisi.layoutManager = layoutTransmisi
    }

    /*class TransmisiHolder(viewTransmisi: View?) : RecyclerView.ViewHolder(viewTransmisi!!) {
        var tvTransmisi: TextView = viewTransmisi!!.findViewById(R.id.tv_beban_transmisi)
        var etIn: TextView = viewTransmisi!!.findViewById(R.id.et_transmisi_In)
        @SuppressLint("ResourceType")
        fun bindData(response: LaporanBebanResponses) {
            tvTransmisi.text = response.namabay.toString()
            etIn.text = response.`in`.toString()
        }
    }*/

    override fun onStart() {
        super.onStart()
        adapterTransmisi.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterTransmisi.stopListening()
    }

    override fun onClick(p0: View?) {
        if (rg_time_beban.checkedRadioButtonId != -1) {
            when (p0?.id) {
                R.id.btnUpload -> {
                    upload()
                }
            }
        } else {
            Toast.makeText(applicationContext, "isi waktunya dulu", Toast.LENGTH_SHORT).show()
        }
    }
}
