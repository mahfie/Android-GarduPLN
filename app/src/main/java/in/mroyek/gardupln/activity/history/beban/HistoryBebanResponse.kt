package `in`.mroyek.gardupln.activity.history.beban

class HistoryBebanResponse {
    var id: String? = null
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
    var cuaca: String? = null
//    var laporan: List<String>? = null\
//    var laporan: List<Lapor>? = null

    constructor() {}
    constructor(id: String?, gardu: String?, namabay: String?, tanggal: String?, waktu: String?, u: String?, ihv: String?, ilv: String?, p: String?, q: String?, inhv: String?, inlv: String?, beban: String?, cuaca: String?) {
        this.id = id
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
        this.cuaca = cuaca
    }

}/*
class Lapor {
    var u: String? = null
    var i: String? = null
    var p: String? = null
    var q: String? = null
    var `in`: String? = null
    var beban: String? = null

    constructor(u: String?, i: String?, p: String?, q: String?, `in`: String?, beban: String?) {
        this.u = u
        this.i = i
        this.p = p
        this.q = q
        this.`in` = `in`
        this.beban = beban
    }
    constructor()
}*/