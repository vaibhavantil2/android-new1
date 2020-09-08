package com.hedvig.app.feature.profile.ui.tab

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hedvig.app.R
import com.hedvig.app.databinding.ProfileLogoutBinding
import com.hedvig.app.databinding.ProfileRowBinding
import com.hedvig.app.util.GenericDiffUtilCallback
import com.hedvig.app.util.extensions.inflate
import com.hedvig.app.util.extensions.setAuthenticationToken
import com.hedvig.app.util.extensions.setIsLoggedIn
import com.hedvig.app.util.extensions.triggerRestartActivity
import com.hedvig.app.util.extensions.view.setHapticClickListener
import com.hedvig.app.util.extensions.viewBinding
import e

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    var items: List<ProfileModel> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(GenericDiffUtilCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.profile_title -> ViewHolder.Title(parent)
        R.layout.profile_row -> ViewHolder.Row(parent)
        R.layout.profile_subtitle -> ViewHolder.Subtitle(parent)
        R.layout.profile_logout -> ViewHolder.Logout(parent)
        else -> throw Error("Invalid viewType")
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        ProfileModel.Title -> R.layout.profile_title
        is ProfileModel.Row -> R.layout.profile_row
        ProfileModel.Subtitle -> R.layout.profile_subtitle
        ProfileModel.Logout -> R.layout.profile_logout
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(data: ProfileModel): Any?

        fun invalid(data: ProfileModel) {
            e { "Invalid data passed to ${this.javaClass.name}::bind - type is ${data.javaClass.name}" }
        }

        class Title(parent: ViewGroup) : ViewHolder(parent.inflate(R.layout.profile_title)) {
            override fun bind(data: ProfileModel) = Unit
        }

        class Row(parent: ViewGroup) : ViewHolder(parent.inflate(R.layout.profile_row)) {
            private val binding by viewBinding(ProfileRowBinding::bind)
            override fun bind(data: ProfileModel) = with(binding) {
                if (data !is ProfileModel.Row) {
                    return invalid(data)
                }

                title.text = data.title
                caption.text = data.caption

                icon.setImageResource(data.icon)
                root.setHapticClickListener { data.action() }
            }
        }

        class Subtitle(parent: ViewGroup) : ViewHolder(parent.inflate(R.layout.profile_subtitle)) {
            override fun bind(data: ProfileModel) = Unit
        }

        class Logout(parent: ViewGroup) : ViewHolder(parent.inflate(R.layout.profile_logout)) {
            private val binding by viewBinding(ProfileLogoutBinding::bind)
            override fun bind(data: ProfileModel) = with(binding) {
                root.setHapticClickListener {
                    root.context.apply {
                        setAuthenticationToken(null)
                        setIsLoggedIn(false)
                        com.google.firebase.iid.FirebaseInstanceId.getInstance().deleteInstanceId()
                        triggerRestartActivity()
                    }
                }
            }
        }
    }
}