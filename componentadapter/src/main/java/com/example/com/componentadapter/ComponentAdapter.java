package com.example.com.componentadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.CommonViewHolder> {
    private Context mContext;
    private List<? extends IDataComponent> mData = new ArrayList<>();
    private ItemComponentManager mItemComponentManager;

    public ComponentAdapter(Context context) {
        mContext = context;
        mItemComponentManager = new ItemComponentManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!hasRegisteredItemComponent()) {
            return super.getItemViewType(position);
        }
        return mItemComponentManager.getItemViewType(mData.get(position), position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IItemComponent itemComponent;
        try {
            itemComponent = mItemComponentManager.getItemComponent(viewType).getClass()
                    .newInstance();
            int layoutId = itemComponent.getItemViewLayoutId();
            View mView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            CommonViewHolder holder = new CommonViewHolder(mView);
            itemComponent.onViewCreated(mView);
            holder.setItemComponent(itemComponent);
            return holder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("cannot create viewHolder");
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        holder.convert(this, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewRecycled(CommonViewHolder holder) {
        holder.onViewRecycled();
    }

    @Override
    public void onViewAttachedToWindow(CommonViewHolder holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(CommonViewHolder holder) {
        holder.onViewDetachedFromWindow();
    }

    public ComponentAdapter() {
        super();
    }

    public List<? extends IDataComponent> getData() {
        return mData;
    }

    public ComponentAdapter setData(@NonNull List<? extends IDataComponent> data) {
        mData = data;
        return this;
    }

    public ComponentAdapter registerItemComponent(IItemComponent itemComponent) {
        mItemComponentManager.registerItemComponent(itemComponent);
        return this;
    }

    public boolean removeItemComponent(@NonNull IItemComponent itemComponent) {
        return mItemComponentManager.removeItemComponent(itemComponent);
    }

    public boolean removeItemComponent(int itemType) {
        return mItemComponentManager.removeItemComponent(itemType);
    }

    private boolean hasRegisteredItemComponent() {
        return mItemComponentManager.getItemComponentCount() > 0;
    }

    public int getItemViewLayoutId(int viewType) {
        return mItemComponentManager.getItemViewLayoutId(viewType);
    }

    int getItemViewType(IItemComponent itemComponent) {
        return mItemComponentManager.getItemViewType(itemComponent);
    }

    public static class CommonViewHolder extends RecyclerView.ViewHolder {

        private IItemComponent itemComponent;

        private View view;

        public View getView() {
            return view;
        }

        public IItemComponent getItemComponent() {
            return itemComponent;
        }

        public void setItemComponent(IItemComponent itemComponent) {
            this.itemComponent = itemComponent;
        }

        public CommonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void convert(RecyclerView.Adapter adapter, IDataComponent t, int position) {
            if (itemComponent == null)
                return;
            itemComponent.bindView(adapter, t, position);
        }

        public void onViewRecycled() {
            if (itemComponent != null) itemComponent.onViewRecycled();
        }

        public void onViewAttachedToWindow() {
            if (itemComponent != null) itemComponent.onViewAttachedToWindow();
        }

        public void onViewDetachedFromWindow() {
            if (itemComponent != null) itemComponent.onViewDetachedFromWindow();
        }
    }

    public interface IDataComponent {
        int getViewType();
    }

    public interface IItemComponent {

        int getItemComponentType();

        int getItemViewLayoutId();

        void onViewCreated(View view);

        void bindView(RecyclerView.Adapter adapter, IDataComponent t, int position);

        void onViewRecycled();

        void onViewAttachedToWindow();

        void onViewDetachedFromWindow();
    }

    public static abstract class EmptyItemComponent implements IItemComponent {
        @Override
        public void onViewRecycled() {

        }

        @Override
        public void onViewAttachedToWindow() {

        }

        @Override
        public void onViewDetachedFromWindow() {

        }
    }

    private class ItemComponentManager {

        private SparseArrayCompat<IItemComponent> itemComponents = new SparseArrayCompat<>();

        int getItemComponentCount() {
            return itemComponents.size();
        }

        void registerItemComponent(@NonNull IItemComponent itemComponent) {
            int viewType = itemComponent.getItemComponentType();
            if (itemComponents.get(viewType) != null) {
                throw new IllegalArgumentException(
                        "An IItemComponent is already registered for the viewType = " + viewType
                                + ". Already registered IItemComponent is "
                                + itemComponents.get(viewType));
            }
            itemComponents.put(viewType, itemComponent);
        }

        boolean removeItemComponent(@NonNull IItemComponent itemComponent) {
            int indexToRemove = itemComponents.indexOfValue(itemComponent);

            if (indexToRemove >= 0) {
                itemComponents.removeAt(indexToRemove);
                return true;
            }
            return false;
        }

        boolean removeItemComponent(int itemType) {
            int indexToRemove = itemComponents.indexOfKey(itemType);

            if (indexToRemove >= 0) {
                itemComponents.removeAt(indexToRemove);
                return true;
            }
            return false;
        }

        int getItemViewType(@NonNull IDataComponent item, int position) {
            int viewType = item.getViewType();
            IItemComponent itemComponent = itemComponents.get(viewType);
            if (itemComponent == null) {
                throw new IllegalArgumentException("No IItemComponent added that matches position="
                        + position + " in data source");
            }
            return viewType;
        }

        IItemComponent getItemComponent(int viewType) {
            return itemComponents.get(viewType);
        }

        int getItemViewLayoutId(int viewType) {
            if (getItemComponent(viewType) == null)
                return -1;
            return getItemComponent(viewType).getItemViewLayoutId();
        }

        int getItemViewType(IItemComponent itemComponent) {
            return itemComponents.indexOfValue(itemComponent);
        }
    }
}
