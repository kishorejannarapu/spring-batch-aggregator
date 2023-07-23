package com.batch.valuation.writer;

import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.batch.item.ItemWriter;

import com.batch.valuation.model.CacheUtil;
import com.batch.valuation.model.StockVolume;
import com.batch.valuation.model.Trade;

/**
 * Writer class
 */
@Slf4j
public class StockVolumeWriter implements ItemWriter<Trade> {

    @Override
    public void write(List<? extends Trade> trades) throws Exception {
        Cache<String, StockVolume> cache = CacheUtil.getEhCache();
        trades.forEach(t -> {
            StockVolume stockVolume = cache.get(t.getStock());
            if (stockVolume != null) {
                // Increment stock volume
                stockVolume.setVolume(stockVolume.getVolume() + t.getShares());
            } else {
                cache.put(t.getStock(), new StockVolume(t.getStock(), t.getShares()));
            }
        });
    }

}
