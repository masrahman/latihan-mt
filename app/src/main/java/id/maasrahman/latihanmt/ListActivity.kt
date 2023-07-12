package id.maasrahman.latihanmt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.maasrahman.latihanmt.databinding.ActivityListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var adapter: BiodataAdapter
    private var listData = mutableListOf<Biodata>()

    private var appDb: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            }
        }
        adapter.updateData(listData)

    }

    private fun initView(){
        with(binding){
            adapter = BiodataAdapter()
            recyclerList.layoutManager = LinearLayoutManager(baseContext)
            recyclerList.adapter = adapter

            btnAdd.setOnClickListener {
                startActivity(Intent(baseContext, MainActivity::class.java))
            }
        }
    }
}