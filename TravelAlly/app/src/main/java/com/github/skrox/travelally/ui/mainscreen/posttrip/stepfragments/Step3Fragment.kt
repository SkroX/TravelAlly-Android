package com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.entities.UserMentionable
import com.github.skrox.travelally.data.network.responses.UserSuggestionResponse
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.FragmentStep3Binding
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripActivity
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripViewModel
import com.google.android.gms.common.api.ApiException
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.mentions.MentionsEditable
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.linkedin.android.spyglass.ui.MentionsEditText
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class Step3Fragment : Fragment(), QueryTokenReceiver, SuggestionsResultListener,
    SuggestionsVisibilityManager, MentionsEditText.MentionWatcher {

    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var postTripViewModel: PostTripViewModel


    private var recyclerView: RecyclerView? = null
    private var adapter: PersonMentionAdapter? = null
    private val BUCKET = "members-network"
    private var editor: MentionsEditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postTripViewModel = (activity as PostTripActivity).postTripViewModel

        // Inflate the layout for this fragment
        val binding: FragmentStep3Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step3, container, false)
        binding.viewmodel = postTripViewModel
        binding.lifecycleOwner = this

        val tokenizerConfig = WordTokenizerConfig.Builder()
            .setWordBreakChars(", ")
            .setExplicitChars("")
            .setMaxNumKeywords(2)
            .setThreshold(1)
            .build()
//        editor = itemView.findViewById(R.id.editor)
        editor = binding.editor
        editor?.setQueryTokenReceiver(this)
        editor?.setSuggestionsVisibilityManager(this)
        editor!!.tokenizer =
            WordTokenizer(tokenizerConfig)
        editor?.addMentionWatcher(this)
//        recyclerView = itemView.findViewById<RecyclerView?>(R.id.mentions_grid)
        recyclerView = binding.mentionsGrid
        recyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = PersonMentionAdapter((ArrayList<UserMentionable>() as List<Suggestible>))
        recyclerView?.adapter = adapter

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as PostTripActivity).postTripComponent.inject(this)
    }

    private var apiJob: Job? = null
    override fun onQueryReceived(@NonNull token: QueryToken): MutableList<String> {
        try {
            apiJob?.cancel()
        } catch (e: CancellationException) {
            e.printStackTrace()
        }

        val text: MentionsEditable = (editor as MentionsEditText).getMentionsText()
        val mentionSpans: List<MentionSpan> = text.getMentionSpans()
        for (span in mentionSpans) {
            val start: Int = text.getSpanStart(span)
            val end: Int = text.getSpanEnd(span)
            val currentText: String = text.substring(start, end).toString()
//            text.replace(start, end, "[" + currentText + ", id:" + span.getId() + "]")
            Log.e("mentioned", currentText);
        }
        try {
            apiJob = lifecycleScope.launch {
                var suggestionList: List<UserSuggestionResponse> = mutableListOf()
                try {
                    suggestionList = getSuggestions(token)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val suggestions: MutableList<UserMentionable> = ArrayList()
                for (suggestion in suggestionList) {
                    suggestions.add(
                        UserMentionable(
                            suggestion.id,
                            suggestion.name,
                            suggestion.family_name,
                            suggestion.picture
                        )
                    )
                }
                val result = SuggestionsResult(token, suggestions)
                onReceiveSuggestionsResult(result, BUCKET)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }

        return mutableListOf(BUCKET)
    }

    private suspend fun getSuggestions(@NonNull token: QueryToken): List<UserSuggestionResponse> =
        userRepository.getUsersSuggestion(token.keywords)


    // --------------------------------------------------
    // SuggestionsResultListener Implementation
    // --------------------------------------------------
    override fun onReceiveSuggestionsResult(
        result: SuggestionsResult,
        bucket: String
    ) {
        val suggestions = result.suggestions
        adapter = PersonMentionAdapter(result.suggestions)
        recyclerView?.swapAdapter(adapter, true)
        val display = suggestions.size > 0
        displaySuggestions(display)
    }

    // --------------------------------------------------
    // SuggestionsManager Implementation
    // --------------------------------------------------
    override fun displaySuggestions(display: Boolean) {
        if (display) {
            recyclerView?.visibility = RecyclerView.VISIBLE
        } else {
            recyclerView?.visibility = RecyclerView.GONE
        }
    }

    override fun isDisplayingSuggestions(): Boolean {
        return recyclerView?.visibility == RecyclerView.VISIBLE
    }

    // --------------------------------------------------
    // PersonMentionAdapter Class
    // --------------------------------------------------
    private class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var picture: ImageView

        init {
            name = itemView.findViewById(R.id.person_name)
            picture = itemView.findViewById(R.id.person_image)
        }
    }

    private inner class PersonMentionAdapter(private val suggestions: List<Suggestible>) :
        RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val v: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.grid_mention_item, viewGroup, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            val suggestion = suggestions[i] as? UserMentionable ?: return
            val person: UserMentionable = suggestion
            viewHolder.name.text = person.fullName
            Picasso.get()
                .load(person.pictureURL)
                .into(viewHolder.picture)
            viewHolder.itemView.setOnClickListener {
                editor?.insertMention(person)
                recyclerView?.swapAdapter(
                    PersonMentionAdapter(java.util.ArrayList<UserMentionable>()),
                    true
                )
                displaySuggestions(false)
                editor?.requestFocus()
            }

        }

        override fun getItemCount(): Int {
            return suggestions.size
        }

    }

    override fun onMentionDeleted(mention: Mentionable, text: String, start: Int, end: Int) {
        Log.e("deleted", " " + mention.suggestibleId)
        postTripViewModel.removePeople(mention.suggestibleId)
    }

    override fun onMentionPartiallyDeleted(
        mention: Mentionable,
        text: String,
        start: Int,
        end: Int
    ) {
    }

    override fun onMentionAdded(mention: Mentionable, text: String, start: Int, end: Int) {
        Log.e("mentioned", " " + mention.suggestibleId)
        postTripViewModel.addPeople(mention.suggestibleId)
    }
}