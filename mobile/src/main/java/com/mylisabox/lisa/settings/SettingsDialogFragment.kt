package com.mylisabox.lisa.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.dagger.components.FragmentComponent
import com.mylisabox.lisa.dagger.modules.FragmentModule
import com.mylisabox.network.preferences.Preferences
import javax.inject.Inject

class SettingsDialogFragment : DialogFragment() {
    @Inject lateinit var preferences: Preferences
    private lateinit var editText: TextInputEditText

    companion object {
        fun newInstance(): SettingsDialogFragment {
            return SettingsDialogFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inject()
        editText.text = preferences.get(Preferences.KEY_EXTERNAL_URL)
    }

    private fun inject() {
        getFragmentComponent(this).inject(this)
    }

    private fun getFragmentComponent(fragment: Fragment): FragmentComponent {
        return (activity as BaseActivity).activityComponent.plusFragmentComponent(FragmentModule(fragment))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_prompt, null)
        editText = view.findViewById(R.id.text)
        return AlertDialog.Builder(context)
                .setTitle(R.string.menu_settings)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    run {
                        if (TextUtils.isEmpty(editText.text)) {
                            preferences.removeAndApply(Preferences.KEY_EXTERNAL_URL)
                        } else {
                            preferences.setAndApply(Preferences.KEY_EXTERNAL_URL, editText.text.toString())
                        }
                    }
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
    }

}