package net.wandroid.transit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.result_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent().hasExtra(EXTRA_RESULT)){
            ResultAdapter adapter = new ResultAdapter(getIntent().getStringArrayListExtra(EXTRA_RESULT));
            recyclerView.setAdapter(adapter);
        }
    }

    private static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>{
        private final List<String> mData;

        private ResultAdapter(@NonNull List<String> data) {
            mData = data;
        }


        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),android.R.layout.simple_list_item_2,null);
            TextView title = (TextView) view.findViewById(android.R.id.text1);
            return new ResultViewHolder(view, title);
        }

        @Override
        public void onBindViewHolder(ResultViewHolder holder, int position) {
            String title = mData.get(position);
            holder.titleView.setText(title);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ResultViewHolder extends RecyclerView.ViewHolder{
            private TextView titleView;
            public ResultViewHolder(View itemView,TextView titleView) {
                super(itemView);
                this.titleView = titleView;
            }
        }
    }

    public static Intent createStartIntent(Context context, ArrayList<String> resultData){
        Intent intent = new Intent(context,ResultActivity.class);
        intent.putStringArrayListExtra(EXTRA_RESULT,resultData);
        return intent;
    }
}
