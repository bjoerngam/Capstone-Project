package com.example.android.stolpersteinear;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.stolpersteinear.data.database.StolperSteineContract;

/**
 * Created by bjoern on 08.09.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The whole widget magic. Here we are displaying the widget
 * and the displaying the name of the local store victims.
 */
class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private final String[] projection = new String[]{
            StolperSteineContract.InventoryEntry._ID,
            StolperSteineContract.InventoryEntry.Description,
            StolperSteineContract.InventoryEntry.Image,
            StolperSteineContract.InventoryEntry.Street,
            StolperSteineContract.InventoryEntry.Website,
            StolperSteineContract.InventoryEntry.Name,
            StolperSteineContract.InventoryEntry.Date_of_death,
            StolperSteineContract.InventoryEntry.Date_of_birth,
            StolperSteineContract.InventoryEntry.Latitude,
            StolperSteineContract.InventoryEntry.Longitude
    };
    private Cursor cursor;
    private Context context;
    private int appWidgetId;

    ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = context.getContentResolver().query(StolperSteineContract.InventoryEntry.CONTENT_URI, projection, null,
                null, null);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_item_widget);

        if (cursor.moveToPosition(position)) {
            remoteView.setTextViewText(R.id.tvWidgetVictimName,
                    cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Name)));
        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
