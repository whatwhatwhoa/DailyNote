package io.github.whatwhatwhoa.dailynote.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.net.toUri
import io.github.whatwhatwhoa.dailynote.MainActivity
import io.github.whatwhatwhoa.dailynote.PaperArt
import io.github.whatwhatwhoa.dailynote.R

/**
 * The note on the home screen, at any size up to a whole page. It shows the page and scrolls;
 * a tap on a line opens the app with the cursor on that line. Writing and erasing happen in the
 * app: a home screen can't host a text field or tell a swipe on a widget from a page turn.
 */
class NoteWidget : AppWidgetProvider() {
    // The application context, not the receiver's: newer Android binds LinesService from the
    // caller's context to fetch the lines, and that binding outlives a receiver or an activity.
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        val app = context.applicationContext
        for (id in ids) update(app, AppWidgetManager.getInstance(app), id)
    }

    override fun onAppWidgetOptionsChanged(context: Context, manager: AppWidgetManager, id: Int, options: Bundle) {
        val app = context.applicationContext
        val m = AppWidgetManager.getInstance(app)
        update(app, m, id)
        m.notifyAppWidgetViewDataChanged(id, R.id.lines)
    }

    companion object {
        /** Redraws every placed widget: after the app leaves the screen, as the note or paper changed. */
        fun refresh(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, NoteWidget::class.java))
            if (ids.isEmpty()) return
            for (id in ids) update(context, manager, id)
            manager.notifyAppWidgetViewDataChanged(ids, R.id.lines)
        }

        private fun update(context: Context, manager: AppWidgetManager, id: Int) {
            val page = WidgetPage(context, manager, id)
            val views = RemoteViews(context.packageName, R.layout.widget_note)
            views.setInt(R.id.paper, "setColorFilter", PaperArt.paperColor(page.paper))
            views.setImageViewBitmap(R.id.band, page.band())

            val open = Intent(context, MainActivity::class.java)
            views.setOnClickPendingIntent(
                R.id.band, PendingIntent.getActivity(context, 0, open, PendingIntent.FLAG_IMMUTABLE),
            )
            // Each line fills in where the cursor goes (LinesService), so the template is mutable.
            views.setPendingIntentTemplate(
                R.id.lines,
                PendingIntent.getActivity(
                    context, 1, Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
                ),
            )
            // One adapter per widget: the data URI keeps the system from sharing one between them.
            val adapter = Intent(context, LinesService::class.java).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            adapter.data = adapter.toUri(Intent.URI_INTENT_SCHEME).toUri()
            @Suppress("DEPRECATION")
            views.setRemoteAdapter(R.id.lines, adapter)
            manager.updateAppWidget(id, views)
        }
    }
}
