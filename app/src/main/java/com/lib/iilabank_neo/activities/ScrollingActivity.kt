package com.lib.illabank_test_vanshika.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lib.iilabank_neo.R
import com.lib.iilabank_neo.databinding.ActivityScrollingBinding
import com.lib.iilabank_neo.databinding.LayoutViewpagerItemsBinding
import com.lib.illabank_test_vanshika.scrollUtil.CommonRecycleListAdapter
import com.lib.illabank_test_vanshika.scrollUtil.PagerData
import com.lib.illabank_test_vanshika.toast
import com.lib.illabank_test_vanshika.utilities.SimpleDividerItemDecoration


class ScrollingActivity : AppCompatActivity() {
    lateinit var mAdapter: ViewsSliderAdapter
    private var doubleBackToExitPressedOnce = false
    lateinit var commonRecycleListAdapter: CommonRecycleListAdapter
    private lateinit var act_scrollBinding: ActivityScrollingBinding
    lateinit var viewPagerItems: List<PagerData>
    lateinit var bankItems: MutableList<PagerData>
    lateinit var hotelsItems: MutableList<PagerData>
    lateinit var mallItems: MutableList<PagerData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act_scrollBinding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(act_scrollBinding.root)

        /*
        * statically geting all list data items
        * */
        viewPagerItems = getListOfPagerContents()
        bankItems = getListOfBankContents()
        hotelsItems = getListOfHotelContents()
        mallItems = getListOfMallsContents()

        //Initializing ViewPager
        mAdapter = ViewsSliderAdapter(this)

        act_scrollBinding.viewpagerTutorial.apply {
            adapter=mAdapter
            registerOnPageChangeCallback(pageChangeCallback)
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit=2

           // it.setPadding(40, 0, 40, 0)

        }
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen._20sdp)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen._30sdp)
        act_scrollBinding.viewpagerTutorial.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
//        act_scrollBinding.viewpagerTutorial.adapter = mAdapter
//        act_scrollBinding.viewpagerTutorial.registerOnPageChangeCallback(pageChangeCallback)

        // adding bottom dots
        TabLayoutMediator(
            act_scrollBinding.tabDots,
            act_scrollBinding.viewpagerTutorial
        ) { tab, position ->
        }.attach()

        /*
        * ClickListener for search image
        * */
        act_scrollBinding.layoutContent.llLay.setOnClickListener {
            if (it.visibility == View.VISIBLE) {
                it.visibility = View.GONE
                act_scrollBinding.layoutContent.searchViewList.visibility = View.VISIBLE
                act_scrollBinding.layoutContent.searchViewList.setIconifiedByDefault(false)
            } else {

                act_scrollBinding.layoutContent.searchViewList.visibility = View.GONE
                it.visibility = View.VISIBLE

            }
        }
        /*
        * Set searchview components properties
        * */
        //act_scrollBinding.layoutContent.searchViewList
        act_scrollBinding.layoutContent.searchViewList.findViewById<ImageView>(R.id.search_close_btn)
            .setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
        act_scrollBinding.layoutContent.searchViewList.findViewById<TextView>(R.id.search_src_text).also {
            it.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            it.setFilters(
                arrayOf<InputFilter>(
                    LengthFilter(
                        5
                    )
                )
            )

        }



        /*
        * SearchView Listenser
        * */
        act_scrollBinding.layoutContent.searchViewList.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                commonRecycleListAdapter.filter.filter(newText)
                return false
            }

        })

        /*
        * First time loading list with Bank data
        * */
        setBankListsAdapter()

    }

    /*
     * ViewPager page change listener
     */
    var pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // addBottomDots(position);

                if (position == 0) {
                    setBankListsAdapter()
                } else if (position == 1) {
                    setHotelListsAdapter()
                } else {
                    setMallListsAdapter()
                }
//
//            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts.length - 1) {
//                // last page. make button text to GOT IT
//                binding.btnNext.setText(getString(R.string.start));
//                binding.btnSkip.setVisibility(View.GONE);
//            } else {
//                // still pages are left
//                binding.btnNext.setText(getString(R.string.next));
//                binding.btnSkip.setVisibility(View.VISIBLE);
//            }
            }
        }

    //View Pager sliding data
    fun getListOfPagerContents(): List<PagerData> {

        val ar1 = PagerData(0, "Banks", R.drawable.ic_bank)
        val ar2 = PagerData(1, "Hotels", R.drawable.ic_hotel)
        val ar3 = PagerData(2, "Malls", R.drawable.ic_malls)
        return mutableListOf(ar1, ar2, ar3)
    }

    //Bank data
    fun getListOfBankContents(): MutableList<PagerData> {

        val ar1 = PagerData(0, "ABC_Bank", R.drawable.ic_bank)
        val ar2 = PagerData(1, "DEF_Bank", R.drawable.ic_bank)
        val ar3 = PagerData(2, "Kotak Mahindra", R.drawable.ic_bank)
        val ar4 = PagerData(3, "PNB", R.drawable.ic_bank)
        val ar5 = PagerData(4, "RBI", R.drawable.ic_bank)
        val ar6 = PagerData(5, "Axis", R.drawable.ic_bank)
        val ar7 = PagerData(6, "Standard Chartered", R.drawable.ic_bank)

        return mutableListOf(ar1, ar2, ar3, ar4, ar5, ar6, ar7)
    }

    //Hotel Data
    fun getListOfHotelContents(): MutableList<PagerData> {

        val ar1 = PagerData(0, "Harmony", R.drawable.ic_hotel)
        val ar2 = PagerData(1, "Yellow Chillies", R.drawable.ic_hotel)
        val ar3 = PagerData(2, "Signature", R.drawable.ic_hotel)
        val ar4 = PagerData(3, "Blue Heaven", R.drawable.ic_hotel)
        val ar5 = PagerData(4, "RajBhoj", R.drawable.ic_hotel)
        val ar6 = PagerData(5, "Tilak", R.drawable.ic_hotel)
        val ar7 = PagerData(6, "QuickFix", R.drawable.ic_hotel)
        val ar8 = PagerData(7, "TTTL", R.drawable.ic_hotel)
        val ar9 = PagerData(8, "PackGave", R.drawable.ic_hotel)
        val ar10 = PagerData(9, "Taj Hotel", R.drawable.ic_hotel)
        val ar11 = PagerData(10, "PizzaHut", R.drawable.ic_hotel)
        val ar12 = PagerData(11, "Red Magic", R.drawable.ic_hotel)

        return mutableListOf(ar1, ar2, ar3, ar4, ar5, ar6, ar7, ar8, ar9, ar10, ar11, ar12)
    }

    //Malls Data
    fun getListOfMallsContents(): MutableList<PagerData> {

        val ar1 = PagerData(0, "BigBasket", R.drawable.ic_malls)
        val ar2 = PagerData(1, "One99", R.drawable.ic_malls)
        val ar3 = PagerData(2, "BigBazzar", R.drawable.ic_malls)
        val ar4 = PagerData(3, "WallMArt", R.drawable.ic_malls)
        val ar5 = PagerData(4, "Shopprix", R.drawable.ic_malls)
        val ar6 = PagerData(6, "GIP", R.drawable.ic_malls)

        return mutableListOf(ar1, ar2, ar3, ar4, ar5, ar6)
    }

    /*
    * ViewPager Adapter
    *
    * */
    inner class ViewsSliderAdapter(private val context: Context) :
        RecyclerView.Adapter<ViewsSliderAdapter.SliderViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
//        RecyclerView.ViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(viewType, parent, false)
//            return SliderViewHolder(view)
                SliderViewHolder {
            val binding = LayoutViewpagerItemsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return SliderViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
            holder.layoutViewpagerItemsBinding.imgItem
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        context, // Context
                        viewPagerItems[position].PagerImage // Drawable
                    )
                )
            holder.layoutViewpagerItemsBinding.tvTitle.text = viewPagerItems[position].pagerTitle
        }
//        override fun getItemViewType(position: Int): PagerData {
//           return viewPagerItems[position]
//        }

        override fun getItemCount(): Int {
            return viewPagerItems.size
        }

        inner class SliderViewHolder(var layoutViewpagerItemsBinding: LayoutViewpagerItemsBinding) :
            RecyclerView.ViewHolder(layoutViewpagerItemsBinding.root)

    }

    /*
    * set hotel list
    * */
    fun setHotelListsAdapter() {
        commonRecycleListAdapter = CommonRecycleListAdapter(this, hotelsItems)
        act_scrollBinding.layoutContent.recyclerViewList.also {
            it.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.addItemDecoration(SimpleDividerItemDecoration(this, R.drawable.line_divider))

            it.adapter = commonRecycleListAdapter
        }
    }

    /*
    * set Bank list
    * */
    fun setBankListsAdapter() {
        commonRecycleListAdapter = CommonRecycleListAdapter(this, bankItems)
        act_scrollBinding.layoutContent.recyclerViewList.also {
            it.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.addItemDecoration(SimpleDividerItemDecoration(this, R.drawable.line_divider))

            it.adapter = commonRecycleListAdapter
        }

    }

    /*
    * set Mall list
    * */
    fun setMallListsAdapter() {
        commonRecycleListAdapter = CommonRecycleListAdapter(this, mallItems)
        act_scrollBinding.layoutContent.recyclerViewList.also {
            it.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.addItemDecoration(SimpleDividerItemDecoration(this, R.drawable.line_divider))
            it.adapter = commonRecycleListAdapter
        }

    }

    /*
    * on back tap :
    * Firstly softkeyboard disappear
    * sec, reset searchView
    * third,Exit toast from app
    * */
    override fun onBackPressed() {
        if (act_scrollBinding.layoutContent.llLay.visibility == View.GONE) {
            act_scrollBinding.layoutContent.searchViewList.visibility = View.GONE
            act_scrollBinding.layoutContent.llLay.visibility = View.VISIBLE
        } else {
            if (doubleBackToExitPressedOnce) {


                super.onBackPressed()
                System.exit(1)
                return
            }

            this.doubleBackToExitPressedOnce = true
            toast(getString(R.string.app_exit))

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }
}