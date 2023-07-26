package id.maasrahman.latihanmt

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BiodataDao {
    @Insert
    fun insertBiodata(biodata: Biodata)

    @Query("select * from biodata")
    fun getBiodata(): List<Biodata>

    @Query("select * from biodata where name like '%' || :str || '%'")
    fun getBiodataParam(str: String): List<Biodata>

    @Update
    fun updateBiodata(biodata: Biodata)

    @Delete
    fun deleteBiodata(biodata: Biodata)
}