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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> implements INameableAdapter {

    private Context context;
    ArrayList<WordDetail> wordList;
    private OnItemClickListener mListener;

    public HistoryAdapter(Context context, ArrayList<WordDetail> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    @Override
    public Character getCharacterForElement(int element) {
        Character c = wordList.get(element).getWord().charAt(0);
        if(Character.isDigit(c)) {
            c = '#';
        }
        return c;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ArrayList<WordDetail> wordList;
        private TextView txtWord,txtAnotherWord,txtType;
        private ImageView voiceHistory;


        public CustomViewHolder(View itemView, Context context, ArrayList<WordDetail> wordList) {
            super(itemView);
            this.context = context;
            this.wordList = wordList;
            txtWord = (TextView) itemView.findViewById(R.id.textView_history);
            txtAnotherWord = (TextView) itemView.findViewById(R.id.textView_another_word_history);
            txtType = (TextView) itemView.findViewById(R.id.textView_word_type_history);
            voiceHistory = itemView.findViewById(R.id.icon_voice_history);

            txtWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view,getLayoutPosition());
                }
            });

            voiceHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view,getLayoutPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
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
        View view = inflater.inflate(R.layout.item_history, null);
        HistoryAdapter.CustomViewHolder viewHolder = new HistoryAdapter.CustomViewHolder(view, context, wordList);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.txtWord.setText(wordList.get(position).getWord());
        holder.txtAnotherWord.setText("/"+wordList.get(position).getMeaning()+"/");
        holder.txtType.setText("/"+wordList.get(position).getType()+"/");

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
    public void setItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

}
