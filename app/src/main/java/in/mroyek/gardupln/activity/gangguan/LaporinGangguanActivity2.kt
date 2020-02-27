package `in`.mroyek.gardupln.activity.gangguan

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.GarduActivity
import `in`.mroyek.gardupln.activity.history.gangguan.HistoryGangguanActivity
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_laporin_gangguan2.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class LaporinGangguanActivity2 : AppCompatActivity() {
    lateinit var date: DatePickerDialog.OnDateSetListener
    lateinit var time: TimePickerDialog.OnTimeSetListener
    lateinit var idgardu: String
    lateinit var namabay: String
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private var totaltower: String? = ""
    private var bay: String? = ""
    private var getJarak: String? = ""
    private var getTower: String? = ""
    private var getAwal: String? = ""
    private var getMulai: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporin_gangguan2)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        setDateTime()
        init()
        getFL(idgardu)
        upload(idgardu)
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

    private fun setDateTime() {
        val calendar = Calendar.getInstance()
        date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dateFormat = "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
            tvDate_gangguan.text = simpleDateFormat.format(calendar.time)
        }
        tvDate_gangguan.setOnClickListener {
            DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        time = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, minute)
            val timeFormat = "HH:mm"
            val sdf = SimpleDateFormat(timeFormat, Locale.US)
            tvTime_gangguan.text = sdf.format(calendar.time)
        }
        tvTime_gangguan.setOnClickListener {
            TimePickerDialog(this, time, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getFL(idgardu: String) {
        val docRef = db!!.collection("Gardu").document(idgardu).collection("Bay").document("$namabay")
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val doc: DocumentSnapshot? = it.result
                tower_transmisi.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        if (!tower_transmisi.text.toString().trim().equals("null")){
                        }
                        else{
                            et_gangguan_FL.setText("")
                            no_tower.text = ""
                            fl_transmisi.visibility = View.GONE
                            line.visibility = View.GONE
                            no_tower.visibility = View.GONE
                        }
                    }
                })
                totaltower = doc?.get("tower").toString()
                bay = doc?.get("namabay").toString()
                getJarak = doc?.get("jarak").toString()
                getTower = doc?.get("tower").toString()
                getAwal = doc?.get("awal").toString()
                getMulai = doc?.get("mulai").toString()
                tower_transmisi.setText("$totaltower")
                et_gangguan_FL.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    }

                    override fun afterTextChanged(editable: Editable) {
                        val a: String = et_gangguan_FL.text.toString()
                        val b: String = getJarak.toString()
                        val c: String = getTower.toString()
                        val d: String = getMulai.toString()
                        val e: String = getAwal.toString()

                        if(a.trim().replace("-", "").replace(".", "").isNotEmpty()){
                            if (e.trim().equals("kecil")){
                                val f = kecil(a.toDouble(), b.toDouble(), c.toDouble(), d.toDouble())
                                val g = DecimalFormat("#,##0")
                                val h = g.format(f)
                                no_tower.setText("Tower $h")
                            }
                            else {
                                val f = besar(a.toDouble(), b.toDouble(), c.toDouble(), d.toDouble())
                                val g = DecimalFormat("#,##0")
                                g.roundingMode = RoundingMode.HALF_EVEN
                                val h = g.format(f)
                                no_tower.setText("Tower $h")
                            }
                        }
                    }
                })
            }
        }
    }

    fun kecil(a: Double, b: Double, c: Double, d: Double): Double? {
        return d+(a/b)*c
    }

    fun besar(a: Double, b: Double, c: Double, d: Double): Double? {
        return d-(a/b)*c
    }

    private fun init() {
        val intent = intent.extras
        namabay = intent?.get("namabay").toString()
    }

    private fun upload(idgardu: String) {
        var msg = ""
        btn_lapor_gangguan.setOnClickListener {
            val date = tvDate_gangguan.text.toString().trim()
            val time = tvTime_gangguan.text.toString().trim()
            val countPMT = "R: ${et_counterPMT_R.text} _n S: ${et_counterPMT_S.text} _n T: ${et_counterPMT_T.text} _n "
            val countGangguan = "R: ${et_counterGangguan_R.text} _n S: ${et_counterGangguan_S.text} _n T: ${et_counterGangguan_T.text} _n "
            val countLa = "R: ${et_counterLA_R.text} _n S: ${et_counterLA_S.text} _n T: ${et_counterLA_T.text} _n "
            val docLapor = hashMapOf(
                    "bay" to "$bay",
                    "tanggal" to tvDate_gangguan.text.toString(),
                    "waktu" to tvTime_gangguan.text.toString(),
                    "fl" to et_gangguan_FL.text.toString(),
                    "catatan" to et_gangguan_Catatan.text.toString(),
                    "conPMT" to countPMT,
                    "conGangguan" to countGangguan,
                    "conLa" to countLa,
                    "bpadam" to et_gangguan_bebanPadam.text.toString(),
                    "bnormal" to et_gangguan_bebanNormal.text.toString(),
                    "analisa" to et_gangguan_analisa.text.toString()
            )
            /*val docDate = hashMapOf(
                    "tanggal" to tvDate_gangguan.text.toString(),
                    "waktu" to tvTime_gangguan.text.toString()
            )*/
            val documentPath = tvDate_gangguan.text.toString()
            if (documentPath.isEmpty()) {
                Toast.makeText(applicationContext, "isi dulu tanggalnya", Toast.LENGTH_SHORT).show()
            } else {
                db!!.collection("Gardu").document(idgardu).collection("Gangguan").document("$date $time").set(docLapor)
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                btn_lapor_gangguan.isEnabled = false
                btn_lapor_gangguan.setText("sudah ke upload")
                val toast = Toast.makeText(applicationContext, "sudah ke upload", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }
}