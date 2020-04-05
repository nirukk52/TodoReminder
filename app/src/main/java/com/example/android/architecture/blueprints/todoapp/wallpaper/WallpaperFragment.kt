package com.example.android.architecture.blueprints.todoapp.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.WallpaperFragBinding
import com.example.android.architecture.blueprints.todoapp.tasks.TasksAdapter
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFragmentArgs
import com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel
import com.example.android.architecture.blueprints.todoapp.util.BitmapUtils
import com.example.android.architecture.blueprints.todoapp.util.getViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WallpaperFragment : Fragment(), View.OnTouchListener{

    val TAG = "TasksFragment"
    private val viewModel by viewModels<TasksViewModel> { getViewModelFactory() }

    private val args: TasksFragmentArgs by navArgs()

    private lateinit var viewDataBinding: WallpaperFragBinding

    private lateinit var listAdapter: TasksAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = WallpaperFragBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewDataBinding.tvTask.setOnTouchListener(this)

        viewDataBinding.btSetWp.setOnClickListener { setWallpaper() }
        super.onViewCreated(view, savedInstanceState)
    }

    fun setWallpaper() {
        val job = GlobalScope.launch {

            val wm = WallpaperManager.getInstance(context)
            val wallpaper = wm.drawable

            wm.run {
                clear()
                setBitmap(BitmapUtils.setViewToBitmapImage(viewDataBinding.flWallpaper))
            }

        }

        job.invokeOnCompletion {
//            Toast.makeText(context, "Wallpaper Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    var dX = 0f
    var dY = 0f
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> view.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(0)
                    .start()
            else -> return false
        }
        return true
    }





}
