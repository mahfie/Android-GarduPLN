package `in`.mroyek.gardupln.activity.beban

class LaporanBebanResponses {
    var id: String? = null
    var gardu: String? = null
    var namabay: String? = null
    var tanggal: String? = null
    var waktu: String? = null
    var u: String? = null
    var i: String? = null
    var p: String? = null
    var q: String? = null
    var `in`: String? = null

    constructor() {}
    constructor(id: String?, gardu: String?, namabay: String?, tanggal: String?, waktu: String?, u: String?, i: String?, p: String?, q: String?, `in`: String?, beban: String?) {
        this.id = id
        this.gardu = gardu
        this.namabay = namabay
        this.tanggal = tanggal
        this.waktu = waktu
        this.u = u
        this.i = i
        this.p = p
        this.q = q
        this.`in` = `in`
    }
}
