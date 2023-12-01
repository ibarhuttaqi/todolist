package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var menuViewModel: MenuViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi ViewModel
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        // Observasi LiveData
        menuViewModel.menuListLiveData.observe(this, { menuList ->
            // Perbarui UI dengan menuList yang baru
            updateUI(menuList)
        })


        val fabAddMenu = findViewById<FloatingActionButton>(R.id.fabAddMenu)

        // Listener untuk Floating Button
        fabAddMenu.setOnClickListener {

            // Menampilkan dialog tambah menu
            showAddMenuDialog()
        }

//        menuViewModel.getMenuList()
    }

    private fun updateUI(menuList: List<ListData>) {
        val menuListLayout = findViewById<LinearLayout>(R.id.menuList)

        // Hapus semua menu yang ada pada menuListLayout
        menuListLayout.removeAllViews()

        // Iterasi melalui setiap menu dalam menuList dan tambahkan ke menuListLayout
        for (menu in menuList) {
            val menuItemView = createMenuItemView(menu)
            menuListLayout.addView(menuItemView)
        }
    }

    // Inside MainActivity class
    private fun createMenuItemView(menu: ListData): View {
        // Mendapatkan font dari res/font/
        val font = ResourcesCompat.getFont(this, R.font.playpensans)

        // Tambahkan menu baru ke dalam list
        val newMenu = ConstraintLayout(this)
        newMenu.id = View.generateViewId()

// 1. Checkbox
        val checkBox = CheckBox(this)
        checkBox.id = View.generateViewId()
        newMenu.addView(checkBox)

//linear layout nyatuin judul n deskripsi
        val judesk = LinearLayout(this)
        judesk.orientation = LinearLayout.VERTICAL
        judesk.id = View.generateViewId()

// 2. Teks Judul
        val titleText = TextView(this)
        titleText.id = View.generateViewId()
        titleText.text = menu.judul
        titleText.textSize = 24f
        titleText.setTextColor(Color.BLACK)
        titleText.typeface = font
        judesk.addView(titleText)

// 3. Deskripsi
        val descriptionText = TextView(this)
        descriptionText.id = View.generateViewId()
        descriptionText.text = menu.deskripsi
        descriptionText.typeface = font
        judesk.addView(descriptionText)


//            Linear layout untuk tanggal dan jam
        val tangjam = LinearLayout(this)
        tangjam.orientation = LinearLayout.VERTICAL
        tangjam.id = View.generateViewId()

// 4. Tanggal
        val dateText = TextView(this)
        dateText.id = View.generateViewId()
        dateText.text = menu.tanggal
        dateText.setTextColor(Color.BLUE)
        dateText.typeface = font
        tangjam.addView(dateText)

// 5. Jam
        val timeText = TextView(this)
        timeText.id = View.generateViewId()
        timeText.text = menu.jam
        timeText.setTextColor(Color.MAGENTA)
        timeText.typeface = font
        tangjam.addView(timeText)

        newMenu.addView(judesk)
        newMenu.addView(tangjam)

// Set constraints
        val constraintSet = ConstraintSet()
        constraintSet.clone(newMenu)

// Constraint untuk checkBox
        constraintSet.connect(
            checkBox.id, ConstraintSet.START,
            ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        constraintSet.connect(
            checkBox.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            checkBox.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )

//            constraint untuk judul deskripsi
        constraintSet.connect(
            judesk.id, ConstraintSet.START,
            checkBox.id, ConstraintSet.END
        )
        constraintSet.connect(
            judesk.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            judesk.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )

//            constarint untuk tangjam
        constraintSet.connect(
            tangjam.id, ConstraintSet.END,
            ConstraintSet.PARENT_ID, ConstraintSet.END
        )

// Terapkan constraints
        constraintSet.applyTo(newMenu)

//        newMenu.setBackgroundColor(getContrastColor(ContextCompat.getColor(this, R.color.white)))
        newMenu.setBackgroundColor(ContextCompat.getColor(this, R.color.biru4))
        newMenu.setBackgroundResource(R.drawable.menu_item_background)
        newMenu.setPadding(16, 16, 16, 16)

        // Atur margin (misalnya, 16dp pada semua sisi)
//            val marginValue = resources.getDimensionPixelSize(R.dimen.menu_margin)
        val marginTop = resources.getDimensionPixelSize(R.dimen.menu_margin_top)
        val marginRight = resources.getDimensionPixelSize(R.dimen.menu_margin_right)
        val marginBottom = resources.getDimensionPixelSize(R.dimen.menu_margin_bottom)
        val marginLeft = resources.getDimensionPixelSize(R.dimen.menu_margin_left)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(marginLeft, marginTop, marginRight, marginBottom)
        }
        newMenu.layoutParams = params

        // Menambahkan listener klik pada setiap menu
        newMenu.setOnClickListener {
            // Tampilkan popup edit ketika menu diklik
            showEditMenuDialog(titleText, descriptionText, dateText, timeText, newMenu)
        }

//            val myTextView = findViewById<TextView>(R.id.menuInfo)
//            myTextView.typeface = resources.getFont(R.font.my_custom_font_family)

        // Customize the layout parameters and styling as needed

        return newMenu
    }




    private fun showAddMenuDialog() {
        // Mendapatkan font dari res/font/
        val font = ResourcesCompat.getFont(this, R.font.playpensans)

        val menuList = findViewById<LinearLayout>(R.id.menuList)
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Tambah Tugas Baru")

        // Layout untuk input
        val inputLayout = LinearLayout(this)
        inputLayout.orientation = LinearLayout.VERTICAL

        val inputJudul = EditText(this)
        inputJudul.hint = "Judul"
        inputLayout.addView(inputJudul)

        val inputDeskripsi = EditText(this)
        inputDeskripsi.hint = "Deskripsi"
        inputLayout.addView(inputDeskripsi)

        val inputTanggal = Button(this)
        inputTanggal.text = "Pilih Tanggal"
        inputTanggal.setOnClickListener {
            showDatePickerDialog(inputTanggal)
        }
        inputLayout.addView(inputTanggal)

        // Input jam
        val inputJam = Button(this)
        inputJam.text = "Pilih Jam"
        inputJam.setOnClickListener {
            showTimePickerDialog(inputJam)
        }
        inputLayout.addView(inputJam)

//ubah font di input dialog tambah tugas
        for (i in 0 until inputLayout.childCount) {
            val view = inputLayout.getChildAt(i)
            if (view is TextView) {
                // Terapkan jenis font ke TextView
                view.typeface = font
            }
        }

        builder.setView(inputLayout)

        // Tombol positif (Tambah)
        builder.setPositiveButton("Tambah") { _, _ ->
            // Ambil nilai dari input
            val judul = inputJudul.text.toString()
            val deskripsi = inputDeskripsi.text.toString()
            val tanggal = inputTanggal.text.toString()
            val jam = inputJam.text.toString()





// Tambahkan menu baru ke dalam list
            val newMenu = ConstraintLayout(this)
            newMenu.id = View.generateViewId()

// 1. Checkbox
            val checkBox = CheckBox(this)
            checkBox.id = View.generateViewId()
            newMenu.addView(checkBox)

//linear layout nyatuin judul n deskripsi
            val judesk = LinearLayout(this)
            judesk.orientation = LinearLayout.VERTICAL
            judesk.id = View.generateViewId()

// 2. Teks Judul
            val titleText = TextView(this)
            titleText.id = View.generateViewId()
            titleText.text = judul
            titleText.textSize = 24f
            titleText.setTextColor(Color.BLACK)
            titleText.typeface = font
            judesk.addView(titleText)

// 3. Deskripsi
            val descriptionText = TextView(this)
            descriptionText.id = View.generateViewId()
            descriptionText.text = deskripsi
            descriptionText.typeface = font
            judesk.addView(descriptionText)


//            Linear layout untuk tanggal dan jam
            val tangjam = LinearLayout(this)
            tangjam.orientation = LinearLayout.VERTICAL
            tangjam.id = View.generateViewId()

// 4. Tanggal
            val dateText = TextView(this)
            dateText.id = View.generateViewId()
            dateText.text = tanggal
            dateText.setTextColor(Color.BLUE)
            dateText.typeface = font
            tangjam.addView(dateText)

// 5. Jam
            val timeText = TextView(this)
            timeText.id = View.generateViewId()
            timeText.text = jam
            timeText.setTextColor(Color.MAGENTA)
            timeText.typeface = font
            tangjam.addView(timeText)

            newMenu.addView(judesk)
            newMenu.addView(tangjam)

// Set constraints
            val constraintSet = ConstraintSet()
            constraintSet.clone(newMenu)

// Constraint untuk checkBox
            constraintSet.connect(
                checkBox.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            constraintSet.connect(
                checkBox.id, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )
            constraintSet.connect(
                checkBox.id, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )

//            constraint untuk judul deskripsi
            constraintSet.connect(
                judesk.id, ConstraintSet.START,
                checkBox.id, ConstraintSet.END
            )
            constraintSet.connect(
                judesk.id, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )
            constraintSet.connect(
                judesk.id, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )

//            constarint untuk tangjam
            constraintSet.connect(
                tangjam.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )

// Terapkan constraints
            constraintSet.applyTo(newMenu)


//            // Mengatur warna font
////            menuInfo.setTextColor(Color.RED)  // Ganti dengan warna yang diinginkan
////            menuInfo.setTextColor(ContextCompat.getColor(this, R.color.black))
////            menuInfo.setTextColor(Color.parseColor("#FF0000"))  // Ini akan mengatur warna ke merah
//            menuInfo.setTextColor(ContextCompat.getColor(this, R.color.black))



////            UBAH WARNA FONT BAGIAN TERTENTU
//            // Gabungkan teks dengan format khusus
//            val formattedText = "Judul: $judul\nDeskripsi: $deskripsi\nTanggal: $tanggal\nJam: $jam"
//            val spannableString = SpannableString(formattedText)
//
//            // Tentukan indeks awal dan akhir untuk bagian judul
//            val startIndex = formattedText.indexOf("Judul:")
//            val endIndex = startIndex + "Judul:".length + judul.length+1
//
//            // Set warna untuk bagian judul
//            spannableString.setSpan(
//                ForegroundColorSpan(Color.RED),  // Ganti dengan warna yang diinginkan
//                startIndex,
//                endIndex,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//
//            // Set teks yang telah diformat ke dalam TextView
//            menuInfo.text = spannableString
//            menuInfo.textSize = 16f



//            newMenu.setBackgroundColor(getContrastColor(ContextCompat.getColor(this, R.color.white)))
            newMenu.setBackgroundColor(ContextCompat.getColor(this, R.color.biru4))
            newMenu.setBackgroundResource(R.drawable.menu_item_background)
            newMenu.setPadding(16, 16, 16, 16)

            // Atur margin (misalnya, 16dp pada semua sisi)
//            val marginValue = resources.getDimensionPixelSize(R.dimen.menu_margin)
            val marginTop = resources.getDimensionPixelSize(R.dimen.menu_margin_top)
            val marginRight = resources.getDimensionPixelSize(R.dimen.menu_margin_right)
            val marginBottom = resources.getDimensionPixelSize(R.dimen.menu_margin_bottom)
            val marginLeft = resources.getDimensionPixelSize(R.dimen.menu_margin_left)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(marginLeft, marginTop, marginRight, marginBottom)
            }
            newMenu.layoutParams = params

            // Menambahkan listener klik pada setiap menu
            newMenu.setOnClickListener {
                // Tampilkan popup edit ketika menu diklik
                showEditMenuDialog(titleText, descriptionText, dateText, timeText, newMenu)
            }

//            val myTextView = findViewById<TextView>(R.id.menuInfo)
//            myTextView.typeface = resources.getFont(R.font.my_custom_font_family)

            ///////////////////////////////////
//            menuList.addView(newMenu)
////////////////////////////////////////////////

            // Tambahkan menu baru ke dalam list menggunakan ViewModel
            menuViewModel.addMenuToList(judul, deskripsi, tanggal, jam)
//            menuViewModel.addMenuToList(menuList)

        }

        // Tombol negatif (Batal)
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        // Tampilkan dialog
        builder.show()
    }







    private fun showDatePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                button.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = "$hourOfDay:$minute"
                button.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()
    }

//    private fun getContrastColor(color: Int): Int {
//        val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
//        return if (y >= 128) Color.BLACK else Color.WHITE
//    }

    private fun showEditMenuDialog(titleText: TextView, descriptionText: TextView, dateText: TextView, timeText: TextView, newMenu: ConstraintLayout) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Menu")

        // Layout untuk input edit
        val inputLayout = LinearLayout(this)
        inputLayout.orientation = LinearLayout.VERTICAL


        val inputJudul = EditText(this)
        inputJudul.hint = "Judul"
        inputJudul.setText(titleText.text) // Set judul yang sudah ada
        inputLayout.addView(inputJudul)

        val inputDeskripsi = EditText(this)
        inputDeskripsi.hint = "Deskripsi"
        inputDeskripsi.setText(descriptionText.text) // Set deskripsi yang sudah ada
        inputLayout.addView(inputDeskripsi)

        val inputTanggal = Button(this)
        inputTanggal.text = dateText.text // Set tanggal yang sudah ada
        inputTanggal.setOnClickListener {
            showDatePickerDialog(inputTanggal)
        }
        inputLayout.addView(inputTanggal)

        // Input jam
        val inputJam = Button(this)
        inputJam.text = timeText.text // Set jam yang sudah ada
        inputJam.setOnClickListener {
            showTimePickerDialog(inputJam)
        }
        inputLayout.addView(inputJam)



        builder.setView(inputLayout)

        // Tombol positif (Simpan Edit)
        builder.setPositiveButton("Simpan") { _, _ ->
            // Ambil nilai dari input edit
            val judul = inputJudul.text.toString()
            val deskripsi = inputDeskripsi.text.toString()
            val tanggal = inputTanggal.text.toString()
            val jam = inputJam.text.toString()


            // Tambahkan menu baru ke dalam list
            val newMenu = LinearLayout(this)
            newMenu.id = View.generateViewId()
            newMenu.orientation = LinearLayout.HORIZONTAL

            titleText.text = judul
            descriptionText.text = deskripsi
            dateText.text = tanggal
            timeText.text = jam

        }

        // Tombol negatif (Batal)
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        // Tombol delete
        builder.setNeutralButton("Hapus") { _, _ ->

            // Hapus menu dari list
            val menuList = findViewById<LinearLayout>(R.id.menuList)

            // Cari indeks menu yang akan dihapus
            val menuIndex = menuList.indexOfChild(newMenu)

            // Pastikan indeks tidak -1 sebelum mencoba menghapusnya
            if (menuIndex != -1) {
                menuList.removeViewAt(menuIndex)
            }
        }

        // Tampilkan dialog
        builder.show()
    }

}