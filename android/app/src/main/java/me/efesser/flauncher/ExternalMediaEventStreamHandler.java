package me.efesser.flauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;

public class ExternalMediaEventStreamHandler implements EventChannel.StreamHandler
{
    private final Context _context;

    private BroadcastReceiver _receiver;

    public ExternalMediaEventStreamHandler(Context context)
    {
        _context = context;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events)
    {
        _receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }

                String action = intent.getAction();
                String name;

                switch (action) {
                    case Intent.ACTION_MEDIA_MOUNTED:
                        name = "MEDIA_INSERTED";
                        break;
                    case Intent.ACTION_MEDIA_REMOVED:
                    case Intent.ACTION_MEDIA_UNMOUNTED:
                    case Intent.ACTION_MEDIA_EJECT:
                    case Intent.ACTION_MEDIA_BAD_REMOVAL:
                        name = "MEDIA_REMOVED";
                        break;
                    default:
                        return;
                }

                Map<String, Object> event = new HashMap<>();
                event.put("name", name);

                Uri data = intent.getData();
                if (data != null && data.getPath() != null) {
                    event.put("path", data.getPath());
                }

                events.success(event);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addDataScheme("file");

        _context.registerReceiver(_receiver, filter);
    }

    @Override
    public void onCancel(Object arguments)
    {
        if (_receiver == null) {
            return;
        }

        try {
            _context.unregisterReceiver(_receiver);
        }
        catch (IllegalArgumentException ignored) { }
        finally {
            _receiver = null;
        }
    }
}
