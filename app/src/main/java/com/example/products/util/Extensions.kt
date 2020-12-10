package com.example.products.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


fun Fragment.setUpActionBar(actionbarTitle:String, displayUpIcon : Boolean = true){
    setHasOptionsMenu(true)
    (requireActivity() as AppCompatActivity).supportActionBar?.apply {
        show()
        setDisplayHomeAsUpEnabled(displayUpIcon)
        title = actionbarTitle

    }

}

fun ChipGroup.unCheckOtherChips(chipId : Int){
    for  (_id in 0 until childCount){
        val chip = getChildAt(_id)
        if (chip is Chip && chip.id != chipId){
            chip.isChecked = false
        }
    }

}