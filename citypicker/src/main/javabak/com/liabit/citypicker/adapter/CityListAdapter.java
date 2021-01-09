package com.liabit.citypicker.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liabit.citypicker.R;
import com.liabit.listpicker.HotItemListAdapter;
import com.liabit.listpicker.InnerListener;
import com.liabit.listpicker.ItemListAdapter;
import com.liabit.listpicker.decoration.GridItemDecoration;
import com.liabit.listpicker.model.Item;
import com.liabit.listpicker.model.HotListItem;
import com.liabit.listpicker.model.VariableState;
import com.liabit.listpicker.model.VariableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/5 12:06
 */
public class CityListAdapter extends RecyclerView.Adapter<ItemListAdapter.BaseViewHolder> {
    private static final int VIEW_TYPE_CURRENT = 10;
    private static final int VIEW_TYPE_HOT = 11;

    private boolean mMultiple;
    private Context mContext;
    private List<Item> mData;
    private List<HotListItem> mHotData;
    private int locateState;
    private com.liabit.listpicker.InnerListener mInnerListener;
    private LinearLayoutManager mLayoutManager;
    private boolean stateChanged;

    private List<Item> mSelected;

    public CityListAdapter(Context context, int locateState, boolean multiple) {
        this.mContext = context;
        this.locateState = locateState;
        this.mMultiple = multiple;
        if (multiple) {
            mSelected = new ArrayList<>();
        }
    }

    public void setCities(List<HotListItem> hotCities, List<Item> cities) {
        mData = cities;
        mHotData = hotCities;
        notifyDataSetChanged();
    }

    public List<Item> getSelected() {
        return mSelected;
    }

    public void setLayoutManager(LinearLayoutManager manager) {
        this.mLayoutManager = manager;
    }

    public void updateData(List<Item> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void updateLocateState(VariableItem location, int state) {
        mData.remove(0);
        mData.add(0, location);
        stateChanged = !(locateState == state);
        locateState = state;
        refreshLocationItem();
    }

    public void refreshLocationItem() {
        //如果定位城市的item可见则进行刷新
        if (stateChanged && mLayoutManager.findFirstVisibleItemPosition() == 0) {
            stateChanged = false;
            notifyItemChanged(0);
        }
    }

    /**
     * 滚动RecyclerView到索引位置
     *
     * @param index
     */
    public void scrollToSection(String index) {
        if (mData == null || mData.isEmpty()) return;
        if (TextUtils.isEmpty(index)) return;
        int size = mData.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(index.substring(0, 1), mData.get(i).getSection().substring(0, 1))) {
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(i, 0);
                    if (TextUtils.equals(index.substring(0, 1), "定")) {
                        //防止滚动时进行刷新
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (stateChanged) notifyItemChanged(0);
                            }
                        }, 1000);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_CURRENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_location_layout, parent, false);
                return new LocationViewHolder(view);
            case VIEW_TYPE_HOT:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_hot_layout, parent, false);
                return new HotViewHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_default_layout, parent, false);
                return new DefaultViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof DefaultViewHolder) {
            final int pos = holder.getAdapterPosition();
            final Item data = mData.get(pos);
            if (data == null) return;
            ((DefaultViewHolder) holder).setSelected(data.isSelected());
            ((DefaultViewHolder) holder).name.setText(data.getName());
            ((DefaultViewHolder) holder).selected.setVisibility(mMultiple ? View.VISIBLE : View.GONE);
            ((DefaultViewHolder) holder).name.setOnClickListener(v -> {
                if (mMultiple) {
                    if (mSelected.contains(data)) {
                        data.setSelected(false);
                        mSelected.remove(data);
                    } else {
                        data.setSelected(true);
                        mSelected.add(data);
                    }
                    notifyDataSetChanged();
                } else {
                    if (mInnerListener != null) {
                        mInnerListener.dismiss(data);
                    }
                }
            });
        }
        //定位城市
        if (holder instanceof LocationViewHolder) {
            final int pos = holder.getAdapterPosition();
            final Item data = mData.get(pos);
            if (data == null) return;
            //设置宽高
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.cpGridItemSpace, typedValue, true);
            int space = mContext.getResources().getDimensionPixelSize(R.dimen.cp_grid_item_space);
            int padding = mContext.getResources().getDimensionPixelSize(R.dimen.cp_default_padding);
            int indexBarWidth = mContext.getResources().getDimensionPixelSize(R.dimen.cp_index_bar_width);
            int itemWidth = (screenWidth - padding - space * (com.liabit.listpicker.HotItemListAdapter.SPAN_COUNT - 1) - indexBarWidth) / com.liabit.listpicker.HotItemListAdapter.SPAN_COUNT;
            ViewGroup.LayoutParams lp = ((LocationViewHolder) holder).container.getLayoutParams();
            lp.width = itemWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((LocationViewHolder) holder).container.setLayoutParams(lp);

            switch (locateState) {
                case VariableState.LOCATING:
                    ((LocationViewHolder) holder).current.setText(R.string.cp_locating);
                    break;
                case VariableState.SUCCESS:
                    ((LocationViewHolder) holder).current.setSelected(data.isSelected());
                    ((LocationViewHolder) holder).current.setText(data.getName());
                    break;
                case VariableState.FAILURE:
                    ((LocationViewHolder) holder).current.setText(R.string.cp_locate_failed);
                    break;
            }
            ((LocationViewHolder) holder).container.setOnClickListener(v -> {
                if (locateState == VariableState.SUCCESS) {
                    if (mMultiple) {
                        if (mSelected.contains(data)) {
                            data.setSelected(false);
                            mSelected.remove(data);
                        } else {
                            data.setSelected(true);
                            mSelected.add(data);
                        }
                        notifyDataSetChanged();
                    } else {
                        if (mInnerListener != null) {
                            mInnerListener.dismiss(data);
                        }
                    }
                } else if (locateState == VariableState.FAILURE) {
                    locateState = VariableState.LOCATING;
                    notifyItemChanged(0);
                    if (mInnerListener != null) {
                        mInnerListener.request();
                    }
                }
            });
        }
        //热门城市
        if (holder instanceof HotViewHolder) {
            final int pos = holder.getAdapterPosition();
            final Item data = mData.get(pos);
            if (data == null) return;
            final com.liabit.listpicker.HotItemListAdapter mAdapter = new com.liabit.listpicker.HotItemListAdapter(mContext, mHotData);
            mAdapter.setInnerListener(new com.liabit.listpicker.InnerListener() {
                @Override
                public void dismiss(Item data) {
                    if (mMultiple) {
                        if (mSelected.contains(data)) {
                            data.setSelected(false);
                            mSelected.remove(data);
                        } else {
                            data.setSelected(true);
                            mSelected.add(data);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (mInnerListener != null) {
                            mInnerListener.dismiss(data);
                        }
                    }
                }

                @Override
                public void request() {
                    mInnerListener.request();
                }
            });
            ((HotViewHolder) holder).mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && TextUtils.equals("定", mData.get(position).getSection().substring(0, 1)))
            return VIEW_TYPE_CURRENT;
        if (position < 2 && TextUtils.equals("热", mData.get(position).getSection().substring(0, 1)))
            return VIEW_TYPE_HOT;
        return super.getItemViewType(position);
    }

    public void setInnerListener(InnerListener listener) {
        this.mInnerListener = listener;
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DefaultViewHolder extends BaseViewHolder {
        TextView name;
        ImageView selected;

        DefaultViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cp_list_item_name);
            selected = itemView.findViewById(R.id.cp_selected);
        }

        public void setSelected(boolean select) {
            if (select) {
                this.selected.setImageResource(R.drawable.cp_selected);
                this.selected.setSelected(true);
            } else {
                this.selected.setImageResource(R.drawable.cp_unselected);
                this.selected.setSelected(false);
            }
        }
    }

    public static class HotViewHolder extends BaseViewHolder {
        RecyclerView mRecyclerView;

        HotViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.cp_hot_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(),
                    com.liabit.listpicker.HotItemListAdapter.SPAN_COUNT, RecyclerView.VERTICAL, false));
            int space = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.cp_grid_item_space);
            mRecyclerView.addItemDecoration(new GridItemDecoration(HotItemListAdapter.SPAN_COUNT,
                    space));
        }
    }

    public static class LocationViewHolder extends BaseViewHolder {
        FrameLayout container;
        TextView current;

        LocationViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.cp_list_item_location_layout);
            current = itemView.findViewById(R.id.cp_list_item_location);
        }
    }
}