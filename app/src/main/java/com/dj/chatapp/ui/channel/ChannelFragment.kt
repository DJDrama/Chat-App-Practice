package com.dj.chatapp.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.dj.chatapp.BaseFragment
import com.dj.chatapp.databinding.FragmentChannelBinding
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

@AndroidEntryPoint
class ChannelFragment : BaseFragment<FragmentChannelBinding>() {
    private val viewModel by activityViewModels<ChannelViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = viewModel.getUser()
        user?.let {
            val factory = ChannelListViewModelFactory(
                filter = Filters.and(Filters.eq("type", "messaging")),
                sort = ChannelListViewModel.DEFAULT_SORT,
                limit = 30
            )
            val channelListViewModel: ChannelListViewModel by viewModels { factory }
            val channelListHeaderViewModel: ChannelListHeaderViewModel by viewModels()
            channelListViewModel.bindView(binding.channelListView, viewLifecycleOwner)
            channelListHeaderViewModel.bindView(binding.channelListHeaderView, viewLifecycleOwner)

            binding.channelListHeaderView.setOnUserAvatarClickListener {
                viewModel.logOut()
                findNavController().popBackStack()
            }
        } ?: findNavController().popBackStack()
    }
}