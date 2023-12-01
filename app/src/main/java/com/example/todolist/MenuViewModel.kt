package com.example.todolist

import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//class MenuViewModel : ViewModel() {
//    private val _menuListLiveData = MutableLiveData<ListData>()
//    val menuListLiveData: LiveData<ListData>
//        get() = _menuListLiveData
//
////    fun getMenuList(): LiveData<List<com.example.todolist.Menu>> {
////        // Di sini, Anda dapat mengambil data menu dari sumber daya atau repository
////        // Misalnya, Anda dapat menggantinya dengan repository.getDataMenu()
////
////        // Contoh menu untuk pengujian
////        val menuList = listOf(
////            com.example.todolist.Menu("Tugas 1", "Deskripsi tugas 1", "01/01/2023", "12:00"),
////            com.example.todolist.Menu("Tugas 2", "Deskripsi tugas 2", "02/01/2023", "14:30"),
////            // Tambahkan menu lainnya sesuai kebutuhan
////        )
////
////        // Perbarui nilai LiveData
////        _menuListLiveData.value = menuList
////
////        return menuListLiveData
////    }
//
//    fun saveListData(judul: String, deskripsi: String, tanggal: String, jam: String) {
//        val listt = ListData(judul, deskripsi, tanggal, jam)
//        _menuListLiveData.value = listt
//    }
//}


class MenuViewModel : ViewModel() {
    private val _menuListLiveData = MutableLiveData<List<ListData>>()
    val menuListLiveData: LiveData<List<ListData>>
        get() = _menuListLiveData

//    fun getMenuList(): LiveData<List<ListData>> {
//        // You can fetch data from your data source or repository here
//        // For now, I'll use a sample list for testing
//
//        val menuList = listOf(
//            ListData("Tugas 1", "Deskripsi tugas 1", "01/01/2023", "12:00"),
//            ListData("Tugas 2", "Deskripsi tugas 2", "02/01/2023", "14:30"),
//            // Add more items as needed
//        )
//
//        // Update the LiveData value
//        _menuListLiveData.value = menuList
//
//        return menuListLiveData
//    }

    fun addMenuToList(judul: String, deskripsi: String, tanggal: String, jam: String) {
        val currentList = _menuListLiveData.value.orEmpty().toMutableList()
        currentList.add(ListData(judul, deskripsi, tanggal, jam))
        _menuListLiveData.value = currentList
    }
}
