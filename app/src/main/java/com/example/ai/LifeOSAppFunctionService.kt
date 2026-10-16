@file:OptIn(ExperimentalAppFunctionsApi::class)

package com.example.ai

import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import androidx.appfunctions.AppFunctionSerializable
import androidx.appfunctions.ExperimentalAppFunctionsApi
import com.example.data.LifeOSDatabase
import com.example.data.model.BillEntity
import com.example.data.model.EventEntity
import com.example.data.model.HabitEntity
import com.example.data.model.NoteEntity
import com.example.data.model.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate

/** Parameters for creating a calendar event or meeting. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateEventParams(
    /** The title of the event or meeting. */
    val title: String,
    /** The date of the event in YYYY-MM-DD format. */
    val date: String,
    /** Start time in HH:mm format. */
    val startTime: String,
    /** End time in HH:mm format. */
    val endTime: String,
    /** Category such as Work, Personal, Health, Home, Study. */
    val category: String? = "Work",
    /** Optional location or notes. */
    val notes: String? = null
)

/** Result of creating an event. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class EventResult(
    /** ID of the created event. */
    val id: Long,
    /** Title of the event. */
    val title: String,
    /** Date of the event. */
    val date: String,
    /** Status message. */
    val message: String
)

/** Parameters for creating a task or to-do. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateTaskParams(
    /** The title of the task. */
    val title: String,
    /** Due date in YYYY-MM-DD format. */
    val date: String,
    /** Priority such as High, Medium, Low. */
    val priority: String? = "Medium",
    /** Estimated cost if applicable. */
    val cost: Double? = 0.0
)

/** Result of creating a task. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class TaskResult(
    /** ID of the created task. */
    val id: Long,
    /** Title of the task. */
    val title: String,
    /** Status message. */
    val message: String
)

/** Parameters for creating a personal note. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateNoteParams(
    /** The title of the note. */
    val title: String,
    /** The body or content of the note. */
    val body: String,
    /** Optional tag or topic. */
    val tag: String? = "General"
)

/** Result of creating a note. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class NoteResult(
    /** ID of the created note. */
    val id: Long,
    /** Title of the note. */
    val title: String,
    /** Status message. */
    val message: String
)

/** Parameters for creating a financial bill. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateBillParams(
    /** The name or title of the bill. */
    val title: String,
    /** Total bill amount. */
    val amount: Double,
    /** Due date in YYYY-MM-DD format. */
    val due: String,
    /** Category such as Housing, Utilities, Subscriptions, Food. */
    val category: String? = "Utilities"
)

/** Result of creating a bill. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class BillResult(
    /** ID of the created bill. */
    val id: Long,
    /** Title of the bill. */
    val title: String,
    /** Amount of the bill. */
    val amount: Double,
    /** Status message. */
    val message: String
)

/** Parameters for creating a habit tracker. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateHabitParams(
    /** The title of the habit. */
    val title: String,
    /** Frequency such as Daily, Weekly, Monthly. */
    val frequency: String? = "Daily",
    /** Category such as Health, Mindset, Fitness, Productivity. */
    val category: String? = "Personal"
)

/** Result of creating a habit. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class HabitResult(
    /** ID of the created habit. */
    val id: Long,
    /** Title of the habit. */
    val title: String,
    /** Status message. */
    val message: String
)

/** Parameters for updating a task status. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class MarkTaskCompletedParams(
    /** The database ID of the task. */
    val taskId: Long,
    /** Whether the task should be marked completed (true) or open (false). */
    val isDone: Boolean? = true
)

/** Parameters for querying events. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryEventsParams(
    /** Optional date filter in YYYY-MM-DD format. */
    val date: String? = null
)

/** Result of querying events. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryEventsResult(
    /** Count of matched events. */
    val totalCount: Int,
    /** Concise textual summary of matched events. */
    val summary: String
)

/** Parameters for querying tasks. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryTasksParams(
    /** Optional date filter in YYYY-MM-DD format. */
    val date: String? = null
)

/** Result of querying tasks. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryTasksResult(
    /** Count of matched tasks. */
    val totalCount: Int,
    /** Concise textual summary of matched tasks. */
    val summary: String
)

/** Parameters for querying notes. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryNotesParams(
    /** Optional search keyword or topic. */
    val searchQuery: String? = null
)

/** Result of querying notes. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class QueryNotesResult(
    /** Count of matched notes. */
    val totalCount: Int,
    /** Concise textual summary of matched notes. */
    val summary: String
)

/** Result of querying financial bills and expenses summary. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class MoneySummaryResult(
    /** Count of unpaid bills. */
    val pendingBillsCount: Int,
    /** Total amount of unpaid bills. */
    val totalPendingAmount: Double,
    /** Financial overview summary. */
    val summary: String
)

/** Workspace insights and summary. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class LifeOSInsightsResult(
    /** Total open tasks count. */
    val openTasksCount: Int,
    /** Today's scheduled events count. */
    val todayEventsCount: Int,
    /** Summary overview message. */
    val summary: String
)

@RequiresApi(36)
@AppFunctionServiceEntryPoint(
    serviceName = "LifeOSAppFunctionService",
    appFunctionXmlFileName = "lifeos_app_function_service",
)
abstract class BaseLifeOSAppFunctionService : AppFunctionService() {

    private val database by lazy {
        LifeOSDatabase.getDatabase(applicationContext)
    }

    /**
     * Creates a calendar event or meeting based on [createEventParams].
     *
     * @param createEventParams The parameter to describe the event details.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun createEvent(createEventParams: CreateEventParams): EventResult = withContext(Dispatchers.IO) {
        if (createEventParams.title.isBlank() || createEventParams.date.isBlank()) {
            throw AppFunctionInvalidArgumentException("Title and date must not be blank")
        }
        val event = EventEntity(
            title = createEventParams.title,
            date = createEventParams.date,
            startTime = createEventParams.startTime.ifBlank { "09:00" },
            endTime = createEventParams.endTime.ifBlank { "10:00" },
            category = createEventParams.category ?: "Work",
            description = createEventParams.notes ?: "",
            location = "",
            isAllDay = false,
            plannedCost = 0.0
        )
        val id = database.eventDao().insertEvent(event)
        EventResult(
            id = id,
            title = createEventParams.title,
            date = createEventParams.date,
            message = "Successfully scheduled event '${createEventParams.title}' on ${createEventParams.date} from ${createEventParams.startTime} to ${createEventParams.endTime}."
        )
    }

    /**
     * Creates a task or to-do item based on [createTaskParams].
     *
     * @param createTaskParams The parameter to describe the task details.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun createTask(createTaskParams: CreateTaskParams): TaskResult = withContext(Dispatchers.IO) {
        if (createTaskParams.title.isBlank()) {
            throw AppFunctionInvalidArgumentException("Task title must not be blank")
        }
        val today = LocalDate.now().toString()
        val task = TaskEntity(
            title = createTaskParams.title,
            date = createTaskParams.date.ifBlank { today },
            time = "12:00",
            priority = createTaskParams.priority ?: "Medium",
            cost = createTaskParams.cost ?: 0.0,
            isDone = false
        )
        val id = database.taskDao().insertTask(task)
        TaskResult(
            id = id,
            title = createTaskParams.title,
            message = "Successfully created task '${createTaskParams.title}' due on ${task.date} with ${task.priority} priority."
        )
    }

    /**
     * Creates a personal note based on [createNoteParams].
     *
     * @param createNoteParams The parameter to describe the note details.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun createNote(createNoteParams: CreateNoteParams): NoteResult = withContext(Dispatchers.IO) {
        if (createNoteParams.title.isBlank()) {
            throw AppFunctionInvalidArgumentException("Note title must not be blank")
        }
        val today = LocalDate.now().toString()
        val note = NoteEntity(
            title = createNoteParams.title,
            date = today,
            body = createNoteParams.body,
            tags = listOf(createNoteParams.tag ?: "General")
        )
        val id = database.noteDao().insertNote(note)
        NoteResult(
            id = id,
            title = createNoteParams.title,
            message = "Successfully saved note '${createNoteParams.title}'."
        )
    }

    /**
     * Creates a financial bill item based on [createBillParams].
     *
     * @param createBillParams The parameter to describe the bill details.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun createBill(createBillParams: CreateBillParams): BillResult = withContext(Dispatchers.IO) {
        if (createBillParams.title.isBlank() || createBillParams.amount <= 0) {
            throw AppFunctionInvalidArgumentException("Title must not be blank and amount must be positive")
        }
        val bill = BillEntity(
            title = createBillParams.title,
            amount = createBillParams.amount,
            due = createBillParams.due,
            category = createBillParams.category ?: "Utilities",
            paidAmount = 0.0,
            isPaid = false,
            repeat = "none"
        )
        val id = database.billDao().insertBill(bill)
        BillResult(
            id = id,
            title = createBillParams.title,
            amount = createBillParams.amount,
            message = "Successfully created bill '${createBillParams.title}' of LKR ${createBillParams.amount} due on ${createBillParams.due}."
        )
    }

    /**
     * Creates a habit tracker based on [createHabitParams].
     *
     * @param createHabitParams The parameter to describe the habit details.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun createHabit(createHabitParams: CreateHabitParams): HabitResult = withContext(Dispatchers.IO) {
        if (createHabitParams.title.isBlank()) {
            throw AppFunctionInvalidArgumentException("Habit title must not be blank")
        }
        val habit = HabitEntity(
            title = createHabitParams.title,
            frequency = createHabitParams.frequency ?: "Daily",
            streak = 0,
            lastLoggedDate = "",
            notes = "",
            tags = listOf(createHabitParams.category ?: "Personal")
        )
        val id = database.habitDao().insertHabit(habit)
        HabitResult(
            id = id,
            title = createHabitParams.title,
            message = "Successfully created habit '${createHabitParams.title}'."
        )
    }

    /**
     * Marks a task as completed or open based on [markTaskCompletedParams].
     *
     * @param markTaskCompletedParams The parameter to identify the task and status.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun markTaskCompleted(markTaskCompletedParams: MarkTaskCompletedParams): TaskResult = withContext(Dispatchers.IO) {
        val isDone = markTaskCompletedParams.isDone ?: true
        database.taskDao().setDone(markTaskCompletedParams.taskId, isDone)
        val statusText = if (isDone) "completed" else "reopened"
        TaskResult(
            id = markTaskCompletedParams.taskId,
            title = "Task #${markTaskCompletedParams.taskId}",
            message = "Successfully $statusText task #${markTaskCompletedParams.taskId}."
        )
    }

    /**
     * Queries scheduled calendar events based on [queryEventsParams].
     *
     * @param queryEventsParams The parameter filter for events query.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun queryEvents(queryEventsParams: QueryEventsParams): QueryEventsResult = withContext(Dispatchers.IO) {
        val allEvents = database.eventDao().getAllEvents().first()
        val filterDate = queryEventsParams.date
        val filtered = if (!filterDate.isNullOrBlank()) {
            allEvents.filter { it.date == filterDate }
        } else {
            allEvents
        }
        val summaryText = if (filtered.isEmpty()) {
            "No events found."
        } else {
            filtered.take(5).joinToString("; ") { "${it.title} on ${it.date} at ${it.startTime}" }
        }
        QueryEventsResult(
            totalCount = filtered.size,
            summary = summaryText
        )
    }

    /**
     * Queries tasks based on [queryTasksParams].
     *
     * @param queryTasksParams The parameter filter for tasks query.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun queryTasks(queryTasksParams: QueryTasksParams): QueryTasksResult = withContext(Dispatchers.IO) {
        val allTasks = database.taskDao().getAllTasks().first()
        val filterDate = queryTasksParams.date
        val filtered = if (!filterDate.isNullOrBlank()) {
            allTasks.filter { it.date == filterDate }
        } else {
            allTasks
        }
        val openTasks = filtered.filter { !it.isDone }
        val summaryText = if (openTasks.isEmpty()) {
            "No active open tasks found."
        } else {
            openTasks.take(5).joinToString("; ") { "${it.title} (due ${it.date}, ${it.priority} priority)" }
        }
        QueryTasksResult(
            totalCount = openTasks.size,
            summary = summaryText
        )
    }

    /**
     * Queries personal notes based on [queryNotesParams].
     *
     * @param queryNotesParams The parameter filter for notes query.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun queryNotes(queryNotesParams: QueryNotesParams): QueryNotesResult = withContext(Dispatchers.IO) {
        val allNotes = database.noteDao().getAllNotes().first()
        val query = queryNotesParams.searchQuery
        val filtered = if (!query.isNullOrBlank()) {
            allNotes.filter { it.title.contains(query, ignoreCase = true) || it.body.contains(query, ignoreCase = true) }
        } else {
            allNotes
        }
        val summaryText = if (filtered.isEmpty()) {
            "No notes found matching query."
        } else {
            filtered.take(5).joinToString("; ") { "${it.title}: ${it.body.take(40)}..." }
        }
        QueryNotesResult(
            totalCount = filtered.size,
            summary = summaryText
        )
    }

    /**
     * Queries financial overview including pending bills and total unpaid amounts.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun queryMoneySummary(): MoneySummaryResult = withContext(Dispatchers.IO) {
        val allBills = database.billDao().getAllBills().first()
        val unpaidBills = allBills.filter { !it.isPaid }
        val totalUnpaid = unpaidBills.sumOf { it.amount - it.paidAmount }
        val summaryText = if (unpaidBills.isEmpty()) {
            "All bills are fully paid. No pending bills due."
        } else {
            "Pending bills (${unpaidBills.size}): " + unpaidBills.take(5).joinToString("; ") { "${it.title} (due ${it.due}, amount LKR ${it.amount})" }
        }
        MoneySummaryResult(
            pendingBillsCount = unpaidBills.size,
            totalPendingAmount = totalUnpaid,
            summary = summaryText
        )
    }

    /**
     * Retrieves workspace insights, open tasks count, and today's schedule summary.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun getLifeOSInsights(): LifeOSInsightsResult = withContext(Dispatchers.IO) {
        val today = LocalDate.now().toString()
        val openTasksCount = database.taskDao().getAllTasks().first().count { !it.isDone }
        val todayEventsCount = database.eventDao().getEventsForDate(today).first().size
        LifeOSInsightsResult(
            openTasksCount = openTasksCount,
            todayEventsCount = todayEventsCount,
            summary = "LifeOS Space is active. Today ($today): $todayEventsCount scheduled events, $openTasksCount open tasks. AI assistant has full control to create, query, and manage events, tasks, notes, habits, and financial bills."
        )
    }
}
