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
    private var biodata: Biodata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appDb = AppDatabase.getDatabase(this)

        val bundle = intent.extras
        if(bundle != null){
            biodata = bundle.getParcelable("biodata") ?: Biodata()
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
                biodata?.nama = etNama.text.toString()
                biodata?.status = spnStatus.selectedItem.toString()
                biodata?.jenisKelamin = if(rbPria.isChecked) "Pria" else "Wanita"
                biodata?.makananFav = favorite

                runBlocking {
                    withContext(Dispatchers.IO){
                        appDb?.bioDao()?.updateBiodata(biodata ?: Biodata())
                    }
                }

                val intent = Intent()
                intent.putExtra("biodata", biodata)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun bindData() {
        with(binding) {
            etNama.setText(biodata?.nama)

            val statusArray = resources.getStringArray(R.array.status)
            spnStatus.setSelection(statusArray.indexOf(biodata?.status))

            rbPria.isChecked = biodata?.jenisKelamin == "Pria"
            rbWanita.isChecked = biodata?.jenisKelamin == "Wanita"

            cbAyam.isChecked = biodata?.makananFav?.contains(cbAyam.text.toString()) ?: false
            cbBakso.isChecked = biodata?.makananFav?.contains(cbBakso.text.toString()) ?: false
            cbRendang.isChecked = biodata?.makananFav?.contains(cbRendang.text.toString()) ?: false
            favorite.addAll(biodata?.makananFav ?: emptyList())
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