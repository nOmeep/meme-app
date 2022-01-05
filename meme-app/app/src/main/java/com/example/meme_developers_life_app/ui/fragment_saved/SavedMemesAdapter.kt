package com.example.meme_developers_life_app.ui.fragment_saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.meme_developers_life_app.R
import com.example.meme_developers_life_app.data.items.Meme
import com.example.meme_developers_life_app.databinding.ItemSavedMemeBinding
import com.example.meme_developers_life_app.db.MemeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedMemesAdapter(
    private val savedMemes: List<Meme>,
    private val memeDao : MemeDao
) : RecyclerView.Adapter<SavedMemesAdapter.SavedMemeViewHolder>() {

    inner class SavedMemeViewHolder(private val binding: ItemSavedMemeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meme: Meme) {
            binding.apply {
                Glide.with(itemView)
                    .asGif()
                    .load(meme.gifURL)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivImageSaved)
            }

            binding.cardView.setOnLongClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    memeDao.deleteSingleSavedMeme(meme.id)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMemeViewHolder {
        val binding = ItemSavedMemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedMemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedMemeViewHolder, position: Int) {
        val currentMeme = savedMemes[position]
        holder.bind(currentMeme)
    }

    override fun getItemCount(): Int {
        return savedMemes.size
    }
}