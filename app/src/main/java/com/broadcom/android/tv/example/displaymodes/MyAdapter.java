package com.broadcom.android.tv.example.displaymodes;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements DisplayManager.DisplayListener {
    private final DisplayManager mDisplayManager;
    private Display.Mode[] mModes;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        MyViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    MyAdapter(Context context) {
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        refresh(false);
    }

    private void refresh(boolean notify) {
        Display display = mDisplayManager.getDisplay(Display.DEFAULT_DISPLAY);
        mModes = display.getSupportedModes();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String text = mModes[position].toString();
        // Mark current config with a star
        if (mDisplayManager.getDisplay(Display.DEFAULT_DISPLAY).getMode().toString().equals(text)) {
            text += "*";
        }
        holder.mTextView.setText(text);

    }

    @Override
    public int getItemCount() {
        return mModes.length;
    }

    // DisplayListener implementation

    @Override
    public void onDisplayAdded(int displayId) {
        if (displayId == Display.DEFAULT_DISPLAY) {
            refresh(true);
        }
    }

    @Override
    public void onDisplayRemoved(int displayId) {
        if (displayId == Display.DEFAULT_DISPLAY) {
            refresh(true);
        }
    }

    @Override
    public void onDisplayChanged(int displayId) {
        if (displayId == Display.DEFAULT_DISPLAY) {
            refresh(true);
        }
    }
}
