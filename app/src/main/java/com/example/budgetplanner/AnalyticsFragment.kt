package com.example.budgetplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class AnalyticsFragment : Fragment() {

    private var expenses: List<ExpenseItem> = listOf()
    private var totalBudget: Double = 0.0

    companion object {
        fun newInstance(expenses: List<ExpenseItem>, totalBudget: Double): AnalyticsFragment {
            val fragment = AnalyticsFragment()
            val args = Bundle()
            args.putSerializable("expenses", ArrayList(expenses))
            args.putDouble("totalBudget", totalBudget)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analytics, container, false)

        arguments?.let {
            expenses = it.getSerializable("expenses") as List<ExpenseItem>
            totalBudget = it.getDouble("totalBudget")
        }

        populateAnalytics(view)
        return view
    }

    private fun populateAnalytics(view: View) {
        val analyticsContainer = view.findViewById<LinearLayout>(R.id.analytics_container)

        val totalSpent = expenses.sumByDouble { it.amount }

        expenses.forEach { expense ->
            val percentSpent = if (totalBudget > 0) (expense.amount / totalBudget) * 100 else 0.0
            val categoryView = TextView(context).apply {
                text = getString(R.string.analytics_format, expense.name, expense.amount, percentSpent)
                textSize = 16f
                setPadding(0, 0, 0, 16)
            }
            analyticsContainer.addView(categoryView)
        }

        val remainingBudgetView = TextView(context).apply {
            text = getString(R.string.analytics_remaining_budget, totalBudget - totalSpent)
            textSize = 18f
            setPadding(0, 0, 0, 16)
        }
        analyticsContainer.addView(remainingBudgetView)
    }
}
