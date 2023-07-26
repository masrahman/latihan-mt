package id.maasrahman.latihanmt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import id.maasrahman.latihanmt.databinding.ActivityListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var adapter: BiodataAdapter
    private var listData = mutableListOf<Biodata>()
    private var filterData = mutableListOf<Biodata>()

    private var appDb: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Result"

        appDb = AppDatabase.getDatabase(this)
        initView()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData(){
        runBlocking {
            withContext(Dispatchers.IO){
                listData = appDb?.bioDao()?.getBiodata()?.toMutableList() ?: mutableListOf()
                with(binding){
                    filterData.clear()
                    if(etSearch.text?.isNotEmpty() == true){
                        val tmpData = listData.filter { e -> e.nama?.lowercase()?.contains(etSearch.text.toString().lowercase()) == true }
                        filterData.addAll(tmpData)
                    }else{
                        filterData.addAll(listData)
                    }
                }
            }
        }
        adapter.updateData(filterData)
    }

    private fun initView(){
        with(binding){
            adapter = BiodataAdapter{ bio ->
                val intent = Intent(baseContext, ResultActivity::class.java)
                intent.putExtra("biodata", bio)
                startActivity(intent)
            }
            recyclerList.layoutManager = LinearLayoutManager(baseContext)
            recyclerList.adapter = adapter

            etSearch.addTextChangedListener(textListener)

            btnAdd.setOnClickListener {
                startActivity(Intent(baseContext, MainActivity::class.java))
            }
        }
    }

    private val textListener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            filterData.clear()
            if(s?.isNotEmpty() == true){
                val tmpData = listData.filter { e -> e.nama?.lowercase()?.contains(s.toString().lowercase()) == true }
                filterData.addAll(tmpData)
            }else{
                filterData.addAll(listData)
            }
            adapter.updateData(filterData)
        }
    }
}