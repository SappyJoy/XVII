package com.twoeightnine.root.xvii.friends.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.twoeightnine.root.xvii.R
import com.twoeightnine.root.xvii.fragments.BaseFragment
import com.twoeightnine.root.xvii.friends.adapters.UsersAdapter
import com.twoeightnine.root.xvii.profile.fragments.ProfileFragment

class FriendsOnlineFragment: BaseFragment() {

    companion object {
        fun newInstance(loadMore: (Int) -> Unit): FriendsOnlineFragment {
            val frag = FriendsOnlineFragment()
            frag.loadMore = loadMore
            return frag
        }
    }

    @BindView(R.id.rvUsers)
    lateinit var rvUsers: RecyclerView

    lateinit var adapter: UsersAdapter

    var loadMore: ((Int) -> Unit)? = null

    override fun bindViews(view: View) {
        super.bindViews(view)
        ButterKnife.bind(this, view)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = UsersAdapter(activity, { loadMore?.invoke(it) }, {
            val user = adapter.items[it]
            rootActivity.loadFragment(ProfileFragment.newInstance(user.id))
        })
        rvUsers.layoutManager = LinearLayoutManager(activity)
        rvUsers.adapter = adapter
    }

    override fun getLayout() = R.layout.fragment_friends_simple
}