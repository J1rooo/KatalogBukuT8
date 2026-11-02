package com.example.katalogbuku

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.katalogbuku.databinding.ItemBukuBinding

class BukuAdapter(
    private val daftar: MutableList<Buku>,
    private val onKlik: (Buku) -> Unit,
    private val onTahan: (Int) -> Unit
) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    class BukuViewHolder(val binding: ItemBukuBinding) : RecyclerView.ViewHolder(binding.root)

    // Membuat View baru (hanya dipanggil bila RecyclerView membutuhkannya)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val binding = ItemBukuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BukuViewHolder(binding)
    }

    // Mengisi View dengan data posisi tertentu (dipanggil setiap kali menggulir)
    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = daftar[position]

        with(holder.binding) {
            tvJudul.text = buku.judul
            tvDetail.text = root.context.getString(R.string.buku_detail, buku.penulis, buku.tahun)

            root.setOnClickListener { onKlik(buku) }
            root.setOnLongClickListener {
                val posisi = holder.bindingAdapterPosition
                if (posisi != RecyclerView.NO_POSITION) onTahan(posisi)
                true
            }
        }
    }

    override fun getItemCount(): Int = daftar.size

    fun tambah(buku: Buku) {
        daftar.add(buku)
        notifyItemInserted(daftar.lastIndex)
    }

    fun hapus(posisi: Int) {
        daftar.removeAt(posisi)
        notifyItemRemoved(posisi)
    }
}
