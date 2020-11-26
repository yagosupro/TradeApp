package pro.evgen.tradeapp.adapters;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import pro.evgen.tradeapp.R;
import pro.evgen.tradeapp.pojo.Trade;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.TradeViewHolder> {

    private List<Trade> tradeInfoList;
    private Consumer<Integer> onShowMoreClickListener;

    public void setOnShowMoreClickListener(Consumer<Integer> onShowMoreClickListener) {
        this.onShowMoreClickListener = onShowMoreClickListener;
    }

    public TradeAdapter() {
        tradeInfoList = new ArrayList<>();
    }

    public void setTradeInfoList(List<Trade> tradeInfoList) {
        this.tradeInfoList = tradeInfoList;
        notifyDataSetChanged();
    }

    public List<Trade> getTradeInfoList() {
        return tradeInfoList;
    }

    @NonNull
    @Override
    public TradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new TradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeViewHolder holder, int position) {
        Trade tradeInfo = tradeInfoList.get(position);
        holder.textViewAmount.setText(String.valueOf(Math.floor(tradeInfo.getAmount() * 100) / 100.0));
        holder.textViewCoin.setText(tradeInfo.getCoin());
        holder.textViewOtherCoin.setText(tradeInfo.getOtherCoin());
        holder.textViewOtherAmount.setText(String.valueOf(Math.floor(tradeInfo.getOtherAmount() * 100) / 100.0));


        holder.textViewAmount.setShadowLayer(0, 0, 0, 0);
        holder.textViewOtherAmount.setShadowLayer(0, 0, 0, 0);
        holder.textViewOtherCoin.setShadowLayer(0, 0, 0, 0);
        holder.textViewCoin.setShadowLayer(0, 0, 0, 0);
        holder.textViewCoin.setTextColor(Color.parseColor("#ABABAB"));
        holder.textViewOtherAmount.setTextColor(Color.parseColor("#ABABAB"));
        holder.textViewAmount.setTextColor(Color.parseColor("#ABABAB"));
        holder.textViewOtherCoin.setTextColor(Color.parseColor("#ABABAB"));

        holder.imageViewShowMore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (onShowMoreClickListener != null){
                    onShowMoreClickListener.accept(position);
                }
            }
        });

        if (tradeInfo.getType().equals("BUY")) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_call_made_24);
        }
        if (tradeInfo.getType().equals("SELL")) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_call_received_24);
        }
        if (tradeInfo.getType().equals("add_liq")) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_swap_vert_green);
        }
        if (tradeInfo.getType().equals("remove_liq")) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_swap_vert_red);
        }
    }

    @Override
    public int getItemCount() {
        return tradeInfoList.size();
    }

    class TradeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewType;
        private TextView textViewAmount;
        private TextView textViewCoin;
        private TextView textViewOtherCoin;
        private TextView textViewOtherAmount;
        private CardView cardView;
        private ImageView imageView;
        private TextView textViewLastPrice;
        private ImageView imageViewShowMore;

        public TradeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewCoin = itemView.findViewById(R.id.textViewCoin);
            textViewOtherCoin = itemView.findViewById(R.id.textViewOtherCoin);
            textViewOtherAmount = itemView.findViewById(R.id.textViewOtherAmount);
            cardView = itemView.findViewById(R.id.cardViewTrade);
            imageView = itemView.findViewById(R.id.imageView);
            textViewLastPrice = itemView.findViewById(R.id.textViewLastPrice);
            imageViewShowMore = itemView.findViewById(R.id.imageViewShowMore);

        }
    }
}
