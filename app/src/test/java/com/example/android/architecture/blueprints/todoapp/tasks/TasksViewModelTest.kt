package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.example.android.architecture.blueprints.todoapp.itIs
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Brandon Quintanilla on May/26/2022.
 */
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    /*
    * This rule runs all Architecture Components-related background jobs in the same thread
    * so that the test results happen synchronously, and in a repeatable order.
    * When you write tests that include testing LiveData, use this rule!
    * */
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    /**
    * you can use the @Before annotation to create a setup method and remove repeated code
    * */
    @Before
    fun setupViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // Given a fresh ViewModel
//        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible

        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), itIs(true))
    }

    @Test
    fun addNewTask_setsNewTaskEvent_short() {

        // Given a fresh ViewModel
//        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))

    }


    @Test
    fun addNewTask_setsNewTaskEvent_long() {

        // Given a fresh TasksViewModel
//        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())


        // Create observer - no need for it to do anything!
        val observer = Observer<Event<Unit>> {}
        try {

            // Observe the LiveData forever
            tasksViewModel.newTaskEvent.observeForever(observer)

            // When adding a new task
            tasksViewModel.addNewTask()

            // Then the new task event is triggered
            val value = tasksViewModel.newTaskEvent.value
            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        } finally {
            // Whatever happens, don't forget to remove the observer!
            tasksViewModel.newTaskEvent.removeObserver(observer)
        }

    }

}