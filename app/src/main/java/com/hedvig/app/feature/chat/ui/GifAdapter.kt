package com.hedvig.app.feature.chat.ui

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.hedvig.android.owldroid.graphql.GifQuery
import com.hedvig.app.R
import com.hedvig.app.databinding.GifItemBinding
import com.hedvig.app.util.GenericDiffUtilItemCallback
import com.hedvig.app.util.extensions.view.setHapticClickListener
import com.hedvig.app.util.extensions.viewBinding

class GifAdapter(
    private val context: Context,
    private val sendGif: (String) -> Unit
) : ListAdapter<GifQuery.Gif, GifAdapter.GifViewHolder>(GenericDiffUtilItemCallback()) {

    val recyclerViewPreloader = RecyclerViewPreloader(
        Glide.with(context),
        GifPreloadModelProvider(),
        ViewPreloadSizeProvider(),
        10
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GifViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.gif_item,
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.apply {
            getItem(position).url?.let { url ->
                Glide
                    .with(image)
                    .load(Uri.parse(url))
                    .transform(CenterCrop(), RoundedCorners(40))
                    .into(image)
                    .clearOnDetach()

                image.setHapticClickListener {
                    sendGif(url)
                }
            }
        }
    }

    override fun onViewRecycled(holder: GifViewHolder) {
        Glide
            .with(holder.image)
            .clear(holder.image)
    }

    inner class GifPreloadModelProvider : ListPreloader.PreloadModelProvider<GifQuery.Gif> {
        override fun getPreloadItems(position: Int): List<GifQuery.Gif> =
            getItem(position)?.let { gif ->
                listOf(gif)
            } ?: listOf()

        override fun getPreloadRequestBuilder(item: GifQuery.Gif): RequestBuilder<*>? =
            Glide
                .with(context)
                .load(Uri.parse(item.url))
                .transform(CenterCrop(), RoundedCorners(40))
    }

    class GifViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding by viewBinding(GifItemBinding::bind)
        val image: ImageView = binding.gifImage
    }
}
