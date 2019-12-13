package `in`.mroyek.gardupln.activity.beban

class LaporanBebanResponses {
    var id: String? = null
    var idbay: String? = null
    var gardu: String? = null
    var namabay: String? = null
    var tanggal: String? = null
    var waktu: String? = null
    var u: String? = null
    var ihv: String? = null
    var ilv: String? = null
    var p: String? = null
    var q: String? = null
    var beban: String? = null
    var inhv: String? = null
    var inlv: String? = null

    constructor() {}
    constructor(id: String?, idbay: String?, gardu: String?, namabay: String?, tanggal: String?, waktu: String?, u: String?, ihv: String?, ilv: String?, p: String?, q: String?, beban: String?, inhv: String?, inlv: String?) {
        this.id = id
        this.idbay = idbay
        this.gardu = gardu
        this.namabay = namabay
        this.tanggal = tanggal
        this.waktu = waktu
        this.u = u
        this.ihv = ihv
        this.ilv = ilv
        this.p = p
        this.q = q
        this.beban = beban
        this.inhv = inhv
        this.inlv = inlv
    }
}
