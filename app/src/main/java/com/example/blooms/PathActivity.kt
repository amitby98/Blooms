package com.example.blooms

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PathActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val rvPath = findViewById<RecyclerView>(R.id.rvPath)

        rvPath.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }

        rvPath.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val screenWidth = parent.width

                outRect.bottom = 40

                when (position % 4) {
                    0 -> { // ימין קיצוני
                        outRect.left = (screenWidth * 0.05).toInt()
                        outRect.right = (screenWidth * 0.45).toInt()
                    }
                    1 -> { // ימין פנימי
                        outRect.left = (screenWidth * 0.15).toInt()
                        outRect.right = (screenWidth * 0.35).toInt()
                    }
                    2 -> { // שמאל פנימי
                        outRect.left = (screenWidth * 0.35).toInt()
                        outRect.right = (screenWidth * 0.15).toInt()
                    }
                    3 -> { // שמאל קיצוני
                        outRect.left = (screenWidth * 0.45).toInt()
                        outRect.right = (screenWidth * 0.05).toInt()
                    }
                }
            }
        })

        val stones = List(20) { it }
        rvPath.adapter = PathAdapter(stones)
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnEdit).setOnClickListener {
            // הלוגיקה של כפתור העריכה
        }
    }
}

class PathAdapter(private val items: List<Int>) :
    RecyclerView.Adapter<PathAdapter.StoneViewHolder>() {

    class StoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stoneView: ImageView = itemView.findViewById(R.id.ivStone)
        val bulbView: ImageView = itemView.findViewById(R.id.ivBulb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stone_item, parent, false)
        return StoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoneViewHolder, position: Int) {
        // עדכון גודל האבן
        val stoneParams = holder.stoneView.layoutParams
        // עדכון גודל הנורה
        val bulbParams = holder.bulbView.layoutParams

        when (position % 4) {
            0 -> { // אבן גדולה
                stoneParams.width = 160.dpToPx(holder.itemView.context)
                stoneParams.height = 90.dpToPx(holder.itemView.context)
                bulbParams.width = 40.dpToPx(holder.itemView.context)
                bulbParams.height = 40.dpToPx(holder.itemView.context)
            }
            1 -> { // אבן בינונית-גדולה
                stoneParams.width = 140.dpToPx(holder.itemView.context)
                stoneParams.height = 80.dpToPx(holder.itemView.context)
                bulbParams.width = 35.dpToPx(holder.itemView.context)
                bulbParams.height = 35.dpToPx(holder.itemView.context)
            }
            2 -> { // אבן בינונית
                stoneParams.width = 120.dpToPx(holder.itemView.context)
                stoneParams.height = 70.dpToPx(holder.itemView.context)
                bulbParams.width = 30.dpToPx(holder.itemView.context)
                bulbParams.height = 30.dpToPx(holder.itemView.context)
            }
            3 -> { // אבן קטנה
                stoneParams.width = 100.dpToPx(holder.itemView.context)
                stoneParams.height = 60.dpToPx(holder.itemView.context)
                bulbParams.width = 25.dpToPx(holder.itemView.context)
                bulbParams.height = 25.dpToPx(holder.itemView.context)
            }
        }
        holder.stoneView.layoutParams = stoneParams
        holder.bulbView.layoutParams = bulbParams
    }

    override fun getItemCount() = items.size

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}