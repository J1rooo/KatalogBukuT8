package com.example.katalogbuku

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.katalogbuku.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BukuAdapter
    private var tampilanGrid = false

    // Pemisah antar baris (hanya dipakai pada tampilan daftar)
    private val pemisah: DividerItemDecoration by lazy {
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL).also { decoration ->
            ContextCompat.getDrawable(this, R.drawable.divider_spacing)
                ?.let(decoration::setDrawable)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyWindowInsets()
        setupRecyclerView()
        setupButtons()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            binding.header.updatePadding(top = bars.top)
            binding.root.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        adapter = BukuAdapter(
            daftar = siapkanData(),
            onKlik = { buku ->
                Toast.makeText(this, getString(R.string.toast_dipilih, buku.judul), Toast.LENGTH_SHORT).show()
            },
            onTahan = { posisi -> adapter.hapus(posisi) }
        )
        binding.recyclerBuku.adapter = adapter
        terapkanLayout()
    }

    private fun setupButtons() = with(binding) {
        btnTambah.setOnClickListener { tambahBuku() }
        btnLayout.setOnClickListener {
            tampilanGrid = !tampilanGrid
            terapkanLayout()
        }
    }

    private fun tambahBuku() {
        val nomor = adapter.itemCount + 1
        adapter.tambah(
            Buku(
                judul = getString(R.string.buku_baru_judul, nomor),
                penulis = getString(R.string.buku_baru_penulis),
                tahun = TAHUN_BUKU_BARU
            )
        )
        binding.recyclerBuku.smoothScrollToPosition(adapter.itemCount - 1)
    }

    /** LayoutManager menentukan bentuk daftar; adapter dan data tidak berubah. */
    private fun terapkanLayout() = with(binding) {
        recyclerBuku.removeItemDecoration(pemisah)

        if (tampilanGrid) {
            recyclerBuku.layoutManager = GridLayoutManager(this@MainActivity, JUMLAH_KOLOM)
        } else {
            recyclerBuku.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerBuku.addItemDecoration(pemisah)
        }
        btnLayout.setText(if (tampilanGrid) R.string.btn_tampilan_daftar else R.string.btn_tampilan_grid)
    }

    private fun siapkanData(): MutableList<Buku> = mutableListOf(
        Buku("Pemrograman Android", "Budi Santoso", 2022),
        Buku("Dasar Kotlin", "Siti Aminah", 2023),
        Buku("Jetpack Compose", "Andi Pratama", 2024),
        Buku("Desain UI/UX", "Maya Lestari", 2020),
        Buku("Basis Data", "Rudi Hartono", 2019)
    )

    private companion object {
        const val JUMLAH_KOLOM = 2
        const val TAHUN_BUKU_BARU = 2026
    }
}
