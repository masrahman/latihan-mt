package id.maasrahman.latihanmt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import id.maasrahman.latihanmt.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = "View Data"

        val intentData = intent.extras
        if(intentData != null){
            val biodata : Biodata = intentData.getParcelable("biodata") ?: Biodata()
            bindData(biodata)
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            val bundle = result.data
            val biodata : Biodata = bundle?.getParcelableExtra("biodata") ?: Biodata()
            bindData(biodata)
        }
    }

    fun bindData(biodata: Biodata){
        with(binding){
            txtNama.text = biodata.nama
            txtStatus.text = biodata.status
            txtJenisKelamin.text = biodata.jenisKelamin
            txtMakanan.text = biodata.makananFav?.joinToString(", ")

            btnUpdate.setOnClickListener {
                val intent = Intent(baseContext, UpdateActivity::class.java)
                intent.putExtra("biodata", biodata)
                startForResult.launch(intent)
            }
        }
    }
}