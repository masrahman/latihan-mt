package id.maasrahman.latihanmt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import id.maasrahman.latihanmt.databinding.ActivityUpdateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    var favorite = arrayListOf<String>()

    private var appDb: AppDatabase? = null
    private var modelBiodata: Biodata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appDb = AppDatabase.getDatabase(this)

        val bundle = intent.extras
        if(bundle != null){
            modelBiodata = bundle.getParcelable("biodata") ?: Biodata()
            bindData()
        }

        initView()
    }

    fun initView(){
        with(binding){
            rbWanita.setOnCheckedChangeListener(radioListener)
            rbPria.setOnCheckedChangeListener(radioListener)
            cbAyam.setOnCheckedChangeListener(checkListener)
            cbBakso.setOnCheckedChangeListener(checkListener)
            cbRendang.setOnCheckedChangeListener(checkListener)

            btnSubmit.setOnClickListener {
                if(etNama.text.toString().isEmpty()){
                    etNama.error = "Wajib diisi"
                    return@setOnClickListener
                }

                modelBiodata?.nama = etNama.text.toString()
                modelBiodata?.status = spnStatus.selectedItem.toString()
                modelBiodata?.jenisKelamin = if(rbPria.isChecked) "Pria" else "Wanita"
                modelBiodata?.makananFav = favorite

                runBlocking {
                    withContext(Dispatchers.IO){
                        appDb?.bioDao()?.updateBiodata(modelBiodata ?: Biodata())
                    }
                }

                val intent = Intent()
                intent.putExtra("biodata", modelBiodata)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun bindData() {
        with(binding) {
            etNama.setText(modelBiodata?.nama)

            val statusArray = resources.getStringArray(R.array.status)
            spnStatus.setSelection(statusArray.indexOf(modelBiodata?.status))

            rbPria.isChecked = modelBiodata?.jenisKelamin == "Pria"
            rbWanita.isChecked = modelBiodata?.jenisKelamin == "Wanita"

            cbAyam.isChecked = modelBiodata?.makananFav?.contains(cbAyam.text.toString()) ?: false
            cbBakso.isChecked = modelBiodata?.makananFav?.contains(cbBakso.text.toString()) ?: false
            cbRendang.isChecked = modelBiodata?.makananFav?.contains(cbRendang.text.toString()) ?: false
            favorite.addAll(modelBiodata?.makananFav ?: emptyList())
        }
    }

    private val radioListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        if(b){
            if(compoundButton.id == R.id.rbPria){
                binding.rbWanita.isChecked = false
            }else if(compoundButton.id == R.id.rbWanita){
                binding.rbPria.isChecked = false
            }
        }
    }

    private val checkListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        if(b){
            favorite.add(compoundButton.text.toString())
        }else{
            favorite.remove(compoundButton.text.toString())
        }
    }
}