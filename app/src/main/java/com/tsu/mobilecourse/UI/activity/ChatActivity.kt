package com.tsu.mobilecourse.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.UI.fragment.ChatMainFragment
import com.tsu.mobilecourse.UI.fragment.PersonChatFragment
import com.tsu.mobilecourse.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity(), ChatMainFragment.OnFragmentInteractionListener{

    private lateinit var binding: ActivityChatBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chats = listOf(
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.profile_image,
                "blockName" to "Имя",
                "blocktext" to "Сообщение Сообщение Сообщение Сообщение Сообщение",
                "id" to "bio"
            ),
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.profile_image,
                "blockName" to "Имя",
                "blocktext" to "Сообщение Сообщение Сообщение Сообщение Сообщение",
                "id" to "bio"
            ),
        )

        if (savedInstanceState == null) {
            for (card in chats) {
                val fragment = ChatMainFragment()
                val bundle = Bundle().apply {
                    putInt("blockImg", card["blockImg"] as Int)
                    putString("blockName", card["blockName"] as String)
                    putString("blocktext", card["blocktext"] as String)
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(binding.chatContainer.id, fragment)
                    .commit()
            }
        }

    }

    override fun onFragmentClicked(name: String) {
        val newFragment = when (name) {
            "Имя" -> {
                val fragment = PersonChatFragment()
                val bundle = Bundle().apply {
                    putString("name", "Имя человека")
                    putString("message", "Сообщение для PersonChatFragment")
                }
                fragment.arguments = bundle
                fragment
            }
            else -> return
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        for (fragment in fragmentManager.fragments) {
            fragmentTransaction.remove(fragment)
        }

        fragmentTransaction.replace(binding.personContainer.id, newFragment)
            .commit() // Выполняем транзакцию

        fragmentManager.beginTransaction()
            .replace(binding.personContainer.id, newFragment)
            .addToBackStack(null)
            .commit()
    }

}