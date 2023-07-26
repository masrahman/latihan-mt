package id.maasrahman.latihanmt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.maasrahman.latihanmt.databinding.ActivityResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding

    var modelBiodata: Biodata? = null
    private var appDb: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = "View Data"
        appDb = AppDatabase.getDatabase(this)

        val intentData = intent.extras
        if(intentData != null){
            modelBiodata = intentData.getParcelable("biodata") ?: Biodata()
            bindData()
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            val bundle = result.data
            modelBiodata = bundle?.getParcelableExtra("biodata") ?: Biodata()
            bindData()
        }
    }

    fun bindData(){
        with(binding){
            txtNama.text = modelBiodata?.nama
            txtStatus.text = modelBiodata?.status
            txtJenisKelamin.text = modelBiodata?.jenisKelamin
            txtMakanan.text = modelBiodata?.makananFav?.joinToString(", ")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.result_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update -> {
                val intent = Intent(baseContext, UpdateActivity::class.java)
                intent.putExtra("biodata", modelBiodata)
                startForResult.launch(intent)
            }
            R.id.action_delete -> {
                AlertDialog.Builder(this@ResultActivity)
                    .setTitle("Konfirmasi")
                    .setMessage("Yakin menghapus data ${modelBiodata?.nama}?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        runBlocking {
                            withContext(Dispatchers.IO) {
                                appDb?.bioDao()?.deleteBiodata(modelBiodata ?: Biodata())
                            }
                        }
//                        dialog.dismiss()
                        finish()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}