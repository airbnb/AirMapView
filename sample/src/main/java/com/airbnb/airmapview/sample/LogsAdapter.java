package com.airbnb.airmapview.sample;

import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  @IntDef({VIEW_TYPE_STRING, VIEW_TYPE_BITMAP})
  @Retention(RetentionPolicy.SOURCE)
  private @interface ViewType { }
  private static final int VIEW_TYPE_STRING = 1;
  private static final int VIEW_TYPE_BITMAP = 2;

  private final List<Object> logs = new ArrayList<>();

  public void addString(String string) {
    logs.add(string);
    notifyItemInserted(logs.size() - 1);
  }

  public void addBitmap(Bitmap bitmap) {
    logs.add(bitmap);
    notifyItemInserted(logs.size() - 1);
  }

  public void clearLogs() {
    logs.clear();
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_STRING:
        return new StringViewHolder(parent);
      case VIEW_TYPE_BITMAP:
        return new BitmapViewHolder(parent);
      default:
        throw new IllegalArgumentException("Can't make ViewHolder of type " + viewType);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Object log = logs.get(position);

    switch (holder.getItemViewType()) {
      case VIEW_TYPE_STRING:
        ((StringViewHolder) holder).bind((String) log);
        break;
      case VIEW_TYPE_BITMAP:
        ((BitmapViewHolder) holder).bind((Bitmap) log);
        break;
      default:
        throw new IllegalArgumentException("Can't bind view holder of type " + holder.getItemViewType());
    }
  }

  @ViewType
  @Override
  public int getItemViewType(int position) {
    Object log = logs.get(position);
    if (log instanceof String) {
      return VIEW_TYPE_STRING;
    } else if (log instanceof Bitmap) {
      return VIEW_TYPE_BITMAP;
    } else {
      throw new IllegalArgumentException("Unknown object of type " + log.getClass());
    }
  }

  @Override
  public int getItemCount() {
    return logs.size();
  }

  private static final class StringViewHolder extends RecyclerView.ViewHolder {

    public StringViewHolder(ViewGroup parent) {
      super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, parent, false));
    }

    public void bind(String string) {
      ((TextView) itemView).setText(string);
    }
  }

  private static final class BitmapViewHolder extends RecyclerView.ViewHolder {

    public BitmapViewHolder(ViewGroup parent) {
      super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bitmap, parent, false));
    }

    public void bind(Bitmap bitmap) {
      ((ImageView) itemView).setImageBitmap(bitmap);
    }
  }
}
