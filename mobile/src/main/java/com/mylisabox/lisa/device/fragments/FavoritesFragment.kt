package com.mylisabox.lisa.device.fragments

import com.mylisabox.lisa.R
import com.mylisabox.lisa.device.viewmodels.FavoriteViewModel
import javax.inject.Inject

/**
 * Created by jaumard on 07.12.16.
 * L.I.S.A. project license GPL-3
 */

class FavoritesFragment : DevicesFragment() {
    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    @Inject
    lateinit var favoriteViewModel: FavoriteViewModel

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.menu_favorites)
    }

    override fun inject() {
        getFragmentComponent(this).inject(this)
        vModel = favoriteViewModel
        super.inject()
    }
}