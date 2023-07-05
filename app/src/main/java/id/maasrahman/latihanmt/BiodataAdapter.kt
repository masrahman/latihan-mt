package id.maasrahman.latihanmt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.maasrahman.latihanmt.databinding.ItemBiodataBinding
import kotlin.properties.Delegates

class BiodataAdapter: RecyclerView.Adapter<BiodataAdapter.BiodataHolder>() {

    private var listData: List<Biodata> by Delegates.observable(emptyList()){_, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(list: List<Biodata>){
        listData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiodataHolder {
        val itemBind = ItemBiodataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BiodataHolder(itemBind)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: BiodataHolder, position: Int) {
        holder.bindData(listData[position])
    }

    inner class BiodataHolder(private val itemBind: ItemBiodataBinding)
        : RecyclerView.ViewHolder(itemBind.root){

        fun bindData(biodata: Biodata){
            with(itemBind){
                txtNama.text = biodata.nama
                txtJenisKelamin.text = biodata.jenisKelamin
                txtStatus.text = biodata.status
                txtMakanan.text = biodata.makananFav?.joinToString(", ")
            }
        }

    }
}