package com.gkt.browse.dictionary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyRecylerAdapter extends RecyclerView.Adapter<MyRecylerAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    public MyRecylerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyRecylerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.first_card,viewGroup,false);

        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String text = "..unavailable..";
        String definition="..unavailable..";
        String domain="..unavailable..";
        String type = "..unavailable..";
        String example = "..unavailable..";
        String etym="..unavailable..";
       String count = String.valueOf(i+1);
        if(MainActivity.data.get(i).getText()!=null)
        {
            text = count;
            text+="Text\n";
            text += MainActivity.data.get(i).getText();
        }

        if(MainActivity.data.get(i).getDefinition()!=null)
        {
           definition =  count;

            definition += ".Definition:\n";

            definition += MainActivity.data.get(i).getDefinition();
        }

        if(MainActivity.data.get(i).getDomain()!=null)
        {

            domain = count;
            domain += ".Domains:\n";
            domain += MainActivity.data.get(i).getDomain();
        }

        if(MainActivity.data.get(i).getType()!=null)
        {
            type = count;

            type += ".Type:\n";
            type += MainActivity.data.get(i).getType();
        }

        if(MainActivity.data.get(i).getExample()!=null)
        {

            example = count;
            example += ".Example:\n";
            example += MainActivity.data.get(i).getExample();
        }

        if(MainActivity.data.get(i).getEtymology()!=null)
        {

            etym = count;
            etym += ".Etymology:\n";
            etym += MainActivity.data.get(i).getEtymology();
        }

        myViewHolder.def.setText(definition);
        myViewHolder.type.setText(type);
        myViewHolder.domain.setText(domain);
        myViewHolder.example.setText(example);
        myViewHolder.etym.setText(etym);
        myViewHolder.text.setText(text);


    }



    @Override
    public int getItemCount() {


        return MainActivity.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView def;
        TextView domain;
        TextView example;

        TextView etym;
        TextView text;
        TextView type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            def = (TextView) itemView.findViewById(R.id.def);
            domain = (TextView) itemView.findViewById(R.id.domain);
            example = (TextView)itemView.findViewById(R.id.example);
            etym = (TextView) itemView.findViewById(R.id.etym);
            text = (TextView) itemView.findViewById(R.id.text);
            type = (TextView) itemView.findViewById(R.id.type);
        }
    }
}
