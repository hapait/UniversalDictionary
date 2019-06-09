package com.mcc.dictionary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcc.dictionary.R;
import com.mcc.dictionary.listener.OnItemClickListener;
import com.mcc.dictionary.model.WordDetail;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.CustomViewHolder> implements INameableAdapter {

    private Context context;
    private ArrayList<WordDetail> dataList,filterList;
    private OnItemClickListener mListener;

    public WordListAdapter(Context context, ArrayList<WordDetail> wordList) {
        this.context = context;
        this.dataList = wordList;
        this.filterList = wordList;
    }

    @Override
    public Character getCharacterForElement(int element) {
        Character c = dataList.get(element).getWord().charAt(0);
        if(Character.isDigit(c)) {
            c = '#';
        }
        return c;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ArrayList<WordDetail> wordList;
        TextView txtWord,txtAnotherWord,txtType;
        ImageView imgPronounc;
        String word=null;
        String anotherWord=null;

        public CustomViewHolder(View itemView, Context context, ArrayList<WordDetail> wordList) {
            super(itemView);
            this.context = context;
            this.wordList = wordList;
            txtWord = (TextView) itemView.findViewById(R.id.textView_wordlist);
            txtAnotherWord = (TextView) itemView.findViewById(R.id.textView_another_word);
            txtType = (TextView) itemView.findViewById(R.id.textView_word_type);
            imgPronounc=itemView.findViewById(R.id.imageView_pronounce);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view,getLayoutPosition());
                }
            });
            imgPronounc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view,getLayoutPosition());
                }
            });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_wordlist, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view, context, dataList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        holder.word= dataList.get(position).getWord();
        holder.anotherWord= dataList.get(position).getMeaning();
        holder.txtWord.setText(holder.word);
        holder.txtAnotherWord.setText("/"+holder.anotherWord+"/");
        holder.txtType.setText(dataList.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setFilter(ArrayList<WordDetail> newDataList) {
        dataList =new ArrayList<>();
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    public ArrayList<WordDetail> getDataList() {
        return dataList;
    }

    public void setItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

}
