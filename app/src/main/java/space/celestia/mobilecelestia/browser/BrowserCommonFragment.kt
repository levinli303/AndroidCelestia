/*
 * BrowserCommonFragment.kt
 *
 * Copyright (C) 2001-2020, the Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.browser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.celestia.mobilecelestia.R
import space.celestia.mobilecelestia.common.TitledFragment
import space.celestia.mobilecelestia.core.CelestiaBrowserItem

class BrowserCommonFragment : TitledFragment() {

    private var listener: Listener? = null
    private var browserItem: CelestiaBrowserItem? = null

    override val title: String
        get() = browserItem!!.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val path = it.getString(ARG_PATH)!!
            browserItem = BrowserFragment.browserMap[path]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browser_common_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = BrowserCommonRecyclerViewAdapter(browserItem!!, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement BrowserCommonFragment.Listener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface Listener {
        fun onBrowserItemSelected(item: BrowserItem)
    }

    companion object {
        const val ARG_PATH = "path"

        @JvmStatic
        fun newInstance(path: String) =
            BrowserCommonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PATH, path)
                }
            }
    }
}