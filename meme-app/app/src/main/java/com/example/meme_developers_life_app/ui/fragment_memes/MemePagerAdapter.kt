package com.example.meme_developers_life_app.ui.fragment_memes

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.meme_developers_life_app.data.items.Meme
import com.example.meme_developers_life_app.data.items.loadAsGif
import com.example.meme_developers_life_app.databinding.ItemSingleMemeBinding
import com.example.meme_developers_life_app.db.MemeDao
import com.example.meme_developers_life_app.util.DoubleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemePagerAdapter(private val memeDao: MemeDao) : PagingDataAdapter<Meme, MemePagerAdapter.MemeViewHolder>(MEME_DIFFER) {

    companion object {
        private val MEME_DIFFER = object : DiffUtil.ItemCallback<Meme>() {
            override fun areItemsTheSame(oldItem: Meme, newItem: Meme): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Meme,
                newItem: Meme
            ): Boolean =
                oldItem == newItem
        }
    }

    inner class MemeViewHolder(private val binding: ItemSingleMemeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meme : Meme) {

            binding.apply {
                meme.loadAsGif(itemView, ivMemePicture)

                tvMemeName.text = meme.description

                // Double click listener
                val drawable = ivLike.drawable
                ivMemePicture.setOnClickListener(object : DoubleClickListener() {
                    override fun onDoubleClick(v: View) {
                        if (meme.gifURL.isNullOrBlank()) return

                        ivLike.alpha = 0.7f

                        if (drawable is AnimatedVectorDrawableCompat) {
                            drawable.start()
                        } else if (drawable is AnimatedVectorDrawable) {
                            drawable.start()
                        }

                        // пытаюсь сохранить элемент в избранное
                        CoroutineScope(Dispatchers.IO).launch {
                            memeDao.saveSingleMeme(meme)
                        }
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val binding = ItemSingleMemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentMeme = getItem(position)
        if (currentMeme != null) {
            holder.bind(currentMeme)
        }
    }
}