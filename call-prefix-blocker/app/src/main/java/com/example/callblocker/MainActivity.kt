package com.example.callblocker

import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var prefixList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 50, 40, 40)
        }

        val title = TextView(this).apply {
            text = "Call Prefix Blocker"
            textSize = 26f
            setTypeface(null, Typeface.BOLD)
        }

        val description = TextView(this).apply {
            text = "Add a prefix such as 140. It will also match +91 140..."
            textSize = 16f
            setPadding(0, 16, 0, 24)
        }

        val roleButton = Button(this).apply {
            text = "Enable Call Screening"
            setOnClickListener { requestCallScreeningRole() }
        }

        val input = EditText(this).apply {
            hint = "Prefix, e.g. 140"
            inputType = InputType.TYPE_CLASS_PHONE
        }

        val addButton = Button(this).apply {
            text = "Add Prefix"
            setOnClickListener {
                val normalized = NumberUtils.normalizeIndianNumber(input.text.toString())

                if (normalized.isBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Enter a valid numeric prefix",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                PrefixStore.addPrefix(this@MainActivity, normalized)
                input.text.clear()
                renderPrefixes()
            }
        }

        val sectionTitle = TextView(this).apply {
            text = "Blocked prefixes"
            textSize = 19f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 30, 0, 10)
        }

        prefixList = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(title)
        root.addView(description)
        root.addView(roleButton)
        root.addView(input)
        root.addView(addButton)
        root.addView(sectionTitle)
        root.addView(prefixList)

        val scroll = ScrollView(this).apply {
            addView(
                root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }

        setContentView(scroll)
        renderPrefixes()
    }

    override fun onResume() {
        super.onResume()
        if (::prefixList.isInitialized) {
            renderPrefixes()
        }
    }

    private fun requestCallScreeningRole() {
        val roleManager = getSystemService(RoleManager::class.java)

        if (!roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            Toast.makeText(
                this,
                "Call screening is not available on this device",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
            Toast.makeText(
                this,
                "Call screening is already enabled",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent: Intent =
            roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        startActivityForResult(intent, REQUEST_CALL_SCREENING_ROLE)
    }

    private fun renderPrefixes() {
        prefixList.removeAllViews()

        val prefixes = PrefixStore.getPrefixes(this).sorted()

        if (prefixes.isEmpty()) {
            prefixList.addView(
                TextView(this).apply {
                    text = "No blocked prefixes yet."
                    textSize = 16f
                }
            )
            return
        }

        prefixes.forEach { prefix ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val label = TextView(this).apply {
                text = "$prefix*"
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val deleteButton = Button(this).apply {
                text = "Delete"
                setOnClickListener {
                    PrefixStore.removePrefix(this@MainActivity, prefix)
                    renderPrefixes()
                }
            }

            row.addView(label)
            row.addView(deleteButton)
            prefixList.addView(row)
        }
    }

    companion object {
        private const val REQUEST_CALL_SCREENING_ROLE = 1001
    }
}
