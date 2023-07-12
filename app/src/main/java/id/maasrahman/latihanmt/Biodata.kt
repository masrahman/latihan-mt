package id.maasrahman.latihanmt

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "biodata")
data class Biodata (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var nama: String? = null,
    @ColumnInfo(name = "status")
    var status: String? = null,
    @ColumnInfo(name = "jenisKelamin")
    var jenisKelamin: String? = null,
    @ColumnInfo(name = "makananFav")
    var makananFav: List<String>? = null,
) : Parcelable