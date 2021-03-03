package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.data.db.entities.UserMentionable
import com.github.skrox.travelally.data.network.responses.UserSuggestionResponse
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.PostTripFragmentBinding
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripViewModelFacotry
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
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
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.post_trip_fragment.*
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class PostTripFragment : Fragment(), QueryTokenReceiver, SuggestionsResultListener,
    SuggestionsVisibilityManager, MentionsEditText.MentionWatcher {

    @Inject
    lateinit var placesClient: PlacesClient

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var factory: PostTripViewModelFacotry

    private var recyclerView: RecyclerView? = null
    private var adapter: PersonMentionAdapter? = null
    private val BUCKET = "members-network"
    private var editor: MentionsEditText? = null

    private val REQUEST_CODE_START = 2
    private val REQUEST_CODE_END = 3

    private val postTripViewModel: PostTripViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: PostTripFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.post_trip_fragment, container, false)
        binding.viewmodel = postTripViewModel
        binding.lifecycleOwner = this
        binding.fragment = this;
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

        setupButtonClick()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as TravelAllyApplication).appComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setPlaceListeners()

    }

    private fun setupButtonClick() {
        postTripViewModel.getPostFields()?.observe(viewLifecycleOwner, Observer<Any> { ptm ->
            Toast.makeText(
                context,
                "Email " + ptm,
                Toast.LENGTH_SHORT
            ).show()
        })
    }


    override fun onQueryReceived(@NonNull token: QueryToken): MutableList<String> {
        val text: MentionsEditable = (editor as MentionsEditText).getMentionsText()
        val mentionSpans: List<MentionSpan> = text.getMentionSpans()
        for (span in mentionSpans) {
            val start: Int = text.getSpanStart(span)
            val end: Int = text.getSpanEnd(span)
            val currentText: String = text.substring(start, end).toString()
//            text.replace(start, end, "[" + currentText + ", id:" + span.getId() + "]")
            Log.e("mentioned", currentText);
        }
        val suggestionList: List<UserSuggestionResponse> = getSuggestions(token)
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
        return mutableListOf(BUCKET)
    }

    private fun getSuggestions(@NonNull token: QueryToken): List<UserSuggestionResponse> =
        runBlocking {
            return@runBlocking userRepository.getUsersSuggestion(token.keywords)
        }

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

    fun showDatePickerDialog(v: View) {
        // get fragment manager so we can launch from fragment
        val fm: FragmentManager = parentFragmentManager
        // create the datePickerFragment
        val newFragment = DatePickerFragment();
        if (v.id == R.id.start_date)
            newFragment.setTargetFragment(this, REQUEST_CODE_START);
        else
            newFragment.setTargetFragment(this, REQUEST_CODE_END);
        // show the datePicker
        fm.let { newFragment.show(it, "datePicker") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // check for the results
        if (resultCode == Activity.RESULT_OK) {
            // get date from string
            val date = data?.getStringExtra("selectedDate")
            // set the value of the editText

            if (requestCode == REQUEST_CODE_START)
                postTripViewModel.liveStartDate.postValue(date)
            else
                postTripViewModel.liveEndDate.postValue(date)
        }

        val status = Autocomplete.getStatusFromIntent(data!!)
        Log.e("gpi", status.toString())
    }

    private fun setPlaceListeners() {
        // Initialize the AutocompleteSupportFragment.
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.start_place) as AutocompleteSupportFragment

        // Specify the types of place data to return.

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                //                Log.i(TAG, "Place: " + place.name + ", " + place.id)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                //                Log.i(TAG, "An error occurred: $status")

                Log.e("places error", status.toString())
            }
        })


        val autocompleteFragmentend: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.end_place) as AutocompleteSupportFragment

        // Specify the types of place data to return.

        // Specify the types of place data to return.
        autocompleteFragmentend.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentend.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                //                Log.i(TAG, "Place: " + place.name + ", " + place.id)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                //                Log.i(TAG, "An error occurred: $status")
            }
        })


    }
}
