package `in`.mroyek.gardupln.activity.crud

import `in`.mroyek.gardupln.R
import `in`.mroyek.gardupln.activity.BayActivity
import `in`.mroyek.gardupln.activity.GarduActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_crud_bay.*
import java.util.*
import kotlin.collections.HashMap


class CrudBayActivity : AppCompatActivity() {

    lateinit var idgardu: String
    private val db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_bay)
        val sharePref: SharedPreferences = getSharedPreferences("idgardunya", 0)
        idgardu = sharePref.getString(GarduActivity.IDGARDU, "").toString()
        /* val nangkep = intent.extras
         idgardu = nangkep!!.get(key.ID_GARDU).toString()
         gardu = nangkep.get("gardu").toString()
         id_doc.text = gardu*/

        btn_transmisi.setOnClickListener {
            ll_transmisi.visibility = View.VISIBLE
            ll_diameter.visibility = View.GONE
            ll_trafo.visibility = View.GONE
            /*et_bay_transmisi.setText("PHT")
            et_bay_transmisi.setSelection(et_bay_transmisi.text.length)
            et_bay_transmisi.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (!p0.toString().startsWith("PHT")){
                        et_bay_transmisi.setText("PHT")
                        et_bay_transmisi.setSelection(et_bay_transmisi.text.length)
                    }
                }
            })*/
            et_bay_transmisi.filters = et_bay_transmisi.filters + InputFilter.AllCaps()
            et_bay_transmisi.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    et_bay_transmisi.setText("PHT")
                }
            }
            btn_close_transmisi.setOnClickListener { ll_transmisi.visibility = View.GONE }
            btn_choose_transmisi.setOnClickListener {
                val id: String = UUID.randomUUID().toString()
                val etbay = et_bay_transmisi.text.toString().trim()
                val inhv =  et_bay_transmisi_in.text.toString().trim()
                val tower = et_bay_transmisi_tower.text.toString().trim()
                val jarak = et_bay_transmisi_jarak.text.toString().trim()
                val mulai = et_bay_transmisi_mulai.text.toString().trim()
                val cheid = rg_arah_tower.checkedRadioButtonId
                val valueRg = findViewById<RadioButton>(cheid)
                val doc = hashMapOf(
                        "awal" to valueRg.text.toString().trim(),
                        "mulai" to mulai,
                        "idbay" to id,
                        "namabay" to etbay,
                        "inhv" to inhv,
                        "tower" to tower,
                        "jarak" to jarak
                        )
                upload(doc, etbay)
            }
        }
        btn_diameter.setOnClickListener {
            ll_diameter.visibility = View.VISIBLE
            ll_transmisi.visibility = View.GONE
            ll_trafo.visibility = View.GONE
            et_bay_diameter.filters = et_bay_transmisi.filters + InputFilter.AllCaps()
            et_bay_diameter.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    et_bay_diameter.setText("DIAMETER")
                }
            }
            btn_close_diameter.setOnClickListener { ll_diameter.visibility = View.GONE }
            btn_choose_diameter.setOnClickListener {
                val id: String = UUID.randomUUID().toString()
                val etbay = et_bay_diameter.text.toString().trim()
                val inhv =  et_bay_diameter_in.text.toString().trim()
                val doc = hashMapOf(
                        "idbay" to id,
                        "namabay" to etbay,
                        "inhv" to inhv
                )
                upload(doc, etbay)
            }
        }

        btn_trafo.setOnClickListener {
            ll_trafo.visibility = View.VISIBLE
            ll_transmisi.visibility = View.GONE
            ll_diameter.visibility = View.GONE
            et_bay_trafo.filters = et_bay_transmisi.filters + InputFilter.AllCaps()
            et_bay_trafo.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    et_bay_trafo.setText("TRAFO")
                }
            }
            btn_close_trafo.setOnClickListener { ll_trafo.visibility = View.GONE }
            btn_choose_trafo.setOnClickListener {
                val id: String = UUID.randomUUID().toString()
                val etbay = et_bay_trafo.text.toString().trim()
                val inhv =  et_bay_trafo_Inhv.text.toString().trim()
                val inlv =  "/"+et_bay_trafo_Inlv.text.toString().trim()
                val doc = hashMapOf(
                        "idbay" to id,
                        "namabay" to etbay,
                        "inhv" to inhv,
                        "inlv" to inlv
                )
                upload(doc, etbay)
            }
        }
        /*btn_transmisi.setOnClickListener { container(TransmisiFragment(), "trasmisi") }
        btn_diameter.setOnClickListener { container(DiameterFragment(), "diameter") }
        btn_trafo.setOnClickListener { container(TrafoFragment(), "trafo") }*/
    }

    private fun upload(doc: HashMap<String, String>, etbay: String) {

        db!!.collection("Gardu").document(idgardu).collection("Bay").document(etbay).set(doc)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "okeeh", Toast.LENGTH_SHORT).show()
                    backtoBay()
                }
                .addOnFailureListener { Toast.makeText(applicationContext, "GAGAL", Toast.LENGTH_SHORT).show() }
    }

    private fun backtoBay() {
//        startActivity(Intent(applicationContext, BayActivity::class.java).putExtra(key.ID_GARDU, idgardu).putExtra("gardu", gardu))
        startActivity(Intent(applicationContext, BayActivity::class.java))
    }

    override fun onStop() {
        super.onStop()
        Log.d("log", "onstopbay")
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
        Log.d("log", "ondestroybay")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        /*  val i = Intent(this, BayActivity::class.java)
          i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/
        finish()
    }
}
