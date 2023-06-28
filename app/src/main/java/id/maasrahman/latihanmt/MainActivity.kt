package id.maasrahman.latihanmt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import id.maasrahman.latihanmt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var favorite = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        with(binding){
            rbPria.setOnCheckedChangeListener(radioListener)
            rbWanita.setOnCheckedChangeListener(radioListener)

            cbAyam.setOnCheckedChangeListener(checkListener)
            cbBakso.setOnCheckedChangeListener(checkListener)
            cbRendang.setOnCheckedChangeListener(checkListener)

            btnSubmit.setOnClickListener {
                if(etNama.text.toString().isEmpty()){
                    etNama.error = "Wajib diisi"
                    return@setOnClickListener
                }

                var biodata = Biodata(
                    nama = etNama.text.toString(),
                    status = spnStatus.selectedItem.toString(),
                    jenisKelamin = if(rbPria.isChecked) "Pria" else "Wanita",
                    makananFav = favorite,
                )

                val intent = Intent(baseContext, ResultActivity::class.java)
                intent.putExtra("biodata", biodata)
                startActivity(intent)
            }
        }
    }
    
    val radioListener = CompoundButton.OnCheckedChangeListener{ button, isChekced ->
        if(isChekced){
            if(button.id == R.id.rbPria){
                binding.rbWanita.isChecked = false
            }else if(button.id == R.id.rbWanita){
                binding.rbPria.isChecked = false
            }
        }
    }

    val checkListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
        if(isChecked){
            favorite.add(button.text.toString())
        }else{
            favorite.remove(button.text.toString())
        }
    }
        
        
        
        
        
        
        
        
        
        
        
}