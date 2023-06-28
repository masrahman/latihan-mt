package id.maasrahman.latihanmt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Biodata (
    var nama: String? = null,
    var status: String? = null,
    var jenisKelamin: String? = null,
    var makananFav: List<String>? = null,
) : Parcelable