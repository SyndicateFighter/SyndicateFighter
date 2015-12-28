package com.arcfighter.syndicateprojectgroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexander.syndicatefighter.Backpack;
import com.alexander.syndicatefighter.Item.Item;

import java.util.List;

/**
 * Created by alex7370 on 12/28/2015.
 */
public class BackpackContentsFragment extends Fragment {

    private RecyclerView mBackpackRecyclerView;
    private BackpackAdapter mBackpackAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backpack_contents, container, false);
        mBackpackRecyclerView = (RecyclerView) view.findViewById(R.id.backpack_recycler_view);
        mBackpackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void updateUI() throws Exception {
        Backpack backpack = new Backpack(); // This should be modified to be a singleton at some point so it actually works right
        List<Pair<Item, Integer>> items = backpack.getItems();

        mBackpackAdapter = new BackpackAdapter(items);
        mBackpackRecyclerView.setAdapter(mBackpackAdapter);

        if(mBackpackAdapter == null) {
            mBackpackAdapter = new BackpackAdapter(items);
            mBackpackRecyclerView.setAdapter(mBackpackAdapter);
        } else {
            mBackpackAdapter.setItemPair(items);
            mBackpackAdapter.notifyDataSetChanged();
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private Item mItem;
        private Backpack mBackpack;

        private TextView mItemNameTextView;
        private TextView mItemCountTextView;

        public void bindItems(Pair<Item, Integer> itemPair) {

            mItemCountTextView.setText(itemPair.second);
            mItemNameTextView.setText(itemPair.first.getName());

        }

        public ItemHolder(View itemView) {
            super(itemView);

            mItemNameTextView = (TextView) itemView.findViewById(R.id.list_item_inbackpack_name_text_view);
            mItemCountTextView = (TextView) itemView.findViewById(R.id.list_item_inbackpack_count_text_view);
        }
    }

    private class BackpackAdapter extends RecyclerView.Adapter<ItemHolder> {

        private Backpack mBackpack;
        private List<Pair<Item, Integer>> itemPair;

        public BackpackAdapter(List<Pair<Item, Integer>> items) { itemPair = items; }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_inbackpack, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            try {
                List<Pair<Item, Integer>> items = mBackpack.getItems();
                Pair<Item, Integer> item = items.get(position);
                holder.bindItems(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return itemPair.size();
        }

        public void setItemPair(List<Pair<Item, Integer>> items) { itemPair = items; }
    }

}

