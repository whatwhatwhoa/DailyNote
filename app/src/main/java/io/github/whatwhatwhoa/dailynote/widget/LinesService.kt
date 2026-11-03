package io.github.whatwhatwhoa.dailynote.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.github.whatwhatwhoa.dailynote.Jump
import io.github.whatwhatwhoa.dailynote.R

/** Feeds the widget's list its lines: the paper strip, the ink, and where a tap goes. */
class LinesService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        Lines(applicationContext, intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID))
}

private class Lines(private val context: Context, private val id: Int) : RemoteViewsService.RemoteViewsFactory {
    private var page: WidgetPage? = null

    override fun onCreate() {}

    override fun onDataSetChanged() {
        page = WidgetPage(context, AppWidgetManager.getInstance(context), id)
    }

    override fun onDestroy() {
        page = null
    }

    override fun getCount() = page?.rows ?: 0

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_row)
        val p = page ?: return views
        views.setImageViewBitmap(R.id.strip, p.strip(position))
        val ink = p.ink(position)
        if (ink != null) {
            views.setImageViewBitmap(R.id.ink, ink.first)
            views.setViewPadding(R.id.ink, p.textStart + ink.second, 0, 0, 0)
        } else {
            views.setViewVisibility(R.id.ink, View.GONE)
        }
        views.setContentDescription(R.id.row, p.line(position).trim().ifEmpty { context.getString(R.string.widget_blank_line) })
        val (offset, newlines) = p.jumpFor(position)
        views.setOnClickFillInIntent(
            R.id.row, Intent().putExtra(Jump.OFFSET, offset).putExtra(Jump.NEWLINES, newlines),
        )
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = true
}
