package com.example.budgetplanner

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class BudgetActivity : AppCompatActivity() {

    private lateinit var budgetDisplay: TextView
    private lateinit var totalSpent: TextView
    private lateinit var remainingBudget: TextView
    private var budgetAmount: Double = 0.0
    private var totalExpenses: Double = 0.0
    private val expenses = mutableListOf<ExpenseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_budget)
        setSupportActionBar(toolbar)

        budgetDisplay = findViewById(R.id.budgetDisplay)
        totalSpent = findViewById(R.id.totalSpent)
        remainingBudget = findViewById(R.id.remainingBudget)

        // Retrieve budget from Intent
        budgetAmount = intent.getDoubleExtra("budgetAmount", 0.0)
        updateBudgetDisplay()

        // Handle Add Expense button click
        findViewById<MaterialButton>(R.id.addExpenseButton).setOnClickListener {
            val expenseInput = findViewById<TextInputEditText>(R.id.expenseInput)
            val expenseNameInput = findViewById<TextInputEditText>(R.id.expenseNameInput)
            val expenseAmount = expenseInput.text.toString().toDoubleOrNull()
            val expenseName = expenseNameInput.text.toString()
            if (expenseAmount != null && expenseName.isNotEmpty()) {
                val expenseItem = ExpenseItem(expenseName, expenseAmount)
                expenses.add(expenseItem)
                totalExpenses += expenseAmount
                updateBudgetDisplay()
                triggerNotification(expenseName, expenseAmount)
                expenseInput.text?.clear()
                expenseNameInput.text?.clear()
            } else {
                Toast.makeText(this, "Please enter a valid expense name and amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBudgetDisplay() {
        budgetDisplay.text = "Your Budget: $${budgetAmount}"
        totalSpent.text = "Total Spent: $${totalExpenses}"
        remainingBudget.text = "Remaining Budget: $${budgetAmount - totalExpenses}"
    }

    private fun triggerNotification(expenseName: String, expenseAmount: Double) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channelId = "budget_notification_channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(channelId, "Budget Notifications", android.app.NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifications for budget app"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = android.app.Notification.Builder(this, channelId)
            .setContentTitle("Expense Added")
            .setContentText("$expenseName: $${expenseAmount}")
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_analytics -> {
                // Pass data to the AnalyticsFragment
                val fragment = AnalyticsFragment.newInstance(expenses, budgetAmount)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
                findViewById<android.widget.FrameLayout>(R.id.fragment_container).visibility = android.view.View.VISIBLE
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
